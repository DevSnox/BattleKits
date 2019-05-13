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
    public void init(final Player player, final InventoryContents contents) {
        contents.fill(ClickableItem.empty(Items.space(7)));
        contents.fillColumn(0, ClickableItem.empty(Items.space(15)));
        contents.fillColumn(8, ClickableItem.empty(Items.space(15)));

        this.renderTypes(contents, KitType.RARE);
    }

    @Override
    public void update(final Player player, final InventoryContents inventoryContents) {

    }

    private void renderTypes(final InventoryContents contents, final KitType target) {

        int i = 1;

        for (final KitType kitType : KitType.values()) {

            final NBTItem nbtItem = new NBTItem(
                    new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE))
                            .setDurability((short) kitType.getDurability())
                            .setDisplayName(kitType.getColor() + kitType.getName()).build()
            );

            nbtItem.setString("type", kitType.name());

            contents.set(SlotPos.of(0, i), ClickableItem.of(nbtItem.getItem(), event -> {
                if (!event.getCurrentItem().getItemMeta().hasEnchants()) {
                    this.renderTypes(contents, KitType.valueOf(new NBTItem(event.getCurrentItem()).getString("type")));
                }
            }));

            i += 2;
        }

        contents.fillRect(SlotPos.of(2, 1), SlotPos.of(4, 7),
                ClickableItem.empty(Items.space(target.getDurability())));
    }
}
