package com.pixesoj.filesmanager.spawn;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.common.Comments;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MainSpawnConfigManager {
    private final CustomSpawnConfig configFile;
    private final DeluxeSpawn plugin;

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
    public int ConfigVersion;

    public void reloadConfig() {
        configFile.reloadSpawnConfig();
        loadSpawnConfig();
    }

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }

    public void saveConfig(){
        configFile.saveSpawnConfig();
        loadSpawnConfig();
    }

    public MainSpawnConfigManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
        configFile = new CustomSpawnConfig("spawn.yml", null, plugin);
        configFile.registerSpawnConfig();
        loadSpawnConfig();
    }

    public void updateSpawnConfig() {
        FileConfiguration config = this.getConfig();
        int version = config.getInt("config_version");
        int newVersion = (int) 4;

        if (version != newVersion) {
            boolean changed = addMissingFields(config, config);

            if (version < newVersion) {
                List<String> spawnCommands = new ArrayList<>();
                spawnCommands.add("spawn");
                config.set("commands_alias", spawnCommands);
                config.setComments("commands_alias", Comments.SpawnCommandsAlias);
                changed = true;
            }

            createFile("spawn-new.yml", "spawn.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "spawn-new.yml");

            try {
                FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                config.set("config_version", newVersion);
                if (changed) {
                    this.saveConfig();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    private void createFile(String name, String from, DeluxeSpawn plugin) {
        String prefix = "[DeluxeSpawn] ";
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                Files.copy(plugin.getResource(from), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(prefix + "Unable to create " + name + " file for DeluxeSpawn!" + e);
            }
        }
    }

    private boolean addMissingFields(FileConfiguration currentConfig, FileConfiguration newConfig) {
        boolean changed = false;
        for (String key : newConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, newConfig.get(key));
                changed = true;
            }
        }
        return changed;
    }

    public void loadSpawnConfig() {
        FileConfiguration config = configFile.getConfig();
        ConfigVersion = config.getInt("config_version");

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

    public int getConfigVersion() {
        return ConfigVersion;
    }
}
