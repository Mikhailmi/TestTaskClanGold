package Gold.DataBase;

import java.sql.*;

// вспомогательный класс для создания и наполнения базы данных
class DataBase {

    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        try {
            connect();

            createClanTableEx();
            createTrackerTableEx();

            for (int i = 1; i <= 5; i++) {
                createClan(i, i + "Clan", 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    // метод для соединения с базой данных
    private static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:GoldDB.db");
        statement = connection.createStatement();
    }

    // метод для закрытия соединения с базой данных
    private static void disconnect() {

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // метод для создания таблицы-хранилища кланов
    private static void createClanTableEx() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS clanMap (\n" +
                "        clanId BIGINT not null,\n" +
                "        name TEXT not null,\n" +
                "        gold INTEGER NOT NULL\n" +
                "    );");
    }

    // метод для создания таблицы-хранилища кланов
    private static void createTrackerTableEx() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS trackerMap (\n" +
                "        clanId BIGINT not null,\n" +
                "        userId BIGINT not null,\n" +
                "        gold INTEGER NOT NULL\n" +
                "    );");
    }

    // вспомогательный метод для наполнения базы
    private static void createClan(long clanId, String name, int gold)  {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert or replace into clanMap (clanId, name, gold) " +
                        "values (?, ?, ?)")) {
            ps.setLong(1, clanId);
            ps.setString(2, name);
            ps.setInt(3, gold);
            ps.addBatch();
            ps.executeBatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // метод для уничтожения таблицы clanMap в базе данных
    private static void dropTableExClanMap() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS clanMap;");
    }

    // метод для очистки таблицы clanMap в базе данных
    private static void clearTableExClanMap() throws SQLException {
        statement.executeUpdate("DELETE FROM clanMap;");
    }

    // метод для уничтожения таблицы trackerMap в базе данных
    private static void dropTableExTrackerMap() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS trackerMap;");
    }

    // метод для очистки таблицы trackerMap в базе данных
    private static void clearTableExTrackerMap() throws SQLException {
        statement.executeUpdate("DELETE FROM trackerMap;");
    }
}
