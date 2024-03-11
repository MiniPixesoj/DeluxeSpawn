package com.pixesoj.commands.tabcompleter;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommandTabCompleter implements org.bukkit.command.TabCompleter {
    private final DeluxeSpawn plugin;
    public MainCommandTabCompleter(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String pr = plugin.getMainPermissionsManager().getReload();
            boolean dr= plugin.getMainPermissionsManager().isReloadDefault();
            if (PlayerUtils.hasPermission(sender, pr, dr)){
                completions.add("reload");
            }

            String pv = plugin.getMainPermissionsManager().getVersion();
            boolean dv = plugin.getMainPermissionsManager().isVersionDefault();
            if (PlayerUtils.hasPermission(sender, pv, dv)){
                completions.add("version");
            }

            String ph = plugin.getMainPermissionsManager().getHelp();
            boolean dh = plugin.getMainPermissionsManager().isHelpDefault();
            if (PlayerUtils.hasPermission(sender, ph, dh)){
                completions.add("help");
            }

            String pll = plugin.getMainPermissionsManager().getLastLocation();
            boolean dll = plugin.getMainPermissionsManager().isLastLocationDefault();
            if (PlayerUtils.hasPermission(sender, pll, dll)){
                completions.add("lastlocation");
            }

            String pu = plugin.getMainPermissionsManager().getUpdate();
            boolean du = plugin.getMainPermissionsManager().isUpdateDefault();
            if (PlayerUtils.hasPermission(sender, pu, du)){
                completions.add("update");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            completions.add("all");
            completions.add("config");
            completions.add("messages");
            completions.add("permissions");
        }
        return completions;
    }
}
