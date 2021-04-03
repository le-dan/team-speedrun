package me.danle.speedrun.commands.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.danle.speedrun.commands.SubCommand;
import org.bukkit.ChatColor;

public class teamCommand extends SubCommand { // speedrun team

    private ArrayList<Team> teamList = new ArrayList<Team>();

    Random random = new Random();

    String[] colors = { "AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE",
            "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "MAGIC", "RED", "WHITE", "YELLOW" };

    @Override
    public String getName() {
        return "team";
    }

    @Override
    public String getDescription() {
        return "creates a new team";
    }

    @Override
    public String getSyntax() {
        return "/speedrun team";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
            case "create":
                if (args.length > 2) {
                    if (args.length == 3 && args[2].length() <= 16) {
                        String teamNewColor = colors[random.nextInt(colors.length)];
                        addTeam(args[2], teamNewColor);
                        player.sendMessage(ChatColor.YELLOW + "Team " + ChatColor.valueOf(teamNewColor) + args[2]
                                + ChatColor.YELLOW + " has been created!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);

                    } else if (args.length == 4 && args[2].length() <= 16) {
                        addTeam(args[2], args[3]);
                        player.sendMessage(ChatColor.YELLOW + "Team " + ChatColor.valueOf(args[3]) + args[2]
                                + ChatColor.YELLOW + " has been created!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);

                    } else {
                        player.sendMessage(ChatColor.RED + "Team name " + ChatColor.BOLD + args[2] + ChatColor.RED
                                + " is too long! Max characters: 16");
                        player.sendMessage("Current character count: " + args[2].length());
                        player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                    }
                } else {
                    correctUsage(player, "Usage: /speedrun team create {name} {color}");
                }
                break;
            case "scramble":
                List<Player> playerList = new ArrayList<Player>(Bukkit.getOnlinePlayers());
                Collections.shuffle(playerList);

                for (int i = 0; i < playerList.size(); i++) {
                    teamList.get(i % teamList.size()).addEntry(playerList.get(i).getName());
                }

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
                player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Players have been scrambled to their respective teams!");
                player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                break;
            case "delete":
                Scoreboard currentTeams = Bukkit.getScoreboardManager().getMainScoreboard();
                if (currentTeams.getTeams().size() > 0) {
                    for (Team team : currentTeams.getTeams()) {
                        currentTeams.getTeam(team.getName()).unregister();
                    }
                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Teams have been deleted!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                } else {
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "There are no teams to delete!");
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                }

                break;
            }

        } else if (args.length == 1) {
            correctUsage(player, "Usage: /speedrun team [delete/create/scramble]");
        }
    }

    public void addTeam(String teamName, String teamColor) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(teamName);
        teamList.add(team);
        team.setColor(ChatColor.valueOf(teamColor));
    }

    public String usage(String usage) {
        return ChatColor.YELLOW + "" + ChatColor.ITALIC + usage;
    }

    public void correctUsage(Player player, String usage) {
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + usage);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 3, 3);
    }

}
