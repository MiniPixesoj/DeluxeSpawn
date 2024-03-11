package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import com.pixesoj.utils.PlayerUtils;
import com.pixesoj.utils.common.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Help implements SubCommand {

    private final DeluxeSpawn plugin;

    public Help (DeluxeSpawn plugin){
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
        String p = plugin.getMainPermissionsManager().getHelp();
        boolean d = plugin.getMainPermissionsManager().isHelpDefault();
        if (!PlayerUtils.hasPermissionMessage(sender, p, d)){
            String m = plugin.getMainMessagesManager().getPermissionDenied();
            colored(sender, prefix(), m);
            return true;
        }
        help(sender);
        return true;
    }

    public void help(CommandSender sender) {
        List<String> message = plugin.getMainMessagesManager().getCommandHelp();
        for (String m : message) {
            colored(sender, "", m);
        }
    }
}
