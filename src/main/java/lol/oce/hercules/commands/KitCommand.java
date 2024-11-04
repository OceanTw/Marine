package lol.oce.hercules.commands;

import lol.oce.hercules.Practice;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.kits.KitManager;
import lol.oce.hercules.utils.ConsoleUtils;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("vpractice.admin")) {
            commandSender.sendMessage(StringUtils.handle("&cYou do not have permission to execute this command."));
            return true;
        }
        Player player = (Player) commandSender;
        int length = strings.length;

        if (length == 0) {
            commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("create")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Create a new kit
            Practice.getKitManager().createKit(strings[1]);
            commandSender.sendMessage(StringUtils.handle("&aKit created successfully"));
            Practice.getKitManager().updateSettings();
            return true;
        }

        if (strings[0].equalsIgnoreCase("delete")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Delete a kit
            KitManager kitManager = Practice.getKitManager();
            Kit kit = kitManager.getKit(strings[1]);
            if (kit == null) {
                commandSender.sendMessage(StringUtils.handle("&cKit not found"));
                return true;
            }
            kitManager.removeKit(kit);
            return true;
        }

        if (strings[0].equalsIgnoreCase("setinv")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Set the kit inventory
            Practice.getKitManager().getKit(strings[1]).setInventory(player.getInventory());
            player.sendMessage(StringUtils.handle("&aKit inventory set successfully"));
            Practice.getKitManager().updateSettings();
            return true;
        }

        if (strings[0].equalsIgnoreCase("set")) {
            if (length < 3) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Set the kit properties
            switch (strings[2]) {
                case "name":
                    Practice.getKitManager().getKit(strings[1]).setName(strings[3]);
                    player.sendMessage(StringUtils.handle("&aKit name set successfully"));
                    break;
                case "displayname":
                    Practice.getKitManager().getKit(strings[1]).setDisplayName(strings[3]);
                    player.sendMessage(StringUtils.handle("&aKit display name set successfully"));
                    break;
                case "description":
                    Practice.getKitManager().getKit(strings[1]).setDescription(strings[3]);
                    player.sendMessage(StringUtils.handle("&aKit description set successfully"));
                    break;
                case "enabled":
                    Practice.getKitManager().getKit(strings[1]).setEnabled(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit enabled set successfully"));
                    break;
                case "editable":
                    Practice.getKitManager().getKit(strings[1]).setEditable(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit editable set successfully"));
                    break;
                case "boxing":
                    Practice.getKitManager().getKit(strings[1]).setBoxing(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit boxing set successfully"));
                    break;
                case "build":
                    Practice.getKitManager().getKit(strings[1]).setBuild(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit build set successfully"));
                    break;
                case "sumo":
                    Practice.getKitManager().getKit(strings[1]).setSumo(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit sumo set successfully"));
                    break;
                case "mapdestroyable":
                    Practice.getKitManager().getKit(strings[1]).setMapDestroyable(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit map destroyable set successfully"));
                    break;
                case "hunger":
                    Practice.getKitManager().getKit(strings[1]).setHunger(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit hunger set successfully"));
                    break;
                case "healthregen":
                    Practice.getKitManager().getKit(strings[1]).setHealthRegen(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit health regen set successfully"));
                    break;
                case "bedfight":
                    Practice.getKitManager().getKit(strings[1]).setBedfight(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit bedfight set successfully"));
                    break;
                case "fireball":
                    Practice.getKitManager().getKit(strings[1]).setFireball(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit fireball set successfully"));
                    break;
                case "enderpearlcd":
                    Practice.getKitManager().getKit(strings[1]).setEnderpearlcd(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit enderpearlcd set successfully"));
                    break;
                case "ranked":
                    Practice.getKitManager().getKit(strings[1]).setRanked(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit ranked set successfully"));
                    break;
                default:
                    commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                    return true;
            }
            Practice.getKitManager().updateSettings();
            ConsoleUtils.info("Kit settings updated");
            return true;
        }
        return false;
    }
}
