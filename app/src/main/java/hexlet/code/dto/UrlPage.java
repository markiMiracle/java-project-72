package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Deque;


@Getter
@AllArgsConstructor
public class UrlPage {
    private Url url;
    private String flash;
    private Deque<UrlCheck> urlChecks;
}
