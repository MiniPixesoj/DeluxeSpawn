package com.pixesoj.filesmanager.spawn;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainSpawnConfigManager {
    private final CustomSpawnConfig configFile;

    public boolean ByWorld;
    public int TeleportDelay;
    public boolean TeleportDelayEnabled;
    public boolean TeleportDelayCancelOnMove;
    public String TeleportDelayMessageType;
    public boolean TeleportBlindness;
    public int TeleportBlindnessTime;
    public boolean TeleportSoundEnabled;
    public String TeleportSound;
    public int TeleportSoundVolume;
    public int TeleportSoundPitch;
    public boolean CommandsEnabled;
    public List<String> CommandsPlayer;
    public List<String> CommandsConsole;
    public boolean CooldownEnabled;
    public int CooldownTime;
    public ConfigurationSection AliasSection;

    public void reloadConfig() {
        configFile.reloadSpawnConfig();
        loadSpawnConfig();
    }

    public MainSpawnConfigManager(DeluxeSpawn plugin) {
        configFile = new CustomSpawnConfig("spawn.yml", null, plugin);
        configFile.registerSpawnConfig();
        loadSpawnConfig();
    }

    public void loadSpawnConfig() {
        FileConfiguration config = configFile.getConfig();

        ByWorld = config.getBoolean("by_world");
        AliasSection = config.getConfigurationSection("aliases");

        TeleportDelayEnabled = config.getBoolean("teleport_delay.enabled");
        TeleportDelay = config.getInt("teleport_delay.seconds");
        TeleportDelayCancelOnMove = config.getBoolean("teleport_delay.cancel_on_move");
        TeleportDelayMessageType = config.getString("teleport_delay.message_type");

        TeleportBlindness = config.getBoolean("teleport_delay.blindness");
        TeleportBlindnessTime = config.getInt("teleport_delay.blindness_time");

        CooldownEnabled = config.getBoolean("cooldown.enabled");
        CooldownTime = config.getInt("cooldown.time");

        TeleportSoundEnabled = config.getBoolean("sound_settings.enabled");
        TeleportSound = config.getString("sound_settings.sound");
        TeleportSoundVolume = config.getInt("sound_settings.volume");
        TeleportSoundPitch = config.getInt("sound_settings.pitch");


        CommandsEnabled = config.getBoolean("commands.enabled");
        CommandsPlayer = config.getStringList("commands.player");
        CommandsConsole = config.getStringList("commands.console");
    }

    public boolean isByWorld() {
        return ByWorld;
    }

    public int getTeleportDelay() {
        return TeleportDelay;
    }

    public boolean isTeleportDelayEnabled() {
        return TeleportDelayEnabled;
    }

    public boolean isTeleportDelayCancelOnMove() {
        return TeleportDelayCancelOnMove;
    }

    public String getTeleportDelayMessageType() {
        return TeleportDelayMessageType;
    }

    public boolean isTeleportBlindness() {
        return TeleportBlindness;
    }

    public int getTeleportBlindnessTime() {
        return TeleportBlindnessTime;
    }

    public boolean isTeleportSoundEnabled() {
        return TeleportSoundEnabled;
    }

    public String getTeleportSound() {
        return TeleportSound;
    }

    public int getTeleportSoundVolume() {
        return TeleportSoundVolume;
    }

    public int getTeleportSoundPitch() {
        return TeleportSoundPitch;
    }

    public boolean isCommandsEnabled() {
        return CommandsEnabled;
    }

    public List<String> getCommandsPlayer() {
        return CommandsPlayer;
    }

    public List<String> getCommandsConsole() {
        return CommandsConsole;
    }

    public boolean isCooldownEnabled() {
        return CooldownEnabled;
    }

    public int getCooldownTime() {
        return CooldownTime;
    }

    public ConfigurationSection getAliasSection() {
        return AliasSection;
    }
}
