package com.pixesoj.handlers;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {

    private final DeluxeSpawn plugin;

    private final Map<String, String> errorMessages;

    public ErrorHandler(DeluxeSpawn plugin) {
        this.plugin = plugin;
        this.errorMessages = new HashMap<>();



        errorMessages.put("general", plugin.getMainMessagesManager().getErrorGeneral());
        errorMessages.put("reload", plugin.getMainMessagesManager().getErrorCommandReload());
        errorMessages.put("nullsound", plugin.getMainMessagesManager().getErrorNullSound());
    }

    public void handleException(CommandSender sender, Exception exception) {
        exception.printStackTrace();

        sendErrorMessage(sender, "general");
    }

    public void sendErrorMessage(CommandSender sender, String errorCode) {
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String errorMessage = errorMessages.getOrDefault(errorCode, plugin.getMainMessagesManager().getErrorGeneral());

        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + errorMessage));
    }

    public void sendPermissionError(CommandSender sender) {
        sendErrorMessage(sender, "permission");
    }
}
