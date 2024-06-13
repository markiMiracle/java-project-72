package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
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
import java.util.stream.Collectors;

public class AppTest {
    private static String testUrl;
    private static MockWebServer fakeServer;
    public static Javalin app;

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = AppTest.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    @BeforeAll
    public static void createMock() throws IOException {
        fakeServer = new MockWebServer();
        fakeServer.start();
        fakeServer.enqueue(new MockResponse().setBody(readResourceFile("fake.html")).setResponseCode(200));
        testUrl = fakeServer.url("/").toString();

    }

    @AfterAll
    public static void shutDown() throws IOException {
        fakeServer.shutdown();
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
            var response2 = client.post("/urls/" + existingUrl.getId() + "/checks");
            assertThat(response.body().string()).contains("https://ru.hexlet.io");
            assertThat(response2.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCheckPath() throws SQLException {
        var createdAt = new Timestamp(System.currentTimeMillis());
        var url = new Url(10L, testUrl, createdAt);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlCheckPath(1L))) {
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string()).contains("fake description", "FAKE H1", "Fake Title");
            }
        });
    }
}
