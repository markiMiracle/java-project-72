package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Deque;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Url {
    private Long id;
    private final String name;
    private final Timestamp createdAt;
    private Deque<UrlCheck> urlChecks;


    public Url(Long id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
