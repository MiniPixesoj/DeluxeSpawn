package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private DeluxeSpawn plugin;
    public MainCommand (DeluxeSpawn deluxeSpawn){
        this.plugin = deluxeSpawn;
        plugin.getCommand("deluxespawn").setExecutor(this);
        plugin.getCommand("deluxespawn").setTabCompleter(new MainCommandTabCompleter());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            // Consola
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("version")){
                    String latestVersion = this.plugin.getUpdateCheckerManager().getLatestVersion();
                    String message = plugin.getMainMessagesManager().getCommandVersion().replace("%version%", plugin.getDescription().getVersion()).replace("%last_version%", latestVersion);
                    sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + message));
                }else if(args[0].equalsIgnoreCase("help")) {
                    help(sender);
                } else if(args[0].equalsIgnoreCase("reload")){
                    reload(sender, args);

                }else{
                    sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                    sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
                }
            }else{
                sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));

            }
            return true;
        }

        Player player = (Player) sender;

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("version")){
                String latestVersion = this.plugin.getUpdateCheckerManager().getLatestVersion();
                String message = plugin.getMainMessagesManager().getCommandVersion().replace("%version%", plugin.getDescription().getVersion()).replace("%last_version%", latestVersion);
                sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + message));
            }else if(args[0].equalsIgnoreCase("help")) {
                help(sender);
            }else if(args[0].equalsIgnoreCase("reload")){
                if (!sender.hasPermission("deluxespawn.command.reload")){
                    sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getPermissionDenied()));
                    return true;
                }
                reload(sender, args);

            }else{
                sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            }
        }else{
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));

        }
        return true;
    }

    public void help (CommandSender sender){
        List<String> message = plugin.getMainMessagesManager().getCommandHelp();
        for(String m : message){
            sender.sendMessage(MessagesUtils.getColoredMessage(m));
        }
    }

    public void reload (CommandSender sender, String[] args) {
        if(args.length == 1){
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandInvalidArgument() + " &a/deluxespawn reload <config|messages|all>"));
            return;
        }
        if(args[1].equalsIgnoreCase("config")){
            plugin.getMainConfigManager().reloadConfig();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadConfig()));
        } else if (args[1].equalsIgnoreCase("messages")) {
            plugin.getMainMessagesManager().reloadMessages();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadMessages()));

        } else if (args[1].equalsIgnoreCase("all")) {
            plugin.getMainMessagesManager().reloadMessages();
            plugin.getMainConfigManager().reloadConfig();
            plugin.getLocationsManager().saveLocationsFile();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadAll().replace("%version%", plugin.getDescription().getVersion())));

        } else {
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandInvalidArgument() + " &a/deluxespawn reload <config|messages|all>"));

        }
    }
}

class MainCommandTabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("version");
            completions.add("help");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            completions.add("all");
            completions.add("config");
            completions.add("messages");
        }
        return completions;
    }
}