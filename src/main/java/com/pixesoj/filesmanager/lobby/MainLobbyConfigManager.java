package com.pixesoj.filesmanager.lobby;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.common.Comments;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class MainLobbyConfigManager {
    private final CustomLobbyConfig configFile;
    private final DeluxeSpawn plugin;

    public boolean Enabled;

    public int TeleportDelay;
    public boolean TeleportDelayEnabled;
    public boolean TeleportDelayCancelOnMove;
    public String TeleportDelayMessageType;
    public boolean TeleportDelayBlindness;
    public int TeleportDelayBlindnessTime;
    public boolean TeleportSoundEnabled;
    public String TeleportSound;
    public int TeleportSoundVolume;
    public int TeleportSoundPitch;
    public boolean CommandsEnabled;
    public List<String> CommandsPlayer;
    public List<String> CommandsConsole;
    public boolean CooldownEnabled;
    public int CooldownTime;
    public List<String> LastLocationTeleportMode;
    public List<String> LastLocationSaveIgnoredWorlds;
    public String LastLocationChangeWorldType;
    public List<String> LastLocationChangeWorldsSpecified;
    public boolean LastLocationChangeWorldsOneTime;
    public boolean LastLocationCommandOneTime;
    public boolean LastLocationSoundEnabled;
    public String LastLocationSound;
    public int LastLocationSoundVolume;
    public int LastLocationSoundPitch;
    public boolean LastLocationCommandsEnabled;
    public List<String> LastLocationCommandsPlayer;
    public List<String> LastLocationCommandsConsole;
    public List<String> LastLocationSaveType;
    public String LastLocationCommandLocationNotExist;
    public String LastLocationCommandSpawn;
    public boolean LastLocationSave;
    public int ConfigVersion;
    public List<String> CommandAlias;

    public void reloadConfig() {
        configFile.reloadLobbyConfig();
        loadLobbyConfig();
    }

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }

    public void saveConfig(){
        configFile.saveLobbyConfig();
        loadLobbyConfig();
    }

    public MainLobbyConfigManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
        configFile = new CustomLobbyConfig("lobby.yml", null, plugin);
        configFile.registerLobbyConfig();
        loadLobbyConfig();
    }

    public void updateLobbyConfig() {
        FileConfiguration config = this.getConfig();
        int version = config.getInt("config_version");
        int newVersion = 4;

        if (version != newVersion) {
            boolean changed = addMissingFields(config, config);

            if (version < newVersion) {
                List<String> lobbyCommands = new ArrayList<>();
                lobbyCommands.add("lobby");
                config.set("commands_alias", lobbyCommands);
                config.setComments("commands_alias", Comments.LobbyCommandsAlias);
                changed = true;
            }

            createFile("lobby-new.yml", "lobby.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "lobby-new.yml");

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

    public void loadLobbyConfig() {
        FileConfiguration config = configFile.getConfig();

        Enabled = config.getBoolean("enabled");
        ConfigVersion = config.getInt("config_version");

        CommandAlias = config.getStringList("commands_alias");

        TeleportDelayEnabled = config.getBoolean("teleport_delay.enabled");
        TeleportDelay = config.getInt("teleport_delay.seconds");
        TeleportDelayCancelOnMove = config.getBoolean("teleport_delay.cancel_on_move");
        TeleportDelayMessageType = config.getString("teleport_delay.");
        TeleportDelayBlindness = config.getBoolean("teleport_delay.blindness");
        TeleportDelayBlindnessTime = config.getInt("teleport_delay.blindness_time");

        TeleportSoundEnabled = config.getBoolean("sound_settings.enabled");
        TeleportSound = config.getString("sound_settings.sound");
        TeleportSoundVolume = config.getInt("sound_settings.volume");
        TeleportSoundPitch = config.getInt("sound_settings.pitch");

        CommandsEnabled = config.getBoolean("commands.enabled");
        CommandsPlayer = config.getStringList("commands.player");
        CommandsConsole = config.getStringList("commands.console");

        CooldownEnabled = config.getBoolean("cooldown.enabled");
        CooldownTime = config.getInt("cooldown.time");


        LastLocationSave = config.getBoolean("last_location.save_settings.enabled");
        LastLocationSaveType = config.getStringList("last_location.save_settings.save_type");
        LastLocationSaveIgnoredWorlds = config.getStringList("last_location.save_settings.ignored_worlds");

        LastLocationTeleportMode = config.getStringList("last_location.teleport_settings.teleport_mode");

        LastLocationChangeWorldType = config.getString("last_location.teleport_settings.change_world.type");
        LastLocationChangeWorldsSpecified = config.getStringList("last_location.teleport_settings.change_world.specified_worlds");
        LastLocationChangeWorldsOneTime = config.getBoolean("last_location.teleport_settings.change_world.one_time");

        LastLocationCommandOneTime = config.getBoolean("last_location.teleport_settings.command.one_time");
        LastLocationCommandLocationNotExist = config.getString("last_location.teleport_settings.command.location_not_exist");
        LastLocationCommandSpawn = config.getString("last_location.teleport_settings.command.spawn");

        LastLocationSoundEnabled = config.getBoolean("last_location.teleport_settings.sound_settings.enabled");
        LastLocationSound = config.getString("last_location.teleport_settings.sound_settings.sound");
        LastLocationSoundVolume = config.getInt("last_location.teleport_settings.sound_settings.volume");
        LastLocationSoundPitch = config.getInt("last_location.teleport_settings.sound_settings.pitch");

        LastLocationCommandsEnabled = config.getBoolean("last_location.teleport_settings.commands.enabled");
        LastLocationCommandsPlayer = config.getStringList("last_location.teleport_settings.commands.player");
        LastLocationCommandsConsole = config.getStringList("last_location.teleport_settings.commands.console");
    }

    public boolean isEnabled() {
        return Enabled;
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

    public boolean isTeleportDelayBlindness() {
        return TeleportDelayBlindness;
    }

    public int getTeleportDelayBlindnessTime() {
        return TeleportDelayBlindnessTime;
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

    public List<String> getLastLocationTeleportMode() {
        return LastLocationTeleportMode;
    }

    public List<String> getLastLocationSaveIgnoredWorlds() {
        return LastLocationSaveIgnoredWorlds;
    }

    public String getLastLocationChangeWorldType() {
        return LastLocationChangeWorldType;
    }

    public List<String> getLastLocationChangeWorldsSpecified() {
        return LastLocationChangeWorldsSpecified;
    }

    public boolean isLastLocationChangeWorldsOneTime() {
        return LastLocationChangeWorldsOneTime;
    }

    public boolean isLastLocationCommandOneTime() {
        return LastLocationCommandOneTime;
    }

    public boolean isLastLocationSoundEnabled() {
        return LastLocationSoundEnabled;
    }

    public String getLastLocationSound() {
        return LastLocationSound;
    }

    public int getLastLocationSoundVolume() {
        return LastLocationSoundVolume;
    }

    public int getLastLocationSoundPitch() {
        return LastLocationSoundPitch;
    }

    public boolean isLastLocationCommandsEnabled() {
        return LastLocationCommandsEnabled;
    }

    public List<String> getLastLocationCommandsPlayer() {
        return LastLocationCommandsPlayer;
    }

    public List<String> getLastLocationCommandsConsole() {
        return LastLocationCommandsConsole;
    }

    public List<String> getLastLocationSaveType() {
        return LastLocationSaveType;
    }

    public String getLastLocationCommandLocationNotExist() {
        return LastLocationCommandLocationNotExist;
    }

    public String getLastLocationCommandSpawn() {
        return LastLocationCommandSpawn;
    }

    public boolean isLastLocationSave() {
        return LastLocationSave;
    }

    public int getConfigVersion() {
        return ConfigVersion;
    }

    public List<String> getCommandAlias() {
        return CommandAlias;
    }
}
