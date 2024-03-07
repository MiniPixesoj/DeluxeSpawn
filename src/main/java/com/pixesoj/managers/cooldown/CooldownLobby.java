package com.pixesoj.managers.cooldown;

import com.pixesoj.commands.Lobby;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.model.internal.CooldownTimeProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;


public class CooldownLobby {
    private int taskID;
    private DeluxeSpawn plugin;
    private CooldownTimeProvider timeProvider;
    private Player player;
    private int time;

    public CooldownLobby(DeluxeSpawn plugin, CooldownTimeProvider timeProvider, int time, Player player) {
        this.plugin = plugin;
        this.timeProvider = timeProvider;
        this.player = player;
        this.time = time;
    }

    private static Map<Player, CooldownLobby> cooldownMap = new HashMap<>();

    public void cooldownLobby() {
        cooldownMap.put(player, this);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    plugin.removeLobbyCooldown(player);
                } else {
                    time--;
                }
            }
        }, 0L, 20);
    }

    public int getRemainingTime() {
        return time;
    }

    public static int getRemainingTime(Player player) {
        CooldownLobby cooldownLobby = cooldownMap.get(player);
        return (cooldownLobby != null) ? cooldownLobby.getRemainingTime() : 0;
    }
}
