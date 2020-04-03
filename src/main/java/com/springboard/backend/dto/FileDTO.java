package com.springboard.backend.dto;


import com.springboard.backend.model.UserRole;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Slf4j
public class FileDTO {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Response {

        @NotNull
        private Long id;


        private String serverPath;

        @NotEmpty
        private String filename;

        @NotEmpty
        private String extension;

        @NotEmpty
        private String contentType;

        private Long size;

//        private byte[] data;


        private Date insertDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Download {

        private Response attachFile;

        private Resource resource;

    }

}
