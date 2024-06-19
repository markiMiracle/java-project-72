package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
public final class UrlsPage {
    private List<Url> urls;
    private String flash;
    private List<UrlCheck> urlChecks;

}
