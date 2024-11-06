package lol.oce.hercules.commands.troll;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DropCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("hercules.troll")) {
            return false;
        }
        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            commandSender.sendMessage("Player not found.");
            return false;
        }
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                continue;
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
        return false;
    }
}
