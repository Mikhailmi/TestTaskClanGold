package Gold.Server;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// контроллер
public class ClanController {

    private ClanManager clanManager;
    private TrackerManager trackerManager;

    // конструктор для запуска приложения
    ClanController() throws InterruptedException {
        clanManager = new ClanManager();
        trackerManager = new TrackerManager();

        // создаем пул потоков и передаем задачи
        ExecutorService pool = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 100; i++) {
            pool.submit(() -> {
                try {
                    incGold(1l,1l,2);
                    incGold(2l,2l,2);
                    incGold(3l,3l,2);
                    incGold(4l,4l,2);
                    incGold(5l,5l,2);

                    decGold(1l,1l,1);
                    decGold(2l,2l,1);
                    decGold(3l,3l,1);
                    decGold(4l,4l,1);
                    decGold(5l,5l,1);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        // ждем одну секунду окончания работы потоков
        pool.awaitTermination(1, TimeUnit.SECONDS);

        // обновляем данные о количестве золота в базе данных кланов (переносим их из буфера в базу данных)
        ClanManager.updateDb();

        // переносим данные из буфера в базу данных трекеров
        TrackerManager.updateDb();

    }

    // добавление золота в клан
    public synchronized void incGold(long clanId, long userId, int gold) throws SQLException {
        Clan clan = ClanManager.getClan(clanId);
        clan.incGold(gold);
        ClanManager.saveClan(clanId, clan.getGold());
        // далее сохраняем в трекер пользователя и количество золота
        TrackerManager.trackGold(clanId, userId, gold);
    }

    // уменьшение золота в клане
    public synchronized void decGold(long clanId, long userId, int gold) throws Exception {
        Clan clan = ClanManager.getClan(clanId);
            if (clan.getGold() < gold) {
                throw new Exception();
            }
        clan.decGold(gold);
        ClanManager.saveClan(clanId, clan.getGold());
        // далее сохраняем в трекер пользователя и количество золота
        TrackerManager.trackGold(clanId, userId, -gold);
    }
}