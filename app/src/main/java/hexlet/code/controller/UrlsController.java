package hexlet.code.controller;

import java.io.IOException;
import java.net.MalformedURLException;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void root(Context ctx) {
        var page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", model("page", page));
    }

    public static void create(Context ctx) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        try {
            var noValidName = ctx.formParam("url");
            var urlName = new URL(Objects.requireNonNull(noValidName));
            String name = urlName.getProtocol() + "://" + urlName.getAuthority();
            var url = new Url(name);
            url.setCreatedAt(createdAt);
            UrlsRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (SQLException e) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect(NamedRoutes.urlsPath());
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

    public static void check(Context ctx) throws SQLException, IOException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlsRepository.find(urlId).orElseThrow(() -> new NotFoundResponse("Url not found"));
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        try {
            HttpResponse<String> response = Unirest.get(url.getName())
                    .asString();
            int statusCode = response.getStatus();

            Document doc = Jsoup.parse(response.getBody());

            String title = doc.title();

            Element h1Element = doc.selectFirst("h1");
            String h1 = h1Element == null ? "" : h1Element.ownText();

            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = descriptionElement == null
                    ? ""
                    : descriptionElement.attr("content");

            var urlCheck = new UrlCheck(urlId, statusCode, title, h1, description);
            urlCheck.setCreatedAt(createdAt);
            UrlChecksRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некоректный адрес");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
