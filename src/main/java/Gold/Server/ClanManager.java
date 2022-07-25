package Gold.Server;

import java.sql.*;
import java.util.TreeMap;

public class ClanManager {

    private static Connection connection;
    private static Statement statement;

    // буферная переменная для ускорения работы приложения. Без нее приложение работает в сотни раз медленнее
   private  static TreeMap<Long, Integer> clans;

    // здесь заполняем буфер данными из базы данных (ключ - clanId, value - gold)
    ClanManager() {
        try {
            connect();
            clans = new TreeMap<>();
            ResultSet rs = statement.executeQuery("SELECT * FROM clanMap");
            while (rs.next()){
                clans.put(rs.getLong("clanId"), rs.getInt("gold"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            disconnect();
        }
    }

    // получаем клан из буфера
    static Clan getClan(long clanId) {
        return new Clan(clanId, Integer.parseInt(clans.get(clanId).toString()));
    }

    // сохраняем клан в буфер
    static void saveClan(long clanId, int gold) {
        clans.replace(clanId, gold);
    }

    // обновляем базу данными из буфера
    static void updateDb() {
         try {
             connect();
             for (long i = 1; i <= clans.size(); i++) {
                 statement.executeUpdate(
                         "update clanMap SET gold = " + clans.get(i) + " WHERE clanId = " + i);
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
