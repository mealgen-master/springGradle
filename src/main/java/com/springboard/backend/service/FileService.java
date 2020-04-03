package com.springboard.backend.service;

import com.google.common.collect.Lists;
import com.springboard.backend.dto.FileDTO;
import com.springboard.backend.exception.StorageFileNotFoundException;
import com.springboard.backend.mapper.FileMapper;
import com.springboard.backend.model.AttachFile;
import com.springboard.backend.properties.FileProperties;
import com.springboard.backend.properties.StorageProperties;
import com.springboard.backend.repository.FileJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class FileService {


    private final StorageProperties storageProperties;

    private final FileProperties fileProperties;

    private final Path fileLocation;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    public FileService(StorageProperties storageProperties, FileProperties fileProperties) {
        this.storageProperties = storageProperties;
        this.fileLocation = Paths.get(storageProperties.getLocation());
        this.fileProperties = fileProperties;
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

    public FileDTO.Download loadFile(final Long id, Long width, Long height, final String type) {
        AttachFile attachFile = fileJpaRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        String convertedFileName = width + "x" + height + "_" + type + ".jpg";
        Path originPath = Paths.get(this.fileLocation + File.separator  + attachFile.getServerPath() + File.separator + attachFile.getId() + File.separator + attachFile.getFilename() );
        Path convertedPath = originPath.getParent().resolve(convertedFileName);

        Path absoluteOriginPath = this.load(originPath.toString());
        Path absoluteConvertedPath = absoluteOriginPath.getParent().resolve(convertedFileName);

        try {
            try {
                Resource resource = this.loadAsResource(convertedPath.toString());
                if (resource.exists() && resource.isFile()) {
                    return FileDTO.Download.builder()
                            .attachFile(FileMapper.FILE_MAPPER.toDto(attachFile))
                            .resource(resource)
                            .build();
                }
            } catch (StorageFileNotFoundException e) {
                // nothing
                log.error(e.getMessage());
            }

            if (!type.equals("crop")) {
                BufferedImage bufferedImage = ImageIO.read(originPath.toFile());
                float w = (float) bufferedImage.getWidth() / (float) width;
                float h = (float) bufferedImage.getHeight() / (float) height;
                if (h > w) {
                    width = 0L;
                } else {
                    height = 0L;
                }
            }

            Map<String, String> args = new LinkedHashMap<>();
            args.put("-resize", width + "x" + height + "^");
            args.put("-quality", "90");
            args.put("-gravity", "center");
            args.put("-auto-orient", "");
            args.put("-crop", width + "x" + height + "+0+0");
            args.put("+repage", "");

            String commandArgs = mapToCommand(args);
            List<String> commandList = Lists.newLinkedList();
            if (System.getProperty("os.name").contains("windows")) {
                commandList.add("\"" + fileProperties.getExtensionConvert() + "\"");
                commandList.add(
                        absoluteOriginPath.toString() + ((attachFile.isGif() || attachFile.isVideo()) ? "[0]"
                                : ""));
                commandList.add(commandArgs);
                commandList.add(absoluteConvertedPath.toString());
            } else {
                commandList.add(fileProperties.getExtensionConvert());
                commandList.add(absoluteOriginPath.toString() + (
                        (attachFile.isGif() || attachFile.isVideo()) ? "[0]" : ""));
                commandList.add(commandArgs);
                commandList.add(absoluteConvertedPath.toString());
            }

            String command = String.join(" ", commandList);
            log.info("command: {}", command);

            try {
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
            } catch (Exception e) {
                // nothing
                log.error("Error occurrence", e);
            }

            return FileDTO.Download.builder()
                    .attachFile(FileMapper.FILE_MAPPER.toDto(attachFile))
                    .resource(this.loadAsResource(convertedPath.toString()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file " + convertedPath, e);
        }
    }


    private void createDirectory(Path path) throws IOException {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            log.error("File already exists: {}", e.getMessage());
        }
    }

    public Path load(String filename) {
        return this.fileLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private String mapToCommand(final Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + " " + map.get(key))
                .collect(Collectors.joining(" "));
    }
}
