package com.pixesoj.config;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfigManager {
    private final CustomConfig configFile;

    private boolean CheckUpdate;
    private String Lang;
    private boolean SpawnByWorld;
    private int SpawnTeleportDelay;
    private boolean SpawnTeleportDelayEnabled;
    private boolean SpawnTeleportDelayCancelOnMove;
    private String SpawnTeleportDelayMessageType;
    private boolean SpawnTeleportBlindness;
    private int SpawnTeleportBlindnessTime;
    private boolean LobbySettingsEnabled;
    private int LobbyTeleportDelay;
    private boolean LobbyTeleportDelayEnabled;
    private boolean LobbyTeleportDelayCancelOnMove;
    private String LobbyTeleportDelayMessageType;
    public boolean LobbyTeleportBlindness;
    public int LobbyTeleportBlindnessTime;
    public boolean TeleportOnJoinEnabled;
    public String TeleportOnJoinDestinationPlace;
    public boolean TeleportOnJoinSendMessage;
    public String TeleportOnJoinSpawn;
    public boolean TeleportOnFirstJoinJoinEnabled;
    public String TeleportOnFirstJoinDestinationPlace;
    public boolean TeleportOnFirstJoinSendMessage;
    public String TeleportOnFirstJoinSpawn;
    public boolean TeleportOnFirstJoinWelcomeMessage;
    public boolean SpawnTeleportSoundEnabled;
    public String SpawnTeleportSound;
    public int SpawnTeleportSoundVolume;
    public int SpawnTeleportSoundPitch;
    public boolean LobbyTeleportSoundEnabled;
    public String LobbyTeleportSound;
    public int LobbyTeleportSoundVolume;
    public int LobbyTeleportSoundPitch;
    public boolean SpawnCommandsEnabled;
    public List<String> SpawnPlayerCommands;
    public List<String> SpawnConsoleCommands;
    public boolean LobbyCommandsEnabled;
    public List<String> LobbyPlayerCommands;
    public List<String> LobbyConsoleCommands;
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


    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }
    public MainConfigManager(DeluxeSpawn plugin){
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();

        CheckUpdate = config.getBoolean("CheckUpdate");
        Lang = config.getString("Lang");
        SpawnByWorld = config.getBoolean("SpawnSettings.SpawnByWorld");
        SpawnTeleportDelay = config.getInt("SpawnSettings.TeleportDelay.Seconds");
        SpawnTeleportDelayEnabled = config.getBoolean("SpawnSettings.TeleportDelay.Enabled");
        SpawnTeleportDelayCancelOnMove = config.getBoolean("SpawnSettings.TeleportDelay.CancelOnMove");
        SpawnTeleportDelayMessageType = config.getString("SpawnSettings.TeleportDelay.MessageType");
        SpawnTeleportBlindness = config.getBoolean("SpawnSettings.TeleportDelay.Blindness");
        SpawnTeleportBlindnessTime = config.getInt("SpawnSettings.TeleportDelay.BlindnessTime");
        LobbySettingsEnabled = config.getBoolean("LobbySettings.Enabled");
        LobbyTeleportDelay = config.getInt("LobbySettings.TeleportDelay.Seconds");
        LobbyTeleportDelayEnabled = config.getBoolean("LobbySettings.TeleportDelay.Enabled");
        LobbyTeleportDelayCancelOnMove = config.getBoolean("LobbySettings.TeleportDelay.CancelOnMove");
        LobbyTeleportDelayMessageType = config.getString("LobbySettings.TeleportDelay.MessageType");
        LobbyTeleportBlindness = config.getBoolean("LobbySettings.TeleportDelay.Blindness");
        LobbyTeleportBlindnessTime = config.getInt("LobbySettings.TeleportDelay.BlindnessTime");
        TeleportOnJoinEnabled = config.getBoolean("TeleportOnJoin.Enabled");
        TeleportOnJoinDestinationPlace = config.getString("TeleportOnJoin.DestinationPlaceSettings.DestinationPlace");
        TeleportOnJoinSendMessage = config.getBoolean("TeleportOnJoin.DestinationPlaceSettings.SendMessageTeleport");
        TeleportOnJoinSpawn = config.getString("TeleportOnJoin.DestinationPlaceSettings.Spawn");
        TeleportOnFirstJoinJoinEnabled = config.getBoolean("TeleportOnJoin.OnlyFirstJoin.Enabled");
        TeleportOnFirstJoinDestinationPlace = config.getString("TeleportOnJoin.OnlyFirstJoin.DestinationPlaceSettings.DestinationPlace");
        TeleportOnFirstJoinSpawn = config.getString("TeleportOnJoin.OnlyFirstJoin.DestinationPlaceSettings.Spawn");
        TeleportOnFirstJoinSendMessage = config.getBoolean("TeleportOnJoin.OnlyFirstJoin.DestinationPlaceSettings.SendMessageTeleport");
        TeleportOnFirstJoinWelcomeMessage = config.getBoolean("TeleportOnJoin.OnlyFirstJoin.DestinationPlaceSettings.WelcomeMessage");
        SpawnTeleportSoundEnabled = config.getBoolean("SpawnSettings.SoundSettings.Enabled");
        SpawnTeleportSound = config.getString("SpawnSettings.SoundSettings.Sound");
        SpawnTeleportSoundVolume = config.getInt("SpawnSettings.SoundSettings.Volume");
        SpawnTeleportSoundPitch = config.getInt("SpawnSettings.SoundSettings.Pitch");
        LobbyTeleportSoundEnabled = config.getBoolean("LobbySettings.SoundSettings.Enabled");
        LobbyTeleportSound = config.getString("LobbySettings.SoundSettings.Sound");
        LobbyTeleportSoundVolume = config.getInt("LobbySettings.SoundSettings.Volume");
        LobbyTeleportSoundPitch = config.getInt("LobbySettings.SoundSettings.Pitch");
        SpawnCommandsEnabled = config.getBoolean("SpawnSettings.Commands.Enabled");
        SpawnPlayerCommands = config.getStringList("SpawnSettings.Commands.Player");
        SpawnConsoleCommands = config.getStringList("SpawnSettings.Commands.Console");
        LobbyCommandsEnabled = config.getBoolean("LobbySettings.Commands.Enabled");
        LobbyPlayerCommands = config.getStringList("LobbySettings.Commands.Player");
        LobbyConsoleCommands = config.getStringList("LobbySettings.Commands.Console");
        TeleportOnVoidEnabled = config.getBoolean("TeleportOnVoid.Enabled");
        TeleportOnVoidDamage = config.getBoolean("TeleportOnVoid.Damage");
        TeleportOnVoidBlindness = config.getBoolean("TeleportOnVoid.Blindness");
        TeleportOnVoidBlindnessTime = config.getInt("TeleportOnVoid.BlindnessTime");
        TeleportOnVoidHeight = config.getInt("TeleportOnVoid.TeleportHeight");
        TeleportOnVoidIgnoreWorlds = config.getStringList("TeleportOnVoid.IgnoredWorlds");
        TeleportOnVoidDestinationPlace = config.getString("TeleportOnVoid.DestinationPlaceSettings.DestinationPlace");
        TeleportOnVoidDestinationSpawn = config.getString("TeleportOnVoid.DestinationPlaceSettings.Spawn");
        TeleportOnVoidSendMessage = config.getBoolean("TeleportOnVoid.DestinationPlaceSettings.SendMessageTeleport");
        TeleportOnVoidSoundEnabled = config.getBoolean("TeleportOnVoid.SoundSettings.Enabled");
        TeleportOnVoidSound = config.getString("TeleportOnVoid.SoundSettings.Sound");
        TeleportOnVoidSoundVolume = config.getInt("TeleportOnVoid.SoundSettings.Volume");
        TeleportOnVoidSoundPitch = config.getInt("TeleportOnVoid.SoundSettings.Pitch");
        TeleportOnVoidCommandsEnabled = config.getBoolean("TeleportOnVoid.Commands.Enabled");
        TeleportOnVoidCommandsPlayer = config.getStringList("TeleportOnVoid.Commands.Player");
        TeleportOnVoidCommandsConsole = config.getStringList("TeleportOnVoid.Commands.Console");
        TeleportOnRespawnEnabled = config.getBoolean("TeleportOnRespawn.Enabled");
        TeleportOnRespawnIgnoreBed = config.getBoolean("TeleportOnRespawn.IgnoreBed");
        TeleportOnRespawnBlindness = config.getBoolean("TeleportOnRespawn.Blindness");
        TeleportOnRespawnBlindnessTime = config.getInt("TeleportOnRespawn.BlindnessTime");
        TeleportOnRespawnIgnoredWorlds = config.getStringList("TeleportOnRespawn.IgnoredWorlds");
        TeleportOnRespawnDestinationPlace = config.getString("TeleportOnRespawn.DestinationPlaceSettings.DestinationPlace");
        TeleportOnRespawnDestinationSpawn = config.getString("TeleportOnRespawn.DestinationPlaceSettings.Spawn");
        TeleportOnRespawnSendMessageTeleport = config.getBoolean("TeleportOnRespawn.DestinationPlaceSettings.SendMessageTeleport");
        TeleportOnRespawnSoundEnabled = config.getBoolean("TeleportOnRespawn.SoundSettings.Enabled");
        TeleportOnRespawnSound = config.getString("TeleportOnRespawn.SoundSettings.Sound");
        TeleportOnRespawnSoundVolume = config.getInt("TeleportOnRespawn.SoundSettings.Volume");
        TeleportOnRespawnSoundPitch = config.getInt("TeleportOnRespawn.SoundSettings.Pitch");
        TeleportOnRespawnCommandsEnabled = config.getBoolean("TeleportOnRespawn.Commands.Enabled");
        TeleportOnRespawnCommandsPlayer = config.getStringList("TeleportOnRespawn.Commands.Player");
        TeleportOnRespawnCommandsConsole = config.getStringList("TeleportOnRespawn.Commands.Console");
        TeleportOnJoinSoundEnabled = config.getBoolean("TeleportOnJoin.SoundSettings.Enabled");
        TeleportOnJoinSound = config.getString("TeleportOnJoin.SoundSettings.Sound");
        TeleportOnJoinSoundVolume = config.getInt("TeleportOnJoin.SoundSettings.Volume");
        TeleportOnJoinSoundPitch = config.getInt("TeleportOnJoin.SoundSettings.Pitch");
        TeleportOnFirstJoinSoundEnabled = config.getBoolean("TeleportOnJoin.OnlyFirstJoin.SoundSettings.Enabled");
        TeleportOnFirstJoinSound = config.getString("TeleportOnJoin.OnlyFirstJoin.SoundSettings.Sound");
        TeleportOnFirstJoinSoundVolume = config.getInt("TeleportOnJoin.OnlyFirstJoin.SoundSettings.Volume");
        TeleportOnFirstJoinSoundPitch = config.getInt("TeleportOnJoin.OnlyFirstJoin.SoundSettings.Pitch");
        TeleportOnFirstJoinCommandsEnabled = config.getBoolean("TeleportOnJoin.OnlyFirstJoin.Commands.Enabled");
        TeleportOnFirstJoinCommandsPlayer = config.getStringList("TeleportOnJoin.OnlyFirstJoin.Commands.Player");
        TeleportOnFirstJoinCommandsConsole = config.getStringList("TeleportOnJoin.OnlyFirstJoin.Commands.Console");
        TeleportOnJoinCommandsEnabled = config.getBoolean("TeleportOnJoin.Commands.Enabled");
        TeleportOnJoinCommandsPlayer = config.getStringList("TeleportOnJoin.Commands.Player");
        TeleportOnJoinCommandsConsole = config.getStringList("TeleportOnJoin.Commands.Console");
    }


    public boolean isCheckUpdate() {
        return CheckUpdate;
    }

    public String getLang() {
        return Lang;
    }

    public boolean isSpawnByWorld() {
        return SpawnByWorld;
    }

    public int getSpawnTeleportDelay() {
        return SpawnTeleportDelay;
    }

    public boolean isSpawnTeleportDelayEnabled() {
        return SpawnTeleportDelayEnabled;
    }

    public boolean isSpawnTeleportDelayCancelOnMove() {
        return SpawnTeleportDelayCancelOnMove;
    }

    public String getSpawnTeleportDelayMessageType() {
        return SpawnTeleportDelayMessageType;
    }

    public boolean isSpawnTeleportBlindness() {
        return SpawnTeleportBlindness;
    }

    public int getSpawnTeleportBlindnessTime() {
        return SpawnTeleportBlindnessTime;
    }

    public boolean isLobbyEnabled() {
        return LobbySettingsEnabled;
    }

    public int getLobbyTeleportDelay() {
        return LobbyTeleportDelay;
    }

    public boolean isLobbyTeleportDelayEnabled() {
        return LobbyTeleportDelayEnabled;
    }

    public boolean isLobbyTeleportDelayCancelOnMove() {
        return LobbyTeleportDelayCancelOnMove;
    }

    public String getLobbyTeleportDelayMessageType() {
        return LobbyTeleportDelayMessageType;
    }

    public boolean isLobbyTeleportBlindness() {
        return LobbyTeleportBlindness;
    }

    public int getLobbyTeleportBlindnessTime() {
        return LobbyTeleportBlindnessTime;
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

    public boolean isSpawnTeleportSoundEnabled() {
        return SpawnTeleportSoundEnabled;
    }

    public String getSpawnTeleportSound() {
        return SpawnTeleportSound;
    }

    public int getSpawnTeleportSoundVolume() {
        return SpawnTeleportSoundVolume;
    }

    public int getSpawnTeleportSoundPitch() {
        return SpawnTeleportSoundPitch;
    }

    public boolean isLobbyTeleportSoundEnabled() {
        return LobbyTeleportSoundEnabled;
    }

    public String getLobbyTeleportSound() {
        return LobbyTeleportSound;
    }

    public int getLobbyTeleportSoundVolume() {
        return LobbyTeleportSoundVolume;
    }

    public int getLobbyTeleportSoundPitch() {
        return LobbyTeleportSoundPitch;
    }

    public boolean isSpawnCommandsEnabled() {
        return SpawnCommandsEnabled;
    }

    public List<String> getSpawnPlayerCommands() {
        return SpawnPlayerCommands;
    }

    public List<String> getSpawnConsoleCommands() {
        return SpawnConsoleCommands;
    }

    public boolean isLobbyCommandsEnabled() {
        return LobbyCommandsEnabled;
    }

    public List<String> getLobbyPlayerCommands() {
        return LobbyPlayerCommands;
    }

    public List<String> getLobbyConsoleCommands() {
        return LobbyConsoleCommands;
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
}
