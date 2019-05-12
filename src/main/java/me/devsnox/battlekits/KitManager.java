package me.devsnox.battlekits;

import de.tr7zw.itemnbtapi.NBTItem;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.entites.KitPlayer;
import me.devsnox.battlekits.kits.kit.KitType;
import me.devsnox.battlekits.kits.loader.BasicPlayerLoader;
import me.devsnox.battlekits.listeners.FrameListener;
import me.devsnox.battlekits.kits.loader.BasicKitLoader;
import me.devsnox.battlekits.kits.loader.PlayerLoader;
import me.devsnox.battlekits.utils.ItemBuilder;
import me.devsnox.itemprotection.api.ItemProtectionAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private Plugin plugin;

    private BasicKitLoader kitLoader;

    private PlayerLoader playerLoader;
    private Map<UUID, KitPlayer> playerCache;
    private ArrayList<Player> openingChache;

    private FrameListener frameListener;

    public KitManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        this.playerLoader = new BasicPlayerLoader(this, plugin.getDataFolder());
        this.playerCache = new HashMap<>();
        this.openingChache = new ArrayList<>();
        this.kitLoader = new BasicKitLoader(this.plugin.getDataFolder());
        kitLoader.loadKits();

        for(Player player : Bukkit.getOnlinePlayers()) {
            this.loadPlayer(player.getUniqueId());
        }
    }

    public void disable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            this.savePlayer(player.getUniqueId());
            player.closeInventory();
        }
        this.kitLoader.saveKits();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public KitPlayer getPlayer(UUID uuid) {
        return this.playerCache.get(uuid);
    }

    public void loadPlayer(UUID uuid) {
        this.playerCache.put(uuid, this.playerLoader.loadPlayer(uuid));
    }

    public void savePlayer(UUID uuid) {
        this.playerLoader.savePlayer(this.playerCache.get(uuid));
        this.playerCache.remove(uuid);
    }

    public Map<Integer, BattleKit> getKits() {
        return this.kitLoader.getKits();
    }


    public FrameListener getFrameListener() {
        return frameListener;
    }

    public void setFrameListener(FrameListener frameListener) {
        this.frameListener = frameListener;
    }

    public void updateKit(BattleKit battleKit) {
        this.kitLoader.updateKit(battleKit);
    }

    public ArrayList<BattleKit> getKitsByType(KitType kitType) {
        ArrayList<BattleKit> kits = new ArrayList<>();

        for(BattleKit battleKit : getKits().values()) {
            if(battleKit.getKitType() == kitType) {
                kits.add(battleKit);
            }
        }

        return kits;
    }

    public ItemStack getShard(int id) {
        BattleKit battleKit = this.getKits().get(id);

        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.PRISMARINE_CRYSTALS);
        itemBuilder.setGlow(true);

        itemBuilder.setDisplayName("§b§lShard §8- " + battleKit.getKitType().getColor() + battleKit.getName());

        NBTItem nbtItem = new NBTItem(itemBuilder.build());

        nbtItem.setInteger("kitId", id);
        nbtItem.setDouble("chance", battleKit.getKitType().getChance());

        return ItemProtectionAPI.getInstance().addProtection(nbtItem.getItem());
    }

    public ItemStack getBooster(double percentage) {
        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.GOLD_NUGGET);
        itemBuilder.setGlow(true);

        itemBuilder.setDisplayName("§6§lBooster §8- §7+ §b" + percentage + "§7%");

        NBTItem nbtItem = new NBTItem(itemBuilder.build());

        nbtItem.setDouble("boost", percentage);

        return nbtItem.getItem();
    }

    public void setPlayerOpening(Player player, boolean active) {
        if(active == true) {
            this.openingChache.add(player);
        } else {
            this.openingChache.remove(player);
        }
    }

    public boolean isOpening(Player player) {
        return this.openingChache.contains(player);
    }
}
