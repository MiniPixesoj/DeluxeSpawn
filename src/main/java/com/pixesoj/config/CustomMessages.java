package com.pixesoj.config;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class CustomMessages {
    private DeluxeSpawn plugin;
    private String fileName;
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private String folderName;

    public CustomMessages(String fileName, String folderName, DeluxeSpawn plugin){
        this.fileName = fileName;
        this.folderName = folderName;
        this.plugin = plugin;
    }

    public String getPath(){
        return this.fileName;
    }

    public void registerMessages(){
        if(folderName != null){
            file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
        }else{
            file = new File(plugin.getDataFolder(), fileName);
        }

        if(!file.exists()){
            if(folderName != null){
                plugin.saveResource(folderName + File.separator + fileName, false);
            }else{
                plugin.saveResource(fileName, false);
            }
        }

        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessages() {
        if (fileConfiguration == null) {
            reloadMessages();
        }
        return fileConfiguration;
    }

    public boolean reloadMessages() {
        if (fileConfiguration == null) {
            if (folderName != null) {
                file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
            } else {
                file = new File(plugin.getDataFolder(), fileName);
            }
        }

        if (!file.exists()) {
            try (InputStream resource = plugin.getResource((folderName != null ? folderName + "/" : "") + fileName)) {
                if (resource != null) {
                    Files.copy(resource, file.toPath());
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            fileConfiguration = YamlConfiguration.loadConfiguration(file);
            return true;
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        return true;
    }

    // Nuevo método para obtener el objeto File
    public File getFile() {
        return file;
    }
}
