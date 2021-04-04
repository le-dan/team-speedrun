package me.danle.speedrun.commands.subcommands;

import java.io.File;

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

public class world implements CommandExecutor {

    private Location original;
    private World world;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // TODO
        // Create /speedrun spectate command
        if (cmd.getName().equalsIgnoreCase("speedrun")) {
            if (args.length > 0 && args[0].toLowerCase() == "world") {
                switch (args[1].toLowerCase()) {
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
                    return true;
                }

                original = Bukkit.getServer().getWorld("world").getSpawnLocation();
                return true;
            }
        }

        return false;
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

}
