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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class KitEditorViewFrame extends Frame {

    private KitManager kitManager;

    public KitEditorViewFrame(KitManager kitManager) {
        super(54, "§a§lEditor: " + "§c§lKits", new FrameOptions(true));
        this.kitManager = kitManager;
        this.kitManager.getFrameListener().addFrame(this);
    }

    public void render(Player player, KitType target) {

        fill(7);


        int start = 11;

        for(int i = 0; i < 4; i++) {
            calc(start);
            start = start + 9;
        }


        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.STAINED_GLASS_PANE);

        int i = 9;

        for (KitType kitType : KitType.values()) {

            switch (i) {
                case 9:
                    itemBuilder.setDurability(3);
                    break;
                case 18:
                    itemBuilder.setDurability(2);
                    break;
                case 27:
                    itemBuilder.setDurability(1);
                    break;
                case 36:
                    itemBuilder.setDurability(14);
                    break;
            }

            itemBuilder.setDisplayName(kitType.getColor() + kitType.getName());

            if (target == kitType) {
                itemBuilder.setGlow(true);
                addKits(11, kitType);
            } else {
                itemBuilder.setGlow(false);
            }

            NBTItem nbtItem = new NBTItem(itemBuilder.build());

            nbtItem.setString("type", kitType.name());

            inventory.setItem(i, nbtItem.getItem());

            i = i + 9;
        }

        super.render(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        NBTItem nbtItem = new NBTItem(event.getCurrentItem());

        if (nbtItem.hasKey("kitId")) {
            int id = nbtItem.getInteger("kitId");

            Frame frame = new KitEditorEditKitItemsFrame(this.kitManager, this, this.kitManager.getKits().get(id));
            frame.render(((Player) event.getWhoClicked()).getPlayer());

            this.kitManager.getFrameListener().addFrame(frame);
        } else if(nbtItem.hasKey("type")) {
            if(!nbtItem.getItem().getItemMeta().hasEnchants()) {
                render((Player) event.getWhoClicked(), KitType.valueOf(nbtItem.getString("type")));
            }
        }

        super.onClick(event);
    }

    private void addKits(int start, KitType kitType) {
        ArrayList<BattleKit> kits = this.kitManager.getKitsByType(kitType);

        ItemBuilder itemBuilder = new ItemBuilder();

        for (int i = start; i < start + kits.size(); i++) {
            BattleKit battleKit = kits.get(i - start);

            itemBuilder.setType(battleKit.getIcon().getType());

            itemBuilder.setDisplayName(kitType.getColor() + kitType.getName() + " §8| " + kitType.getColor() + battleKit.getName());

            itemBuilder.addLoreLine("§aBeschreibung:");
            itemBuilder.addLoreLine("§7" + battleKit.getDescription());

            NBTItem nbtItem = new NBTItem(itemBuilder.build());
            nbtItem.setInteger("kitId", battleKit.getId());

            this.inventory.setItem(i, nbtItem.getItem());

            itemBuilder.setLore(new ArrayList<String>());
        }
    }

    private void calc(int start) {
        for(int i = start; i < start + 6; i++) {
            this.inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }
}
