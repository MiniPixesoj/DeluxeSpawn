package com.pixesoj.managers.commands;

import org.bukkit.command.CommandSender;

public abstract class BukkitCommandExecutor {
    public abstract void execute(CommandSender sender, String[] args);
}