package com.pixesoj.filesmanager.permissions;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.filesmanager.permissions.CustomPermissions;
import org.bukkit.configuration.file.FileConfiguration;

public class MainPermissionsManager {
    private final CustomPermissions permissionsFile;

    public String Lobby;
    public boolean LobbyDefault;
    public String SetLobby;
    public boolean SetLobbyDefault;
    public String Spawn;
    public boolean SpawnDefault;
    public String SpawnWorld;
    public boolean SpawnWorldDefault;
    public String SetSpawn;
    public boolean SetSpawnDefault;
    public String Reload;
    public boolean ReloadDefault;
    public String Version;
    public boolean VersionDefault;
    public String Help;
    public boolean HelpDefault;
    public String Notify;
    public boolean NotifyDefault;
    public String NotifyUpdate;
    public boolean NotifyUpdateDefault;
    public String LobbyBypassDelay;
    public boolean LobbyBypassDelayDefault;
    public String SpawnBypassDelay;
    public boolean SpawnBypassDelayDefault;
    public String LastLocationBypassCommand;
    public boolean LastLocationBypassCommandDefault;
    public String LastLocation;
    public  boolean LastLocationDefault;
    public String LobbyBypassCooldown;
    public boolean LobbyBypassCooldownDefault;
    public String SpawnBypassCooldown;
    public boolean SpawnBypassCooldownDefault;


    public void reloadPermissions() {
        permissionsFile.reloadPermissions();
        loadPermissions();
    }

    public MainPermissionsManager(DeluxeSpawn plugin) {
        permissionsFile = new CustomPermissions("permissions.yml", null, plugin);
        permissionsFile.registerPermissions();
        loadPermissions();
    }

    public void loadPermissions() {
        FileConfiguration permissions = permissionsFile.getPermissions();
        Lobby = permissions.getString("Lobby.Permission");
        LobbyDefault = permissions.getBoolean("Lobby.Default");
        SetLobby = permissions.getString("SetLobby.Permission");
        SetLobbyDefault = permissions.getBoolean("SetLobby.Default");
        Spawn = permissions.getString("Spawn.Permission");
        SpawnDefault = permissions.getBoolean("Spawn.Default");
        SpawnWorld = permissions.getString("SpawnWorld.Permission");
        SpawnWorldDefault = permissions.getBoolean("SpawnWorld.Default");
        SetSpawn = permissions.getString("SetSpawn.Permission");
        SetSpawnDefault = permissions.getBoolean("SetSpawn.Default");
        Reload = permissions.getString("Reload.Permission");
        ReloadDefault = permissions.getBoolean("Reload.Default");
        Version = permissions.getString("Version.Permission");
        VersionDefault = permissions.getBoolean("Version.Default");
        Help = permissions.getString("Help.Permission");
        HelpDefault = permissions.getBoolean("Help.Default");
        Notify = permissions.getString("Notify.Permission");
        NotifyDefault = permissions.getBoolean("Notify.Default");
        NotifyUpdate = permissions.getString("NotifyUpdate.Permission");
        NotifyUpdateDefault = permissions.getBoolean("NotifyUpdate.Default");
        LobbyBypassDelay = permissions.getString("LobbyBypassDelay.Permission");
        LobbyBypassDelayDefault = permissions.getBoolean("LobbyBypassDelay.Default");
        SpawnBypassDelay = permissions.getString("SpawnBypassDelay.Permission");
        SpawnBypassDelayDefault = permissions.getBoolean("SpawnBypassDelay.Default");
        LastLocationBypassCommand = permissions.getString("LastLocationBypassCommand.Permission");
        LastLocationBypassCommandDefault = permissions.getBoolean("LastLocationBypassCommand.Default");
        LastLocation = permissions.getString("LastLocation.Permission");
        LastLocationDefault = permissions.getBoolean("LastLocation.Default");
        LobbyBypassCooldown = permissions.getString("LobbyBypassCooldown.Permission");
        LobbyBypassCooldownDefault = permissions.getBoolean("LobbyBypassCooldown.Default");
        SpawnBypassCooldown = permissions.getString("SpawnBypassCooldown.Permission");
        SpawnBypassCooldownDefault = permissions.getBoolean("SpawnBypassCooldown.Default");
    }

    public String getLobby() {
        return Lobby;
    }

    public boolean isLobbyDefault() {
        return LobbyDefault;
    }

    public String getSetLobby() {
        return SetLobby;
    }

    public boolean isSetLobbyDefault() {
        return SetLobbyDefault;
    }

    public String getSpawn() {
        return Spawn;
    }

    public boolean isSpawnDefault() {
        return SpawnDefault;
    }

    public String getSpawnWorld() {
        return SpawnWorld;
    }

    public boolean isSpawnWorldDefault() {
        return SpawnWorldDefault;
    }

    public String getSetSpawn() {
        return SetSpawn;
    }

    public boolean isSetSpawnDefault() {
        return SetSpawnDefault;
    }

    public String getReload() {
        return Reload;
    }

    public boolean isReloadDefault() {
        return ReloadDefault;
    }

    public String getVersion() {
        return Version;
    }

    public boolean isVersionDefault() {
        return VersionDefault;
    }

    public String getHelp() {
        return Help;
    }

    public boolean isHelpDefault() {
        return HelpDefault;
    }

    public String getNotify() {
        return Notify;
    }

    public boolean isNotifyDefault() {
        return NotifyDefault;
    }

    public String getNotifyUpdate() {
        return NotifyUpdate;
    }

    public boolean isNotifyUpdateDefault() {
        return NotifyUpdateDefault;
    }

    public String getLobbyBypassDelay() {
        return LobbyBypassDelay;
    }

    public boolean isLobbyBypassDelayDefault() {
        return LobbyBypassDelayDefault;
    }

    public String getSpawnBypassDelay() {
        return SpawnBypassDelay;
    }

    public boolean isSpawnBypassDelayDefault() {
        return SpawnBypassDelayDefault;
    }

    public String getLastLocationBypassCommand() {
        return LastLocationBypassCommand;
    }

    public boolean isLastLocationBypassCommandDefault() {
        return LastLocationBypassCommandDefault;
    }

    public String getLastLocation() {
        return LastLocation;
    }

    public boolean isLastLocationDefault() {
        return LastLocationDefault;
    }

    public String getLobbyBypassCooldown() {
        return LobbyBypassCooldown;
    }

    public boolean isLobbyBypassCooldownDefault() {
        return LobbyBypassCooldownDefault;
    }

    public String getSpawnBypassCooldown() {
        return SpawnBypassCooldown;
    }

    public boolean isSpawnBypassCooldownDefault() {
        return SpawnBypassCooldownDefault;
    }
}