package com.springboard.backend.mapper;

import com.springboard.backend.dto.FileDTO;
import com.springboard.backend.model.AttachFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDTO.Response , AttachFile> {

    FileMapper FILE_MAPPER = Mappers.getMapper(FileMapper.class);
}
