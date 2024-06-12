package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


@Getter
@AllArgsConstructor
public class UrlPage {
    private Url url;
    private String flash;
    private Deque<UrlCheck> urlChecks;
}
