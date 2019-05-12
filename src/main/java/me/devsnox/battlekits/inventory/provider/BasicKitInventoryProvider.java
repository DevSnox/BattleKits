package me.devsnox.battlekits.inventory.provider;

import de.tr7zw.itemnbtapi.NBTItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import me.devsnox.battlekits.kits.kit.KitType;
import me.devsnox.battlekits.utils.Items;
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BasicKitInventoryProvider implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(1, ClickableItem.empty(Items.space(7)));
        contents.fillRow(2, ClickableItem.empty(Items.space(7)));
        contents.fillRow(3, ClickableItem.empty(Items.space(7)));
        contents.fillColumn(1, ClickableItem.empty(Items.space(15)));
        contents.fillColumn(9, ClickableItem.empty(Items.space(15)));

        this.renderTypes(contents, KitType.RARE);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private void renderTypes(InventoryContents contents, KitType target) {

        int i = 2;

        for (KitType kitType : KitType.values()) {

            NBTItem nbtItem = new NBTItem(
                    new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE))
                    .setDurability((short) kitType.getDurability())
                    .setDisplayName(kitType.getColor() + kitType.getName()).build()
            );

            nbtItem.setString("type", kitType.name());

            contents.set(SlotPos.of(1, i), ClickableItem.of(nbtItem.getItem(), event -> {
                if(!event.getCurrentItem().getItemMeta().hasEnchants()) {
                    renderTypes(contents, KitType.valueOf(new NBTItem(event.getCurrentItem()).getString("type")));
                }
            }));

            i += 2;
        }

        contents.fillRect(SlotPos.of(3, 2), SlotPos.of(5, 8),
                ClickableItem.empty(Items.space(target.getDurability())));
    }
}
