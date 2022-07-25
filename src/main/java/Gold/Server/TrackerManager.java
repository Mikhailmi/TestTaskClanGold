package Gold.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackerManager {

    private static Connection connection;
    private static Statement statement;

    // список для временного хранения трекеров перед их записью в базу данных
    private static List<TrackerMap> trackerMap;

    // вложенный класс для заполнения трекера из трех переменных
     private static class TrackerMap{
     private long clanId;
     private long userId;
     private int gold;

       TrackerMap(long clanId, long userId, int gold) {
            this.clanId = clanId;
            this.userId = userId;
            this.gold = gold;
        }

        private long getClanId() {
            return clanId;
        }

        private long getUserId() {
            return userId;
        }

        private int getGold() {
            return gold;
        }
    }

    TrackerManager() {
        trackerMap = new ArrayList<>();
    }

    // сохраняем данные в список для временного хранения
     static void trackGold(long clanId, long userId, int gold)  {
        trackerMap.add(new TrackerMap(clanId, userId, gold));
    }

    // обновляем базу данными из буфера
    static void updateDb() {
         try {
            connect();
             try (PreparedStatement ps = connection.prepareStatement(
                     "insert or replace into trackerMap (clanId, userId, gold) " +
                             "values (?, ?, ?)")) {
                 for (int i = 0; i < trackerMap.size(); i++) {
                     ps.setLong(1, trackerMap.get(i).getClanId());
                     ps.setLong(2, trackerMap.get(i).getUserId());
                     ps.setInt(3, trackerMap.get(i).getGold());
                     ps.addBatch();
                 }
                 ps.executeBatch();
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
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
}