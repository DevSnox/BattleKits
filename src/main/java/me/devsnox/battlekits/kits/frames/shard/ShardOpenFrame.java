package me.devsnox.battlekits.kits.frames.shard;

import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.inventory.Frame;
import me.devsnox.battlekits.inventory.FrameOptions;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.kit.KitType;
import me.devsnox.battlekits.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ShardOpenFrame extends Frame {

    private Plugin plugin;

    private KitManager kitManager;

    private BattleKit battleKit;
    private double chance;

    public ShardOpenFrame(KitManager kitManager, BattleKit battleKit, double chance) {
        super(27, "§b§lShard", new FrameOptions(true));
        this.kitManager = kitManager;
        this.plugin = kitManager.getPlugin();
        this.battleKit = battleKit;
        this.chance = chance;
    }

    @Override
    public void render(Player player) {

        kitManager.setPlayerOpening(player, true);

        ArrayList<ItemStack> items = new ArrayList<>();

        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.DEAD_BUSH);

        for (int i = 0; i < 9; i++) {
            if(getRandomPercentage(this.chance)) {
                itemBuilder.setType(Material.PRISMARINE_CRYSTALS);
                itemBuilder.setGlow(true);
                KitType kitType = this.battleKit.getKitType();
                itemBuilder.setDisplayName(kitType.getColor() + "§7§k||§r" + kitType.getColor() + kitType.getName() + "§7§k||§r §8(§7§lKit§8) " + kitType.getColor() + battleKit.getName());
            } else {
                itemBuilder.setType(Material.DEAD_BUSH);
                itemBuilder.setDisplayName("§c§lPech :(");
                itemBuilder.setGlow(false);
            }

            items.add(itemBuilder.build());
        }

        for (int i = 0; i != 27; i++) {
            if(i < 9 || i > 17) {
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            }
        }

        inventory.setItem(4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10));

        inventory.setItem(22, new ItemStack(Material.HOPPER));

        inventory.setItem(9, items.get(0));

        new BukkitRunnable() {

            int count = 0;

            int itemcount = 1;

            @Override
            public void run() {
                if (count == 7) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            new BukkitRunnable() {
                                double delay = 0.0D;
                                int ticks = 0;
                                boolean done = false;
                                int location = 4;
                                int location2 = 22;
                                int status = 1;

                                public void run() {
                                    if (this.done) {
                                        return;
                                    }
                                    this.ticks += 1;
                                    this.delay += 1.0D / (20.0D * ThreadLocalRandom.current().nextInt(10, 15));
                                    if (this.ticks > this.delay * 10.0D) {
                                        this.ticks = 0;
                                        inventory.setItem(location, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
                                        inventory.setItem(location2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));

                                        if (location == 8) {
                                            status = 1;
                                        }

                                        if (location == 0) {
                                            status = 0;
                                        }

                                        if (status == 0) {
                                            location++;
                                            location2++;
                                        }

                                        if (status == 1) {
                                            location--;
                                            location2--;
                                        }

                                        inventory.setItem(location, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10));
                                        inventory.setItem(location2, new ItemStack(Material.HOPPER));
                                        if (this.delay >= 1.0D) {
                                            inventory.setItem(location, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10));
                                            inventory.setItem(location2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 10));
                                            for (int i = 9; i != 18; i++) {
                                                if (i == location + 9) {
                                                    continue;
                                                } else {
                                                    inventory.setItem(i, new ItemStack(Material.AIR, i));
                                                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 9999, 9999);
                                                }
                                            }
                                            new BukkitRunnable() {

                                                @Override
                                                public void run() {
                                                    new BukkitRunnable() {

                                                        int count = 0;

                                                        @Override
                                                        public void run() {
                                                            if (count == 8) {
                                                                new BukkitRunnable() {

                                                                    @Override
                                                                    public void run() {
                                                                        kitManager.getPlayer(player.getUniqueId()).addKit(battleKit);
                                                                        kitManager.setPlayerOpening(player, false);

                                                                        player.closeInventory();
                                                                    }
                                                                }.runTask(plugin);
                                                                cancel();
                                                            }

                                                            ItemStack itemStack = inventory.getItem(location);
                                                            ItemStack itemStack2 = inventory.getItem(location2);

                                                            if (itemStack.getDurability() == 5) {
                                                                itemStack.setDurability((short) 14);
                                                                itemStack2.setDurability((short) 14);
                                                            } else {
                                                                itemStack.setDurability((short) 5);
                                                                itemStack2.setDurability((short) 5);
                                                            }
                                                            count++;
                                                        }
                                                    }.runTaskTimer(plugin, 0L, 10L);
                                                }
                                            }.runTaskLater(plugin, 30L);
                                            cancel();
                                        }
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 1L);

                        }
                    }.runTaskLater(plugin, 20L);
                    cancel();
                }

                for (int i = 17; i > 8; i--) {
                    if(inventory.getItem(i) == null) continue;
                    if(inventory.getItem(i).getType() == Material.AIR) continue;

                    ItemStack itemStack = inventory.getItem(i);

                    inventory.setItem(i + 1, itemStack);

                    if(i == 9) {
                        inventory.setItem(9, items.get(itemcount));
                    }
                }

                itemcount++;
                count++;
            }
        }.runTaskTimer(plugin, 3L, 3L);

        player.openInventory(inventory);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(this.kitManager.isOpening(player)) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.openInventory(inventory);
                }
            }.runTaskLater(plugin, 20L);
        }
    }

    public boolean getRandomPercentage(double percentage) {
        double percent = (ThreadLocalRandom.current().nextDouble() * 100);

        if(percent <= percentage) {
            return true;
        }

        return false;
    }
}
