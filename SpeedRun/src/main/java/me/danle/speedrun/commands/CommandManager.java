package me.danle.speedrun.commands;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.danle.speedrun.commands.subcommands.teamCommand;
import net.md_5.bungee.api.ChatColor;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new teamCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        getSubcommands().get(i).perform(p, args);
                    }
                }
            } else if (args.length == 0) {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                p.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "\n===========================================");
                p.sendMessage(ChatColor.GOLD + "\n------------------------------------------");
                for (int i = 0; i < subcommands.size(); i++) {
                    p.sendMessage(ChatColor.GOLD + subcommands.get(i).getSyntax() + " :\n" + "\n"
                            + subcommands.get(i).getDescription() + "\n------------------------------------------");
                    p.sendMessage(" ");
                }
                p.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "===========================================\n");
            }

        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

}
