package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.utils.spigot.PlayerUtils;
import com.pixesoj.utils.common.SubCommand;
import com.pixesoj.utils.common.UpdaterUtils;
import org.bukkit.command.CommandSender;

import java.io.File;

public class Update implements SubCommand {

    private final DeluxeSpawn plugin;

    public Update(DeluxeSpawn plugin) {
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
        String p = plugin.getMainPermissionsManager().getUpdate();
        boolean d = plugin.getMainPermissionsManager().isUpdateDefault();
        if (!PlayerUtils.hasPermissionMessage(sender, p, d)){
            String message = plugin.getMainMessagesManager().getPermissionDenied();
            colored(sender, prefix(), message);
            return true;
        }

        String currentVersion = plugin.version;
        String jarName = plugin.getName();
        boolean enabled = true;
        int resourceID = 111403;
        File pathName = new File("plugins");

        UpdaterUtils updater = new UpdaterUtils(currentVersion, jarName, enabled, resourceID, pathName, sender);
        String message = "&bLooking for updates...";
        colored(sender, prefix(), message);

        return true;
    }
}