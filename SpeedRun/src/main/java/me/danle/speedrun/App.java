package me.danle.speedrun;

import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import me.danle.speedrun.commands.CommandManager;
import me.danle.speedrun.commands.Speedrun;
import me.danle.speedrun.commands.subcommands.teleport;
import me.danle.speedrun.commands.subcommands.world;
import me.danle.speedrun.utils.CommandTab;
import net.md_5.bungee.api.ChatColor;

public class App extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("speedrun").setExecutor(new CommandManager());
        getCommand("speedrun").setTabCompleter(new CommandTab());
        getLogger().info(ChatColor.RED + "Speedrun Plugin Activated.");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Speedrun Plugin Deactivated.");
    }
}
