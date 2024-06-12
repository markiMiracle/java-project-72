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
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private final Timestamp createdAt;

    public UrlCheck(
            int statusCode,
            String title,
            String h1,
            String description,
            Long urlId,
            Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
    public UrlCheck(Long urlId, Timestamp createdAt, Integer statusCode) {
        this.urlId = urlId;
        this.createdAt = createdAt;
        this.statusCode = statusCode;
    }
}
