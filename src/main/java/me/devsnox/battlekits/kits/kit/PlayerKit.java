package me.devsnox.battlekits.kits.kit;

import java.util.concurrent.TimeUnit;

public class PlayerKit {

    private int id;

    private BattleKit battleKit;

    private long lastReceive;

    public PlayerKit(int id, BattleKit battleKit, long lastReceive) {
        this.id = id;
        this.battleKit = battleKit;
        this.lastReceive = lastReceive;
    }

    public int getId() {
        return id;
    }

    public BattleKit getBattleKit() {
        return battleKit;
    }

    public long getLastReceive() {
        return lastReceive;
    }

    public boolean checkReceive() {
        if(this.lastReceive + TimeUnit.DAYS.toMillis(7) >= System.currentTimeMillis() ) {
            return true;
        }
        return false;
    }

    public void setLastReceive(long lastReceive) {
        this.lastReceive = lastReceive;
    }
}
