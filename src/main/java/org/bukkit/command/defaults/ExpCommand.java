package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import org.bukkit.util.StringUtil;

public class ExpCommand extends VanillaCommand {
    public ExpCommand() {
        super("xp");
        this.description = "Выдает указанному игроку указаное количество опыта. Указывайте <количество>L для выдачи уровней. Если значение отричательное, то опыт снимается.";
        this.usageMessage = "/xp <количество> [игрок] ИЛИ /xp <количество>L [игрок]";
        this.setPermission("bukkit.command.xp");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length > 0) {
            String inputAmount = args[0];
            Player player = null;

            boolean isLevel = inputAmount.endsWith("l") || inputAmount.endsWith("L");
            if (isLevel && inputAmount.length() > 1) {
                inputAmount = inputAmount.substring(0, inputAmount.length() - 1);
            }

            int amount = getInteger(sender, inputAmount, Integer.MIN_VALUE, Integer.MAX_VALUE);
            boolean isTaking = amount < 0;

            if (isTaking) {
                amount *= -1;
            }

            if (args.length > 1) {
                player = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }

            if (player != null) {
                if (isLevel) {
                    if (isTaking) {
                        player.giveExpLevels(-amount);
                        Command.broadcastCommandMessage(sender, StringUtil.plural(amount, "Забран", "Забрано", "Забрано") + " " + amount + " " + StringUtil.plural(amount, "уровень", "уровня", "уровней") + " у игрока " + player.getName());
                    } else {
                        player.giveExpLevels(amount);
                        Command.broadcastCommandMessage(sender, amount + " " + StringUtil.plural(amount, "уровень выдан", "уровня выдано", "уровней выдано") + " игроку " + player.getName());
                    }
                } else {
                    if (isTaking) {
                        sender.sendMessage(ChatColor.RED + "Снятие опыта доступно только с помощью уровней.");
                        return false;
                    } else {
                        player.giveExp(amount);
                        Command.broadcastCommandMessage(sender, amount + " " + StringUtil.plural(amount, "опыт выдан", "опаты выдано", "опыта выдано") + " игроку " + player.getName());
                    }
                }
            } else {
                sender.sendMessage("Игрок не найден. Вы указали его?\n" + ChatColor.RED + "Использование: " + usageMessage);
                return false;
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Использование: " + usageMessage);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
