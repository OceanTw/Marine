package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import lol.oce.hercules.match.Participant;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.ConsoleUtils;
import lol.oce.hercules.utils.VisualUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Practice.getLobbyManager().giveItems(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // if the server is not loaded, don't load the user
        if (!Practice.getInstance().isEnabled()) {
            return;
        }
        Practice.getUserManager().load(event.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        // if the server is not loaded, don't load the user
        Practice.getUserManager().unload(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        User damager;
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (!(entityEvent.getDamager() instanceof Player)) {
                return;
            }
            damager = Practice.getUserManager().getUser((entityEvent.getDamager()).getUniqueId());
            if (damager.getStatus() != UserStatus.IN_MATCH) {
                event.setCancelled(true);
                return;
            }
            Participant killer = damager.getMatch().getParticipant(damager);
            Participant killed = user.getMatch().getParticipant(user);
            if (damager.getMatch().getParticipants().contains(killed)) {
                if (player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    damager.getMatch().onDeath(killer, killed);
                    killed.getPlayer().getInventory().clear();
                    player.setHealth(20);
                    VisualUtils.playDeathAnimation(player, (Player) entityEvent.getDamager());
                }

            }
            return;
        }
        if (event instanceof EntityDamageByBlockEvent) {
            if (user.getMatch() == null) {
                event.setCancelled(true);
                return;
            }
            Participant killed = user.getMatch().getParticipant(user);
            if (user.getMatch().getParticipants().contains(killed)) {
                if (player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    user.getMatch().onDeath(null, killed);
                    player.setHealth(20);
                }
            }
            return;
        }
        if (event.getFinalDamage() >= player.getHealth()) {
            User damaged = Practice.getUserManager().getUser(((Player) event).getUniqueId());
            if (damaged.getStatus() != UserStatus.IN_MATCH) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.setHealth(20);
            damaged.getMatch().onDeath(null, damaged.getMatch().getParticipant(damaged));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killerPlayer = event.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }
        User dead = Practice.getUserManager().getUser(deadPlayer.getUniqueId());
        User killer = Practice.getUserManager().getUser(killerPlayer.getUniqueId());
        if (dead.getStatus() != UserStatus.IN_MATCH) {
            return;
        }
        Participant killerParticipant = killer.getMatch().getParticipant(killer);
        Participant deadParticipant = dead.getMatch().getParticipant(dead);
        if (dead.getMatch().getParticipants().contains(killerParticipant)) {
            dead.getMatch().onDeath(killerParticipant, deadParticipant);
        }

        deadPlayer.spigot().respawn();
        ConsoleUtils.debug(deadPlayer.getName() + " death");

        event.getDrops().clear();
    }
}