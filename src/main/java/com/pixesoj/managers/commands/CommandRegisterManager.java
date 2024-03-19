package com.pixesoj.managers.commands;

import com.pixesoj.commands.Lobby;
import com.pixesoj.commands.Spawn;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CommandRegisterManager {

    private DeluxeSpawn plugin;

    public CommandRegisterManager(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    private CommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerCommands() {
        registerCommandsFromConfig(plugin.getMainSpawnConfigManager().getConfig(), Spawn.class);
        registerCommandsFromConfig(plugin.getMainLobbyConfigManager().getConfig(), Lobby.class);
    }

    private void registerCommandsFromConfig(FileConfiguration config, Class<? extends CommandExecutor> commandExecutorClass) {
        if (config.contains("commands_alias")) {
            List<String> commands = config.getStringList("commands_alias");
            for (String commandName : commands) {
                registerCommand(commandName, commandExecutorClass);
            }
        }
    }

    private void registerCommand(String commandName, Class<? extends CommandExecutor> commandExecutorClass) {
        CommandExecutor commandExecutor = null;
        try {
            Constructor<? extends CommandExecutor> constructor = commandExecutorClass.getConstructor(DeluxeSpawn.class);
            commandExecutor = constructor.newInstance(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (commandExecutor != null) {
            CommandRegister commandRegister = new CommandRegister(commandName, commandExecutor);
            CommandMap commandMap = getCommandMap();
            if (commandMap != null) {
                commandMap.register("DeluxeSpawn", commandRegister);
            }
        }
    }
}