package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.handlers.ErrorHandler;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.utils.spigot.PlayerUtils;
import com.pixesoj.utils.common.SubCommand;
import org.bukkit.command.CommandSender;

public class Reload implements SubCommand {

    private final DeluxeSpawn plugin;
    private final ErrorHandler errorHandler;

    public Reload(DeluxeSpawn plugin) {
        this.plugin = plugin;
        this.errorHandler = new ErrorHandler(plugin);
    }

    public void colored(CommandSender sender, String prefix, String text){
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + text));
    }

    public String prefix(){
        return plugin.getMainMessagesManager().getPrefix();
    }

    public boolean execute(CommandSender sender, String[] args) {
        reload(sender, args);
        return true;
    }

    public void reload(CommandSender sender, String[] args) {
        String p = plugin.getMainPermissionsManager().getReload();
        boolean d = plugin.getMainPermissionsManager().isReloadDefault();
        if (!PlayerUtils.hasPermissionMessage(sender, p, d)){
            String m = plugin.getMainMessagesManager().getPermissionDenied();
            colored(sender, prefix(), m);
            return;
        }

        try {
            if (args.length == 0) {

                String usage = " &a/deluxespawn reload &8<&aconfig&8|&amessages&8|&apermissions&8|&aall&8>";
                colored(sender, prefix(),plugin.getMainMessagesManager().getCommandInvalidArgument() + usage);
                return;
            }

            switch (args[0].toLowerCase()) {
                case "config":
                    reloadConfig(sender);
                    break;
                case "messages":
                    reloadMessages(sender);
                    break;
                case "permissions":
                    reloadPermissions(sender);
                    break;
                case "all":
                    reloadAll(sender);
                    break;
                default:
                    String usage = " &a/deluxespawn reload &8<&aconfig&8|&amessages&8|&apermissions&8|&aall&8>";
                    colored(sender, prefix(),plugin.getMainMessagesManager().getCommandInvalidArgument() + usage);
            }
        } catch (Exception e) {
            errorHandler.sendErrorMessage(sender, "reload");
        }
    }

    private void reloadConfig(CommandSender sender) {
        plugin.getMainConfigManager().reloadConfig();
        plugin.getMainLobbyConfigManager().reloadConfig();
        plugin.getMainSpawnConfigManager().reloadConfig();
        String m = plugin.getMainMessagesManager().getCommandReloadConfig();
        colored(sender, prefix(), m);
    }

    private void reloadMessages(CommandSender sender) {
        plugin.getMainMessagesManager().reloadMessages();
        String m = plugin.getMainMessagesManager().getCommandReloadMessages();
        colored(sender, prefix(), m);
    }

    private void reloadPermissions(CommandSender sender) {
        plugin.getMainPermissionsManager().reloadPermissions();
        String m = plugin.getMainMessagesManager().getCommandReloadConfig();
        colored(sender, prefix(), m);
    }

    private void reloadAll(CommandSender sender) {
        plugin.getMainConfigManager().reloadConfig();
        plugin.getMainLobbyConfigManager().reloadConfig();
        plugin.getMainSpawnConfigManager().reloadConfig();
        plugin.getMainMessagesManager().reloadMessages();
        plugin.getMainPermissionsManager().reloadPermissions();
        String m = plugin.getMainMessagesManager().getCommandReloadAll().replace("%version%", plugin.getDescription().getVersion());
        colored(sender, prefix(), m);
    }
}
