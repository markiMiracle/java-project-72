package hexlet.code;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.UrlsController;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;
import hexlet.code.repository.BaseRepository;

@Slf4j
public class App {
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
    public static Javalin getApp() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project:DB_CLOSE_DELAY=-1;"));
        var dataSource = new HikariDataSource(hikariConfig);

        var fileName = hikariConfig.getJdbcUrl().equals("jdbc:h2:mem:project:DB_CLOSE_DELAY=-1;")
                ? "schema.sql"
                : "postgreSchema.sql";

        String sql = readResourceFile(fileName);
        log.info(sql);


        try (var connection = dataSource.getConnection();
                var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });



        app.get(NamedRoutes.rootPath(), UrlsController::root);

        app.get(NamedRoutes.urlsPath(), UrlsController::index);

        app.post(NamedRoutes.urlsPath(), UrlsController::create);

        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);


        return app;
    }

    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(getPort());
    }

}
