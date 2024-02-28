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

public class DelayManagerSpawnByWorldOther {
    int TaskID;
    private DeluxeSpawn plugin;
    int time;
    private Player player;
    private Location l;

    public DelayManagerSpawnByWorldOther(DeluxeSpawn plugin, int time, Player player, Location l){
        this.plugin = plugin;
        this.time = time;
        this.player = player;
        this.l = l;
    }

    public void DelaySpawnGlobal(){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean cancelOnMove = plugin.getMainConfigManager().isSpawnTeleportDelayCancelOnMove();

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
                        String message = plugin.getMainMessagesManager().getSpawnTeleported();
                        player.teleport(l);
                        spawnSound(player);
                        spawnExecuteCommands(player);
                        plugin.removePlayer(player);
                        player.sendMessage(MessagesUtils.getColoredMessage(prefix + message));
                        BlindnessTeleport(player);
                    });
                    return;
                } else {
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnMovementCanceledTeleport()));
                        Bukkit.getScheduler().cancelTask(TaskID);
                        plugin.removePlayer(player);
                        return;
                    }
                    if (Objects.equals(plugin.getMainConfigManager().getSpawnTeleportDelayMessageType(), "Chat")){
                        String message = plugin.getMainMessagesManager().getSpawnChatMessageInTeleport() + time;
                        message = message.replace("%time%", String.valueOf(time));
                        player.sendMessage(MessagesUtils.getColoredMessage(message));
                    }
                    time--;
                }
            }
        }, 0L, 20);
    }

    public void BlindnessTeleport (CommandSender sender){
        Player player = (Player) sender;
        int time = plugin.getMainConfigManager().getSpawnTeleportBlindnessTime();
        if (plugin.getMainConfigManager().isSpawnTeleportBlindness()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * time, 2));
            return;
        }
        return;
    }

    public void spawnSound(CommandSender sender) {
        Player player = (Player) sender;

        if (plugin.getMainConfigManager().isSpawnTeleportSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getSpawnTeleportSound();
            String prefix = plugin.getMainMessagesManager().getPrefix();
            if (soundName == null){
                if (player.hasPermission("deluxespawn.notify") || player.isOp()){
                    String m = prefix + plugin.getMainMessagesManager().getSpawnNullSound();
                    sender.sendMessage(MessagesUtils.getColoredMessage(m));
                }
                return;
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getSpawnInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }

            float volume = plugin.getMainConfigManager().getSpawnTeleportSoundVolume();
            float pitch = plugin.getMainConfigManager().getSpawnTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void spawnExecuteCommands(CommandSender sender) {
        if (plugin.getMainConfigManager().isSpawnCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainConfigManager().getSpawnPlayerCommands();
            List<String> consoleCommands = plugin.getMainConfigManager().getSpawnConsoleCommands();

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