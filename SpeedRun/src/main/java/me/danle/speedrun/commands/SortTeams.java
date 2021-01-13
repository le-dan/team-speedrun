package me.danle.speedrun.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import me.danle.speedrun.App;

public class SortTeams implements CommandExecutor {

    private App plugin;
    private ArrayList<Team> teamList = new ArrayList<Team>();
    private World world;
    private Location original;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // TODO
        // Create /speedrun spectate command
        if (cmd.getName().equalsIgnoreCase("speedrun")) {
            if (args == null || args.length == 0) {
                sender.sendMessage(
                        ChatColor.RED + "" + ChatColor.BOLD + "Usage: /speedrun {world/team create/teleport}");
            }

            switch (args[0].toLowerCase()) {

                case "world":
                    if (args.length <= 1) {
                        sender.sendMessage(
                                ChatColor.RED + "" + ChatColor.BOLD + "Usage: /speedrun world {create/reset/leave}");
                        return false;
                    }
                    switch (args[1].toLowerCase()) {
                        case "delete":
                        case "reset": // Resets world and creates new world
                            World reset = Bukkit.getServer().getWorld("speedrun_world");
                            for (Player player : reset.getPlayers()) {
                                player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
                            }
                            Bukkit.unloadWorld(reset, false);
                            if (!this.delete(reset.getWorldFolder())) {
                                sender.sendMessage(ChatColor.RED + "World has not been deleted.");
                                return false;
                            }
                            sender.sendMessage(ChatColor.RED + "Speedrun World has been deleted.");
                            if (args[1].toLowerCase().equals("delete"))
                                return true;

                        case "create":
                            sender.sendMessage(ChatColor.YELLOW + "Speedrun World has been created.");
                            WorldCreator wc = new WorldCreator("speedrun_world");

                            wc.environment(World.Environment.NORMAL);
                            wc.type(WorldType.NORMAL);

                            world = wc.createWorld();
                            return true;
                        case "leave":
                            if (sender instanceof Player) {
                                ((Player) sender).teleport(Bukkit.getWorld("world").getSpawnLocation());
                                ((Player) sender).setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation());
                            }
                    }

                    original = Bukkit.getServer().getWorld("world").getSpawnLocation();
                    return true;

                case "team":

                    ChatColor teamColor;

                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Usage: /speedrun team {create}");
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "create":
                            // TODO
                            // make teams customizable :DDDD
                            if (args.length >= 3) {
                                addTeam(args[2]);
                                sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Team " + args[2]
                                        + " has been created.");
                            }
                            // ChatColor teamColor = ChatColor.RESET;
                            if (args.length >= 4) {
                                teamColor = setTeamColor(args[3].toUpperCase().trim());

                            }
                            // for (Team team : teamList) {
                            // teamColor = setTeamColor(args[3].toUpperCase().trim());
                            // String prefix = teamColor + "[" + team.getName().toUpperCase().substring(0,
                            // 3) + "] ";
                            // team.setPrefix(prefix);
                            // team.setDisplayName(teamColor + team.getName());
                            // team.setAllowFriendlyFire(false);

                            // for (String entry : team.getEntries()) {
                            // Player p = Bukkit.getPlayerExact(entry);
                            // p.setDisplayName(teamColor + prefix + ChatColor.RESET + p.getName());
                            // }
                            // }
                            if (args.length == 3) {
                                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
                                        + "Usage: /speedrun team create {Team Name}");

                            }
                            return true;

                        case "assign":
                            List<Player> playerList = new ArrayList<Player>(Bukkit.getOnlinePlayers());
                            Collections.shuffle(playerList);

                            for (int i = 0; i < playerList.size(); i++) {
                                teamList.get(i % teamList.size()).addEntry(playerList.get(i).getName());

                            }

                            // ChatColor teamColor = ChatColor.RESET;
                            // if (args.length >= 4) {
                            // teamColor = setTeamColor(args[3].toUpperCase().trim());
                            // }
                            for (Team team : teamList) {
                                ChatColor teamColor2 = team.getColor();
                                String prefix = teamColor2 + "[" + team.getName().toUpperCase().substring(0, 3) + "] ";
                                team.setPrefix(prefix);
                                team.setDisplayName(teamColor2 + team.getName());
                                team.setAllowFriendlyFire(false);

                                for (String entry : team.getEntries()) {
                                    Player p = Bukkit.getPlayerExact(entry);
                                    p.setDisplayName(teamColor2 + prefix + ChatColor.RESET + p.getName());
                                }
                            }
                            sender.sendMessage(ChatColor.YELLOW + "Teams have been assigned!");
                            return true;
                    }
                    return true;

                case "teleport":
                    Location spawn = world.getSpawnLocation();
                    int radius = 50;
                    for (int i = 0; i < teamList.size(); i++) {
                        Location loc = world
                                .getHighestBlockAt(
                                        (int) (spawn.getX() + radius * Math.cos(Math.PI / teamList.size() * i)),
                                        (int) (spawn.getZ() + radius * Math.sin(Math.PI / teamList.size() * i)))
                                .getLocation();

                        for (String entry : teamList.get(i).getEntries()) { // get all player names from team
                            Player player = Bukkit.getPlayerExact(entry);
                            if (player != null) {
                                if (teamList == null) {
                                    sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
                                            + "Teams have not been created yet. \nDo /speedrun team create in order to create teams.");
                                }
                                player.teleport(loc);
                                player.setBedSpawnLocation(loc);
                            }
                        }
                    }

                    sender.sendMessage(ChatColor.YELLOW + "Teams have been teleported!");
                    return true;
                default:
                    sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + ""
                            + "Invalid Command, do /speedrun for more info.");

                    return true;
            }
        }
        return false;
    }

    private boolean delete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (!delete(subFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    private void addTeam(String teamName) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(teamName);
        teamList.add(team);
    }

    private ChatColor setTeamColor(String color) {
        return ChatColor.valueOf(color);
    }
}
