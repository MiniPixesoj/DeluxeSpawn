package com.pixesoj.managers.commands;

import com.pixesoj.commands.Lobby;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

public class CommandRegister extends BukkitCommand {

    private final CommandExecutor commandExecutor;

    public CommandRegister(@NotNull String name, CommandExecutor commandExecutor) {
        super(name);
        this.commandExecutor = commandExecutor;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return commandExecutor.onCommand(sender, this, commandLabel, args);
    }
}