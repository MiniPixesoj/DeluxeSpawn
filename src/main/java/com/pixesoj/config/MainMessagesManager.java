package com.pixesoj.config;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.file.FileConfiguration;

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

    public void reloadMessages(){
        messageFile.reloadMessages();
        loadMessages();
    }
    public MainMessagesManager(DeluxeSpawn plugin){
        this.plugin = plugin;
        messageFile = new CustomMessages("messages-es.yml", "lang", plugin);
        messageFile.registerMessages();
        loadMessages();
    }

    public void loadMessages() {
        FileConfiguration config = plugin.getConfig();
        String lang = config.getString("Lang", "en");

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
}
