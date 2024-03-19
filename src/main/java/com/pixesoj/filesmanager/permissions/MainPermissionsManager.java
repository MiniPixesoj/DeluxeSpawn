package com.pixesoj.filesmanager.permissions;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.filesmanager.permissions.CustomPermissions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MainPermissionsManager {
    private final CustomPermissions permissionsFile;
    private final DeluxeSpawn plugin;

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
    public String DelSpawn;
    public boolean DelSpawnDefault;
    public String DelLobby;
    public boolean DelLobbyDefault;
    public String LobbyOther;
    public boolean LobbyOtherDefault;
    public String Update;
    public boolean UpdateDefault;
    public String SpawnOther;
    public boolean SpawnOtherDefault;
    public String AdminPanel;
    public boolean AdminPanelDefault;


    public void reloadPermissions() {
        permissionsFile.reloadPermissions();
        loadPermissions();
    }

    public FileConfiguration getPermissions(){
        return permissionsFile.getPermissions();
    }

    public void savePermissions(){
        permissionsFile.savePermissions();
        loadPermissions();
    }

    public MainPermissionsManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
        permissionsFile = new CustomPermissions("permissions.yml", null, plugin);
        permissionsFile.registerPermissions();
        loadPermissions();
    }

    public void updatePermissions() {
        FileConfiguration config = this.getPermissions();
        int version = config.getInt("permissions_version");
        int newVersion = 4;

        if (version != newVersion) {
            boolean changed = addMissingFields(config, config);

            if (version < newVersion) {
                changed = true;
            }

            createFile("permissions-new.yml", "permissions.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "permissions-new.yml");

            try {
                FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                config.set("permissions_version", newVersion);
                if (changed) {
                    this.savePermissions();
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

    public void loadPermissions() {
        FileConfiguration permissions = permissionsFile.getPermissions();
        Lobby = permissions.getString("lobby.permission");
        LobbyDefault = permissions.getBoolean("lobby.default");
        SetLobby = permissions.getString("setlobby.permission");
        SetLobbyDefault = permissions.getBoolean("setlobby.default");
        Spawn = permissions.getString("spawn.permission");
        SpawnDefault = permissions.getBoolean("spawn.default");
        SpawnWorld = permissions.getString("spawn_world.permission");
        SpawnWorldDefault = permissions.getBoolean("spawn_world.default");
        SetSpawn = permissions.getString("setspawn.permission");
        SetSpawnDefault = permissions.getBoolean("setspawn.default");
        Reload = permissions.getString("reload.permission");
        ReloadDefault = permissions.getBoolean("reload.default");
        Version = permissions.getString("version.permission");
        VersionDefault = permissions.getBoolean("version.default");
        Help = permissions.getString("help.permission");
        HelpDefault = permissions.getBoolean("help.default");
        Notify = permissions.getString("notify.permission");
        NotifyDefault = permissions.getBoolean("notify.default");
        NotifyUpdate = permissions.getString("notify_update.permission");
        NotifyUpdateDefault = permissions.getBoolean("notify_update.default");
        LobbyBypassDelay = permissions.getString("lobby_bypass_delay.permission");
        LobbyBypassDelayDefault = permissions.getBoolean("lobby_bypass_delay.default");
        SpawnBypassDelay = permissions.getString("spawn_bypass_delay.permission");
        SpawnBypassDelayDefault = permissions.getBoolean("spawn_bypass_delay.default");
        LastLocationBypassCommand = permissions.getString("lastlocation_bypass_command.permission");
        LastLocationBypassCommandDefault = permissions.getBoolean("lastlocation_bypass_command.default");
        LastLocation = permissions.getString("lastlocation.permission");
        LastLocationDefault = permissions.getBoolean("lastlocation.default");
        LobbyBypassCooldown = permissions.getString("lobby_bypass_cooldown.permission");
        LobbyBypassCooldownDefault = permissions.getBoolean("lobby_bypass_cooldown.default");
        SpawnBypassCooldown = permissions.getString("spawn_bypass_cooldown.permission");
        SpawnBypassCooldownDefault = permissions.getBoolean("spawn_bypass_cooldown.default");
        DelSpawn = permissions.getString("delspawn.permission");
        DelSpawnDefault = permissions.getBoolean("delspawn.default");
        DelLobby = permissions.getString("dellobby.permission");
        DelLobbyDefault = permissions.getBoolean("dellobby.default");
        LobbyOther = permissions.getString("lobby_other.permission");
        LobbyOtherDefault = permissions.getBoolean("lobby_other.default");
        Update = permissions.getString("update.permission");
        UpdateDefault = permissions.getBoolean("update.default");
        SpawnOther = permissions.getString("spawn_other.permission");
        SpawnOtherDefault = permissions.getBoolean("spawn_other.default");
        AdminPanel = permissions.getString("admin_panel.permission");
        AdminPanelDefault = permissions.getBoolean("admin_panel.default");
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

    public String getDelSpawn() {
        return DelSpawn;
    }

    public boolean isDelSpawnDefault() {
        return DelSpawnDefault;
    }

    public String getDelLobby() {
        return DelLobby;
    }

    public boolean isDelLobbyDefault() {
        return DelLobbyDefault;
    }

    public String getLobbyOther() {
        return LobbyOther;
    }

    public boolean isLobbyOtherDefault() {
        return LobbyOtherDefault;
    }

    public String getUpdate() {
        return Update;
    }

    public boolean isUpdateDefault() {
        return UpdateDefault;
    }

    public String getSpawnOther() {
        return SpawnOther;
    }

    public boolean isSpawnOtherDefault() {
        return SpawnOtherDefault;
    }

    public String getAdminPanel() {
        return AdminPanel;
    }

    public boolean isAdminPanelDefault() {
        return AdminPanelDefault;
    }
}