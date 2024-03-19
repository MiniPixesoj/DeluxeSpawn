package com.pixesoj.deluxespawn;

import com.pixesoj.commands.*;
import com.pixesoj.filesmanager.LocationsManager;
import com.pixesoj.filesmanager.config.CustomConfig;
import com.pixesoj.filesmanager.config.MainConfigManager;
import com.pixesoj.filesmanager.lobby.MainLobbyConfigManager;
import com.pixesoj.filesmanager.messages.MainMessagesManager;
import com.pixesoj.filesmanager.permissions.MainPermissionsManager;
import com.pixesoj.filesmanager.spawn.MainSpawnConfigManager;
import com.pixesoj.listeners.*;
import com.pixesoj.managers.commands.CommandRegisterManager;
import com.pixesoj.managers.playerdata.PlayerDataManager;
import com.pixesoj.managers.UpdateCheckManager;
import com.pixesoj.model.internal.UpdateCheckResult;
import com.pixesoj.utils.common.JavaVersion;
import com.pixesoj.utils.common.MySQLUtils;
import com.pixesoj.utils.common.ServerVersion;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.managers.dependencies.Metrics;
import com.pixesoj.utils.common.UpdaterUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DeluxeSpawn extends JavaPlugin {

    public String version;
    public static String prefix;
    private MainConfigManager mainConfigManager;
    private CustomConfig customConfig;
    private MainLobbyConfigManager mainLobbyConfigManager;
    private MainSpawnConfigManager mainSpawnConfigManager;
    private MainMessagesManager mainMessagesManager;
    private UpdateCheckManager updateCheckerManager;
    private LocationsManager locationsManager;
    private MainPermissionsManager mainPermissionsManager;
    private PlayerDataManager playerDataManager;
    public static ServerVersion serverVersion;
    private UpdaterUtils updater;
    private MySQLUtils mySQLUtils;
    private CommandRegisterManager commandRegisterManager;

    public static void colored(String message) {
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void onEnable() {
        version = getDescription().getVersion();
        prefix = ChatColor.translateAlternateColorCodes('&', "&8[&eDeluxeSpawn&8] ");

        mainMessagesManager = new MainMessagesManager(this);
        locationsManager = new LocationsManager(this);
        mainConfigManager = new MainConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        commandRegisterManager = new CommandRegisterManager(this);
        mainLobbyConfigManager = new MainLobbyConfigManager(this);
        mainSpawnConfigManager = new MainSpawnConfigManager(this);
        mainPermissionsManager = new MainPermissionsManager(this);

        registerMessages();
        registerEvents();
        registerCommands();
        getCommandsRegisterManager();

        this.updateCheckerManager = new UpdateCheckManager(this.version);
        if (this.getMainConfigManager().isCheckUpdate()) {
            this.updateMessage(this.updateCheckerManager.check());
        }

        delayPlayers = new ArrayList<>();
        cooldownLobbyPlayers = new ArrayList<>();
        cooldownSpawnPlayers = new ArrayList<>();
        lastLocationOneTime = new ArrayList<>();

        mySQLUtils = new MySQLUtils(this);

        int pluginId = 21247;
        Metrics metrics = new Metrics(this, pluginId);

        getServer().getScheduler().runTaskLater(this, this::update, 50L);
        getServer().getScheduler().runTaskLater(this, this::updateConfigs, 1L);
        setVersion();

        colored("&6 ____   ___");
        colored("&6(  _ \\ / __)  " + "&8By &bPixesoj &av" + this.version);
        colored("&6 )(_) )\\__ \\  " + "&8Running on " + getServerSoftwareName());
        colored("&6(____/ (___/");
        colored(" ");
    }

    private String getServerSoftwareName() {
        String serverName = Bukkit.getName();
        if (serverName.equalsIgnoreCase("CraftBukkit")) {
            return ChatColor.GREEN + "CraftBukkit";
        } else if (serverName.equalsIgnoreCase("Spigot")) {
            return ChatColor.AQUA + "Spigot";
        } else if (serverName.equalsIgnoreCase("Paper")) {
            return ChatColor.YELLOW + "Paper";
        } else if (serverName.equalsIgnoreCase("Purpur")) {
            return ChatColor.LIGHT_PURPLE + "Purpur";
        } else if (serverName.equalsIgnoreCase("Tuinity")) {
            return ChatColor.GOLD + "Tuinity";
        } else if (serverName.equalsIgnoreCase("Mohist")) {
            return ChatColor.DARK_RED + "Mohist";
        } else if (serverName.equalsIgnoreCase("Yatopia")) {
            return ChatColor.BLUE + "Yatopia";
        } else if (serverName.equalsIgnoreCase("CatServer")) {
            return ChatColor.DARK_GREEN + "CatServer";
        } else {
            return ChatColor.RED + "Unknown Server Software";
        }
    }


    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Version: &a" + this.version));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Author: &bPixesoj"));
    }

    public void getCommandsRegisterManager (){
        int currentJavaVersion = JavaVersion.getCurrentJavaVersionNo();
        if (JavaVersion.getCurrentJavaVersion().ordinal() < JavaVersion.JAVA_11.ordinal()) {
            colored(prefix + "&cThis plugin requires Java 11 or higher to enjoy all its features, you can use &8(&6" + currentJavaVersion + "&8) &cbut command aliases will not work");
            return;
        }
        commandRegisterManager.registerCommands();
    }

    public void registerCommands() {
        this.getCommand("deluxespawn").setExecutor(new MainCommand(this));
        this.getCommand("setspawn").setExecutor(new SetSpawn(this));
        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("setlobby").setExecutor(new SetLobby(this));
        this.getCommand("lobby").setExecutor(new Lobby(this));
        this.getCommand("delspawn").setExecutor(new DelSpawn(this));
        this.getCommand("dellobby").setExecutor(new DelLobby(this));
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        getServer().getPluginManager().registerEvents(new OnVoid(this), this);
        getServer().getPluginManager().registerEvents(new OnRespawn(this), this);
        getServer().getPluginManager().registerEvents(new OnQuit(this), this);
        getServer().getPluginManager().registerEvents(new LastLocation(this), this);
        getServer().getPluginManager().registerEvents(new OnShutdown(this), this);
        getServer().getPluginManager().registerEvents(new ClickPanel(this), this);
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public CustomConfig getCustomConfig() {
        return customConfig;
    }

    public MainLobbyConfigManager getMainLobbyConfigManager() {
        return mainLobbyConfigManager;
    }

    public MainSpawnConfigManager getMainSpawnConfigManager() {
        return mainSpawnConfigManager;
    }

    public MainMessagesManager getMainMessagesManager() {
        return mainMessagesManager;
    }

    public UpdateCheckManager getUpdateCheckerManager() {
        return this.updateCheckerManager;
    }

    public MainPermissionsManager getMainPermissionsManager() {
        return mainPermissionsManager;
    }

    public UpdaterUtils getUpdater() {
        return updater;
    }

    public Connection getMySQL() {
        return mySQLUtils.getConnection();
    }

    public void updateMessage(UpdateCheckResult result) {
        if (!result.isError()) {
            String latestVersion = result.getLatestVersion();
            if (latestVersion != null) {
                Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&cThere is a new version available. &e(&7" + latestVersion + "&e)"));
                Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&cYou can download it at: &fhttps://spigotmc.org/resources/111403/"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + " &cError while checking update."));
        }
    }

    public LocationsManager getLocationsManager() {
        return locationsManager;
    }

    public void update(){
        boolean enabled = getMainConfigManager().isUpdate();
        if (!enabled){
            return;
        }
        String currentVersion = version;
        String jarName = getName();
        int resourceID = 111403;
        File pathName = new File("plugins");
        String message = "&eDeluxeSpawn &8»  &bLooking for updates...";
        colored(message);
        updater = new UpdaterUtils(currentVersion, jarName, true, resourceID, pathName, getServer().getConsoleSender());
    }

    public void updateConfigs(){
        getMainConfigManager().updateConfig();
        getMainMessagesManager().updateMessages();
        getMainSpawnConfigManager().updateSpawnConfig();
        getMainLobbyConfigManager().updateLobbyConfig();
        getMainPermissionsManager().updatePermissions();
    }

    public void registerMessages(){
        File langFolder = new File(this.getDataFolder(), "lang");
        if (!langFolder.exists()){
            langFolder.mkdirs();
        }

        File messagesEs = new File(langFolder, "messages-es.yml");
        if (!messagesEs.exists()){
            saveResource("lang/messages-es.yml", false);
        }

        File messagesEn = new File(langFolder, "messages-en.yml");
        if (!messagesEn.exists()){
            saveResource("lang/messages-en.yml", false);
        }

        File messagesZh = new File(langFolder, "messages-zh.yml");
        if (!messagesZh.exists()){
            saveResource("lang/messages-zh.yml", false);
        }

        File messagesAr = new File(langFolder, "messages-ar.yml");
        if (!messagesAr.exists()){
            saveResource("lang/messages-ar.yml", false);
        }

        File messagesPt = new File(langFolder, "messages-pt.yml");
        if (!messagesPt.exists()){
            saveResource("lang/messages-pt.yml", false);
        }

        File messagesRu = new File(langFolder, "messages-ru.yml");
        if (!messagesRu.exists()){
            saveResource("lang/messages-ru.yml", false);
        }

        File messagesFr = new File(langFolder, "messages-fr.yml");
        if (!messagesFr.exists()){
            saveResource("lang/messages-fr.yml", false);
        }

        File messagesDe = new File(langFolder, "messages-de.yml");
        if (!messagesDe.exists()){
            saveResource("lang/messages-de.yml", false);
        }

        File messagesJa = new File(langFolder, "messages-ja.yml");
        if (!messagesJa.exists()){
            saveResource("lang/messages-ja.yml", false);
        }
    }

    public void setVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        serverVersion = ServerVersion.valueOf(packageName.replace("org.bukkit.craftbukkit.", ""));
    }

    private ArrayList<String> delayPlayers;
    private ArrayList<String> cooldownLobbyPlayers;
    private ArrayList<String> cooldownSpawnPlayers;
    private ArrayList<String> lastLocationOneTime;

    public void addPlayerTeleport(Player player){
        delayPlayers.add(player.getName());
    }

    public void removePlayerTeleport(Player player){
        delayPlayers.remove(player.getName());
    }

    public boolean playerInDelay(Player player){
        return delayPlayers.contains(player.getName());
    }

    public void addLobbyCooldown (Player player){
        cooldownLobbyPlayers.add(player.getName());
    }

    public void removeLobbyCooldown (Player player){
        cooldownLobbyPlayers.remove(player.getName());
    }

    public boolean playerLobbyInCooldown(Player player){
        return cooldownLobbyPlayers.contains(player.getName());
    }

    public void addSpawnCooldown (Player player){
        cooldownSpawnPlayers.add(player.getName());
    }

    public void removeSpawnCooldown (Player player){
        cooldownSpawnPlayers.remove(player.getName());
    }

    public boolean playerSpawnInCooldown(Player player){
        return cooldownSpawnPlayers.contains(player.getName());
    }

    public void addLastLocationOneTime (Player player){
        lastLocationOneTime.add(player.getName());
    }

    public void removeLastLocationOneTime (Player player){
        lastLocationOneTime.remove(player.getName());
    }

    public boolean playerLastLocationOneTime(Player player){
        return lastLocationOneTime.contains(player.getName());
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}