package me.danle.speedrun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Speedrun implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speedrun")) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Usage: /speedrun [team/teleport/world]");
        } else {

        }
        return false;
    }

}
