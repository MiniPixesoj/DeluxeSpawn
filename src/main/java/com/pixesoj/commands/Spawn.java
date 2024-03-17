package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.cooldown.CooldownSpawn;
import com.pixesoj.managers.cooldown.SpawnCooldownProvider;
import com.pixesoj.managers.delays.DelaySpawn;
import com.pixesoj.managers.delays.DelaySpawnWorld;
import com.pixesoj.model.internal.CooldownTimeProvider;
import com.pixesoj.utils.spigot.CommandUtils;
import com.pixesoj.utils.spigot.LocationUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.utils.spigot.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Spawn implements CommandExecutor {
    private final DeluxeSpawn plugin;

    public Spawn(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
        Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("spawn")).setTabCompleter(new com.pixesoj.commands.tabcompleter.Spawn(deluxeSpawn));
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!(sender instanceof Player)) {
            if (args.length > 0) {
                commandConsole(sender, args);
                return true;
            }
            boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
            if (byWorld){
                sendMessage(sender, prefix, args, "ConsoleUsageBySpawn");
            } else {
                sendMessage(sender, prefix, args, "ConsoleUsage");
            }
            return true;
        }

        commandPlayer(sender, args);
        return true;
    }

    public void commandConsole(CommandSender sender, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sendMessage(sender, prefix, args, "PlayerOffline");
            return;
        }

        getTeleportConsole(sender, targetPlayer, args);
    }

    public void getTeleportConsole(CommandSender sender, Player player, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();

        if (byWorld) {
            if (args.length < 2 || args[1].isEmpty()) {
                sendMessage(sender, prefix, args, "ConsoleUsageBySpawn");
                return;
            }

            String worldName = args[1];
            if (!locations.contains("SpawnByWorld." + worldName)) {
                sendMessage(sender, prefix, args, "NotExistConsole");
                return;
            }

            getSpawnConsole(sender, player, worldName, args);
        } else {
            if (args.length < 1) {
                sendMessage(sender, prefix, args, "ConsoleUsage");
            }
            if (!locations.contains("Spawn.world")) {
                sendMessage(sender, prefix, args, "NotExistConsoleG");
                return;
            }
            getSpawnConsole(sender, player, null, args);
        }
    }

    public void getSpawnConsole(CommandSender sender, Player targetPlayer, String worldName, String[] args) {
        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!byWorld) {
            Location spawnLocation = LocationUtils.getSpawn(locations, false, null);
            assert spawnLocation != null;
            assert args[1] == null;

            sendMessage(sender, prefix, args, "ConsolePlayerTeleported");
            sendMessage(targetPlayer, prefix, args, "ConsolePlayerTeleport");
            targetPlayer.teleport(spawnLocation);
        } else {
            World worldN = Bukkit.getWorld(worldName);

            if (worldN == null) {
                sendMessage(sender, prefix, args, "ConsoleInvalidWorld");
                return;
            }

            Location spawnLocation = LocationUtils.getSpawn(locations, true, worldN);
            assert spawnLocation != null;

            sendMessage(sender, prefix, args, "ConsolePlayerTeleported");
            sendMessage(targetPlayer, prefix, args, "ConsolePlayerTeleport");
            targetPlayer.teleport(spawnLocation);
        }
    }

    public void commandPlayer(CommandSender sender, String[] args){
        if (args.length > 0) {
            getTeleportOtherPlayer(sender, args);
            return;
        }

        getTeleportPlayer(sender, args);
    }

    public void getTeleportOtherPlayer(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
        String p = plugin.getMainPermissionsManager().getSpawnOther();
        boolean d = plugin.getMainPermissionsManager().isSpawnOtherDefault();
        if (args.length > 0) {
            if (byWorld) {
                if (args.length > 1) {
                    if (PlayerUtils.hasPermission(sender, p, d)) {
                        String spawnName = args[0];
                        String targetPlayerName = args[1];
                        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                        if (targetPlayer == null || !targetPlayer.isOnline()) {
                            sendMessage(sender, prefix, args, "PlayerOffline");
                            return;
                        }

                        if (!locations.contains("SpawnByWorld." + spawnName)) {
                            sendMessage(sender, prefix, args, "NotExist");
                            return;
                        }

                        getSpawnOtherPlayer(sender, targetPlayer, args);
                        return;
                    }
                }
                getTeleportPlayer(sender, args);
                return;
            }

            if (PlayerUtils.hasPermission(sender, p, d)) {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    sendMessage(sender, prefix, args, "PlayerOffline");
                    return;
                }

                if (!locations.contains("Spawn.world")) {
                    sendMessage(sender, prefix, args, "NotExist");
                    return;
                }
                getSpawnOtherPlayer(sender, targetPlayer, args);
                return;
            }
            getTeleportPlayer(sender, args);
        } else {
            getTeleportPlayer(sender, args);
        }
    }

    public void getSpawnOtherPlayer(CommandSender sender, Player player, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();

        if (byWorld){
            String spawnName = args[0];
            String targetPlayerName = args[1];
            World spawnN = Bukkit.getWorld(spawnName);
            Location location = LocationUtils.getSpawn(locations, true, spawnN);

            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
            assert targetPlayer != null;

            sendMessage(player, prefix, args, "PlayerTeleportByWorld");

            sendMessage(sender, prefix, args, "PlayerTeleportedByWorld");

            assert location != null;
            targetPlayer.teleport(location);
            return;
        }

        String targetPlayerName = args[0];
        Location location = LocationUtils.getSpawn(locations, false, null);

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        assert targetPlayer != null;

        sendMessage(player, prefix, args, "PlayerTeleport");

        sendMessage(sender, prefix, args, "PlayerTeleported");

        assert location != null;
        targetPlayer.teleport(location);
    }

    public void getTeleportPlayer (CommandSender sender, String[] args){
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String spawnP = plugin.getMainPermissionsManager().getSpawn();
        String cooldownP = plugin.getMainPermissionsManager().getSpawnBypassCooldown();
        boolean cooldownD = plugin.getMainPermissionsManager().isSpawnBypassCooldownDefault();
        boolean cooldownE = plugin.getMainSpawnConfigManager().isCooldownEnabled();
        boolean spawnD = plugin.getMainPermissionsManager().isSpawnDefault();
        boolean inCooldown = plugin.playerSpawnInCooldown(player);

        if (!PlayerUtils.hasPermissionMessage(plugin, player, spawnP, spawnD)){
            return;
        }

        if (cooldownE){
            if (inCooldown) {
                if (PlayerUtils.hasPermission(sender, cooldownP, cooldownD)){
                    getTeleport(player, args);
                    handleSpawnCooldown(player);
                    return;
                }
                sendMessage(player, prefix, args, "InCooldown");
                return;
            }
            getTeleport(player, args);
            handleSpawnCooldown(player);
        } else {
            getTeleport(player, args);
        }
    }

    public void getTeleport(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Player player = (Player) sender;
        String worldPlayer = player.getWorld().getName();

        if (!plugin.getMainSpawnConfigManager().isByWorld()) {
            if (!locations.contains("Spawn.world")) {
                sendMessage(sender, prefix, args, "NotExistG");
                return;
            }

            getSpawnGlobal(player, args);
            return;
        }

        if (!locations.contains("SpawnByWorld." + worldPlayer)) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        if (args.length > 0) {
            String world = args[0];

            if (!locations.contains("SpawnByWorld." + world)) {
                sendMessage(sender, prefix, args, "NotExist");
                return;
            }

            String spawnP = plugin.getMainPermissionsManager().getSpawnWorld();
            boolean spawnD = plugin.getMainPermissionsManager().isSpawnWorldDefault();
            if (!PlayerUtils.hasPermissionMessage(plugin, sender, spawnP, spawnD)) {
                return;
            }

            getSpawnWorld(sender, args);
        } else {
            getSpawn(sender, args);
        }
    }

    public void getSpawnGlobal(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String spawnP = plugin.getMainPermissionsManager().getSpawn();
        boolean spawnD = plugin.getMainPermissionsManager().isSpawnDefault();
        if (!PlayerUtils.hasPermission(sender, spawnP, spawnD)) {
            return;
        }

        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
        Location spawnLocation = LocationUtils.getSpawn(locations, byWorld, player.getWorld());
        if (spawnLocation == null) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        int delay = plugin.getMainSpawnConfigManager().getTeleportDelay();
        DelaySpawn d = new DelaySpawn(plugin, delay, player, spawnLocation);

        String delayP = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayD = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainSpawnConfigManager().isTeleportDelayEnabled() || PlayerUtils.hasPermission(sender, delayP, delayD)) {
            player.teleport(spawnLocation);
            plugin.addSpawnCooldown(player);
            sound(sender, args);
            sendMessage(sender, prefix, args, "Teleported");

            List<String> cmdPlayer = plugin.getMainSpawnConfigManager().getCommandsPlayer();
            List<String> cmdConsole = plugin.getMainSpawnConfigManager().getCommandsConsole();
            boolean enabled = plugin.getMainSpawnConfigManager().isCommandsEnabled();
            CommandUtils.executeCommands(sender, enabled, cmdPlayer, cmdConsole);
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }

        sendMessage(sender, prefix, args, "InTeleport");
    }


    public void getSpawn(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        String spawnP = plugin.getMainPermissionsManager().getSpawn();
        boolean spawnD = plugin.getMainPermissionsManager().isSpawnDefault();
        if (!PlayerUtils.hasPermission(sender, spawnP, spawnD)) {
            return;
        }

        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
        Location spawnLocation = LocationUtils.getSpawn(locations, byWorld, player.getWorld());
        if (spawnLocation == null) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        int delay = plugin.getMainSpawnConfigManager().getTeleportDelay();
        DelaySpawn d = new DelaySpawn(plugin, delay, player, spawnLocation);

        String delayP = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayD = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainSpawnConfigManager().isTeleportDelayEnabled()  || PlayerUtils.hasPermission(sender, delayP, delayD)) {
            player.teleport(spawnLocation);
            plugin.addSpawnCooldown(player);
            sound(sender, args);
            sendMessage(sender, prefix, args, "Teleported");

            List<String> cmdPlayer = plugin.getMainSpawnConfigManager().getCommandsPlayer();
            List<String> cmdConsole = plugin.getMainSpawnConfigManager().getCommandsConsole();
            boolean enabled = plugin.getMainSpawnConfigManager().isCommandsEnabled();
            CommandUtils.executeCommands(sender, enabled, cmdPlayer, cmdConsole);
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }

        sendMessage(sender, prefix, args, "InTeleport");
    }


    public void getSpawnWorld(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        ConfigurationSection aliasSection = plugin.getMainSpawnConfigManager().getAliasSection();
        String realName = args.length > 0 ? args[0] : player.getWorld().getName();
        String aliasName = aliasSection.getString(realName, realName);

        World targetWorld = Bukkit.getWorld(realName);
        if (targetWorld == null) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        boolean byWorld = plugin.getMainSpawnConfigManager().isByWorld();
        Location spawnLocation = LocationUtils.getSpawn(locations, byWorld, targetWorld);
        if (spawnLocation == null) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        int delay = plugin.getMainSpawnConfigManager().getTeleportDelay();
        DelaySpawnWorld d = new DelaySpawnWorld(plugin, delay, player, spawnLocation);

        String delayP = plugin.getMainPermissionsManager().getSpawnBypassDelay();
        boolean delayD = plugin.getMainPermissionsManager().isSpawnBypassDelayDefault();

        if (!plugin.getMainSpawnConfigManager().isTeleportDelayEnabled()  || PlayerUtils.hasPermission(sender, delayP, delayD)) {
            player.teleport(spawnLocation);
            plugin.addSpawnCooldown(player);
            sound(sender, args);
            String teleportMessage = prefix + plugin.getMainMessagesManager().getSpawnOtherTeleported().replace("%world%", aliasName);
            player.sendMessage(MessagesUtils.getColoredMessage(teleportMessage));

            List<String> cmdPlayer = plugin.getMainSpawnConfigManager().getCommandsPlayer();
            List<String> cmdConsole = plugin.getMainSpawnConfigManager().getCommandsConsole();
            boolean enabled = plugin.getMainSpawnConfigManager().isCommandsEnabled();
            CommandUtils.executeCommands(sender, enabled, cmdPlayer, cmdConsole);
            return;
        }

        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            d.DelaySpawnGlobal();
            sendMessage(sender);
            return;
        }
        sendMessage(sender, prefix, args, "InTeleport");
    }

    public void sound(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!plugin.getMainSpawnConfigManager().isTeleportSoundEnabled()) {
            return;
        }

        String soundName = plugin.getMainSpawnConfigManager().getTeleportSound();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (soundName == null) {
            handleNullSound(sender, prefix, args);
            return;
        }

        Sound sound;
        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName);
            return;
        }

        float volume = plugin.getMainSpawnConfigManager().getTeleportSoundVolume();
        float pitch = plugin.getMainSpawnConfigManager().getTeleportSoundPitch();

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    private void handleNullSound(CommandSender sender, String prefix, String[] args) {
        String notifyP = plugin.getMainPermissionsManager().getNotify();
        boolean notifyD = plugin.getMainPermissionsManager().isNotifyDefault();
        if (PlayerUtils.hasPermission(sender, notifyP, notifyD)) {
            sendMessage(sender, prefix, args, "NullSound");
        }
    }

    private void handleInvalidSound(Player player, String prefix, String soundName) {
        String notifyP = plugin.getMainPermissionsManager().getNotify();
        boolean notifyD = plugin.getMainPermissionsManager().isNotifyDefault();
        if (PlayerUtils.hasPermission(player, notifyP, notifyD)) {
            String message = prefix + plugin.getMainMessagesManager().getSpawnInvalidSound().replace("%sound%", soundName);
            player.sendMessage(message);
        }
    }

    public void sendMessage (CommandSender sender){
        Player player = (Player) sender;
        if (Objects.equals(plugin.getMainSpawnConfigManager().getTeleportDelayMessageType(), "Chat")){
            return;
        } else {
            int time = plugin.getMainSpawnConfigManager().getTeleportDelay();
            String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void handleSpawnCooldown(Player player){
        CooldownTimeProvider timeProvider = new SpawnCooldownProvider(plugin, player);
        int time = plugin.getMainSpawnConfigManager().getCooldownTime();
        CooldownSpawn c = new CooldownSpawn(plugin, timeProvider, time, player);
        c.cooldownSpawn();
    }

    public void sendMessage (CommandSender sender, String prefix, String[] args, String reason){
        switch (reason) {
            case "NoPermission": {
                String message = plugin.getMainMessagesManager().getPermissionDenied();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InvalidArguments": {
                String message = plugin.getMainMessagesManager().getInvalidArguments();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsoleUsage": {
                String message = plugin.getMainMessagesManager().getSpawnOtherConsoleUsage();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsoleUsageBySpawn": {
                String message = "&cDebes usar &aspawn <jugador> <spawn>";
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsoleInvalidSpawn": {
                String message = "&cNo existe un spawn en ese mundo";
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerOffline": {
                String message = plugin.getMainMessagesManager().getSpawnOtherPlayerOffline();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsolePlayerTeleport": {
                String replaced = plugin.getMainConfigManager().getReplacedMessagesConsole();
                String message = plugin.getMainMessagesManager().getSpawnOtherConsolePlayerTeleport();
                message = prefix + message.replace("%sender%", replaced);
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NotExist": {
                String message = plugin.getMainMessagesManager().getSpawnDoesNotExist();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message).replace("%world%", args[0]));
                break;
            }
            case "NotExistG": {
                String message = plugin.getMainMessagesManager().getSpawnDoesNotExistG();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NotExistConsole": {
                String message = plugin.getMainMessagesManager().getSpawnDoesNotExistConsole();
                message = prefix + message.replace("%world%", args[1]);
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NotExistConsoleG": {
                String message = plugin.getMainMessagesManager().getSpawnDoesNotExistConsoleGlobal();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleported": {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                assert targetPlayer != null;

                String message = plugin.getMainMessagesManager().getSpawnOtherPlayerTeleported();
                message = prefix + message.replace("%player%", targetPlayer.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleport": {
                String message = plugin.getMainMessagesManager().getSpawnOtherPlayerTeleport();
                message = prefix + message.replace("%player%", sender.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleportedByWorld": {
                String targetPlayerName = args[1];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                assert targetPlayer != null;
                String spawnWorld = args[0];
                World spawnW = Bukkit.getWorld(spawnWorld);
                assert spawnW != null;

                String message = plugin.getMainMessagesManager().getSpawnOtherPlayerTeleportedByWorld().replace("%spawn%", spawnW.getName());
                message = prefix + message.replace("%player%", targetPlayer.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleportByWorld": {
                String message = plugin.getMainMessagesManager().getSpawnOtherPlayerTeleportByWorld();
                message = prefix + message.replace("%player%", sender.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InCooldown": {
                Player player = (Player) sender;
                String message = plugin.getMainMessagesManager().getSpawnInCooldown();
                int remainingTime = CooldownSpawn.getRemainingTime(player);
                message = prefix + message.replace("%time%", String.valueOf(remainingTime));
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "Teleported": {
                String message = plugin.getMainMessagesManager().getSpawnTeleported();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "MessageDelay": {
                int time = plugin.getMainSpawnConfigManager().getTeleportDelay();
                String message = plugin.getMainMessagesManager().getSpawnMessageDelayTeleport();
                message = prefix + message.replace("%time%", String.valueOf(time));
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InTeleport": {
                String message = plugin.getMainMessagesManager().getSpawnInTeleport();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NullSound": {
                String message = plugin.getMainMessagesManager().getSpawnNullSound();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InvalidSound": {
                String soundName = plugin.getMainSpawnConfigManager().getTeleportSound();
                String message = plugin.getMainMessagesManager().getSpawnInvalidSound();
                message = prefix + message.replace("%sound%", soundName);
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsolePlayerTeleported": {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                assert targetPlayer != null;

                String message = plugin.getMainMessagesManager().getSpawnOtherConsoleTeleported();
                message = prefix + message.replace("%player%", targetPlayer.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
        }
    }
}

