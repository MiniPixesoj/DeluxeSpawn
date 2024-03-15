package com.pixesoj.utils.spigot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.model.item.ItemSkullData;
import com.pixesoj.utils.common.ServerVersion;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class ItemUtils {

    public static ItemSkullData getSkullData(ItemStack item) {
        ItemSkullData kitItemSkullData = null;
        String owner = null;
        String texture = null;
        String id = null;

        String typeName = item.getType().name();
        if (!typeName.equals("PLAYER_HEAD") && !typeName.equals("SKULL_ITEM")) {
            return null;
        }

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);

            GameProfile gameProfile = (GameProfile) profileField.get(skullMeta);
            if (gameProfile != null && gameProfile.getProperties() != null) {
                PropertyMap propertyMap = gameProfile.getProperties();
                owner = gameProfile.getName();
                if (gameProfile.getId() != null) {
                    id = gameProfile.getId().toString();
                }

                ServerVersion serverVersion = DeluxeSpawn.serverVersion;
                for (Property p : propertyMap.values()) {
                    if (serverVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_20_R2)) {
                        String pName = (String) p.getClass().getMethod("name").invoke(p);
                        if (pName.equals("textures")) {
                            texture = (String) p.getClass().getMethod("value").invoke(p);
                        }
                    } else {
                        if (p.getName().equals("textures")) {
                            texture = p.getValue();
                        }
                    }
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (texture != null || id != null || owner != null) {
            kitItemSkullData = new ItemSkullData(owner, texture, id);
        }

        return kitItemSkullData;
    }

    @SuppressWarnings("deprecation")
    public static void setSkullData(ItemStack item, ItemSkullData skullData, Player player) {
        String typeName = item.getType().name();
        if (!typeName.equals("PLAYER_HEAD") && !typeName.equals("SKULL_ITEM")) {
            return;
        }

        if (skullData == null) {
            return;
        }

        String texture = skullData.getTexture();
        String owner = skullData.getOwner();
        if (owner != null && player != null) {
            owner = owner.replace("%player%", player.getName());
        }
        String id = skullData.getId();
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (texture == null && owner != null) {
            skullMeta.setOwner(owner);
            item.setItemMeta(skullMeta);
            return;
        }

        ServerVersion serverVersion = DeluxeSpawn.serverVersion;
        if (serverVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_20_R2)) {
            UUID uuid = id != null ? UUID.fromString(id) : UUID.randomUUID();
            PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
            PlayerTextures textures = profile.getTextures();
            URL url;
            try {
                String decoded = new String(Base64.getDecoder().decode(texture));
                String decodedFormatted = decoded.replaceAll("\\s", "");
                int firstIndex = decodedFormatted.indexOf("\"SKIN\":{\"url\":") + 15;
                int lastIndex = decodedFormatted.indexOf("\"", firstIndex + 1);
                url = new URL(decodedFormatted.substring(firstIndex, lastIndex));
            } catch (MalformedURLException error) {
                error.printStackTrace();
                return;
            }
            textures.setSkin(url);
            profile.setTextures(textures);
            skullMeta.setOwnerProfile(profile);
        } else {
            GameProfile profile = null;
            if (id == null) {
                profile = new GameProfile(UUID.randomUUID(), owner != null ? owner : "");
            } else {
                profile = new GameProfile(UUID.fromString(id), owner != null ? owner : "");
            }
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | NoSuchFieldException | SecurityException |
                     IllegalAccessException error) {
                error.printStackTrace();
            }
        }

        item.setItemMeta(skullMeta);
    }
}
