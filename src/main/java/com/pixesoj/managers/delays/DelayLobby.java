package com.pixesoj.managers.delays;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.CommandUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
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

public class DelayLobby {
    public boolean playerLobbyMovedDuringDelay;
    int TaskID;
    private DeluxeSpawn plugin;
    int time;
    private Player player;
    private Location l;

    public DelayLobby(DeluxeSpawn plugin, int time, Player player, Location l){
        this.plugin = plugin;
        this.time = time;
        this.player = player;
        this.l = l;
    }

    public void DelayLobby(){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean cancelOnMove = plugin.getMainLobbyConfigManager().isTeleportDelayCancelOnMove();

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
                        plugin.removePlayerTeleport(player);
                        return;
                    }
                    if (Objects.equals(plugin.getMainLobbyConfigManager().getTeleportDelayMessageType(), "Chat")){
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
        plugin.addLobbyCooldown(player);
        lobbySound(player);
        plugin.removePlayerTeleport(player);
        player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyTeleported()));
        BlindnessTeleport(player);
        playerLobbyMovedDuringDelay = false;

        List<String> cmdPlayer = plugin.getMainLobbyConfigManager().getCommandsPlayer();
        List<String> cmdConsole = plugin.getMainLobbyConfigManager().getCommandsConsole();
        boolean enabled = plugin.getMainLobbyConfigManager().isCommandsEnabled();
        CommandUtils.executeCommands(player, enabled, cmdPlayer, cmdConsole);
    }

    public void BlindnessTeleport (CommandSender sender){
        Player player = (Player) sender;
        int time = plugin.getMainLobbyConfigManager().getTeleportDelayBlindnessTime();
        if (plugin.getMainLobbyConfigManager().isTeleportDelayBlindness()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * time, 2));
            return;
        }
    }

    public void lobbySound(CommandSender sender) {
        Player player = (Player) sender;

        if (plugin.getMainLobbyConfigManager().isTeleportSoundEnabled()) {
            String soundName = plugin.getMainLobbyConfigManager().getTeleportSound();
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

            float volume = plugin.getMainLobbyConfigManager().getTeleportSoundVolume();
            float pitch = plugin.getMainLobbyConfigManager().getTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}