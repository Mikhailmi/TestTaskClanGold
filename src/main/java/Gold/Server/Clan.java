package Gold.Server;

// шаблон клана
class Clan {

    private long clanId;
    private String name;
    private int gold;

    public int getGold() {
        return gold;
    }

    // конструктор для записи клана в базу данных
    public Clan (long clanId, String name, int gold) {
        this.clanId = clanId;
        this.name = name;
        this.gold = gold;
    }

    // конструктор для сохранения клана в буферную переменную при изменении количества золота в клане
    public Clan(long clanId, int gold) {
        this.clanId = clanId;
        this.gold = gold;
    }

    // метод добавления золота в клан
    public void incGold(int gold) {
        this.gold += gold;
    }

    // метод уменьшения золота в клане
    public void decGold(int gold){
        this.gold -=gold;
    }
}