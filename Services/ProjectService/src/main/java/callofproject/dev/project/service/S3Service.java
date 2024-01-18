package callofproject.dev.project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * Service class for managing files with Amazon S3.
 * This class provides functionalities to upload, download, and delete files from Amazon S3 buckets.
 */
@Service
public class S3Service
{
    @Value("${application.bucket.name}")
    private String m_bucketName;
    private final AmazonS3 m_s3Client;


    /**
     * Constructor for the S3Service class.
     * It is used to inject dependencies into the service.
     *
     * @param s3Client The AmazonS3 object to be injected.
     */
    public S3Service(AmazonS3 s3Client)
    {
        m_s3Client = s3Client;
    }

    /**
     * Uploads a file to Amazon S3 and returns its URL.
     *
     * @param multipartFile The file to be uploaded as a MultipartFile.
     * @param fileName      The name to be assigned to the file on S3.
     * @return The URL of the uploaded file.
     */
    public String uploadToS3AndGetUrl(MultipartFile multipartFile, String fileName)
    {
        var fileObject = doForDataService(() -> toFile(multipartFile), "Failed to convert multipart file to file");

        m_s3Client.putObject(new PutObjectRequest(m_bucketName, fileName, fileObject).withCannedAcl(CannedAccessControlList.PublicRead));
        fileObject.delete();

        return "https://" + m_bucketName + ".s3.amazonaws.com/" + fileName;
    }

    /**
     * Uploads a file to Amazon S3 using MultipartFile and deletes any existing files with similar names.
     *
     * @param multipartFile The file to be uploaded as a MultipartFile.
     * @param fileName      The name to be assigned to the file on S3.
     * @return A success message upon successful upload.
     */
    public String uploadToS3WithMultiPartFile(MultipartFile multipartFile, String fileName)
    {
        File fileObject = doForDataService(() -> toFile(multipartFile), "Failed to convert multipart file to file");

        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);

        ObjectListing objectListing = m_s3Client.listObjects(m_bucketName);
        for (S3ObjectSummary file : objectListing.getObjectSummaries())
        {
            String fileKey = file.getKey();
            int fileDotIndex = fileKey.lastIndexOf('.');
            String fileBaseName = (fileDotIndex == -1) ? fileKey : fileKey.substring(0, fileDotIndex);

            if (fileBaseName.equals(baseName) && !fileKey.equals(fileName))
            {
                m_s3Client.deleteObject(m_bucketName, fileKey);
            }
        }

        m_s3Client.putObject(new PutObjectRequest(m_bucketName, fileName, fileObject));
        fileObject.delete();
        return fileName + " upload success";
    }

    /**
     * Uploads a file to Amazon S3 using a File object.
     *
     * @param file     The file to be uploaded.
     * @param fileName The name to be assigned to the file on S3.
     * @return A success message upon successful upload.
     */
    public String uploadToS3WithFile(File file, String fileName)
    {
        doForDataService(() -> m_s3Client.putObject(new PutObjectRequest(m_bucketName, fileName, file)),
                "Upload file to S3 failed");

        return fileName + " upload success";
    }

    /**
     * Downloads a file from Amazon S3 and returns its byte array.
     *
     * @param fileName The name of the file to be downloaded from S3.
     * @return The byte array of the downloaded file.
     */
    public byte[] downloadFromS3(String fileName)
    {
        try (S3Object s3Object = m_s3Client.getObject(m_bucketName, fileName);
             S3ObjectInputStream inputStream = s3Object.getObjectContent())
        {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves an image from Amazon S3, encodes it in Base64, and returns the encoded string.
     * This method is cached to improve performance for repeated requests.
     *
     * @param fileName The name of the image file to be retrieved from S3.
     * @return The Base64 encoded string of the image.
     */
    @Cacheable(value = "getImage", key = "#fileName")
    public String getImage(String fileName)
    {
        return doForDataService(() -> Base64.getEncoder().encodeToString(downloadFromS3(fileName)), "Failed to get image from S3");
    }

    /**
     * Deletes a file from Amazon S3.
     *
     * @param fileName The name of the file to be deleted from S3.
     * @return A success message upon successful deletion.
     */
    public String deleteFromS3(String fileName)
    {
        m_s3Client.deleteObject(m_bucketName, fileName);
        return fileName + " removed successfully";
    }

    /**
     * Converts a MultipartFile to a File object.
     *
     * @param multipartFile The MultipartFile to be converted.
     * @return The converted File object.
     * @throws IOException if the conversion fails.
     */
    private File toFile(MultipartFile multipartFile) throws IOException
    {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile))
        {
            fos.write(multipartFile.getBytes());
        } catch (IOException e)
        {
            throw new IOException("Failed to convert multipartFile to file", e);
        }
        return convertedFile;
    }
}
