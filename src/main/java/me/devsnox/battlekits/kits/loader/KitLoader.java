package me.devsnox.battlekits.kits.loader;

import me.devsnox.battlekits.kits.kit.BattleKit;

import java.util.Map;

public interface KitLoader {

    void loadKits();

    void saveKits();

    void updateKit(BattleKit battleKit);

    Map<Integer, BattleKit> getKits();
}
