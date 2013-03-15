package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.CustomTimingsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.CustomTimingsHandler;

import java.util.Arrays;

public class ReloadCommand extends BukkitCommand {
    public ReloadCommand(String name) {
        super(name);
        this.description = "Презагружает конфигурацию сервера и плагины";
        this.usageMessage = "/reload";
        this.setPermission("bukkit.command.reload");
        this.setAliases(Arrays.asList("rl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        CustomTimingsHandler.reload(); // Spigot
        Bukkit.reload();
        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Перезагрузка завершена.");

        return true;
    }
}
