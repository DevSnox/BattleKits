package me.devsnox.battlekits.kits.entites;

import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.kit.PlayerKit;

import java.util.HashMap;
import java.util.UUID;

public class KitPlayer {

    private UUID uuid;

    private HashMap<Integer, PlayerKit> kits;

    public KitPlayer(UUID uuid, HashMap<Integer, PlayerKit> kits) {
        this.uuid = uuid;
        this.kits = kits;
    }

    public void addKit(BattleKit battleKit) {
        this.kits.put(battleKit.getId(), new PlayerKit(battleKit.getId(), battleKit, 0));
    }

    public void removeKit(BattleKit battleKit) {
        this.kits.remove(battleKit.getId());
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<Integer, PlayerKit> getKits() {
        return kits;
    }
}
