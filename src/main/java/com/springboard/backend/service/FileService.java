package com.springboard.backend.service;

import com.springboard.backend.dto.FileDTO;
import com.springboard.backend.mapper.FileMapper;
import com.springboard.backend.model.AttachFile;
import com.springboard.backend.properties.StorageProperties;
import com.springboard.backend.repository.FileJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Transactional
@Service
public class FileService {


    private final StorageProperties storageProperties;

    private final Path fileLocation;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    public FileService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.fileLocation = Paths.get(storageProperties.getLocation());
    }

    private FileDTO.Response toResource(AttachFile attachFile) {
        return FileMapper.FILE_MAPPER.toDto(attachFile);
    }

    public FileDTO.Response storeFile(MultipartFile file) throws FileUploadException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        LocalDateTime now = LocalDateTime.now();
        try {
            if(filename.contains("..")) {
                throw new FileUploadException("["+filename+"] 부적합한 문자가 포함되어 있습니다.");
            }
            AttachFile attachFile = AttachFile.builder().filename(filename).extension(AttachFile.extractExtension(filename)).size(file.getSize()).contentType(file.getContentType()).data(file.getBytes()).serverPath(now.format(DateTimeFormatter.ofPattern("yyyy/MM"))).build();
            AttachFile saveAttachFile = fileJpaRepository.save(attachFile);
            Path targetlocation = this.fileLocation.resolve(attachFile.getServerPath() + File.separator + saveAttachFile.getId() + File.separator + filename);

            log.info("targetlocation: {}", targetlocation);
            log.info("targetlocation.getParent(): {}", targetlocation.getParent());
            log.info("Files.isDirectory(target): {}", Files.isDirectory(targetlocation.getParent()));
            if (!Files.isDirectory(targetlocation.getParent())) {
                this.createDirectory(targetlocation.getParent());
            }
            Files.copy(file.getInputStream() , targetlocation , StandardCopyOption.REPLACE_EXISTING);
            return  toResource(saveAttachFile);
        } catch (Exception error) {
            throw new FileUploadException("["+filename+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",error);
        }
    }

    public AttachFile loadFile(Long id) throws FileNotFoundException {
        AttachFile attachFile = fileJpaRepository.findById(id).orElseThrow(() -> new FileNotFoundException("file을 찾기 못했습니다."));
        return attachFile;
    }

    private void createDirectory(Path path) throws IOException {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            log.error("File already exists: {}", e.getMessage());
        }
    }
}
