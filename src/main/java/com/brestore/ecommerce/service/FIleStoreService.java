package com.brestore.ecommerce.service;

import com.brestore.ecommerce.config.StorageProperties;
import com.brestore.ecommerce.exceptions.StorageException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FIleStoreService {

    private StorageProperties properties = new StorageProperties();
    Path rootLocation = Paths.get(properties.getLocation());



    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Falha ao armazenar um arquivo vazio.");
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadedFileName = UUID.randomUUID().toString() + "." + extension;

            Path destinationFile = rootLocation.resolve(
                    Paths.get(uploadedFileName))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);

                final String baseUrl =
                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

                return baseUrl+"/fileUpload/files/"+uploadedFileName;
            }
        }
        catch (IOException e) {
            throw new StorageException("Falha ao armazenar arquivo.", e);
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Falha ao fazer a leitura do arquivo", e);
        }

    }

    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Este arquivo n??o p??de ser lido!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }
}
