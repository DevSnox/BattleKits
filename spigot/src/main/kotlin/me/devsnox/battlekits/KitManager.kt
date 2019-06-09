package me.devsnox.battlekits

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.functions.KitsB
import me.devsnox.battlekits.functions.OpeningChache
import me.devsnox.battlekits.functions.PlayerCache
import me.devsnox.battlekits.kits.entites.KitPlayer
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.KitType
import me.devsnox.battlekits.kits.loader.KitLoader
import me.devsnox.battlekits.kits.loader.PlayerLoader
import me.devsnox.itemprotection.api.ItemProtectionFactory
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.glow
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.Utils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.*

class KitManager(
    val plugin: Plugin,
    private val players: Collection<Player> = Utils.players
) {

    private var playerCache: PlayerCache = mutableMapOf()
    private var openingChache: OpeningChache = mutableListOf()
    private var playerLoader: PlayerLoader = PlayerLoader(this)
    private var kitLoader: KitLoader = KitLoader(plugin)

    val kits: KitsB get() = kitLoader.kits

    suspend fun enable() {
        kitLoader.loadKits()

        for (player in players) loadPlayer(player.uniqueId)
    }

    suspend fun disable() {
        for (player in players) {
            savePlayer(player.uniqueId)
            player.closeInventory()
        }
        kitLoader.saveKits()
    }

    fun getPlayer(uuid: UUID): KitPlayer? = playerCache[uuid]

    suspend fun loadPlayer(uuid: UUID) {
        playerCache[uuid] = playerLoader.loadPlayer(uuid)
    }

    suspend fun savePlayer(uuid: UUID) {
        val kitPlayer = playerCache[uuid] ?: return
        playerLoader.savePlayer(kitPlayer)
        playerCache.remove(uuid)
    }

    fun updateKit(battleKit: BattleKit): Unit = kitLoader.updateKit(battleKit)

    fun getKitsByType(kitType: KitType): List<BattleKit> = kits.values.filter { it.kitType == kitType }

    fun getShard(id: Int): ItemStack {
        val battleKit = kits[id] ?: return ItemStack(Material.BARRIER)
        val itemBuilder = ItemBuilder(Material.PRISMARINE_CRYSTALS).glow()
            .setDisplayName("§b§lShard §8- ${battleKit.kitType.color}${battleKit.name}")

        val nbtItem = NBTItem(itemBuilder.build())
        nbtItem.setInteger("kitId", id)
        nbtItem.setDouble("chance", battleKit.kitType.chance)
        return ItemProtectionFactory.itemProtection!!.addProtection(nbtItem.item)
    }

    fun getBooster(percentage: Double): ItemStack {
        val itemBuilder = ItemBuilder(Material.GOLD_NUGGET).glow()
            .setDisplayName("§6§lBooster §8- §7+ §b$percentage§7%")
        return NBTItem(itemBuilder.build()).apply { setDouble("boost", percentage) }.item
    }

    fun setPlayerOpening(player: Player, active: Boolean) {
        if (active) openingChache.add(player) else openingChache.remove(player)
    }

    fun isOpening(player: Player): Boolean = player in openingChache
}
