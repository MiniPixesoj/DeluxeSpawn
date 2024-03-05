package com.pixesoj.managers;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;


public class CooldownManagerLobby {
    int TaskID;
    private DeluxeSpawn plugin;
    int time;
    private Player player;

    public CooldownManagerLobby(DeluxeSpawn plugin, int time, Player player) {
        this.plugin = plugin;
        this.time = time;
        this.player = player;
    }

    public void cooldownLobby() {

            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (time == 0){
                        Bukkit.getScheduler().cancelTask(TaskID);
                        plugin.removeLobbyCooldown(player);
                        // Time finish
                    } else {
                        time--;
                    }
                }
                }, 0L, 20);
    }
}
