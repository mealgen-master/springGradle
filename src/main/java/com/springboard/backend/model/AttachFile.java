package com.springboard.backend.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Entity
public class AttachFile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(nullable = false)
    private String serverPath;

    @Column(nullable = false)
    private String filename;

    @Column(length = 20)
    private String extension;

    @Column(length = 30, nullable = false)
    private String contentType;

    @Builder.Default
    private Long size = 0L;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @CreationTimestamp    // 입력시 시간 정보를 자동으로 입력해는 어노테이션.
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    public static String extractExtension(String filename) {
        if (filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return StringUtils.EMPTY;
    }

}
