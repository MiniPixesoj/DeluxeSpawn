package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.DelayManagerSpawn;
import com.pixesoj.managers.DelayManagerSpawnWorld;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Spawn implements CommandExecutor {
    private DeluxeSpawn plugin;

    public Spawn(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String message = prefix + plugin.getMainMessagesManager().getCommandDeniedConsole();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return true;
        }

        Player player = (Player) sender;
        String permission = plugin.getMainPermissionsManager().getSpawn();

        if (plugin.getMainPermissionsManager().isSpawnDefault() || player.hasPermission(permission)) {
            teleportPlayer(sender, args);
        } else {
            noPermission(sender);
        }

        return true;
    }

    public void teleportPlayer (CommandSender sender, String[] args){
        getTeleport(sender, args);
    }

    public void getTeleport(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Player player = (Player) sender;
        String worldPlayer = player.getWorld().getName();

        if (!plugin.getMainConfigManager().isSpawnByWorld()) {
            if (!locations.contains("Spawn.world")) {
                player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnDoesNotExist()));
                return;
            }

            getSpawnGlobal(player);
            return;
        }

        if (!locations.contains("SpawnByWorld." + worldPlayer)) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldDoesNotExist()));
            return;
        }

        if (args.length > 0) {
            String world = args[0];

            if (!locations.contains("SpawnByWorld." + world)) {
                player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldSpecifyDoesNotExist()));
                return;
            }

            String permission = plugin.getMainPermissionsManager().getSpawnWorld();
            if (!plugin.getMainPermissionsManager().isSpawnWorldDefault() && !player.hasPermission(permission)) {
                noPermission(sender);
                return;
            }

            getSpawnWorld(sender, args);
        } else {
            getSpawn(sender);
        }
    }

    public void getSpawnGlobal(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String permission = plugin.getMainPermissionsManager().getSpawn();
        if (!plugin.getMainPermissionsManager().isSpawnDefault() && !player.hasPermission(permission)) {
            noPermission(sender);
            return;
        }

        String worldKey = "Spawn.world";
        if (!locations.contains(worldKey)) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnDoesNotExist()));
            return;
        }

        String worldName = locations.getString(worldKey);

        double x = locations.getDouble("Spawn.x");
        double y = locations.getDouble("Spawn.y");
        double z = locations.getDouble("Spawn.z");
        float yaw = (float) locations.getDouble("Spawn.yaw");
        float pitch = (float) locations.getDouble("Spawn.pitch");

        Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawn d = new DelayManagerSpawn(plugin, delay, player, spawnLocation);

        String delayBypassPermission = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayBypassDefault = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()  || delayBypassDefault || player.hasPermission(delayBypassPermission)) {
            player.teleport(spawnLocation);
            sound(sender);
            executeCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnTeleported()));
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }

        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
    }


    public void getSpawn(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String permission = plugin.getMainPermissionsManager().getSpawn();
        if (!plugin.getMainPermissionsManager().isSpawnDefault() && !player.hasPermission(permission)) {
            noPermission(sender);
            return;
        }

        String worldPlayer = player.getWorld().getName();

        String spawnKey = "SpawnByWorld." + worldPlayer;
        if (!locations.contains(spawnKey)) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldDoesNotExist()));
            return;
        }

        double x = locations.getDouble(spawnKey + ".x");
        double y = locations.getDouble(spawnKey + ".y");
        double z = locations.getDouble(spawnKey + ".z");
        float yaw = (float) locations.getDouble(spawnKey + ".yaw");
        float pitch = (float) locations.getDouble(spawnKey + ".pitch");

        Location spawnLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawn d = new DelayManagerSpawn(plugin, delay, player, spawnLocation);

        String delayBypassPermission = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayBypassDefault = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()  || delayBypassDefault || player.hasPermission(delayBypassPermission)) {
            player.teleport(spawnLocation);
            sound(sender);
            executeCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnTeleported()));
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }

        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
    }

    public void getSpawnWorld(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String realName = args.length > 0 ? args[0] : player.getWorld().getName();
        String aliasName = plugin.getConfig().getString("Aliases." + realName, realName);

        World targetWorld = Bukkit.getWorld(realName);
        if (targetWorld == null) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldSpecifyDoesNotExist()));
            return;
        }

        String spawnKey = "SpawnByWorld." + realName;
        double x = locations.getDouble(spawnKey + ".x");
        double y = locations.getDouble(spawnKey + ".y");
        double z = locations.getDouble(spawnKey + ".z");
        float yaw = (float) locations.getDouble(spawnKey + ".yaw");
        float pitch = (float) locations.getDouble(spawnKey + ".pitch");

        Location spawnLocation = new Location(targetWorld, x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawnWorld d = new DelayManagerSpawnWorld(plugin, delay, player, spawnLocation);

        String delayBypassPermission = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayBypassDefault = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()  || delayBypassDefault || player.hasPermission(delayBypassPermission)) {
            player.teleport(spawnLocation);
            sound(sender);
            executeCommands(sender);
            String teleportMessage = prefix + plugin.getMainMessagesManager().getSpawnOtherTeleported().replace("%world%", aliasName);
            player.sendMessage(MessagesUtils.getColoredMessage(teleportMessage));
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
    }

    public void sound(CommandSender sender) {
        Player player = (Player) sender;

        if (!plugin.getMainConfigManager().isSpawnTeleportSoundEnabled()) {
            return;
        }

        String soundName = plugin.getMainConfigManager().getSpawnTeleportSound();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (soundName == null) {
            handleNullSound(sender, prefix);
            return;
        }

        Sound sound;
        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName);
            return;
        }

        float volume = plugin.getMainConfigManager().getSpawnTeleportSoundVolume();
        float pitch = plugin.getMainConfigManager().getSpawnTeleportSoundPitch();

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    private void handleNullSound(CommandSender sender, String prefix) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || sender.hasPermission(permission)) {
            String message = prefix + plugin.getMainMessagesManager().getSpawnNullSound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            noPermission(sender);
        }
    }

    private void handleInvalidSound(Player player, String prefix, String soundName) {
        String message = prefix + plugin.getMainMessagesManager().getSpawnInvalidSound().replace("%sound%", soundName);
        player.sendMessage(message);
    }

    public void executeCommands(CommandSender sender) {
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

    public void sendMessage (CommandSender sender){
        Player player = (Player) sender;
        if (Objects.equals(plugin.getMainConfigManager().getSpawnTeleportDelayMessageType(), "Chat")){
            return;
        } else {
            int time = plugin.getMainConfigManager().getSpawnTeleportDelay();
            String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void noPermission (CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
    }
}
