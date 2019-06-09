package me.devsnox.battlekits.kits.loader

import me.devsnox.battlekits.functions.KitsB
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.KitType
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.io.IOException
import java.util.*

//todo change to json
class KitLoader(plugin: Plugin) {

    private val configData = plugin.dataFolder.toConfigData("config", suffix = ".yml")
    private val yamlConfiguration: YamlConfiguration

    val kits: KitsB = mutableMapOf()

    init {

        yamlConfiguration = YamlConfiguration.loadConfiguration(configData.file)

        try {

            kits[1] = BattleKit(
                1,
                "test",
                ItemStack(Material.DIAMOND_AXE),
                "Werde zum Ritter!",
                KitType.EPIC,
                listOf(ItemStack(Material.GOLD_BLOCK))
            )

            saveKits()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun loadKits() {
        for (name in yamlConfiguration.getKeys(false)) {
            val mainSection = yamlConfiguration.getConfigurationSection(name)
            val itemSection = mainSection.getConfigurationSection("items")

            val items = ArrayList<ItemStack>()

            for (item in itemSection.getKeys(false)) {
                items.add(itemSection.getItemStack(item))
            }

            kits[mainSection.getInt("id")] = BattleKit(
                mainSection.getInt("id"),
                name,
                ItemStack(Material.valueOf(mainSection.getString("icon"))),
                mainSection.getString("description"),
                KitType.valueOf(mainSection.getString("rarity").toUpperCase()),
                items
            )
        }
    }

    fun saveKits() {
        for (battleKit in kits.values) {
            val mainSection = yamlConfiguration.createSection(battleKit.name)

            mainSection.set("rarity", battleKit.kitType.toString())
            mainSection.set("id", battleKit.id)
            mainSection.set("icon", battleKit.icon.type.toString())
            mainSection.set("description", battleKit.description)

            val itemSection = mainSection.createSection("items")

            for (i in 0 until battleKit.items.size) itemSection.set(i.toString(), battleKit.items[i])
        }

        try {
            yamlConfiguration.save(configData.file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun updateKit(battleKit: BattleKit) {
        kits.replace(battleKit.id, battleKit)
    }
}
