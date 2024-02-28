package com.pixesoj.utils;

import org.bukkit.ChatColor;

public class MessagesUtils {

    public static String getColoredMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
