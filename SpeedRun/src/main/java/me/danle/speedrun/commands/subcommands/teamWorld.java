package me.danle.speedrun.commands.subcommands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import me.danle.speedrun.commands.SubCommand;

// Help from aiteru and coolerblast ^^
public class teamWorld extends SubCommand {
    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String getDescription() {
        return "manages speedrun world";
    }

    @Override
    public String getSyntax() {
        return "/speedrun world";
    }

    private Location original;
    private static World world;
    private String speedrun_world = ChatColor.ITALIC + "Speedrun World";
    private String redBold = ChatColor.RED + "" + ChatColor.BOLD;
    private String yellowBold = ChatColor.YELLOW + "" + ChatColor.BOLD;

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
            case "build":
                if (Bukkit.getServer().getWorld("speedrun_world") != null) {
                    player.sendMessage(redBold + speedrun_world + redBold + " has already been built!");
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                } else {
                    WorldCreator wc = new WorldCreator("speedrun_world");
                    wc.environment(World.Environment.NORMAL);
                    wc.type(WorldType.NORMAL);
                    world = wc.createWorld();
                    player.sendMessage(yellowBold + speedrun_world + yellowBold + " has been built!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                }
                break;

            case "leave":
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation());
                player.sendMessage(yellowBold + "You have left the speedrun world.");
                player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                break;

            case "delete":
                World reset = Bukkit.getServer().getWorld("speedrun_world");
                if (reset == null) {
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "There is no " + ChatColor.RED
                            + speedrun_world + ChatColor.RED + "" + ChatColor.BOLD + " to delete.");
                    player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                } else {
                    for (Player players : reset.getPlayers()) {
                        players.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
                    }
                    Bukkit.unloadWorld(reset, false);
                    if (!this.delete(reset.getWorldFolder())) {
                        player.sendMessage(redBold + speedrun_world + redBold + " has not been deleted!");
                        player.playSound(player.getLocation(), Sound.ENTITY_COW_MILK, 5, 3);
                    }
                    player.sendMessage(yellowBold + speedrun_world + yellowBold + " has been deleted!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
                }

                break;

            }
        } else if (args.length == 1) {
            correctUsage(player, "Usage: /speedrun world [build/delete/leave]");
        }

    }

    public boolean delete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (!delete(subFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static World getWorld() {
        return world;
    }

    public void correctUsage(Player player, String usage) {
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + usage);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 3, 3);
    }

}
