package me.devsnox.battlekits.commands;

import fr.minuskube.inv.SmartInventory;
import me.devsnox.battlekits.inventory.provider.BasicKitInventoryProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by DevSnox on 12.02.18
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
public class PreviewTest implements CommandExecutor {


    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        SmartInventory.builder()
                .provider(new BasicKitInventoryProvider())
                .size(6, 9)
                .build().open((Player) sender);
        return false;
    }
}
