package me.devsnox.battlekits.listeners;

import de.tr7zw.itemnbtapi.NBTItem;
import me.devsnox.battlekits.KitManager;
import me.devsnox.itemprotection.api.ItemProtectionAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class KitListener extends FrameListener {

    private KitManager kitManager;

    public KitListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block block = event.getClickedBlock().getRelative(BlockFace.UP);
            ItemStack itemStack = event.getItem();

            if(itemStack == null) return;

            if(itemStack.getType() == Material.STORAGE_MINECART) {
                NBTItem nbtItem = new NBTItem(itemStack);

                if(nbtItem.hasKey("kitId")) {
                    int id = nbtItem.getInteger("kitId");

                    if(this.kitManager.getKits().containsKey(id)) {
                        ItemProtectionAPI protectionAPI = ItemProtectionAPI.getInstance();

                        if(!protectionAPI.alreadyUsed(itemStack)) {
                            block.setType(Material.CHEST);

                            Chest chest = (Chest) block.getState();
                            ArrayList<ItemStack> items = this.kitManager.getKits().get(id).getItems();

                            for(int i = 0; i < items.size(); i++) {
                                chest.getBlockInventory().setItem(i, items.get(i));
                            }

                            protectionAPI.validate(itemStack);

                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    }

                    event.setCancelled(true);
                }
            }
        }
    }
}
