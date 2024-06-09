@import hexlet.code.dto.UrlPage
@import hexlet.code.utils.BeautyTime
@param UrlPage page


@template.layout.page(
    content = @`
    <main class="flex-grow-1">

        <section>

            <div class="container-lg mt-5">
                <h1>Сайт: https://ru.hexlet.io</h1>

                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${BeautyTime.getBeautyTime(page.getUrl().getCreatedAt())}</td>
                    </tr>
                    </tbody>
                </table>

                <h2 class="mt-5">Проверки</h2>
                <form method="post" action="/urls/1/checks">
                    <button type="submit" class="btn btn-primary">Запустить проверку</button>
                </form>

                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>

        </section>
    </main>

`
)