package com.pixesoj.commands.tabcompleter;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCommandTabCompleter implements org.bukkit.command.TabCompleter {
    private DeluxeSpawn plugin;
    public MainCommandTabCompleter(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("version");
            completions.add("help");
            completions.add("lastlocation");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            completions.add("all");
            completions.add("config");
            completions.add("messages");
            completions.add("permissions");
        }
        return completions;
    }
}
