package com.springboard.backend.controller;


import com.google.common.net.HttpHeaders;
import com.springboard.backend.dto.FileDTO;
import com.springboard.backend.model.AttachFile;
import com.springboard.backend.service.FileService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"파일 API"})
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/files", produces = MediaTypes.HAL_JSON_VALUE)
public class FileController {

    @Autowired
    private FileService fileService;

    private FileDTO.Response response;

    @Autowired
    private FileResourceAssembler fileResourceAssembler;


//    @ApiOperation(value = "파일 업로드" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "FIle Upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<FileDTO.Response>> uploadFile(@ApiParam(value = "Select te file to Upload")MultipartFile file) throws FileUploadException {
        return ResponseEntity.ok(fileResourceAssembler.toModel(fileService.storeFile(file)));
    }

    @ApiOperation(value = "다중 파일 업로드", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(path = "/multiPart",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileDTO.Response>> uploadFile(@ApiParam(value = "Select te file to Upload") MultipartFile[] files) {
        return ResponseEntity.ok(Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        response = fileService.storeFile(file);
                    } catch (FileUploadException e) {
                        e.printStackTrace();
                    }
                    return response;
                })
                .collect(Collectors.toList()));
    }

//    @ApiOperation(value = "파일 다운로드")
    @ApiOperation(value = "File Download")
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final Long id) throws FileNotFoundException {
        AttachFile attachFile = fileService.loadFile(id);

        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(attachFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + attachFile.getFilename() + "\"")
                .header(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString())
                .body(new ByteArrayResource(attachFile.getData()));
    }

    @ApiOperation(value = "이미지 편집 다운로드")
    @GetMapping("/{id}/thumb")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id, @ApiParam(example = "200") @RequestParam(value = "w", required = false) long width,
                                                 @ApiParam(example = "200") @RequestParam(value = "h", required = false) long height,
                                                 @ApiParam(example = "crop") @RequestParam(value = "t", required = false, defaultValue = "crop") String type) {
        FileDTO.Download download = fileService.loadFile(id,width,height,type);
        Resource resource = download.getResource();
        FileDTO.Response attachFile = download.getAttachFile();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
