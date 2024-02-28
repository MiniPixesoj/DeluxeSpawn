package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.DelayManagerSpawn;
import com.pixesoj.managers.DelayManagerSpawnByWorldOther;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Spawn implements CommandExecutor {
    private DeluxeSpawn plugin;

    public Spawn(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (!(sender instanceof Player)) {
            String message = prefix + plugin.getMainMessagesManager().getCommandDeniedConsole();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return true;
        }

        Player player = (Player) sender;
        String worldPlayer = player.getWorld().getName();

        if (!player.hasPermission("deluxespawn.command.spawn")) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
            return true;
        }

        if (!plugin.getMainConfigManager().isSpawnByWorld()){
            if (!locations.contains("Spawn.world")){
                player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnDoesNotExist()));
                return true;
            } else {
                SpawnGlobal(player);
                return true;
            }
        }

        if (!locations.contains("SpawnByWorld." + worldPlayer)) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldDoesNotExist()));
            return true;
        } else if (args.length > 0) {
            String world = args[0];

            if (!locations.contains("SpawnByWorld." + world)) {
                player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldSpecifyDoesNotExist()));
                return true;
            }

            if (!player.hasPermission("deluxespawn.command.spawn.world." + world)){
                sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
                return true;
            }
                SpawnByWorldOther(sender, args);
        } else {
            SpawnByWorld(sender);
            return true;
        }
        return true;
    }

    public void SpawnGlobal (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String world = locations.getString("Spawn.world");
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (world == null) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnDoesNotExist()));
            return;
        }
        double x = locations.getDouble("Spawn.x");
        double y = locations.getDouble("Spawn.y");
        double z = locations.getDouble("Spawn.z");
        float yaw = (float) locations.getDouble("Spawn.yaw");
        float pitch = (float) locations.getDouble("Spawn.pitch");
        Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawn d = new DelayManagerSpawn(plugin, delay, player, spawnLocation);

        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()){
            player.teleport(spawnLocation);
            spawnSound(player);
            spawnExecuteCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnTeleported()));
            return;
        }

        if (!plugin.playerInDelay(player)){
            int time = plugin.getMainConfigManager().getSpawnTeleportDelay();
            String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            return;
        }
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
        return;
    }

    public void SpawnByWorldOther(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String realName = args[0];
        String aliasName = realName;

        if (plugin.getConfig().contains("Aliases." + realName)) {
            aliasName = plugin.getConfig().getString("Aliases." + realName);
        }

        if (Bukkit.getWorld(realName) == null) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnByWorldSpecifyDoesNotExist()));
            return;
        }

        double x = locations.getDouble("SpawnByWorld." + realName + ".x");
        double y = locations.getDouble("SpawnByWorld." + realName + ".y");
        double z = locations.getDouble("SpawnByWorld." + realName + ".z");
        float yaw = (float) locations.getDouble("SpawnByWorld." + realName + ".yaw");
        float pitch = (float) locations.getDouble("SpawnByWorld." + realName + ".pitch");
        Location spawnLocation = new Location(Bukkit.getWorld(realName), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawnByWorldOther d = new DelayManagerSpawnByWorldOther(plugin, delay, player, spawnLocation);


        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()){
            player.teleport(spawnLocation);
            spawnSound(player);
            spawnExecuteCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnOtherTeleported().replace("%world%", aliasName)));
            return;
        }

        if (!plugin.playerInDelay(player)){
            int time = plugin.getMainConfigManager().getSpawnTeleportDelay();
            String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            return;
        }
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
        return;
    }

    public void SpawnByWorld (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String worldPlayer = player.getWorld().getName();

        double x = locations.getDouble("SpawnByWorld." + worldPlayer + ".x");
        double y = locations.getDouble("SpawnByWorld." + worldPlayer + ".y");
        double z = locations.getDouble("SpawnByWorld." + worldPlayer + ".z");
        float yaw = (float) locations.getDouble("SpawnByWorld." + worldPlayer + ".yaw");
        float pitch = (float) locations.getDouble("SpawnByWorld." + worldPlayer + ".pitch");
        Location spawnLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getSpawnTeleportDelay();
        DelayManagerSpawn d = new DelayManagerSpawn(plugin, delay, player, spawnLocation);

        if (!plugin.getMainConfigManager().isSpawnTeleportDelayEnabled()){
            player.teleport(spawnLocation);
            spawnSound(player);
            spawnExecuteCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnTeleported()));
            return;
        }

        if (!plugin.playerInDelay(player)){
            int time = plugin.getMainConfigManager().getSpawnTeleportDelay();
            String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            plugin.addPlayer(player);
            d.DelaySpawnGlobal();
            return;
        }
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getSpawnInTeleport()));
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
