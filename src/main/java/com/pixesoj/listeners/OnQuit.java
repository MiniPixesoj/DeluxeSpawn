package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class OnQuit implements Listener {
    private final DeluxeSpawn plugin;

    public OnQuit(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void saveLastLocation(PlayerQuitEvent event) {
        boolean save = plugin.getMainConfigManager().isLastLocationSave();
        if (!save){
            return;
        }

        List<String> saveType = plugin.getMainConfigManager().getLastLocationSaveType();
        if (!saveType.contains("OnPlayerQuit")){
            return;
        }

        Player player = event.getPlayer();
        String playerWorld = player.getWorld().getName();

        plugin.getPlayerDataManager().savePlayer(player);
        plugin.removeLastLocationOneTime(player);

        List<String> ignoredWorlds = plugin.getMainConfigManager().getLobbyLastLocationIgnoredWorldsToSave();
        if (ignoredWorlds.contains(playerWorld)){
            return;
        }

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        plugin.getPlayerDataManager().savePlayerLocation(player, x, y, z, yaw, pitch);
    }
}
