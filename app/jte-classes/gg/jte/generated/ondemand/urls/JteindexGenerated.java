package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.UrlsPage;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.BeautyTime;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,6,6,8,8,10,10,11,11,13,13,13,16,16,18,18,18,21,21,22,22,38,38,41,41,41,44,44,44,44,44,44,44,44,44,44,44,44,46,46,48,48,48,51,51,51,53,53,60,60,63,63,71,71,71,71,71,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\n\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <main class=\"flex-grow-1\">\n    ");
				if (page.getFlash() != null) {
					jteOutput.writeContent("\n        ");
					if (page.getFlash().equals("Страница уже существует")) {
						jteOutput.writeContent("\n            <div class=\"rounded-0 m-0 alert alert-dismissible fade show alert-info\" role=\"alert\">\n                <p class=\"m-0\">");
						jteOutput.setContext("p", null);
						jteOutput.writeUserContent(page.getFlash());
						jteOutput.writeContent("</p>\n                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n            </div>\n        ");
					} else {
						jteOutput.writeContent("\n            <div class=\"rounded-0 m-0 alert alert-dismissible fade show alert-success\" role=\"alert\">\n                <p class=\"m-0\">");
						jteOutput.setContext("p", null);
						jteOutput.writeUserContent(page.getFlash());
						jteOutput.writeContent("</p>\n                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n            </div>\n        ");
					}
					jteOutput.writeContent("\n    ");
				}
				jteOutput.writeContent("\n    <section>\n\n        <div class=\"container-lg mt-5\">\n            <h1>Сайты</h1>\n\n            <table class=\"table table-bordered table-hover mt-3\">\n                <thead>\n                <tr>\n                    <th class=\"col-1\">ID</th>\n                    <th>Имя</th>\n                    <th class=\"col-2\">Последняя проверка</th>\n                    <th class=\"col-1\">Код ответа</th>\n                </tr>\n                </thead>\n                <tbody>\n                    ");
				for (var url: page.getUrls()) {
					jteOutput.writeContent("\n                    <tr>\n                        <td>\n                            ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("\n                        </td>\n                        <td>\n                            <a");
					var __jte_html_attribute_0 = NamedRoutes.urlPath(url.getId());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" href=\"");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("a", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\n                        </td>\n                        ");
					if (page.getUrlChecks().get(url.getId()) != null) {
						jteOutput.writeContent("\n                        <td>\n                            ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(BeautyTime.getBeautyTime(page.getUrlChecks().get(url.getId()).getCreatedAt()));
						jteOutput.writeContent("\n                        </td>\n                        <td>\n                            ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(page.getUrlChecks().get(url.getId()).getStatusCode());
						jteOutput.writeContent("\n                        </td>\n                        ");
					} else {
						jteOutput.writeContent("\n                            <td>\n\n                            </td>\n                            <td>\n\n                            </td>\n                        ");
					}
					jteOutput.writeContent("\n\n                    </tr>\n                    ");
				}
				jteOutput.writeContent("\n\n                </tbody>\n            </table>\n        </div>\n\n    </section>\n    </main>\n");
			}
		}, null);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
