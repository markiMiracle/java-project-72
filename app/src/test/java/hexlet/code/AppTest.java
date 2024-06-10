package hexlet.code;


import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Timestamp;

public class AppTest {
    private static Javalin app;
    private static Url existingUrl;

    @BeforeEach
    void setup() throws SQLException {
        app = App.getApp();
        var createdAt = new Timestamp(System.currentTimeMillis());
        existingUrl = new Url("https://ru.hexlet.io/projects/72/members/39826?step=6", createdAt);
        UrlsRepository.save(existingUrl);
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("ID", "Имя", "Последняя проверка", "Код ответа");
        });
    }

    @Test
    void testUrlsShow() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/1");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testShowPost() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + existingUrl.getId());
            assertThat(response.body().string()).contains("https://ru.hexlet.io");
        });
    }
}
