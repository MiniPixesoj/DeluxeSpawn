package com.pixesoj.commands.tabcompleter;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpawnTabCompleter implements TabCompleter {
    private DeluxeSpawn plugin;

    public SpawnTabCompleter(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        boolean spawnWorldEnabled = plugin.getMainConfigManager().isSpawnByWorld();
        if (command.getName().equalsIgnoreCase("spawn") && spawnWorldEnabled) {
            if (args.length == 1) {
                List<String> worldNames = new ArrayList<>();
                for (World world : Bukkit.getWorlds()) {
                    worldNames.add(world.getName());
                }
                return worldNames;
            } else {
                return Collections.emptyList();
            }
        }
        return null;
    }
}
