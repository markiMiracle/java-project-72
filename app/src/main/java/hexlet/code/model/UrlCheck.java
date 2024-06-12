package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor

public class UrlCheck {
    private Long id;
    private Long urlId;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private final Timestamp createdAt;

    public UrlCheck(
            Long urlId,
            Integer statusCode,
            String title,
            String h1,
            String description,
            Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
    public UrlCheck(Long urlId, Integer statusCode, Timestamp createdAt) {
        this.urlId = urlId;
        this.createdAt = createdAt;
        this.statusCode = statusCode;
    }
}
