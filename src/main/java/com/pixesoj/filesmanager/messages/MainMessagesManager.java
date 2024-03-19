package com.pixesoj.filesmanager.messages;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.filesmanager.messages.CustomMessages;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class MainMessagesManager {
    private CustomMessages messageFile;
    private DeluxeSpawn plugin;

    private String Prefix;

    private List<String> CommandHelp;
    private String CommandReloadConfig;
    private String CommandReloadMessages;
    private String CommandInvalidArgument;
    private String PermissionDenied;
    private String CommandVersion;
    private String CommandUsage;
    private String CommandDeniedConsole;
    private String CommandReloadAll;
    private String CommandSetSpawnSuccessfully;
    private String SpawnDoesNotExist;
    private String SpawnTeleported;
    private String SpawnByWorldDoesNotExist;
    private String SpawnByWorldSpecifyDoesNotExist;
    private String SpawnOtherTeleported;
    private String SpawnInTeleport;
    private String SpawnMovementCanceledTeleport;
    private String SpawnChatMessageInTeleport;
    private String SpawnMessageDelayTeleport;
    private String LobbyIsNotEnabled;
    private String CommandSetLobbySuccessfully;
    private String LobbyDoesNotExist;
    private String LobbyTeleported;
    private String LobbyMessageDelayTeleport;
    private String LobbyMovementCanceledTeleport;
    private String LobbyChatMessageInTeleport;
    private String LobbyInTeleport;
    private String OnJoinSendMessage;
    private String OnJoinDoesNotExist;
    private String OnJoinDestinationInvalid;
    private String OnFirstJoinDestinationInvalid;
    private String OnFirstJoinDoesNotExist;
    private String OnFirstJoinSendMessage;
    private List<String> OnFirstJoinSendWelcomeMessage;
    private String SpawnNullSound;
    private String SpawnInvalidSound;
    private String LobbyNullSound;
    private String LobbyInvalidSound;
    private String OnVoidDestinationInvalid;
    private String OnVoidTeleportMessage;
    private String OnVoidInvalidSound;
    private String OnRespawnDestinationInvalid;
    private String OnRespawnSendMessage;
    private String OnRespawnDoesNotExist;
    private String OnRespawnDoesNotExistByWorld;
    private String OnRespawnNullSound;
    private String OnRespawnInvalidSound;
    private String OnJoinInvalidSound;
    private String OnJoinNullSound;
    private String OnFirstJoinInvalidSound;
    private String  OnFirstJoinNullSound;
    private String LobbyInCooldown;
    private String GlobalInCooldown;
    private String SpawnInCooldown;
    private String LastLocationNotFound;
    private String LastLocationTeleport;
    private String LastLocationOneTime;
    private String LastLocationNullSound;
    private String LastLocationInvalidSound;
    private String DelSpawnNotExist;
    private String DelSpawnNotExistWorld;
    private String DelSpawnSuccessfullyRemoved;
    private String DelSpawnSuccessfullyRemovedWorld;
    private String DelSpawnNotSpecified;
    private String DelLobbyNotExist;
    private String DelLobbySuccessfullyRemoved;
    private String LobbyOtherConsoleUsage;
    private String LobbyOtherPlayerOffline;
    private String LobbyOtherConsoleTeleported;
    private String LobbyOtherConsolePlayerTeleport;
    private String LobbyOtherPlayerTeleport;
    private String LobbyOtherPlayerTeleported;
    private String ErrorCommandReload;
    private String ErrorGeneral;
    private String ErrorNullSound;
    private String ErrorInvalidSound;
    private String SpawnOtherConsoleUsage;
    private String SpawnOtherPlayerOffline;
    private String SpawnOtherConsoleTeleported;
    private String SpawnOtherConsolePlayerTeleport;
    private String SpawnOtherPlayerTeleport;
    private String SpawnOtherPlayerTeleported;
    private String SpawnDoesNotExistConsole;
    private String SpawnOtherPlayerTeleportByWorld;
    private String SpawnOtherPlayerTeleportedByWorld;
    private String InvalidArguments;
    private String SpawnDoesNotExistConsoleGlobal;
    private String SpawnDoesNotExistG;

    public void reloadMessages(){
        messageFile.reloadMessages();
        loadMessages();
    }

    public void saveMessages(){
        messageFile.saveMessages();
        loadMessages();
    }

    public FileConfiguration getMessages(){
        return messageFile.getMessages();
    }

    public static void colored(String message) {
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public MainMessagesManager(DeluxeSpawn plugin){
        this.plugin = plugin;
        messageFile = new CustomMessages("messages-es.yml", "lang", plugin);
        messageFile.registerMessages();
        loadMessages();
    }

    public void updateMessages() {
        FileConfiguration config = plugin.getConfig();
        String lang = config.getString("lang", "en");
        String prefix = "&eDeluxeSpawn &8»";
        FileConfiguration messages = this.getMessages();
        int version = messages.getInt("messages_version");
        int newVersion = 4;

        if (version != newVersion) {
            colored(prefix + "&aUpdating messages to the latest version...");
            boolean changed = addMissingFields(messages, messages);

            if (version < newVersion) {
                /*messages.set("test", "test");*/
                changed = true;
            }

            createFile("messages-" + lang + "-new.yml", "lang/messages-" + lang + ".yml", plugin);
            File tempFile = new File(plugin.getDataFolder() + "/lang", "messages-" + lang + "-new.yml");

            try {
                FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                messages.set("messages_version", newVersion);
                if (changed) {
                    this.saveMessages();
                    colored(MessagesUtils.getColoredMessage(prefix + "&aDone! Updated messages!"));
                } else {
                    colored(MessagesUtils.getColoredMessage(prefix + "&aNo changes needed in the messages file"));
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
        File file = new File(plugin.getDataFolder() + "/lang", name);
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



    public void loadMessages() {
        FileConfiguration config = plugin.getConfig();
        String lang = config.getString("lang", "en");

        messageFile = new CustomMessages("messages-" + lang + ".yml", "lang", plugin);
        messageFile.registerMessages();

        if (!messageFile.getFile().exists()) {
            plugin.saveResource("lang/messages-" + lang + ".yml", false);
            messageFile.reloadMessages();
        }

        FileConfiguration messages = messageFile.getMessages();

        Prefix = messages.getString("Prefix");

        CommandHelp = messages.getStringList("CommandHelp");
        CommandReloadConfig = messages.getString("CommandReloadConfig");
        CommandReloadMessages = messages.getString("CommandReloadMessages");
        CommandInvalidArgument = messages.getString("CommandInvalidArgument");
        PermissionDenied = messages.getString("PermissionDenied");
        CommandVersion = messages.getString("CommandVersion");
        CommandUsage = messages.getString("CommandUsage");
        CommandDeniedConsole = messages.getString("CommandDeniedConsole");
        CommandReloadAll = messages.getString("CommandReloadAll");
        CommandSetSpawnSuccessfully = messages.getString("CommandSetSpawnSuccessfully");
        SpawnDoesNotExist = messages.getString("SpawnDoesNotExist");
        SpawnTeleported = messages.getString("SpawnTeleported");
        SpawnByWorldDoesNotExist = messages.getString("SpawnByWorldDoesNotExist");
        SpawnByWorldSpecifyDoesNotExist = messages.getString("SpawnByWorldSpecifyDoesNotExist");
        SpawnOtherTeleported = messages.getString("SpawnOtherTeleported");
        SpawnInTeleport = messages.getString("SpawnInTeleport");
        SpawnMovementCanceledTeleport = messages.getString("SpawnMovementCanceledTeleport");
        SpawnChatMessageInTeleport = messages.getString("SpawnChatMessageInTeleport");
        SpawnMessageDelayTeleport = messages.getString("SpawnMessageDelayTeleport");
        LobbyIsNotEnabled = messages.getString("LobbyIsNotEnabled");
        CommandSetLobbySuccessfully = messages.getString("CommandSetLobbySuccessfully");
        LobbyDoesNotExist = messages.getString("LobbyDoesNotExist");
        LobbyTeleported = messages.getString("LobbyTeleported");
        LobbyMessageDelayTeleport = messages.getString("LobbyMessageDelayTeleport");
        LobbyMovementCanceledTeleport = messages.getString("LobbyMovementCanceledTeleport");
        LobbyChatMessageInTeleport = messages.getString("LobbyChatMessageInTeleport");
        LobbyInTeleport = messages.getString("LobbyInTeleport");
        OnJoinSendMessage = messages.getString("OnJoinSendMessage");
        OnJoinDoesNotExist = messages.getString("OnJoinDoesNotExist");
        OnJoinDestinationInvalid = messages.getString("OnJoinDestinationInvalid");
        OnFirstJoinDestinationInvalid = messages.getString("OnFirstJoinDestinationInvalid");
        OnFirstJoinDoesNotExist = messages.getString("OnFirstJoinDoesNotExist");
        OnFirstJoinSendMessage = messages.getString("OnFirstJoinSendMessage");
        OnFirstJoinSendWelcomeMessage = messages.getStringList("OnFirstJoinSendWelcomeMessage");
        SpawnNullSound = messages.getString("SpawnNullSound");
        SpawnInvalidSound = messages.getString("SpawnInvalidSound");
        LobbyInvalidSound = messages.getString("LobbyInvalidSound");
        LobbyNullSound = messages.getString("LobbyNullSound");
        OnVoidDestinationInvalid = messages.getString("OnVoidDestinationInvalid");
        OnVoidTeleportMessage = messages.getString("OnVoidTeleportMessage");
        OnVoidInvalidSound = messages.getString("OnVoidInvalidSound");
        OnRespawnDestinationInvalid = messages.getString("OnRespawnDestinationInvalid");
        OnRespawnSendMessage = messages.getString("OnRespawnSendMessage");
        OnRespawnDoesNotExist = messages.getString("OnRespawnDoesNotExist");
        OnRespawnDoesNotExistByWorld = messages.getString("OnRespawnDoesNotExistByWorld");
        OnRespawnNullSound = messages.getString("OnRespawnNullSound");
        OnRespawnInvalidSound = messages.getString("OnRespawnInvalidSound");
        OnJoinInvalidSound = messages.getString("OnJoinInvalidSound");
        OnJoinNullSound = messages.getString("OnJoinNullSound");
        OnFirstJoinInvalidSound = messages.getString("OnFirstJoinInvalidSound");
        OnFirstJoinNullSound = messages.getString("OnFirstJoinNullSound");
        LobbyInCooldown = messages.getString("LobbyInCooldown");
        GlobalInCooldown = messages.getString("GlobalInCooldown");
        SpawnInCooldown = messages.getString("SpawnInCooldown");
        LastLocationNotFound = messages.getString("LastLocationNotFound");
        LastLocationTeleport = messages.getString("LastLocationTeleport");
        LastLocationOneTime = messages.getString("LastLocationOneTime");
        LastLocationNullSound = messages.getString("LastLocationNullSound");
        LastLocationInvalidSound = messages.getString("LastLocationInvalidSound");
        DelSpawnNotExist = messages.getString("DelSpawnNotExist");
        DelSpawnNotExistWorld = messages.getString("DelSpawnNotExistWorld");
        DelSpawnSuccessfullyRemoved = messages.getString("DelSpawnSuccessfullyRemoved");
        DelSpawnSuccessfullyRemovedWorld = messages.getString("DelSpawnSuccessfullyRemovedWorld");
        DelSpawnNotSpecified = messages.getString("DelSpawnNotSpecified");
        DelLobbyNotExist = messages.getString("DelLobbyNotExist");
        DelLobbySuccessfullyRemoved = messages.getString("DelLobbySuccessfullyRemoved");
        LobbyOtherConsoleUsage = messages.getString("LobbyOtherConsoleUsage");
        LobbyOtherPlayerOffline = messages.getString("LobbyOtherPlayerOffline");
        LobbyOtherConsoleTeleported = messages.getString("LobbyOtherConsoleTeleported");
        LobbyOtherConsolePlayerTeleport = messages.getString("LobbyOtherConsolePlayerTeleport");
        LobbyOtherPlayerTeleport = messages.getString("LobbyOtherPlayerTeleport");
        LobbyOtherPlayerTeleported = messages.getString("LobbyOtherPlayerTeleported");
        SpawnOtherConsoleUsage = messages.getString("SpawnOtherConsoleUsage");
        SpawnOtherPlayerOffline = messages.getString("SpawnOtherPlayerOffline");
        SpawnOtherConsoleTeleported = messages.getString("SpawnOtherConsoleTeleported");
        SpawnOtherConsolePlayerTeleport = messages.getString("SpawnOtherConsolePlayerTeleport");
        SpawnOtherPlayerTeleport = messages.getString("SpawnOtherPlayerTeleport");
        SpawnOtherPlayerTeleported = messages.getString("SpawnOtherPlayerTeleported");
        SpawnDoesNotExistConsole = messages.getString("SpawnDoesNotExistConsole");
        SpawnOtherPlayerTeleportByWorld = messages.getString("SpawnOtherPlayerTeleportByWorld");
        SpawnOtherPlayerTeleportedByWorld = messages.getString("SpawnOtherPlayerTeleportedByWorld");
        InvalidArguments = messages.getString("InvalidArguments");
        SpawnDoesNotExistConsoleGlobal = messages.getString("SpawnDoesNotExistConsoleGlobal");
        SpawnDoesNotExistG = messages.getString("SpawnDoesNotExistG");

        ErrorCommandReload = messages.getString("ErrorCommandReload");
        ErrorGeneral = messages.getString("ErrorGeneral");
        ErrorNullSound = messages.getString("ErrorNullSound");
        ErrorInvalidSound = messages.getString("ErrorInvalidSound");
    }

    public String getPrefix() {
        return Prefix;
    }

    public String getCommandReloadConfig() {
        return CommandReloadConfig;
    }

    public String getCommandReloadMessages() {
        return CommandReloadMessages;
    }

    public String getCommandInvalidArgument() {
        return CommandInvalidArgument;
    }

    public String getPermissionDenied() {
        return PermissionDenied;
    }

    public String getCommandVersion() {
        return CommandVersion;
    }

    public String getCommandUsage() {
        return CommandUsage;
    }

    public List<String> getCommandHelp() {
        return CommandHelp;
    }

    public String getCommandDeniedConsole() {
        return CommandDeniedConsole;
    }

    public String getCommandReloadAll() {
        return CommandReloadAll;
    }

    public String getCommandSetSpawnSuccessfully() {
        return CommandSetSpawnSuccessfully;
    }

    public String getSpawnDoesNotExist() {
        return SpawnDoesNotExist;
    }

    public String getSpawnTeleported() {
        return SpawnTeleported;
    }

    public String getSpawnByWorldDoesNotExist() {
        return SpawnByWorldDoesNotExist;
    }

    public String getSpawnByWorldSpecifyDoesNotExist() {
        return SpawnByWorldSpecifyDoesNotExist;
    }

    public String getSpawnOtherTeleported() {
        return SpawnOtherTeleported;
    }

    public String getSpawnInTeleport() {
        return SpawnInTeleport;
    }

    public String getSpawnMovementCanceledTeleport() {
        return SpawnMovementCanceledTeleport;
    }

    public String getSpawnChatMessageInTeleport() {
        return SpawnChatMessageInTeleport;
    }

    public String getSpawnMessageDelayTeleport() {
        return SpawnMessageDelayTeleport;
    }

    public String getLobbyIsNotEnabled() {
        return LobbyIsNotEnabled;
    }

    public String getCommandSetLobbySuccessfully() {
        return CommandSetLobbySuccessfully;
    }

    public String getLobbyDoesNotExist() {
        return LobbyDoesNotExist;
    }

    public String getLobbyTeleported() {
        return LobbyTeleported;
    }

    public String getLobbyMessageDelayTeleport() {
        return LobbyMessageDelayTeleport;
    }

    public String getLobbyMovementCanceledTeleport() {
        return LobbyMovementCanceledTeleport;
    }

    public String getLobbyChatMessageInTeleport() {
        return LobbyChatMessageInTeleport;
    }

    public String getLobbyInTeleport() {
        return LobbyInTeleport;
    }

    public String getOnJoinSendMessage() {
        return OnJoinSendMessage;
    }

    public String getOnJoinDoesNotExist() {
        return OnJoinDoesNotExist;
    }

    public String getOnJoinDestinationInvalid() {
        return OnJoinDestinationInvalid;
    }

    public String getOnFirstJoinDestinationInvalid() {
        return OnFirstJoinDestinationInvalid;
    }

    public String getOnFirstJoinDoesNotExist() {
        return OnFirstJoinDoesNotExist;
    }

    public String getOnFirstJoinSendMessage() {
        return OnFirstJoinSendMessage;
    }

    public List<String> getOnFirstJoinSendWelcomeMessage() {
        return OnFirstJoinSendWelcomeMessage;
    }

    public String getSpawnNullSound() {
        return SpawnNullSound;
    }

    public String getSpawnInvalidSound() {
        return SpawnInvalidSound;
    }

    public String getLobbyNullSound() {
        return LobbyNullSound;
    }

    public String getLobbyInvalidSound() {
        return LobbyInvalidSound;
    }

    public String getOnVoidDestinationInvalid() {
        return OnVoidDestinationInvalid;
    }

    public String getOnVoidTeleportMessage() {
        return OnVoidTeleportMessage;
    }

    public String getOnVoidInvalidSound() {
        return OnVoidInvalidSound;
    }

    public String getOnRespawnDestinationInvalid() {
        return OnRespawnDestinationInvalid;
    }

    public String getOnRespawnSendMessage() {
        return OnRespawnSendMessage;
    }

    public String getOnRespawnDoesNotExist() {
        return OnRespawnDoesNotExist;
    }

    public String getOnRespawnDoesNotExistByWorld() {
        return OnRespawnDoesNotExistByWorld;
    }

    public String getOnRespawnNullSound() {
        return OnRespawnNullSound;
    }

    public String getOnRespawnInvalidSound() {
        return OnRespawnInvalidSound;
    }

    public String getOnJoinInvalidSound() {
        return OnJoinInvalidSound;
    }

    public String getOnJoinNullSound() {
        return OnJoinNullSound;
    }

    public String getOnFirstJoinInvalidSound() {
        return OnFirstJoinInvalidSound;
    }

    public String getOnFirstJoinNullSound() {
        return OnFirstJoinNullSound;
    }

    public String getLobbyInCooldown() {
        return LobbyInCooldown;
    }

    public String getGlobalInCooldown() {
        return GlobalInCooldown;
    }

    public String getSpawnInCooldown() {
        return SpawnInCooldown;
    }

    public String getLastLocationNotFound() {
        return LastLocationNotFound;
    }

    public String getLastLocationTeleport() {
        return LastLocationTeleport;
    }

    public String getLastLocationOneTime() {
        return LastLocationOneTime;
    }

    public String getLastLocationNullSound() {
        return LastLocationNullSound;
    }

    public String getLastLocationInvalidSound() {
        return LastLocationInvalidSound;
    }

    public String getDelSpawnNotExist() {
        return DelSpawnNotExist;
    }

    public String getDelSpawnNotExistWorld() {
        return DelSpawnNotExistWorld;
    }

    public String getDelSpawnSuccessfullyRemoved() {
        return DelSpawnSuccessfullyRemoved;
    }

    public String getDelSpawnSuccessfullyRemovedWorld() {
        return DelSpawnSuccessfullyRemovedWorld;
    }

    public String getDelSpawnNotSpecified() {
        return DelSpawnNotSpecified;
    }

    public String getDelLobbyNotExist() {
        return DelLobbyNotExist;
    }

    public String getDelLobbySuccessfullyRemoved() {
        return DelLobbySuccessfullyRemoved;
    }

    public String getLobbyOtherConsoleUsage() {
        return LobbyOtherConsoleUsage;
    }

    public String getLobbyOtherPlayerOffline() {
        return LobbyOtherPlayerOffline;
    }

    public String getLobbyOtherConsoleTeleported() {
        return LobbyOtherConsoleTeleported;
    }

    public String getLobbyOtherConsolePlayerTeleport() {
        return LobbyOtherConsolePlayerTeleport;
    }

    public String getLobbyOtherPlayerTeleport() {
        return LobbyOtherPlayerTeleport;
    }

    public String getLobbyOtherPlayerTeleported() {
        return LobbyOtherPlayerTeleported;
    }

    public String getErrorCommandReload() {
        return ErrorCommandReload;
    }

    public String getErrorGeneral() {
        return ErrorGeneral;
    }

    public String getErrorNullSound() {
        return ErrorNullSound;
    }

    public String getErrorInvalidSound() {
        return ErrorInvalidSound;
    }

    public String getSpawnOtherConsoleUsage() {
        return SpawnOtherConsoleUsage;
    }

    public String getSpawnOtherPlayerOffline() {
        return SpawnOtherPlayerOffline;
    }

    public String getSpawnOtherConsoleTeleported() {
        return SpawnOtherConsoleTeleported;
    }

    public String getSpawnOtherConsolePlayerTeleport() {
        return SpawnOtherConsolePlayerTeleport;
    }

    public String getSpawnOtherPlayerTeleport() {
        return SpawnOtherPlayerTeleport;
    }

    public String getSpawnOtherPlayerTeleported() {
        return SpawnOtherPlayerTeleported;
    }

    public String getSpawnDoesNotExistConsole() {
        return SpawnDoesNotExistConsole;
    }

    public String getSpawnOtherPlayerTeleportByWorld() {
        return SpawnOtherPlayerTeleportByWorld;
    }

    public String getSpawnOtherPlayerTeleportedByWorld() {
        return SpawnOtherPlayerTeleportedByWorld;
    }

    public String getInvalidArguments() {
        return InvalidArguments;
    }

    public String getSpawnDoesNotExistConsoleGlobal() {
        return SpawnDoesNotExistConsoleGlobal;
    }

    public String getSpawnDoesNotExistG() {
        return SpawnDoesNotExistG;
    }
}
