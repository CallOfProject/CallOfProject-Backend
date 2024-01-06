package callofproject.dev.project.controller;

import callofproject.dev.project.service.S3Service;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("api/storage")
@SecurityRequirement(name = "Authorization")
public class StorageController
{
    private final S3Service m_storageService;

    public StorageController(S3Service storageService)
    {
        m_storageService = storageService;
    }

    @PostMapping("/upload/multipart")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("name") String fileName)
    {
        return new ResponseEntity<>(m_storageService.uploadToS3WithMultiPartFile(file, fileName), HttpStatus.OK);
    }

    @PostMapping("/upload/file")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") File file, @RequestParam("name") String fileName)
    {
        return new ResponseEntity<>(m_storageService.uploadToS3WithFile(file, fileName), HttpStatus.OK);
    }

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

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName)
    {
        return new ResponseEntity<>(m_storageService.deleteFromS3(fileName), HttpStatus.OK);
    }

    @GetMapping("find/image")
    public ResponseEntity<String> findImage(@RequestParam("name") String name)
    {
        return new ResponseEntity<>(m_storageService.getImage(name), HttpStatus.OK);
    }
}
