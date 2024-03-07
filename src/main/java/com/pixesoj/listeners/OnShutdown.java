package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;

public class OnShutdown implements Listener {
    private DeluxeSpawn plugin;

    public OnShutdown(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerShutdown(ServerCommandEvent event) {
        boolean save = plugin.getMainConfigManager().isLastLocationSave();
        if (!save){
            return;
        }

        List<String> saveType = plugin.getMainConfigManager().getLastLocationSaveType();
        if (!saveType.contains("OnServerStop")){
            return;
        }

        boolean stop = event.getCommand().equalsIgnoreCase("stop");
        boolean restart = event.getCommand().equalsIgnoreCase("restart");

        if (stop || restart) {
            saveAllPlayersData();
        }
    }

    private void saveAllPlayersData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }
    }

    private void savePlayerData(Player player) {
        String playerWorld = player.getWorld().getName();

        plugin.getPlayerDataManager().savePlayer(player);
        plugin.getPlayerDataManager().removeLastLocationTeleportOneTime(player);

        List<String> ignoredWorlds = plugin.getMainConfigManager().getLobbyLastLocationIgnoredWorldsToSave();
        if (ignoredWorlds.contains(playerWorld)) {
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