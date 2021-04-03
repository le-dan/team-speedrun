package me.danle.speedrun.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTab implements TabCompleter {

    // /speedrun world {delete/reset/leave/create}
    // /speedrun team create {name} {color}
    // /speedrun teleport

    // TODO
    // Change "team_name" so tab completer works when inputting any string still
    // completes tab for team color.
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> arguements = new ArrayList<String>();

        List<String> colors = Arrays.asList("AQUA", "BLACK", "BLUE", "BOLD", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY",
                "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "ITALIC", "LIGHT_PURPLE", "MAGIC",
                "RED", "WHITE", "YELLOW");

        if (args.length <= 1) {
            arguements.addAll(Arrays.asList("team", "teleport", "world"));
        } else {
            switch (args[0].toLowerCase()) {
            case "world":
                arguements.addAll(Arrays.asList("delete", "reset", "leave", "create"));
                break;
            case "team": // /speedrun team add|assign
                if (args[1].equalsIgnoreCase("create")) {
                    if (args.length == 4) {
                        arguements.addAll(colors);
                    }
                } else {
                    arguements.addAll(Arrays.asList("delete", "create", "scramble"));
                }
                break;
            }
        }

        List<String> result = new ArrayList<String>();
        if (args.length >= 1) {
            for (String a : arguements) {
                if (a.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }

}
