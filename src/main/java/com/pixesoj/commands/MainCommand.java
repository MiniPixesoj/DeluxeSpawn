package com.pixesoj.commands;

import com.pixesoj.commands.tabcompleter.MainCommandTabCompleter;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.subcommands.*;
import com.pixesoj.utils.MessagesUtils;
import com.pixesoj.utils.common.SubCommand;
import com.pixesoj.utils.common.Updater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class MainCommand implements CommandExecutor {

    private final DeluxeSpawn plugin;
    private final Map<String, SubCommand> subCommands;

    public MainCommand(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
        plugin.getCommand("deluxespawn").setExecutor(this);
        plugin.getCommand("deluxespawn").setTabCompleter(new MainCommandTabCompleter(deluxeSpawn));

        this.subCommands = new HashMap<>();
        subCommands.put("help", new Help(deluxeSpawn));
        subCommands.put("lastlocation", new Lastlocation(deluxeSpawn));
        subCommands.put("reload", new Reload(deluxeSpawn));
        subCommands.put("update", new Update(deluxeSpawn));
        subCommands.put("version", new Version(deluxeSpawn));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand != null) {
            return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            return true;
        }
    }
}


