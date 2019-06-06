package me.devsnox.battlekits.kits.frames.playerview

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.functions.backItem
import me.devsnox.battlekits.functions.filLBattle
import me.devsnox.battlekits.functions.hasSpace
import me.devsnox.battlekits.inventory.Frame
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.KitType
import me.devsnox.battlekits.kits.kit.PlayerKit
import me.devsnox.itemprotection.api.ItemProtectionFactory
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.glow
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class KitInformationFrame(private val kitSelectionFrame: KitSelectionFrame, private val playerKit: PlayerKit) :
    Frame(45, "${playerKit.battleKit.kitType.color}§l${playerKit.battleKit.name}", true) {
    private val battleKit: BattleKit = playerKit.battleKit

    override fun render(player: Player) {
        fill()
        val itemBuilder = ItemBuilder(Material.WATCH)
        val receive = playerKit.checkReceive()
        if (receive) itemBuilder
            .setDisplayName("§aAbholbar!")
            .addLore("§7Du kannst dein Kit einlösen!")
            .glow()
        else itemBuilder
            .setDisplayName("§cNicht abholbar!")
            .addLore("§a7 §5Tage§8(§7n§8) §a2 §7Stunde§8(§7n§8) §7und §a15 §7Minute§8(§7n§8)")

        val nbtItem = NBTItem(itemBuilder.build()).apply { setBoolean("receive", receive) }
        if (receive) nbtItem.setInteger("kitId", battleKit.id)

        inventory.setItem(1, nbtItem.item)

//        itemBuilder.setGlow(false)
        itemBuilder.removeLore(0)
            .setType(battleKit.icon.type)
            .setDisplayName("${battleKit.kitType.color}${battleKit.name}")
            .addLore(battleKit.description)
        inventory.setItem(4, itemBuilder.build())
        inventory.setItem(7, backItem)
        inventory.filLBattle(battleKit.items)
        super.render(player)
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val itemStack = event.currentItem ?: return
        val nbtItem = NBTItem(itemStack)
        if (nbtItem.hasKey("back")) {
            kitSelectionFrame.render(player, KitType.RARE)
        } else if (nbtItem.hasKey("receive")) {
            if (nbtItem.getBoolean("receive")) {
                if (inventory.hasSpace()) {
                    val itemBuilder = ItemBuilder(Material.STORAGE_MINECART).glow()
                    val kitType = battleKit.kitType
                    itemBuilder.setDisplayName("${kitType.color}§7§k||§r${kitType.color}${kitType.displayName}§7§k||§r §8(§7§lKit§8) ${kitType.color}${battleKit.name}")
                    val kitItemNBT = NBTItem(itemBuilder.build())
                    kitItemNBT.setInteger("kitId", battleKit.id)
                    player.inventory.addItem(ItemProtectionFactory.itemProtection?.addProtection(kitItemNBT.item))
                } else {
                    player.sendMessage("§cDu hast keinen Platz in deinem Inventar!")
                }
            } else {
                player.sendMessage("${ChatColor.RED}Du kannst dieses Kit das nächste mal in §a7 §5Tage§8(§7n§8) §a2 §7Stunde§8(§7n§8) §7und §a15 §7Minute§8(§7n§8) abholen!")
            }
            player.closeInventory()
        }
        super.onClick(event)
    }
}
