package com.pixesoj.utils.common;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    boolean execute(CommandSender sender, String[] args);
}