package me.devsnox.battlekits.listeners;

import de.tr7zw.itemnbtapi.NBTItem;
import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.kits.frames.shard.ShardOpenFrame;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.utils.InventoryUtils;
import me.devsnox.itemprotection.api.ItemProtectionAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShardListener implements Listener {

    private KitManager kitManager;

    public ShardListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        NBTItem nbtItem = new NBTItem(itemStack);

        if (itemStack.getType() == Material.PRISMARINE_CRYSTALS) {
            if (nbtItem.hasKey("kitId")) {
                int id = nbtItem.getInteger("kitId");

                ItemProtectionAPI itemProtectionAPI = ItemProtectionAPI.getInstance();

                if (!ItemProtectionAPI.getInstance().alreadyUsed(itemStack)) {
                    if (this.kitManager.getKits().containsKey(id)) {
                        if (!this.kitManager.isOpening(player)) {
                            BattleKit battleKit = this.kitManager.getKits().get(id);

                            ShardOpenFrame shardOpenFrame = new ShardOpenFrame(this.kitManager, battleKit, nbtItem.getDouble("chance"));
                            shardOpenFrame.render(player);

                            itemProtectionAPI.validate(itemStack);
                            player.setItemInHand(new ItemStack(Material.AIR));

                            this.kitManager.getFrameListener().addFrame(shardOpenFrame);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem().getType() == Material.PRISMARINE_CRYSTALS) {
            if(event.getCursor().getType() == Material.GOLD_NUGGET) {
                ItemStack shard = event.getCurrentItem();
                ItemStack booster = event.getCursor();

                NBTItem nbtItem = new NBTItem(shard);
                NBTItem nbtItem2 = new NBTItem(booster);

                if(nbtItem.hasKey("chance") && nbtItem2.hasKey("boost")) {
                    nbtItem.setDouble("chance", (nbtItem.getDouble("chance") + nbtItem2.getDouble("boost")));
                    player.getInventory().setItem(event.getSlot(), nbtItem.getItem());

                    if(booster.getAmount() != 1) {
                        booster.setAmount(booster.getAmount() - 1);
                    } else {
                        booster.setType(Material.AIR);
                    }

                    player.getInventory().addItem(booster);
                }
            }
        }
    }
}
