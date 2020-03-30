package com.springboard.backend.dto;

import com.springboard.backend.model.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.EntityModel;

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

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Reset {

        @NotEmpty
        private String username;

        @Length(min = 2, max = 20)
        @ApiModelProperty(value = "비밀번호", example = "010123", position = 1)
        @NotEmpty
        private String phonenumber;

    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Role {

        @NotEmpty
        private String username;


        @ApiModelProperty(value = "권한", example = "USER", position = 1)
        @NotEmpty
        private UserRole.Role role;

    }
}
