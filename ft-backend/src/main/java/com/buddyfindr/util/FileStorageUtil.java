package com.buddyfindr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageUtil {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDir, String uuid) throws IOException {
        String ext = getFileExtension(file.getOriginalFilename());
        String fileName = (uuid != null ? uuid : UUID.randomUUID().toString()) + (ext != null ? "." + ext : "");
        Path dirPath = Paths.get(uploadDir, subDir);
        Files.createDirectories(dirPath);
        Path filePath = dirPath.resolve(fileName);
        file.transferTo(filePath);
        return "/" + uploadDir + subDir + "/" + fileName;
    }

    private String getFileExtension(String filename) {
        if (filename == null) return null;
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1) : null;
    }
} 