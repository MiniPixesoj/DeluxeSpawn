package com.pixesoj.utils.spigot;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.MySQL;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerUtils {

    public static boolean hasPermissionMessage(CommandSender sender, String p, boolean d){
        if (sender instanceof Player){
            return sender.hasPermission(p) || sender.isOp() || d;
        }
        return true;
    }

    public static boolean hasPermission(CommandSender sender, String p, boolean d){
        if (sender instanceof Player){
            return sender.hasPermission(p) || sender.isOp() || d;
        }
        return true;
    }
}
