package com.pixesoj.deluxespawn;

import com.pixesoj.commands.*;
import com.pixesoj.config.LocationsManager;
import com.pixesoj.config.MainConfigManager;
import com.pixesoj.config.MainMessagesManager;
import com.pixesoj.listeners.OnJoin;
import com.pixesoj.listeners.OnRespawn;
import com.pixesoj.listeners.OnVoid;
import com.pixesoj.managers.UpdateCheckerManager;
import com.pixesoj.model.internal.UpdateCheckerResult;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class DeluxeSpawn extends JavaPlugin {

    public String version;
    public static String prefix;
    private MainConfigManager mainConfigManager;
    private MainMessagesManager mainMessagesManager;
    private UpdateCheckerManager updateCheckerManager;
    private LocationsManager locationsManager;

    public void onEnable() {

        version = getDescription().getVersion();
        prefix = ChatColor.translateAlternateColorCodes('&', "&8[&eDeluxeSpawn&8] ");

        registerCommands();
        registerEvents();

        registerMessages();
        mainConfigManager = new MainConfigManager(this);
        mainMessagesManager = new MainMessagesManager(this);
        locationsManager = new LocationsManager(this);
        locationsManager.loadLocationsFile();
        this.updateCheckerManager = new UpdateCheckerManager(this.version);
        if (this.getMainConfigManager().isCheckUpdate()) {
            this.updateMessage(this.updateCheckerManager.check());
        }
        players = new ArrayList<>();
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6╔═══╦═══╦╗  ╔╗ ╔╦═╗╔═╦═══╦═══╦═══╦═══╦╗╔╗╔╦═╗ ╔╗"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6╚╗╔╗║╔══╣║  ║║ ║╠╗╚╝╔╣╔══╣╔═╗║╔═╗║╔═╗║║║║║║║╚╗║║"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6 ║║║║╚══╣║  ║║ ║║╚╗╔╝║╚══╣╚══╣╚═╝║║ ║║║║║║║╔╗╚╝║"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6 ║║║║╔══╣║ ╔╣║ ║║╔╝╚╗║╔══╩══╗║╔══╣╚═╝║╚╝╚╝║║╚╗║║"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6╔╝╚╝║╚══╣╚═╝║╚═╝╠╝╔╗╚╣╚══╣╚═╝║║  ║╔═╗╠╗╔╗╔╣║ ║║║"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&6╚═══╩═══╩═══╩═══╩═╝╚═╩═══╩═══╩╝  ╚╝ ╚╝╚╝╚╝╚╝ ╚═╝"));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Version: &a" + this.version));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Author: &bPixesoj"));
    }


    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Version: &a" + this.version));
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(prefix + "&7Author: &bPixesoj"));
    }

    public void registerCommands() {
        this.getCommand("deluxespawn").setExecutor(new MainCommand(this));
        this.getCommand("setspawn").setExecutor(new SetSpawn(this));
        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("setlobby").setExecutor(new SetLobby(this));
        this.getCommand("lobby").setExecutor(new Lobby(this));
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        getServer().getPluginManager().registerEvents(new OnVoid(this), this);
        getServer().getPluginManager().registerEvents(new OnRespawn(this), this);
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public MainMessagesManager getMainMessagesManager() {
        return mainMessagesManager;
    }

    public UpdateCheckerManager getUpdateCheckerManager() {
        return this.updateCheckerManager;
    }

    public void updateMessage(UpdateCheckerResult result) {
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

        File messagesHi = new File(langFolder, "messages-hi.yml");
        if (!messagesHi.exists()){
            saveResource("lang/messages-hi.yml", false);
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

    private ArrayList<String> players;

    public void addPlayer(Player player){
        players.add(player.getName());
    }

    public void removePlayer(Player player){
        players.remove(player.getName());
    }

    public boolean playerInDelay(Player player){
        if (players.contains(player.getName())){
            return true;
        } else {
            return false;
        }
    }
}


