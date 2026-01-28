package com.nguyenhuuquang.hotelmanagement.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping("/room-images")
    public ResponseEntity<?> uploadRoomImage(@RequestParam("file") MultipartFile file) {
        try {
            // Tạo thư mục uploads nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file duy nhất
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResponseEntity.badRequest().body("File name is empty");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Trả về URL - chỉ trả về path từ uploads
            String fileUrl = "/uploads/" + uniqueFilename;

            return ResponseEntity.ok(new UploadResponse(
                    fileUrl,
                    originalFilename,
                    file.getSize(),
                    file.getContentType()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file: " + e.getMessage());
        }
    }

    // THÊM ENDPOINT NÀY - Giống hệt room-images
    @PostMapping("/roomtype-images")
    public ResponseEntity<?> uploadRoomTypeImage(@RequestParam("file") MultipartFile file) {
        try {
            // Tạo thư mục uploads nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file duy nhất
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResponseEntity.badRequest().body("File name is empty");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Trả về URL - chỉ trả về path từ uploads
            String fileUrl = "/uploads/" + uniqueFilename;

            return ResponseEntity.ok(new UploadResponse(
                    fileUrl,
                    originalFilename,
                    file.getSize(),
                    file.getContentType()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file: " + e.getMessage());
        }
    }

    // DTO cho response
    public static class UploadResponse {
        private String url;
        private String originalName;
        private long size;
        private String contentType;

        public UploadResponse() {
        }

        public UploadResponse(String url, String originalName, long size, String contentType) {
            this.url = url;
            this.originalName = originalName;
            this.size = size;
            this.contentType = contentType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }
}