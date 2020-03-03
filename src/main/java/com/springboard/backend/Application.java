package com.springboard.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@EnableResourceServer
// 리소스서버 사용을 가능하게
//@EnableAuthorizationServer
// 인증서버 사용을 가능하게
//@EnableAutoConfiguration
@SpringBootApplication
//@EnableConfigurationProperties
//@EntityScan(basePackages = {"com.springboard.backend"})  
public class Application extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
