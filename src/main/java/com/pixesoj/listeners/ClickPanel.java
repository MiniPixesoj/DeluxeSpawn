package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.subcommands.Admin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.Objects;

public class ClickPanel implements Listener {

    private final DeluxeSpawn plugin;

    public ClickPanel (DeluxeSpawn plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void clickInventorySettings(InventoryClickEvent event) throws IOException, InvalidConfigurationException {
        if (event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        String inventoryTitle = ChatColor.stripColor(event.getView().getTitle());

        if (Objects.equals(event.getClickedInventory(), player.getOpenInventory().getTopInventory())) {
            if (inventoryTitle.equals("DeluxeSpawn » SETTINGS")) {
                event.setCancelled(true);
                int slot = event.getSlot();
                if (slot == 20) {
                    Admin.inventorySettings2(player, plugin);
                }
            }

            if (inventoryTitle.equals("DeluxeSpawn » SETTINGS (2)")) {
                event.setCancelled(true);
                int slot = event.getSlot();
                if (slot == 20) {
                    Admin.inventorySettingsGlobal(player, plugin);
                }
            }

            if (inventoryTitle.equals("DeluxeSpawn » SETTINGS GLOBAL")) {
                
            }
        }
    }
}
