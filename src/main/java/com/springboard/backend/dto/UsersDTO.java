package com.springboard.backend.dto;

import com.springboard.backend.model.UserRole;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UsersDTO {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Create {

        @NotEmpty
        private String username;

        @NotEmpty
        private String phonenumber;

        @NotEmpty
        private String address;

        @NotEmpty
        private String address2;

        @NotNull
        private UserRole.Role rolename;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Update {

        @NotEmpty
        private String username;

        @NotEmpty
        private String phonenumber;

        @NotEmpty
        private String address;

        @NotEmpty
        private String address2;

        @NotNull
        private UserRole.Role rolename;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Response {

        private Integer id;

        @NotEmpty
        private String username;

        @NotEmpty
        private String phonenumber;

        @NotEmpty
        private String address;

        @NotEmpty
        private String address2;

        @NotEmpty
        private List<UserRole> userRoles = new ArrayList<>();
    }
}
