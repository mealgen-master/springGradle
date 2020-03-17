package com.springboard.backend.config;

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.Collections;
import com.google.common.base.Predicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.classmate.TypeResolver;

import io.swagger.models.Contact;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.springboard.backend")
public class SwaggerConfig {

	@Autowired
	private TypeResolver typeResolver;
	
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
//                .directModelSubstitute(LocalDate.class, String.class)
//                .genericModelSubstitutes(ResponseEntity.class)
//                .alternateTypeRules(
//                    newRule(typeResolver.resolve(DeferredResult.class,
//                        typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
//                        typeResolver.resolve(WildcardType.class)
//                    ))
//                .useDefaultResponseMessages(false)
//                .ignoredParameterTypes(
//                    Pageable.class,
//                    PagedResourcesAssembler.class,
//                    AuthenticationPrincipal.class
//                )
//                .securitySchemes(singletonList(securityScheme()))
//                .securityContexts(singletonList(securityContext()))
                .apiInfo(getApiInfo());
    }
    
    
    
    private ApiInfo getApiInfo() {
        return new ApiInfo(
//            "jinhee generation Application",
        	"스웨거 테스트 프로젝트 적용중",
            "이부분은 부가적인설명입니다.",
            "API 버전부분", 
            "Terms of service", 
            null,
            "라이센스 부분",
            "http://localhost:8080",
            Collections.emptyList()
        );
      }
    
    @Bean
    public SecurityConfiguration securityConfiguration() {
      return SecurityConfigurationBuilder.builder()
          .clientId("testClientId")
          .clientSecret("testSecret")
          .scopeSeparator("password refresh_token client_credentials")
          .useBasicAuthenticationWithAccessCodeGrant(true)
          .build();
    }
    
    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");

        return new OAuthBuilder().name("spring_oauth")
            .grantTypes(singletonList(grantType))
            .scopes(Arrays.asList(scopes()))
            .build();
      }

      private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder()
            .securityReferences(
                singletonList(new SecurityReference("spring_oauth", scopes()))
            )
            .forPaths(PathSelectors.ant("/api/**"))
            .build();
      }

      private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
            new AuthorizationScope("read", "for read operations"),
            new AuthorizationScope("write", "for write operations")};
      }

    
}
