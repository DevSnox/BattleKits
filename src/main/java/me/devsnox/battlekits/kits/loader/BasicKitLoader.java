package me.devsnox.battlekits.kits.loader;

import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.kit.KitType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BasicKitLoader implements KitLoader {

    private File config;
    private YamlConfiguration yamlConfiguration;

    private HashMap<Integer, BattleKit> kits;

    public BasicKitLoader(File dataFolder) {

        this.config = new File(dataFolder + File.separator + "config.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.config);

        this.kits = new HashMap<>();

        try {

            if (this.config.exists()) {
                return;
            }

            if (!dataFolder.exists()) {
                dataFolder.createNewFile();
            }

            this.config.createNewFile();

            this.kits.put(1, new BattleKit(1, "test", Material.DIAMOND_AXE, "Werde zum Ritter!", KitType.EPIC, new ArrayList<>(Arrays.asList(new ItemStack(Material.GOLD_BLOCK)))));

            saveKits();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadKits() {
        for (String name : this.yamlConfiguration.getKeys(false)) {
            ConfigurationSection mainSection = this.yamlConfiguration.getConfigurationSection(name);
            ConfigurationSection itemSection = mainSection.getConfigurationSection("items");

            ArrayList<ItemStack> items = new ArrayList<ItemStack>();

            for (String item : itemSection.getKeys(false)) {
                items.add(itemSection.getItemStack(item));
            }

            this.kits.put(mainSection.getInt("id"), new BattleKit(mainSection.getInt("id"), name, Material.valueOf(mainSection.getString("icon")), mainSection.getString("description"), KitType.valueOf(mainSection.getString("rarity").toUpperCase()), items));
        }
    }

    @Override
    public void saveKits() {
        for (BattleKit battleKit : this.kits.values()) {
            ConfigurationSection mainSection = this.yamlConfiguration.createSection(battleKit.getName());

            mainSection.set("rarity", battleKit.getKitType().toString());
            mainSection.set("id", battleKit.getId());
            mainSection.set("icon", battleKit.getIcon().getType().toString());
            mainSection.set("description", battleKit.getDescription());

            ConfigurationSection itemSection = mainSection.createSection("items");

            for (int i = 0; i < battleKit.getItems().size(); i++) {
                itemSection.set(String.valueOf(i), battleKit.getItems().get(i));
            }
        }

        try {
            this.yamlConfiguration.save(this.config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Integer, BattleKit> getKits() {
        return kits;
    }

    @Override
    public void updateKit(BattleKit battleKit) {
        this.kits.replace(battleKit.getId(), battleKit);
    }
}
