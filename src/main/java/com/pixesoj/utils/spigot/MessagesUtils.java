package com.pixesoj.utils.spigot;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class MessagesUtils {

    public static String getColoredMessage (String text){
        if (Bukkit.getVersion().contains("1.16")){
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher match = pattern.matcher(text);

            while (match.find()){
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, ChatColor.of(color) + "");

                match = pattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
