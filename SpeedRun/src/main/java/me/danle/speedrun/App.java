package me.danle.speedrun;

import org.bukkit.plugin.java.JavaPlugin;

import me.danle.speedrun.commands.SortTeams;
import me.danle.speedrun.utils.CommandTab;
import net.md_5.bungee.api.ChatColor;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("speedrun").setExecutor(new SortTeams());
        this.getCommand("speedrun").setTabCompleter(new CommandTab());
        getLogger().info(ChatColor.RED + "Speedrun Plugin Activated.");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Speedrun Plugin Deactivated.");
    }
}
