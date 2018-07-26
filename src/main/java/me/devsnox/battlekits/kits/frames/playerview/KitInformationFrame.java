package me.devsnox.battlekits.kits.frames.playerview;

import de.tr7zw.itemnbtapi.NBTItem;
import me.devsnox.battlekits.inventory.Frame;
import me.devsnox.battlekits.inventory.FrameOptions;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.kit.KitType;
import me.devsnox.battlekits.kits.kit.PlayerKit;
import me.devsnox.battlekits.utils.InventoryUtils;
import me.devsnox.battlekits.utils.ItemBuilder;
import me.devsnox.itemprotection.api.ItemProtectionAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class KitInformationFrame extends Frame {

    private KitSelectionFrame kitSelectionFrame;

    private PlayerKit playerKit;
    private BattleKit battleKit;

    public KitInformationFrame(KitSelectionFrame kitSelectionFrame, PlayerKit playerKit) {
        super(45, playerKit.getBattleKit().getKitType().getColor() + "§l" + playerKit.getBattleKit().getName(), new FrameOptions(true));

        this.kitSelectionFrame = kitSelectionFrame;
        this.playerKit = playerKit;
        this.battleKit = playerKit.getBattleKit();
    }

    @Override
    public void render(Player player) {

        fill(7);

        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.WATCH);

        boolean receive = playerKit.checkReceive();

        if (receive) {
            itemBuilder.setGlow(true);
            itemBuilder.setDisplayName("§aAbholbar!");
            itemBuilder.addLoreLine("§7Du kannst dein Kit einlösen!");
        } else {
            itemBuilder.setDisplayName("§cNicht abholbar!");
            itemBuilder.addLoreLine("§a7 §5Tage§8(§7n§8) §a2 §7Stunde§8(§7n§8) §7und §a15 §7Minute§8(§7n§8)");
        }

        NBTItem nbtItem = new NBTItem(itemBuilder.build());

        if(receive) {
            nbtItem.setBoolean("receive", true);
            nbtItem.setInteger("kitId", this.battleKit.getId());
        } else {
            nbtItem.setBoolean("receive", false);
        }

        inventory.setItem(1, nbtItem.getItem());

        itemBuilder.setGlow(false);

        itemBuilder.removeLoreLine(0);
        itemBuilder.setType(battleKit.getIcon().getType());

        itemBuilder.setDisplayName(battleKit.getKitType().getColor() + battleKit.getName());
        itemBuilder.addLoreLine(battleKit.getDescription());

        this.inventory.setItem(4, itemBuilder.build());

        itemBuilder.setDisplayName("§cZurück");
        itemBuilder.setType(Material.REDSTONE);
        itemBuilder.setLore(new ArrayList<>());

        NBTItem nbtItem2 = new NBTItem(itemBuilder.build());

        nbtItem2.setBoolean("back", true);

        this.inventory.setItem(7, nbtItem2.getItem());

        for (int i = 18; i < 27 + 18; i++) {
            if(i - 18 < this.battleKit.getItems().size()) {
                inventory.setItem(i, this.battleKit.getItems().get(i - 18));
            } else {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }

        super.render(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = ((Player) event.getWhoClicked()).getPlayer();

            ItemStack itemStack = event.getCurrentItem();

            NBTItem nbtItem = new NBTItem(itemStack);

            if (nbtItem.hasKey("back")) {
                this.kitSelectionFrame.render(player, KitType.RARE);
            } else if(nbtItem.hasKey("receive")) {
                if(nbtItem.getBoolean("receive")) {
                    if(InventoryUtils.hasSpace(this.inventory)) {
                        ItemBuilder itemBuilder = new ItemBuilder();

                        itemBuilder.setType(Material.STORAGE_MINECART);
                        itemBuilder.setGlow(true);

                        KitType kitType = this.battleKit.getKitType();
                        itemBuilder.setDisplayName(kitType.getColor() + "§7§k||§r" + kitType.getColor() + kitType.getName() + "§7§k||§r §8(§7§lKit§8) " + kitType.getColor() + battleKit.getName());

                        NBTItem kitItemNBT = new NBTItem(itemBuilder.build());

                        kitItemNBT.setInteger("kitId", this.battleKit.getId());

                        player.getInventory().addItem(ItemProtectionAPI.getInstance().addProtection(kitItemNBT.getItem()));
                    } else {
                        player.sendMessage("§cDu hast keinen Platz in deinem Inventar!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Du kannst dieses Kit das nächste mal in §a7 §5Tage§8(§7n§8) §a2 §7Stunde§8(§7n§8) §7und §a15 §7Minute§8(§7n§8) abholen!");
                }

                player.closeInventory();
            }
        }

        super.onClick(event);
    }
}
