package com.pixesoj.utils.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static Location getLobby(FileConfiguration locations) {
        String world = locations.getString("Lobby.world");
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        assert world != null;
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static Location getSpawn(FileConfiguration locations, boolean byWorld, World worldPlayer) {
        if (byWorld) {
            String spawnKey = "SpawnByWorld." + worldPlayer.getName();
            if (!locations.contains(spawnKey)) {
                return null;
            }
            double x = locations.getDouble(spawnKey + ".x");
            double y = locations.getDouble(spawnKey + ".y");
            double z = locations.getDouble(spawnKey + ".z");
            float yaw = (float) locations.getDouble(spawnKey + ".yaw");
            float pitch = (float) locations.getDouble(spawnKey + ".pitch");
            return new Location(worldPlayer, x, y, z, yaw, pitch);
        } else {
            String spawnKey = "Spawn";
            if (!locations.contains(spawnKey)) {
                return null;
            }
            double x = locations.getDouble(spawnKey + ".x");
            double y = locations.getDouble(spawnKey + ".y");
            double z = locations.getDouble(spawnKey + ".z");
            float yaw = (float) locations.getDouble(spawnKey + ".yaw");
            float pitch = (float) locations.getDouble(spawnKey + ".pitch");
            String world = locations.getString(spawnKey + ".world");
            assert world != null;
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
    }

    public static Location getPlayer(Player player){
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        String world = player.getWorld().getName();

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}
