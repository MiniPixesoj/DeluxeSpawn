package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.utils.spigot.PlayerUtils;
import com.pixesoj.utils.common.SubCommand;
import org.bukkit.command.CommandSender;

public class Version implements SubCommand {

    private final DeluxeSpawn plugin;

    public Version (DeluxeSpawn plugin){
        this.plugin = plugin;
    }

    public void colored(CommandSender sender, String prefix, String text){
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + text));
    }

    public String prefix(){
        return plugin.getMainMessagesManager().getPrefix();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String p = plugin.getMainPermissionsManager().getVersion();
        boolean d = plugin.getMainPermissionsManager().isVersionDefault();
        if (!PlayerUtils.hasPermissionMessage(sender, p, d)){
            String message = plugin.getMainMessagesManager().getPermissionDenied();
            colored(sender, prefix(), message);
            return true;
        }

        version(sender);
        return false;
    }

    public void version(CommandSender sender) {
        String latestVersion = this.plugin.getUpdateCheckerManager().getLatestVersion();
        String message = plugin.getMainMessagesManager().getCommandVersion();
        message = message.replace("%version%", plugin.getDescription().getVersion()).replace("%last_version%", latestVersion);
        colored(sender, prefix(), message);
    }
}
