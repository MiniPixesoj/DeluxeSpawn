package com.pixesoj.managers.playerdata;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {
    private DeluxeSpawn plugin;
    private File playerDataFolder;

    public PlayerDataManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
        this.playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    public File getPlayerDataFolder() {
        return playerDataFolder;
    }

    public File getPlayerFile(UUID playerUUID) {
        return new File(playerDataFolder, playerUUID.toString() + ".yml");
    }

    public FileConfiguration getPlayerConfig(UUID playerUUID) {
        File playerFile = getPlayerFile(playerUUID);
        return YamlConfiguration.loadConfiguration(playerFile);
    }

    public void savePlayerConfig(UUID playerUUID, FileConfiguration config) {
        File playerFile = getPlayerFile(playerUUID);
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        File playerFile = getPlayerFile(uuid);

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        playerData.set("PlayerName", player.getName());

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerLocation(Player player, double x, double y, double z, float yaw, float pitch) {
        UUID uuid = player.getUniqueId();
        File playerFile = getPlayerFile(uuid);

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        playerData.set("LastLocation.world", player.getWorld().getName());
        playerData.set("LastLocation.x", x);
        playerData.set("LastLocation.y", y);
        playerData.set("LastLocation.z", z);
        playerData.set("LastLocation.yaw", yaw);
        playerData.set("LastLocation.pitch", pitch);

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLastLocationTeleportOneTime (Player player){
        boolean save = plugin.getMainConfigManager().isLastLocationSave();
        if (save) {
            UUID uuid = player.getUniqueId();
            File playerFile = getPlayerFile(uuid);

            if (!playerFile.exists()) {
                try {
                    playerFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            playerData.set("LastLocation.OneTime", "yes");

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeLastLocationTeleportOneTime (Player player){
        boolean save = plugin.getMainConfigManager().isLastLocationSave();
        if (save) {
            UUID uuid = player.getUniqueId();
            File playerFile = getPlayerFile(uuid);

            if (!playerFile.exists()) {
                try {
                    playerFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            playerData.set("LastLocation.OneTime", "no");

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}