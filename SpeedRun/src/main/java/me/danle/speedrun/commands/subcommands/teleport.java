package me.danle.speedrun.commands.subcommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import me.danle.speedrun.App;

public class teleport implements CommandExecutor {
    private App plugin;

    private ArrayList<Team> teamList = new ArrayList<Team>();
    private World world;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speedrun")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("teleport")) {
                Location spawn = world.getSpawnLocation();
                int radius = 50;

                for (int i = 0; i < teamList.size(); i++) {
                    Location loc = world
                            .getHighestBlockAt((int) (spawn.getX() + radius * Math.cos(Math.PI / teamList.size() * i)),
                                    (int) (spawn.getZ() + radius * Math.sin(Math.PI / teamList.size() * i)))
                            .getLocation();
                    for (String entry : teamList.get(i).getEntries()) { // get all player names from team
                        Player player = Bukkit.getPlayerExact(entry);
                        if (player != null) {
                            if (teamList == null) {
                                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
                                        + "Teams have not been created yet. \nDo /speedrun team create in order to create teams.");
                            }
                        }
                        player.teleport(loc);
                        player.setBedSpawnLocation(loc);
                    }
                }

                sender.sendMessage(ChatColor.YELLOW + "Teams have been teleported!");
                return true;
            }
        }

        return false;
    }
}
