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

@Service
public class S3Service
{
    @Value("${application.bucket.name}")
    private String m_bucketName;

    private final AmazonS3 m_s3Client;

    public S3Service(AmazonS3 s3Client)
    {
        m_s3Client = s3Client;
    }


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

        // Yeni dosyayı yükle
        m_s3Client.putObject(new PutObjectRequest(m_bucketName, fileName, fileObject));
        fileObject.delete();
        return fileName + " upload success";
    }


    public String uploadToS3WithFile(File file, String fileName)
    {
        doForDataService(() -> m_s3Client.putObject(new PutObjectRequest(m_bucketName, fileName, file)),
                "Upload file to S3 failed");

        return fileName + " upload success";
    }

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


    @Cacheable(value = "getImage", key = "#fileName")
    public String getImage(String fileName)
    {
        return doForDataService(() -> Base64.getEncoder().encodeToString(downloadFromS3(fileName)), "Failed to get image from S3");
    }


    public String deleteFromS3(String fileName)
    {
        m_s3Client.deleteObject(m_bucketName, fileName);
        return fileName + " removed successfully";
    }

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
