package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class UrlChecksRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (statusCode, title, h1, description, urlId, createdAt)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, urlCheck.getCreatedAt());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Deque<UrlCheck> getEntitiesByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE urlId = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();
            Deque<UrlCheck> entities = new ArrayDeque<>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("statusCode");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("createdAt");

                var urlCheck = new UrlCheck(id, urlId, statusCode, title, h1, description, createdAt);
                entities.addFirst(urlCheck);
            }

            return entities;
        }
    }
    public static List<UrlCheck> getEntitiesByChecks() throws SQLException {
        var sql = "SELECT\n"
               + "    urls.id AS id,\n"
               + "    url_checks.statusCode AS statusCode,\n"
               + "    url_checks.createdAt AS createdAt\n"
               + "FROM urls\n"
               + "INNER JOIN url_checks\n"
               + "ON urls.id = url_checks.urlId\n"
               + "INNER JOIN (\n"
               + "    SELECT urlId, MAX(createdAt) AS maxCreatedAt\n"
               + "    FROM url_checks\n"
               + "    GROUP BY urlId\n"
               + ") AS latest_checks\n"
               + "ON url_checks.urlId = latest_checks.urlId\n"
               + "AND url_checks.createdAt = latest_checks.maxCreatedAt\n"
               + "ORDER BY id";

        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            var entities = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var urlId = resultSet.getLong("id");
                var statusCode = resultSet.getInt("statusCode");
                var createdAt = resultSet.getTimestamp("createdAt");
                var urlAndCheck = new UrlCheck(urlId, statusCode, createdAt);
                entities.add(urlAndCheck);
            }
            return entities;
        }
    }
}
