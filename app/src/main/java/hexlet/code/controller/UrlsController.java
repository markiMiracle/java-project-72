package hexlet.code.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void root(Context ctx) {
        var page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", model("page", page));
    }

    public static void create(Context ctx) throws URISyntaxException, SQLException {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        try {
            var noValidName = ctx.formParam("url");
            var urlName = new URL(Objects.requireNonNull(noValidName));
            String name = urlName.getProtocol() + "://" + urlName.getAuthority();
            var url = new Url(name, createdAt);
            try {
                UrlsRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.redirect(NamedRoutes.urlsPath());
            } catch (SQLException e) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некоректный URL");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var page = new UrlsPage(UrlsRepository.getEntities(),
                ctx.consumeSessionAttribute("flash"),
                UrlChecksRepository.getEntitiesByChecks());
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        url.setUrlChecks(UrlChecksRepository.getEntitiesByUrlId(id));
        String flash = ctx.consumeSessionAttribute("flash");
        var page = new UrlPage(url, flash, UrlChecksRepository.getEntitiesByUrlId(id));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlsRepository.find(urlId).orElseThrow(() -> new NotFoundResponse("Url not found"));
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        Unirest.config().defaultBaseUrl(url.getName());

       try {
           HttpResponse<String> response = Unirest.get(url.getName())
                   .asString();
           int statusCode = response.getStatus();
           var urlCheck = new UrlCheck(urlId, createdAt, statusCode);
           UrlChecksRepository.save(urlCheck);
           ctx.sessionAttribute("flash", "Страница успешно проверена");
           ctx.redirect(NamedRoutes.urlPath(urlId));
       } catch (UnirestException e) {
           ctx.sessionAttribute("flash", "Некоректный адрес");
           ctx.redirect(NamedRoutes.urlPath(urlId));
       }
    }
}
