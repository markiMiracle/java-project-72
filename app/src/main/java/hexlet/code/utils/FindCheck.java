package hexlet.code.utils;

import hexlet.code.model.UrlCheck;

import java.util.List;
import java.util.Optional;

public class FindCheck {
    public static Optional<UrlCheck> findCheckByParentId(Long id, List<UrlCheck> urlChecks) {
        for (var check: urlChecks) {
            if (check.getUrlId().equals(id)) {
                return Optional.of(check);
            }
        }
        return Optional.empty();
    }
}
