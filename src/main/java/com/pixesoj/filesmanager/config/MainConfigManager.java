package com.pixesoj.filesmanager.config;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfigManager {
    private final CustomConfig configFile;

    private boolean CheckUpdate;
    private String Lang;
    public boolean TeleportOnJoinEnabled;
    public String TeleportOnJoinDestinationPlace;
    public boolean TeleportOnJoinSendMessage;
    public String TeleportOnJoinSpawn;
    public boolean TeleportOnFirstJoinJoinEnabled;
    public String TeleportOnFirstJoinDestinationPlace;
    public boolean TeleportOnFirstJoinSendMessage;
    public String TeleportOnFirstJoinSpawn;
    public boolean TeleportOnFirstJoinWelcomeMessage;
    public boolean TeleportOnVoidEnabled;
    public boolean TeleportOnVoidDamage;
    public boolean TeleportOnVoidBlindness;
    public int TeleportOnVoidBlindnessTime;
    public int TeleportOnVoidHeight;
    public List<String> TeleportOnVoidIgnoreWorlds;
    public String TeleportOnVoidDestinationPlace;
    public String TeleportOnVoidDestinationSpawn;
    public boolean TeleportOnVoidSendMessage;
    public boolean TeleportOnVoidSoundEnabled;
    public String  TeleportOnVoidSound;
    public int TeleportOnVoidSoundVolume;
    public int TeleportOnVoidSoundPitch;
    public boolean TeleportOnVoidCommandsEnabled;
    public List<String> TeleportOnVoidCommandsPlayer;
    public List<String> TeleportOnVoidCommandsConsole;
    public boolean TeleportOnRespawnEnabled;
    public boolean TeleportOnRespawnIgnoreBed;
    public boolean TeleportOnRespawnBlindness;
    public int TeleportOnRespawnBlindnessTime;
    public List<String> TeleportOnRespawnIgnoredWorlds;
    public String TeleportOnRespawnDestinationPlace;
    public String TeleportOnRespawnDestinationSpawn;
    public boolean TeleportOnRespawnSendMessageTeleport;
    public boolean TeleportOnRespawnSoundEnabled;
    public String  TeleportOnRespawnSound;
    public int TeleportOnRespawnSoundVolume;
    public int TeleportOnRespawnSoundPitch;
    public boolean TeleportOnRespawnCommandsEnabled;
    public List<String> TeleportOnRespawnCommandsPlayer;
    public List<String> TeleportOnRespawnCommandsConsole;
    public boolean TeleportOnJoinSoundEnabled;
    public String TeleportOnJoinSound;
    public int TeleportOnJoinSoundVolume;
    public int TeleportOnJoinSoundPitch;
    public boolean TeleportOnFirstJoinSoundEnabled;
    public String TeleportOnFirstJoinSound;
    public int TeleportOnFirstJoinSoundVolume;
    public int TeleportOnFirstJoinSoundPitch;
    public boolean TeleportOnFirstJoinCommandsEnabled;
    public List<String> TeleportOnFirstJoinCommandsPlayer;
    public List<String> TeleportOnFirstJoinCommandsConsole;
    public boolean TeleportOnJoinCommandsEnabled;
    public List<String> TeleportOnJoinCommandsPlayer;
    public List<String> TeleportOnJoinCommandsConsole;
    public String ReplacedMessagesConsole;
    public boolean Update;
    public String DataType;
    public String DataAddress;
    public int DataPort;
    public String Database;
    public String DataUserName;
    public String DataPassword;
    public String DataTableName;



    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }
    public MainConfigManager(DeluxeSpawn plugin){
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = configFile.getConfig();

        CheckUpdate = config.getBoolean("check_update");
        Update = config.getBoolean("auto_update");
        Lang = config.getString("lang");
        ReplacedMessagesConsole = config.getString("replaced_messages.console");

        TeleportOnJoinEnabled = config.getBoolean("teleport_on_join.enabled");
        TeleportOnJoinDestinationPlace = config.getString("teleport_on_join.destination_place_settings.destination_place");
        TeleportOnJoinSendMessage = config.getBoolean("teleport_on_join.destination_place_settings.send_message_teleport");
        TeleportOnJoinSpawn = config.getString("teleport_on_join.destination_place_settings.spawn");
        TeleportOnJoinSoundEnabled = config.getBoolean("teleport_on_join.sound_settings.enabled");
        TeleportOnJoinSound = config.getString("teleport_on_join.sound_settings.sound");
        TeleportOnJoinSoundVolume = config.getInt("teleport_on_join.sound_settings.volume");
        TeleportOnJoinSoundPitch = config.getInt("teleport_on_join.sound_settings.pitch");
        TeleportOnJoinCommandsEnabled = config.getBoolean("teleport_on_join.commands.enabled");
        TeleportOnJoinCommandsPlayer = config.getStringList("teleport_on_join.commands.player");
        TeleportOnJoinCommandsConsole = config.getStringList("teleport_on_join.commands.console");

        TeleportOnFirstJoinJoinEnabled = config.getBoolean("teleport_on_join.only_first_join.enabled");
        TeleportOnFirstJoinDestinationPlace = config.getString("teleport_on_join.only_first_join.destination_place_settings.destination_place");
        TeleportOnFirstJoinSpawn = config.getString("teleport_on_join.only_first_join.destination_place_settings.spawn");
        TeleportOnFirstJoinSendMessage = config.getBoolean("teleport_on_join.only_first_join.destination_place_settings.send_message_teleport");
        TeleportOnFirstJoinWelcomeMessage = config.getBoolean("teleport_on_join.only_first_join.destination_place_settings.welcome_message");
        TeleportOnFirstJoinSoundEnabled = config.getBoolean("teleport_on_join.only_first_join.sound_settings.enabled");
        TeleportOnFirstJoinSound = config.getString("teleport_on_join.only_first_join.sound_settings.sound");
        TeleportOnFirstJoinSoundVolume = config.getInt("teleport_on_join.only_first_join.sound_settings.volume");
        TeleportOnFirstJoinSoundPitch = config.getInt("teleport_on_join.only_first_join.sound_settings.pitch");
        TeleportOnFirstJoinCommandsEnabled = config.getBoolean("teleport_on_join.only_first_join.commands.enabled");
        TeleportOnFirstJoinCommandsPlayer = config.getStringList("teleport_on_join.only_first_join.commands.player");
        TeleportOnFirstJoinCommandsConsole = config.getStringList("teleport_on_join.only_first_join.commands.console");

        TeleportOnVoidEnabled = config.getBoolean("teleport_on_void.enabled");
        TeleportOnVoidDamage = config.getBoolean("teleport_on_void.damage");
        TeleportOnVoidBlindness = config.getBoolean("teleport_on_void.blindness");
        TeleportOnVoidBlindnessTime = config.getInt("teleport_on_void.blindness_time");
        TeleportOnVoidHeight = config.getInt("teleport_on_void.teleport_height");
        TeleportOnVoidIgnoreWorlds = config.getStringList("teleport_on_void.ignored_worlds");
        TeleportOnVoidDestinationPlace = config.getString("teleport_on_void.destination_place_settings.destination_place");
        TeleportOnVoidDestinationSpawn = config.getString("teleport_on_void.destination_place_settings.spawn");
        TeleportOnVoidSendMessage = config.getBoolean("teleport_on_void.destination_place_settings.send_message_teleport");
        TeleportOnVoidSoundEnabled = config.getBoolean("teleport_on_void.sound_settings.enabled");
        TeleportOnVoidSound = config.getString("teleport_on_void.sound_settings.sound");
        TeleportOnVoidSoundVolume = config.getInt("teleport_on_void.sound_settings.volume");
        TeleportOnVoidSoundPitch = config.getInt("teleport_on_void.sound_settings.pitch");
        TeleportOnVoidCommandsEnabled = config.getBoolean("teleport_on_void.commands.enabled");
        TeleportOnVoidCommandsPlayer = config.getStringList("teleport_on_void.commands.player");
        TeleportOnVoidCommandsConsole = config.getStringList("teleport_on_void.commands.console");

        TeleportOnRespawnEnabled = config.getBoolean("teleport_on_respawn.enabled");
        TeleportOnRespawnIgnoreBed = config.getBoolean("teleport_on_respawn.ignore_bed");
        TeleportOnRespawnBlindness = config.getBoolean("teleport_on_respawn.blindness");
        TeleportOnRespawnBlindnessTime = config.getInt("teleport_on_respawn.blindness_time");
        TeleportOnRespawnIgnoredWorlds = config.getStringList("teleport_on_respawn.ignored_worlds");
        TeleportOnRespawnDestinationPlace = config.getString("teleport_on_respawn.destination_place_settings.destination_place");
        TeleportOnRespawnDestinationSpawn = config.getString("teleport_on_respawn.destination_place_settings.spawn");
        TeleportOnRespawnSendMessageTeleport = config.getBoolean("teleport_on_respawn.destination_place_settings.send_message_teleport");
        TeleportOnRespawnSoundEnabled = config.getBoolean("teleport_on_respawn.sound_settings.enabled");
        TeleportOnRespawnSound = config.getString("teleport_on_respawn.sound_settings.sound");
        TeleportOnRespawnSoundVolume = config.getInt("teleport_on_respawn.sound_settings.volume");
        TeleportOnRespawnSoundPitch = config.getInt("teleport_on_respawn.sound_settings.pitch");
        TeleportOnRespawnCommandsEnabled = config.getBoolean("teleport_on_respawn.commands.enabled");
        TeleportOnRespawnCommandsPlayer = config.getStringList("teleport_on_respawn.commands.player");
        TeleportOnRespawnCommandsConsole = config.getStringList("teleport_on_respawn.commands.console");

        DataType = config.getString("database.type");
        DataAddress = config.getString("database.address");
        DataPort = config.getInt("database.port");
        Database = config.getString("database.database");
        DataUserName = config.getString("database.username");
        DataPassword = config.getString("database.password");
        DataTableName = config.getString("database.table_name");
    }


    public boolean isCheckUpdate() {
        return CheckUpdate;
    }

    public String getLang() {
        return Lang;
    }

    public boolean isTeleportOnJoinEnabled() {
        return TeleportOnJoinEnabled;
    }

    public String getTeleportOnJoinDestinationPlace() {
        return TeleportOnJoinDestinationPlace;
    }

    public boolean isTeleportOnJoinSendMessage() {
        return TeleportOnJoinSendMessage;
    }

    public String getTeleportOnJoinSpawn() {
        return TeleportOnJoinSpawn;
    }

    public boolean isTeleportOnFirstJoinJoinEnabled() {
        return TeleportOnFirstJoinJoinEnabled;
    }

    public String getTeleportOnFirstJoinDestinationPlace() {
        return TeleportOnFirstJoinDestinationPlace;
    }

    public boolean isTeleportOnFirstJoinSendMessage() {
        return TeleportOnFirstJoinSendMessage;
    }

    public String getTeleportOnFirstJoinSpawn() {
        return TeleportOnFirstJoinSpawn;
    }

    public boolean isTeleportOnFirstJoinWelcomeMessage() {
        return TeleportOnFirstJoinWelcomeMessage;
    }

    public boolean isTeleportOnVoidEnabled() {
        return TeleportOnVoidEnabled;
    }

    public boolean isTeleportOnVoidDamage() {
        return TeleportOnVoidDamage;
    }

    public boolean isTeleportOnVoidBlindness() {
        return TeleportOnVoidBlindness;
    }

    public int getTeleportOnVoidBlindnessTime() {
        return TeleportOnVoidBlindnessTime;
    }

    public int getTeleportOnVoidHeight() {
        return TeleportOnVoidHeight;
    }

    public List<String> getTeleportOnVoidIgnoreWorlds() {
        return TeleportOnVoidIgnoreWorlds;
    }

    public String getTeleportOnVoidDestinationPlace() {
        return TeleportOnVoidDestinationPlace;
    }

    public String getTeleportOnVoidDestinationSpawn() {
        return TeleportOnVoidDestinationSpawn;
    }

    public boolean isTeleportOnVoidSendMessage() {
        return TeleportOnVoidSendMessage;
    }

    public boolean isTeleportOnVoidSoundEnabled() {
        return TeleportOnVoidSoundEnabled;
    }

    public String getTeleportOnVoidSound() {
        return TeleportOnVoidSound;
    }

    public int getTeleportOnVoidSoundVolume() {
        return TeleportOnVoidSoundVolume;
    }

    public int getTeleportOnVoidSoundPitch() {
        return TeleportOnVoidSoundPitch;
    }

    public boolean isTeleportOnVoidCommandsEnabled() {
        return TeleportOnVoidCommandsEnabled;
    }

    public List<String> getTeleportOnVoidCommandsPlayer() {
        return TeleportOnVoidCommandsPlayer;
    }

    public List<String> getTeleportOnVoidCommandsConsole() {
        return TeleportOnVoidCommandsConsole;
    }

    public boolean isTeleportOnRespawnEnabled() {
        return TeleportOnRespawnEnabled;
    }

    public boolean isTeleportOnRespawnIgnoreBed() {
        return TeleportOnRespawnIgnoreBed;
    }

    public boolean isTeleportOnRespawnBlindness() {
        return TeleportOnRespawnBlindness;
    }

    public int getTeleportOnRespawnBlindnessTime() {
        return TeleportOnRespawnBlindnessTime;
    }

    public List<String> getTeleportOnRespawnIgnoredWorlds() {
        return TeleportOnRespawnIgnoredWorlds;
    }

    public String getTeleportOnRespawnDestinationPlace() {
        return TeleportOnRespawnDestinationPlace;
    }

    public String getTeleportOnRespawnDestinationSpawn() {
        return TeleportOnRespawnDestinationSpawn;
    }

    public boolean isTeleportOnRespawnSendMessageTeleport() {
        return TeleportOnRespawnSendMessageTeleport;
    }

    public boolean isTeleportOnRespawnSoundEnabled() {
        return TeleportOnRespawnSoundEnabled;
    }

    public String getTeleportOnRespawnSound() {
        return TeleportOnRespawnSound;
    }

    public int getTeleportOnRespawnSoundVolume() {
        return TeleportOnRespawnSoundVolume;
    }

    public int getTeleportOnRespawnSoundPitch() {
        return TeleportOnRespawnSoundPitch;
    }

    public boolean isTeleportOnRespawnCommandsEnabled() {
        return TeleportOnRespawnCommandsEnabled;
    }

    public List<String> getTeleportOnRespawnCommandsPlayer() {
        return TeleportOnRespawnCommandsPlayer;
    }

    public List<String> getTeleportOnRespawnCommandsConsole() {
        return TeleportOnRespawnCommandsConsole;
    }

    public boolean isTeleportOnJoinSoundEnabled() {
        return TeleportOnJoinSoundEnabled;
    }

    public String getTeleportOnJoinSound() {
        return TeleportOnJoinSound;
    }

    public int getTeleportOnJoinSoundVolume() {
        return TeleportOnJoinSoundVolume;
    }

    public int getTeleportOnJoinSoundPitch() {
        return TeleportOnJoinSoundPitch;
    }

    public boolean isTeleportOnFirstJoinSoundEnabled() {
        return TeleportOnFirstJoinSoundEnabled;
    }

    public String getTeleportOnFirstJoinSound() {
        return TeleportOnFirstJoinSound;
    }

    public int getTeleportOnFirstJoinSoundVolume() {
        return TeleportOnFirstJoinSoundVolume;
    }

    public int getTeleportOnFirstJoinSoundPitch() {
        return TeleportOnFirstJoinSoundPitch;
    }

    public boolean isTeleportOnFirstJoinCommandsEnabled() {
        return TeleportOnFirstJoinCommandsEnabled;
    }

    public List<String> getTeleportOnFirstJoinCommandsPlayer() {
        return TeleportOnFirstJoinCommandsPlayer;
    }

    public List<String> getTeleportOnFirstJoinCommandsConsole() {
        return TeleportOnFirstJoinCommandsConsole;
    }

    public boolean isTeleportOnJoinCommandsEnabled() {
        return TeleportOnJoinCommandsEnabled;
    }

    public List<String> getTeleportOnJoinCommandsPlayer() {
        return TeleportOnJoinCommandsPlayer;
    }

    public List<String> getTeleportOnJoinCommandsConsole() {
        return TeleportOnJoinCommandsConsole;
    }

    public String getReplacedMessagesConsole() {
        return ReplacedMessagesConsole;
    }

    public boolean isUpdate() {
        return Update;
    }

    public String getDataType() {
        return DataType;
    }

    public String getDataAddress() {
        return DataAddress;
    }

    public int getDataPort() {
        return DataPort;
    }

    public String getDatabase() {
        return Database;
    }

    public String getDataUserName() {
        return DataUserName;
    }

    public String getDataPassword() {
        return DataPassword;
    }

    public String getDataTableName() {
        return DataTableName;
    }
}
