package com.projectfinal.greenthumb_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            System.out.println("-> Directorio de subidas creado en: " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio para las subidas de archivos.", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new RuntimeException("No se puede guardar un archivo vacío o sin nombre.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

            Path destinationFile = this.rootLocation.resolve(uniqueFilename).normalize();

            if (!destinationFile.toAbsolutePath().startsWith(this.rootLocation.toAbsolutePath().normalize())) {
                throw new RuntimeException("No se puede guardar el archivo fuera del directorio actual.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Falló al guardar el archivo.", e);
        }
    }
}