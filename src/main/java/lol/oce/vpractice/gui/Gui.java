package lol.oce.vpractice.gui;

import lol.oce.vpractice.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Gui {
    public void open(Player player) {
        player.sendMessage(StringUtils.handle("&7&oOpening GUI..."));
        player.openInventory(getInventory(player));
    }

    public Inventory getInventory(Player player) {
        return Bukkit.createInventory(player, 9, "GUI");
    }
}
