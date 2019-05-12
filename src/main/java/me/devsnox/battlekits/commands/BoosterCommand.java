package me.devsnox.battlekits.commands;

import me.devsnox.battlekits.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterCommand implements CommandExecutor {

    private KitManager kitManager;

    public BoosterCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if(player.hasPermission("battlekits.getbooster")) {
                if(args.length == 1) {
                    try {
                        player.getInventory().addItem(this.kitManager.getBooster(Double.valueOf(args[0])));
                        return true;
                    } catch (Exception ex) {
                    }
                }

                player.sendMessage("§cBenutze /" + command.getName() + " <id>");
            }
        }
        return false;
    }
}