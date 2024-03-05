package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class OnQuit implements Listener {
    private DeluxeSpawn plugin;

    public OnQuit(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void saveLastLocation(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerWorld = player.getWorld().getName();

        plugin.getPlayerDataManager().savePlayer(player);
        plugin.getPlayerDataManager().removeLastLocationTeleportOneTime(player);

        List<String> ignoredWorlds = plugin.getMainConfigManager().getLobbyLastLocationIgnoredWorldsToSave();
        if (ignoredWorlds.contains(playerWorld)){
            return;
        }

        boolean save = plugin.getMainConfigManager().isLastLocationSave();
        if (save) {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();

            plugin.getPlayerDataManager().savePlayerLocation(player, x, y, z, yaw, pitch);
        }
    }
}
