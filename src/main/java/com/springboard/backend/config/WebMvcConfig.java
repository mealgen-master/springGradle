package com.springboard.backend.config;

import com.springboard.backend.controller.FileResourceAssembler;
import com.springboard.backend.dto.Users;
import com.springboard.backend.dto.UsersDTO;
import com.springboard.backend.mapper.UserMapper;
import com.springboard.backend.properties.StorageProperties;
import com.springboard.backend.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 
    private static final long MAX_AGE_SECONDS = 3600;
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECONDS);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
     
        registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
 
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public FileResourceAssembler fileResourceAssembler() { return new FileResourceAssembler();}

}