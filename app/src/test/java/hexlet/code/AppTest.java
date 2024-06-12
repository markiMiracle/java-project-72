package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.utils.BeautyTime;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppTest {
    private static MockWebServer mockServer;
    public static Javalin app;
    @BeforeAll
    public static void createMock() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    public static void shutDown() throws IOException {
        mockServer.shutdown();
    }


    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }
    @Test
    void testMainPageHandler() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testUrlsPageHandler() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("ID", "Имя", "Последняя проверка", "Код ответа");
        });
    }

    @Test
    void testUrlsShowHandlerFail() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/2");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testShowUrlHandler() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var createdAt = new Timestamp(System.currentTimeMillis());
            var existingUrl = new Url("https://ru.hexlet.io/projects/72/members/39826?step=6", createdAt);
            UrlsRepository.save(existingUrl);
            var response = client.get("/urls/" + existingUrl.getId());
            assertThat(response.body().string()).contains("https://ru.hexlet.io");
        });
    }

    @Test
    void testCheckHandler() throws SQLException {

        var inputStream = Objects.requireNonNull(AppTest.class.getClassLoader().getResourceAsStream("example.page.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var htmlResponse = reader.lines().collect(Collectors.joining());

        var response = new MockResponse()
                .setBody(htmlResponse)
                .setResponseCode(404)
                .addHeader("Content-Type", "text-html");
        mockServer.enqueue(response);

        String serverUrl = mockServer.url("/").toString();
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        var url = new Url(serverUrl, createdAt);

        UrlsRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response1 = client.get("/urls/1");
            var response2 = client.post("/urls/1/checks");
            assertThat(response1.body().string()).contains(url.getName());
            assertThat(response1.code()).isEqualTo(200);

        });

    }
}
