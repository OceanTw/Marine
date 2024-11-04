package lol.oce.vpractice.duels;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.match.MatchType;
import lol.oce.vpractice.players.User;
import lol.oce.vpractice.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class RequestManager implements Listener {

    List<Request> requests;

    public void sendRequest(User sender, User receiver, Kit kit) {
        Request request = new Request(sender, receiver, kit);
        TextComponent accept = new TextComponent(StringUtils.handle("&9&lClick to Accept"));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + sender.getPlayer().getName()));
        receiver.getPlayer().sendMessage(StringUtils.handle("&9&lDuel Request"));
        receiver.getPlayer().sendMessage(StringUtils.handle("&f- Sender: " + sender.getPlayer().getName()));
        receiver.getPlayer().sendMessage(StringUtils.handle("&f- Kit: " + kit.getName()));
        receiver.getPlayer().spigot().sendMessage(accept);

        requests.add(request);

        BukkitTask task = Practice.getInstance().getServer().getScheduler().runTaskLater(Practice.getInstance(), () -> {
            requests.remove(request);
        }, 20 * 60);
    }

    public Request getRequest(User sender, User receiver) {
        return requests.stream().filter(request -> request.getSender().equals(sender) && request.getReceiver().equals(receiver)).findFirst().orElse(null);
    }

    public void acceptRequest(User sender, User receiver) {
        Request request = getRequest(sender, receiver);
        if (request != null) {
            request.getSender().getPlayer().sendMessage(StringUtils.handle("&7&oYour request has been accepted"));
            request.getReceiver().getPlayer().sendMessage(StringUtils.handle("&7&oYou have accepted the request"));
            Practice.getMatchManager().startMatch(MatchType.DUEL, request.getKit(), new User[]{request.getSender(), request.getReceiver()}, new User[]{request.getReceiver(), request.getSender()}, false);
            requests.remove(request);
        }
    }
}
