package me.devsnox.battlekits.kits.frames.editor;

import de.tr7zw.itemnbtapi.NBTItem;
import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.inventory.Frame;
import me.devsnox.battlekits.inventory.FrameOptions;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.kit.KitType;
import me.devsnox.battlekits.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class KitEditorEditKitItemsFrame extends Frame {

    private KitManager kitManager;

    private KitEditorViewFrame kitEditorViewFrame;
    private BattleKit battleKit;

    public KitEditorEditKitItemsFrame(KitManager kitManager, KitEditorViewFrame kitEditorViewFrame, BattleKit battleKit) {
        super(45, "§a§lEditor: " + battleKit.getKitType().getColor() + "§l" + battleKit.getName(), new FrameOptions(false));

        this.kitManager = kitManager;

        this.kitEditorViewFrame = kitEditorViewFrame;
        this.battleKit = battleKit;
    }

    @Override
    public void render(Player player) {

        fill(7);

        ItemBuilder itemBuilder = new ItemBuilder();

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
                this.updateKit();
                this.kitEditorViewFrame.render(player, KitType.RARE);
            }
        }

        if(event.getSlot() < 18) {
            event.setCancelled(true);
        }

        super.onClick(event);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        updateKit();
        super.onClose(event);
    }

    private void updateKit() {
        ArrayList<ItemStack> items = new ArrayList<>();

        for(int i = 18; i < this.inventory.getSize(); i++) {
            items.add(this.inventory.getItem(i));
        }

        battleKit.setItems(items);

        this.kitManager.updateKit(this.battleKit);
    }
}
