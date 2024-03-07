package com.pixesoj.managers.cooldown;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.model.internal.CooldownTimeProvider;
import org.bukkit.entity.Player;

public class LobbyCooldownProvider implements CooldownTimeProvider {
    private DeluxeSpawn plugin;
    private Player player;

    public LobbyCooldownProvider(DeluxeSpawn plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public int getRemainingTime() {
        return plugin.getMainConfigManager().getLobbyCooldownTime();
    }
}
