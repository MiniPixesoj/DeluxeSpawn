package com.pixesoj.managers.cooldown;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.model.internal.CooldownTimeProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public class CooldownSpawn {
    private int taskID;
    private DeluxeSpawn plugin;
    private CooldownTimeProvider timeProvider;
    private Player player;
    private int time;

    public CooldownSpawn(DeluxeSpawn plugin, CooldownTimeProvider timeProvider, int time, Player player) {
        this.plugin = plugin;
        this.timeProvider = timeProvider;
        this.player = player;
        this.time = time;
    }

    private static Map<Player, CooldownSpawn> cooldownMap = new HashMap<>();

    public void cooldownSpawn() {
        cooldownMap.put(player, this);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    plugin.removeSpawnCooldown(player);
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
        CooldownSpawn cooldownSpawn = cooldownMap.get(player);
        return (cooldownSpawn != null) ? cooldownSpawn.getRemainingTime() : 0;
    }
}
