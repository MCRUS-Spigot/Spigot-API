package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class ToggleDownfallCommand extends VanillaCommand {
    public ToggleDownfallCommand() {
        super("toggledownfall");
        this.description = "Включает или выключает дождь.";
        this.usageMessage = "/toggledownfall";
        this.setPermission("bukkit.command.toggledownfall");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        World world = null;

        if (args.length == 1) {
            world = Bukkit.getWorld(args[0]);

            if (world == null) {
                sender.sendMessage(ChatColor.RED + "Мир '" + args[0] + "' не найден.");
                return true;
            }
        } else if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else {
            world = Bukkit.getWorlds().get(0);
        }

        if (world.hasStorm())
            Command.broadcastCommandMessage(sender, "Теперь в мире '" + world.getName() + "' шторм.");
        else
            Command.broadcastCommandMessage(sender, "Теперь в мире '" + world.getName() + "' солнечно.");
        world.setStorm(!world.hasStorm());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        return ImmutableList.of();
    }
}
