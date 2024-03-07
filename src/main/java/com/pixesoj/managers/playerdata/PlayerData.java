package com.pixesoj.managers.playerdata;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PlayerData {
    private UUID playerUUID;
    private int playerScore;

    public PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.playerScore = 0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void saveToFile(FileConfiguration config) {
        config.set("playerUUID", playerUUID.toString());
        config.set("playerScore", playerScore);
    }

    public static PlayerData loadFromFile(FileConfiguration config) {
        String playerUUIDString = config.getString("playerUUID");
        UUID playerUUID = UUID.fromString(playerUUIDString);
        int playerScore = config.getInt("playerScore");

        PlayerData playerData = new PlayerData(playerUUID);
        playerData.setPlayerScore(playerScore);

        return playerData;
    }
}