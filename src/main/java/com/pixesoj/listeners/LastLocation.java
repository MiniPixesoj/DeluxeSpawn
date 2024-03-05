package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class LastLocation implements Listener {
    private DeluxeSpawn plugin;

    public LastLocation(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void changeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        List<String> teleportMode = plugin.getMainConfigManager().getLobbyLastLocationTeleportMode();
        if (teleportMode.contains("OnChangeWorld")){
            boolean oneTime = plugin.getMainConfigManager().isLobbyLastLocationChangeWorldsOneTime();
            if (oneTime){
                UUID uuid = player.getUniqueId();
                FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
                String OneTime = playerConfig.getString("LastLocation.OneTime");
                if (OneTime.equals("yes")){
                    return;
                }
                    teleportPlayer(player, event);
                return;
            }
                teleportPlayer(player, event);
        }
    }

    public void teleportPlayer(Player player, PlayerChangedWorldEvent event){
        String changeWorldType = plugin.getMainConfigManager().getLobbyLastLocationChangeWorldType();
        if (changeWorldType.equals("AnyWorld")){
            getLastLocation(player);
            return;
        }

        if (changeWorldType.equals("SpecifiedWorlds")){
            String playerWorld = event.getPlayer().getWorld().getName();
            List<String> specifiedWorlds = plugin.getMainConfigManager().getLobbyLastLocationChangeWorldsSpecified();

            if (specifiedWorlds.contains(playerWorld)) {
                getLastLocation(player);
            }
        }
    }

    public void getLastLocation(Player player) {
        UUID uuid = player.getUniqueId();
        FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
        if (playerConfig.contains("LastLocation.world")) {
            String worldName = playerConfig.getString("LastLocation.world");
            double x = playerConfig.getDouble("LastLocation.x");
            double y = playerConfig.getDouble("LastLocation.y");
            double z = playerConfig.getDouble("LastLocation.z");
            float yaw = (float) playerConfig.getDouble("LastLocation.yaw");
            float pitch = (float) playerConfig.getDouble("LastLocation.pitch");

            Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(lastLocation);plugin.getPlayerDataManager().addLastLocationTeleportOneTime(player);
        }
    }
}

