package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;
import org.bukkit.util.rus.RussianModel;

public class GameModeCommand extends VanillaCommand {
    private static final List<String> GAMEMODE_NAMES = ImmutableList.of("adventure", "creative", "survival");

    public GameModeCommand() {
        super("gamemode");
        this.description = "Изменяет игровой режим указанного игрока";
        this.usageMessage = "/gamemode <режим> [игрок]";
        this.setPermission("bukkit.command.gamemode");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Использование: " + usageMessage);
            return false;
        }

        String modeArg = args[0];
        String playerArg = sender.getName();

        if (args.length == 2) {
            playerArg = args[1];
        }

        Player player = Bukkit.getPlayerExact(playerArg);

        if (player != null) {
            int value = -1;

            try {
                value = Integer.parseInt(modeArg);
            } catch (NumberFormatException ex) {}

            GameMode mode = GameMode.getByValue(value);

            if (mode == null) {
                if (modeArg.equalsIgnoreCase("creative") || modeArg.equalsIgnoreCase("c")) {
                    mode = GameMode.CREATIVE;
                } else if (modeArg.equalsIgnoreCase("adventure") || modeArg.equalsIgnoreCase("a")) {
                    mode = GameMode.ADVENTURE;
                } else {
                    mode = GameMode.SURVIVAL;
                }
            }

            if (mode != player.getGameMode()) {
                player.setGameMode(mode);

                if (mode != player.getGameMode()) {
                    sender.sendMessage("Ошибка смены игрового режима игрока " + player.getName() + "!");
                } else {
                    if (player == sender) {
                        Command.broadcastCommandMessage(sender, "Ваш игровой режим изменен на " + RussianModel.getGamemode(mode).toLowerCase());
                    } else {
                        Command.broadcastCommandMessage(sender, "Игровой режим игрока " + player.getName() + " изменен на " + RussianModel.getGamemode(mode).toLowerCase());
                    }
                }
            } else {
                sender.sendMessage("Игровой режим игрока "+player.getName() + " уже " + RussianModel.getGamemode(mode).toLowerCase());
            }
        } else {
            sender.sendMessage("Игрок " + playerArg + " не найден.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], GAMEMODE_NAMES, new ArrayList<String>(GAMEMODE_NAMES.size()));
        } else if (args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
