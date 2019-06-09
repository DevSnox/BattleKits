package me.devsnox.battlekits.kits.frames.shard

import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.javaPlugin
import me.devsnox.battlekits.inventory.Frame
import me.devsnox.battlekits.kits.kit.BattleKit
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.glow
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.schedule
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.setGlass
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class ShardOpenFrame(
    private val kitManager: KitManager,
    private val battleKit: BattleKit,
    private val chance: Double,
    private val plugin: Plugin = javaPlugin
) : Frame(27, "§b§lShard", true) {

    override fun render(player: Player) {
        kitManager.setPlayerOpening(player, true)
        val items = ArrayList<ItemStack>()
        for (i in 0..8) {
            val itemBuilder = if (getRandomPercentage(chance)) {
                val kitType = battleKit.kitType
                ItemBuilder(Material.PRISMARINE_CRYSTALS).glow()
                    .setDisplayName("${kitType.color}§7§k||§r${kitType.color}${kitType.displayName}§7§k||§r §8(§7§lKit§8) ${kitType.color}${battleKit.name}")
            } else ItemBuilder(Material.DEAD_BUSH).setDisplayName("§c§lPech :(")
            items.add(itemBuilder.build())
        }
        for (i in 0..26) if (i < 9 || i > 17) inventory.setItem(
            i,
            ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toByte().toShort())
        )
        inventory.setItem(4, ItemStack(Material.STAINED_GLASS_PANE, 1, 10.toByte().toShort()))
        inventory.setItem(22, ItemStack(Material.HOPPER))
        inventory.setItem(9, items[0])
        object : BukkitRunnable() {
            var count = 0
            var itemcount = 1

            override fun run() {
                if (count == 7) {
                    object : BukkitRunnable() {
                        override fun run() {
                            object : BukkitRunnable() {
                                var delay = 0.0
                                var ticks = 0
                                var location = 4
                                var location2 = 22
                                var status = 1

                                override fun run() {
                                    ticks += 1
                                    delay += 1.0 / (20.0 * ThreadLocalRandom.current().nextInt(10, 15))
                                    if (ticks > delay * 10.0) {
                                        ticks = 0

                                        inventory.setGlass(location, 7)
                                        inventory.setGlass(location2, 7)
                                        if (location == 8) status = 1
                                        if (location == 0) status = 0
                                        if (status == 0) {
                                            location++
                                            location2++
                                        }
                                        if (status == 1) {
                                            location--
                                            location2--
                                        }
                                        inventory.setGlass(location, 10)
                                        inventory.setItem(location2, ItemStack(Material.HOPPER))
                                        if (delay >= 1.0) {
                                            inventory.setGlass(location, 10)
                                            inventory.setGlass(location2, 10)
                                            for (i in 9..17) if (i == location + 9) continue else {
                                                inventory.setItem(i, ItemStack(Material.AIR))
                                                player.playSound(player.location, Sound.LEVEL_UP, 9999f, 9999f)
                                            }
                                            plugin.schedule(delay = 30L) {
                                                var count = 0
                                                plugin.schedule(period = 10L) {
                                                    if (count == 8) {
                                                        plugin.schedule {
                                                            kitManager.getPlayer(player.uniqueId)!!.addKit(
                                                                battleKit
                                                            )
                                                            kitManager.setPlayerOpening(player, false)
                                                            player.closeInventory()
                                                        }
                                                        cancel()
                                                    }
                                                    val itemStack = inventory.getItem(location)
                                                    val itemStack2 = inventory.getItem(location2)
                                                    if (itemStack.durability.toInt() == 5) {
                                                        itemStack.durability = 14.toShort()
                                                        itemStack2.durability = 14.toShort()
                                                    } else {
                                                        itemStack.durability = 5.toShort()
                                                        itemStack2.durability = 5.toShort()
                                                    }
                                                    count++
                                                }
                                            }
                                            cancel()
                                        }
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L)
                        }
                    }.runTaskLater(plugin, 20L)
                    cancel()
                }
                for (i in 17 downTo 9) {
                    val itemStack = inventory.getItem(i) ?: continue
                    if (itemStack.type == Material.AIR) continue
                    inventory.setItem(i + 1, itemStack)
                    if (i == 9) inventory.setItem(9, items[itemcount])
                }
                itemcount++
                count++
            }
        }.runTaskTimer(plugin, 3L, 3L)
        player.openInventory(inventory)
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        if (kitManager.isOpening(player)) plugin.schedule(delay = 20) { player.openInventory(inventory) }
    }

    private fun getRandomPercentage(percentage: Double): Boolean =
        ThreadLocalRandom.current().nextDouble() * 100 <= percentage
}
