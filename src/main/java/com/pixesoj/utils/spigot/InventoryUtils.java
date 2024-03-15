package com.pixesoj.utils.spigot;

import com.pixesoj.model.item.ItemSkullData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtils {

    public static void fillSettings(Inventory inv, Player player){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        for (int i=0;i<=8;i++){
            inv.setItem(i, item);
        }

        for (int i=45;i<=53;i++){
            inv.setItem(i, item);
        }

        inv.setItem(9, item);
        inv.setItem(18, item);
        inv.setItem(27, item);
        inv.setItem(36, item);
        inv.setItem(17, item);
        inv.setItem(26, item);
        inv.setItem(35, item);
        inv.setItem(44, item);

        item = new ItemStack(Material.PLAYER_HEAD, 1);
        String texture1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJiOThhMjE5YmE0YWY1MTMyMWE4NWRiZjVmZjgzN2M1NjdkODBmMTA2NWE4ZGIxYTJjZWNiMTI1ZTYyMzAyNyJ9fX0=";
        ItemSkullData skullData = new ItemSkullData(null, texture1, null);
        ItemUtils.setSkullData(item, skullData, player);
        inv.setItem(20, item);
        meta.setDisplayName(MessagesUtils.getColoredMessage("&aAjustes"));
    }

    public static void fillSettings2(Inventory inv, Player player){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        for (int i=0;i<=8;i++){
            inv.setItem(i, item);
        }

        for (int i=45;i<=53;i++){
            inv.setItem(i, item);
        }

        inv.setItem(9, item);
        inv.setItem(18, item);
        inv.setItem(27, item);
        inv.setItem(36, item);
        inv.setItem(17, item);
        inv.setItem(26, item);
        inv.setItem(35, item);
        inv.setItem(44, item);

        item = new ItemStack(Material.PLAYER_HEAD, 1);
        String texture1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFkZTE5OWQ1YjNhOGI4YzZmZTJlZWI2MjhhZjM4ZGFhZGEzMDVmN2M5MGQxYmQxMjI0Y2YxZjA3MWZjYmU4YiJ9fX0=";
        ItemSkullData skullData = new ItemSkullData(null, texture1, null);
        ItemUtils.setSkullData(item, skullData, player);
        inv.setItem(20, item);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(MessagesUtils.getColoredMessage("&bGlobal"));
    }

    public static void fillSettingsGlobal(Inventory inv, Player player){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        for (int i=0;i<=8;i++){
            inv.setItem(i, item);
        }

        for (int i=45;i<=53;i++){
            inv.setItem(i, item);
        }

        inv.setItem(9, item);
        inv.setItem(18, item);
        inv.setItem(27, item);
        inv.setItem(36, item);
        inv.setItem(17, item);
        inv.setItem(26, item);
        inv.setItem(35, item);
        inv.setItem(44, item);

        item = new ItemStack(Material.LIME_DYE, 1);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Verificar nuevas actualizaciones");
        item.setItemMeta(meta);
        inv.setItem(20, item);
    }
}
