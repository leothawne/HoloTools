package net.kokoricraft.holotools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandsCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return switch (args.length) {
            case 1 -> Arrays.asList("give", "reload").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
            case 2 -> "give".equalsIgnoreCase(args[0]) ?
                    Arrays.asList("holocrafter", "holowardrobe").stream()
                            .filter(s -> s.startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList()) : null;
            case 3 -> "give".equalsIgnoreCase(args[0]) && isNumeric(args[1]) ?
                    Arrays.asList("1", "2", "3", "4", "5").stream()
                            .filter(s -> s.startsWith(args[2].toLowerCase()))
                            .collect(Collectors.toList()) : null;
            default -> new ArrayList<>();
        };
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}
