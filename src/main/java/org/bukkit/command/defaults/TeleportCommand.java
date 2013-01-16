package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.common.collect.ImmutableList;

public class TeleportCommand extends VanillaCommand {

    public TeleportCommand() {
        super("tp");
        this.description = "Телепортирует игрока к другому игроку или на заданую точку.";
        this.usageMessage = "/tp [игрок] <цель>\n/tp [игрок] <x> <y> <z>";
        this.setPermission("bukkit.command.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1 || args.length > 4) {
            sender.sendMessage(ChatColor.RED + "Использование: " + usageMessage);
            return false;
        }

        Player player;

        if (args.length == 1 || args.length == 3) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("Пожалуйста, укажите игрока!");
                return true;
            }
        } else {
            player = Bukkit.getPlayerExact(args[0]);
        }

        if (player == null) {
            sender.sendMessage("Игрок " + args[0] + " не найден.");
            return true;
        }

        if (args.length < 3) {
            Player target = Bukkit.getPlayerExact(args[args.length - 1]);
            if (target == null) {
                sender.sendMessage("Игрок " + args[args.length - 1] + " не найден. Телепортация не возможна.");
                return true;
            }
            player.teleport(target, TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, "Игрок " + player.getDisplayName() + " телепортирован к " + target.getDisplayName());
        } else if (player.getWorld() != null) {
            Location playerLocation = player.getLocation();
            double x = getCoordinate(sender, playerLocation.getX(), args[args.length - 3]);
            double y = getCoordinate(sender, playerLocation.getY(), args[args.length - 2], 0, 0);
            double z = getCoordinate(sender, playerLocation.getZ(), args[args.length - 1]);

            if (x == MIN_COORD_MINUS_ONE || y == MIN_COORD_MINUS_ONE || z == MIN_COORD_MINUS_ONE) {
                sender.sendMessage("Пожалуйста, введите корректные координаты!");
                return true;
            }

            playerLocation.setX(x);
            playerLocation.setY(y);
            playerLocation.setZ(z);

            player.teleport(playerLocation);
            Command.broadcastCommandMessage(sender, String.format("Игрок %s телепортирован на %.2f, %.2f, %.2f", player.getDisplayName(), x, y, z));
        }
        return true;
    }

    private double getCoordinate(CommandSender sender, double current, String input) {
        return getCoordinate(sender, current, input, MIN_COORD, MAX_COORD);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max) {
        boolean relative = input.startsWith("~");
        double result = relative ? current : 0;

        if (!relative || input.length() > 1) {
            boolean exact = input.contains(".");
            if (relative) input = input.substring(1);

            double testResult = getDouble(sender, input);
            if (testResult == MIN_COORD_MINUS_ONE) {
                return MIN_COORD_MINUS_ONE;
            }
            result += testResult;

            if (!exact && !relative) result += 0.5f;
        }
        if (min != 0 || max != 0) {
            if (result < min) {
                result = MIN_COORD_MINUS_ONE;
            }

            if (result > max) {
                result = MIN_COORD_MINUS_ONE;
            }
        }

        return result;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1 || args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
