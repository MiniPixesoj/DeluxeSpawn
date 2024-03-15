package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.common.SubCommand;
import com.pixesoj.utils.spigot.InventoryUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Admin implements SubCommand {

    private final DeluxeSpawn plugin;

    public Admin(DeluxeSpawn plugin){
        this.plugin = plugin;
    }

    private void colored(CommandSender sender, String prefix, String text){
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + text));
    }

    private String prefix(){
        return plugin.getMainMessagesManager().getPrefix();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            sendMessage(plugin, sender, "DeniedConsole");
            return true;
        }

        Player player = (Player) sender;
        inventorySettings(player, plugin);
        return true;
    }

    public static void inventorySettings (Player player, DeluxeSpawn plugin){
        String title = "&eDeluxeSpawn &8» &bSETTINGS";
        title = MessagesUtils.getColoredMessage(title);
        Inventory inv = Bukkit.createInventory(null, 54, title);
        InventoryUtils.fillSettings(inv, player);
        player.openInventory(inv);
    }

    public static void inventorySettings2 (Player player, DeluxeSpawn plugin){
        String title = "&eDeluxeSpawn &8» &bSETTINGS &8(&72&8)";
        title = MessagesUtils.getColoredMessage(title);
        Inventory inv = Bukkit.createInventory(null, 54, title);
        InventoryUtils.fillSettings2(inv, player);
        player.openInventory(inv);
    }

    public static void inventorySettingsGlobal (Player player, DeluxeSpawn plugin){
        String title = "&eDeluxeSpawn &8» &bSETTINGS GLOBAL";
        title = MessagesUtils.getColoredMessage(title);
        Inventory inv = Bukkit.createInventory(null, 54, title);
        InventoryUtils.fillSettingsGlobal(inv, player);
        player.openInventory(inv);
    }

    private void sendMessage(DeluxeSpawn plugin, CommandSender sender, String reason){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        switch (reason) {
            case "NoPermission": {
                String message = plugin.getMainMessagesManager().getPermissionDenied();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "DeniedConsole": {
                String message = plugin.getMainMessagesManager().getCommandDeniedConsole();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
        }
    }
}