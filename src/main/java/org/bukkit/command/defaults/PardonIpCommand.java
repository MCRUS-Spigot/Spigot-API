package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class PardonIpCommand extends VanillaCommand {
    public PardonIpCommand() {
        super("pardon-ip");
        this.description = "Разрешает игрокам заходить в игру с указанного IP адреса.";
        this.usageMessage = "/pardon-ip <адрес>";
        this.setPermission("bukkit.command.unban.ip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Использование: " + usageMessage);
            return false;
        }

        if (BanIpCommand.ipValidity.matcher(args[0]).matches()) {
            Bukkit.unbanIP(args[0]);
            Command.broadcastCommandMessage(sender, "Теперь игроки с IP адресом " + args[0] + " могут заходить на сервер.");
        } else {
            sender.sendMessage("IP адрес введен не верно.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Bukkit.getIPBans(), new ArrayList<String>());
        }
        return ImmutableList.of();
    }
}
