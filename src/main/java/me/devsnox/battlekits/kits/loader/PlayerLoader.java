package me.devsnox.battlekits.kits.loader;

import me.devsnox.battlekits.kits.entites.KitPlayer;

import java.util.UUID;

public interface PlayerLoader {

    boolean existPlayer(UUID uuid);

    void removePlayer(UUID uuid);

    KitPlayer loadPlayer(UUID uuid);

    void savePlayer(KitPlayer kitPlayer);
}
