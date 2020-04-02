package com.springboard.backend.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("file")
public class FileProperties {
    private String fileRedirect;
    private String xSendFile;
    private String extensionHeic;
    private String extensionConvert;
    private String extensionComposite;
}
