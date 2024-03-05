package com.pixesoj.managers;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Objects;

public class DelayManagerLobby {
    public boolean playerLobbyMovedDuringDelay;
    int TaskID;
    private DeluxeSpawn plugin;
    int time;
    private Player player;
    private Location l;

    public DelayManagerLobby(DeluxeSpawn plugin, int time, Player player, Location l){
        this.plugin = plugin;
        this.time = time;
        this.player = player;
        this.l = l;
    }

    public void DelayLobby(){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean cancelOnMove = plugin.getMainConfigManager().isLobbyTeleportDelayCancelOnMove();

        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        TaskID = sh.scheduleAsyncRepeatingTask(plugin, new Runnable() {
            int initialX = player.getLocation().getBlockX();
            int initialY = player.getLocation().getBlockY();
            int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        getTeleport();
                    });
                } else {
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyMovementCanceledTeleport()));
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerLobbyMovedDuringDelay = true;
                        plugin.removePlayer(player);
                        return;
                    }
                    if (Objects.equals(plugin.getMainConfigManager().getLobbyTeleportDelayMessageType(), "Chat")){
                        String message = plugin.getMainMessagesManager().getLobbyChatMessageInTeleport();
                        message = message.replace("%time%", String.valueOf(time));
                        player.sendMessage(MessagesUtils.getColoredMessage(message));
                    }
                    time--;
                }
            }
        }, 0L, 20);
    }

    public void getTeleport (){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        player.teleport(l);
        lobbySound(player);
        lobbyExecuteCommands(player);
        plugin.removePlayer(player);
        player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyTeleported()));
        BlindnessTeleport(player);
        playerLobbyMovedDuringDelay = false;
    }

    public void BlindnessTeleport (CommandSender sender){
        Player player = (Player) sender;
        int time = plugin.getMainConfigManager().getLobbyTeleportBlindnessTime();
        if (plugin.getMainConfigManager().isLobbyTeleportBlindness()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * time, 2));
            return;
        }
    }

    public void lobbySound(CommandSender sender) {
        Player player = (Player) sender;

        if (plugin.getMainConfigManager().isLobbyTeleportSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();
            String prefix = plugin.getMainMessagesManager().getPrefix();
            if (soundName == null){
                String permission = plugin.getMainPermissionsManager().getNotify();
                if (plugin.getMainPermissionsManager().isNotifyDefault()) {
                    String m = prefix + plugin.getMainMessagesManager().getLobbyNullSound();
                    sender.sendMessage(MessagesUtils.getColoredMessage(m));
                } else {
                    if (player.hasPermission(permission)) {
                        String m = prefix + plugin.getMainMessagesManager().getLobbyNullSound();
                        sender.sendMessage(MessagesUtils.getColoredMessage(m));
                        return;
                    }
                }
                return;
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getLobbyInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }

            float volume = plugin.getMainConfigManager().getLobbyTeleportSoundVolume();
            float pitch = plugin.getMainConfigManager().getLobbyTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void lobbyExecuteCommands(CommandSender sender) {
        if (plugin.getMainConfigManager().isLobbyCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainConfigManager().getLobbyPlayerCommands();
            List<String> consoleCommands = plugin.getMainConfigManager().getLobbyConsoleCommands();

            Player player = (Player) sender;
            for (String command : playerCommands) {
                String replacedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(sender, replacedCommand);
            }

            CommandSender consoleSender = Bukkit.getConsoleSender();
            for (String command : consoleCommands) {
                String replacedCommand = command.replace("%player%", sender.getName());
                Bukkit.dispatchCommand(consoleSender, replacedCommand);
            }
        }
    }
}