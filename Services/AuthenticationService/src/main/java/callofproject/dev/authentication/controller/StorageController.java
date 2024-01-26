package callofproject.dev.authentication.controller;


import callofproject.dev.authentication.service.S3Service;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * This class represents a controller for managing storage-related operations.
 * It handles HTTP requests related to storage and interacts with the S3Service.
 */
@RestController
@RequestMapping("api/project/storage")
@SecurityRequirement(name = "Authorization")
public class StorageController
{
    private final S3Service m_storageService;

    /**
     * Constructs a new StorageController with the provided dependencies.
     *
     * @param storageService The S3Service instance used for handling storage-related operations.
     */
    public StorageController(S3Service storageService)
    {
        m_storageService = storageService;
    }

    /**
     * Handles the HTTP POST request to upload a file to the storage service using a File object.
     *
     * @param file     The File object representing the file to be uploaded.
     * @param fileName The name to be assigned to the uploaded file.
     * @return ResponseEntity with a message indicating the success of the upload, or an error message in case of failure.
     */
    @PostMapping("/upload/multipart")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("name") String fileName)
    {
        return new ResponseEntity<>(m_storageService.uploadToS3WithMultiPartFile(file, fileName), HttpStatus.OK);
    }


    /**
     * Handles the HTTP POST request to upload a file to the storage service using MultipartFile and obtain its URL.
     *
     * @param file     The MultipartFile containing the file to be uploaded.
     * @param fileName The name to be assigned to the uploaded file.
     * @return ResponseEntity with the URL of the uploaded file, or an error message in case of failure.
     */
    @PostMapping("/upload/multipart/url")
    public ResponseEntity<String> uploadFileAndGetUrl(@RequestParam(value = "file") MultipartFile file, @RequestParam("name") String fileName)
    {
        return new ResponseEntity<>(m_storageService.uploadToS3AndGetUrl(file, fileName), HttpStatus.OK);
    }

    /**
     * Handles the HTTP POST request to upload a file to the storage service using a File object.
     *
     * @param file     The File object representing the file to be uploaded.
     * @param fileName The name to be assigned to the uploaded file.
     * @return ResponseEntity with a message indicating the success of the upload, or an error message in case of failure.
     */
    @PostMapping("/upload/file")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") File file, @RequestParam("name") String fileName)
    {
        return new ResponseEntity<>(m_storageService.uploadToS3WithFile(file, fileName), HttpStatus.OK);
    }

    /**
     * Handles the HTTP GET request to download a file from the storage service.
     *
     * @param fileName The name of the file to be downloaded.
     * @return ResponseEntity with the file data as a ByteArrayResource, or an error message in case of failure.
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName)
    {
        byte[] data = m_storageService.downloadFromS3(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * Handles the HTTP DELETE request to delete a file from the storage service.
     *
     * @param fileName The name of the file to be deleted.
     * @return ResponseEntity with a message indicating the success of the deletion, or an error message in case of failure.
     */
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName)
    {
        return new ResponseEntity<>(m_storageService.deleteFromS3(fileName), HttpStatus.OK);
    }

    /**
     * Handles the HTTP GET request to find an image by its name from the storage service.
     *
     * @param name The name of the image to be retrieved.
     * @return ResponseEntity with the image data as a String, or an error message in case of failure.
     */
    @GetMapping("find/image")
    public ResponseEntity<String> findImage(@RequestParam("name") String name)
    {
        return new ResponseEntity<>(m_storageService.getImage(name), HttpStatus.OK);
    }
}
