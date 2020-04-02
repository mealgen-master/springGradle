package com.springboard.backend.controller;

import com.springboard.backend.dto.FileDTO;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class FileResourceAssembler implements RepresentationModelAssembler<FileDTO.Response, EntityModel<FileDTO.Response>> {

    private MultipartFile file;

    @SneakyThrows
    @Override
    public EntityModel<FileDTO.Response> toModel(FileDTO.Response entity) {
        return  new EntityModel<>(
                entity,
                linkTo(methodOn(FileController.class).uploadFile(file)).withSelfRel(),
                linkTo(methodOn(FileController.class).downloadFile(entity.getId())).withRel("download")
        );
    }
}
