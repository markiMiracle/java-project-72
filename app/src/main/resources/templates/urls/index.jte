@import hexlet.code.dto.UrlsPage
@import hexlet.code.utils.NamedRoutes
@import hexlet.code.utils.BeautyTime
@param UrlsPage page


@template.layout.page(
    content = @`
    <main class="flex-grow-1">
    @if(page.getFlash() != null)
        @if(page.getFlashType().equals("exists"))
            <div class="rounded-0 m-0 alert alert-dismissible fade show alert-info" role="alert">
                <p class="m-0">${page.getFlash()}</p>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @else
            <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
                <p class="m-0">${page.getFlash()}</p>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif
    @endif
    <section>

        <div class="container-lg mt-5">
            <h1>Сайты</h1>

            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    !{var urlCheck = page.getUrlChecks().get(url.getId());}

                    <tr>
                        <td>
                            ${url.getId()}
                        </td>
                        <td>
                            <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                        </td>
                        <td>
                            ${urlCheck == null ? "" : BeautyTime.getBeautyTime(urlCheck.getCreatedAt())}
                        </td>
                        <td>
                            ${urlCheck == null ? "" : String.valueOf(urlCheck.getStatusCode())}
                        </td>

                    </tr>
                @endfor

                </tbody>
            </table>
        </div>

    </section>
    </main>
`
)