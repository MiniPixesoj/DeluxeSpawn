package com.pixesoj.config;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationsManager {
    private DeluxeSpawn plugin;
    private String fileName = "locations.yml";
    private FileConfiguration fileConfiguration;
    private File file;

    public LocationsManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    public void loadLocationsFile() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Crear directorios si no existen
            plugin.saveResource(fileName, false); // Guardar el archivo desde recursos
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void saveLocationsFile() {
        if (fileConfiguration != null && file != null) {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getLocationsFile() {
        if (fileConfiguration == null) {
            loadLocationsFile();
        }
        return fileConfiguration;
    }
}