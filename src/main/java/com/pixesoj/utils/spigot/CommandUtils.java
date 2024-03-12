package com.pixesoj.utils.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandUtils {

    public static void executeCommands(CommandSender sender,  boolean enabled, List<String> cmdPlayer, List<String> cmdConsole) {
        if (enabled) {

            Player player = (Player) sender;
            for (String command : cmdPlayer) {
                String replacedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(sender, replacedCommand);
            }

            CommandSender consoleSender = Bukkit.getConsoleSender();
            for (String command : cmdConsole) {
                String replacedCommand = command.replace("%player%", sender.getName());
                Bukkit.dispatchCommand(consoleSender, replacedCommand);
            }
        }
    }
}
