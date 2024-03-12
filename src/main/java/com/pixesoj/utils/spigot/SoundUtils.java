package com.pixesoj.utils.spigot;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundUtils {
    public static void getSound (DeluxeSpawn plugin, CommandSender sender, boolean soundEnabled, String soundName, int volume, int pitch, String description){
        if (!(sender instanceof Player)){
            return;
        }

        Player player = (Player) sender;

        if (!soundEnabled) {
            return;
        }

        if (soundName == null) {
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String p = plugin.getMainPermissionsManager().getNotify();
            boolean d = plugin.getMainPermissionsManager().isNotifyDefault();

            String errorNullSound = plugin.getMainMessagesManager().getErrorNullSound();
            if (errorNullSound != null || PlayerUtils.hasPermission(sender, p, d)) {
                assert errorNullSound != null;
                String m = errorNullSound.replace("%description%", description);
                sender.sendMessage(MessagesUtils.getColoredMessage(prefix + m));
            }
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String p = plugin.getMainPermissionsManager().getNotify();
            boolean d = plugin.getMainPermissionsManager().isNotifyDefault();

            String errorInvalidSound = plugin.getMainMessagesManager().getErrorInvalidSound();
            if (errorInvalidSound != null || PlayerUtils.hasPermission(sender, p, d)) {
                assert errorInvalidSound != null;
                String m = errorInvalidSound.replace("%sound%", soundName).replace("%description%", description);
                sender.sendMessage(MessagesUtils.getColoredMessage(prefix + m));
            }
        }
    }
}
