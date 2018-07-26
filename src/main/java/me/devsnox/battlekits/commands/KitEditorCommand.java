package me.devsnox.battlekits.commands;

import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.kits.frames.editor.KitEditorViewFrame;
import me.devsnox.battlekits.kits.kit.KitType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitEditorCommand implements CommandExecutor {

    private KitManager kitManager;

    public KitEditorCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if(player.hasPermission("battlekits.kiteditor")) {
                KitEditorViewFrame kitEditorViewFrame = new KitEditorViewFrame(this.kitManager);
                kitEditorViewFrame.render(player, KitType.RARE);
            }
        }
        return false;
    }
}
