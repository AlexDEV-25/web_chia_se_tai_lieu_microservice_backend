package com.example.fileservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fileservice.constant.AppError;
import com.example.fileservice.dto.response.FileResponse;
import com.example.fileservice.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileService {

    private final Cloudinary cloudinary;
    private final RestTemplate restTemplate;

    @Value("${app.cloud.name}")
    private String cloudName;

    public FileResponse downloadFile(String url) throws Exception {

        ResponseEntity<Resource> response = restTemplate.getForEntity(url, Resource.class);

        Resource resource = response.getBody();

        if (resource == null) {
            throw new RuntimeException("Không lấy được file");
        }

        // Lấy InputStream
        InputStreamResource inputStreamResource = new InputStreamResource(resource.getInputStream());

        // Lấy content-type
        MediaType mediaType = response.getHeaders().getContentType();
        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        // Lấy content-length
        long contentLength = response.getHeaders().getContentLength();

        // Lấy tên file từ URL
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        return new FileResponse(inputStreamResource, contentLength, mediaType, fileName);
    }

    public String getThumbnail(String publicId) {
        return "https://res.cloudinary.com/" + cloudName + "/image/upload/pg_1/" + publicId + ".jpg";
    }

    public void deleteFile(String url) {
        try {
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String fileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "file";
        }

        return originalFilename.toLowerCase();
    }

    public Map<String, Object> uploadImage(MultipartFile file) {
        String avatarName = this.fileName(file);
        if (avatarName.endsWith(".jpg") || avatarName.endsWith(".jpeg") || avatarName.endsWith(".png")
                || avatarName.endsWith(".webp")) {
            String folder = "tailieu/image";
            String publicId = folder + "/" + UUID.randomUUID();

            return uploadToCloudinary(file, publicId);
        } else {
            throw AppException.builder().appError(AppError.INVALID_IMAGE_FORMAT).build();
        }

    }

    public Map<String, Object> uploadPdf(MultipartFile file) {
        String documentName = this.fileName(file);
        if (documentName.endsWith(".pdf")) {
            String folder = "tailieu/pdf";
            String publicId = folder + "/" + UUID.randomUUID();
            return uploadToCloudinary(file, publicId);
        } else {
            throw AppException.builder().appError(AppError.INVALID_DOCUMENT_FORMAT).build();
        }
    }

    private Map<String, Object> uploadToCloudinary(MultipartFile file, String publicId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "public_id", publicId,
                            "type", "upload",
                            "access_mode", "public"
                    )
            );
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String extractPublicId(String url) {

        // Lấy phần sau "/upload/"
        int index = url.indexOf("/upload/");
        String path = url.substring(index + 8);

        // Bỏ version (v123456/)
        path = path.replaceFirst("v\\d+/", "");

        // Bỏ extension (.pdf, .jpg, .zip...)
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex != -1) {
            path = path.substring(0, dotIndex);
        }

        return path;
    }

}
