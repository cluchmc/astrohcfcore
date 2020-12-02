package net.frozenorb.foxtrot.listener;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import com.cheatbreaker.api.object.MinimapStatus;
import com.cheatbreaker.nethandler.obj.ServerRule;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.nametag.FoxtrotNametagProvider;
import net.frozenorb.foxtrot.server.conditional.staff.ModModeEnterEvent;
import net.frozenorb.foxtrot.server.conditional.staff.ModModeExitEvent;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import net.frozenorb.foxtrot.events.events.EventDeactivatedEvent;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.koth.events.KOTHControlLostEvent;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientListener implements Listener {


    public ClientListener() {
        Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Bukkit.getOnlinePlayers().forEach(player -> CheatBreakerAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player));
            }
        }, 0, 40);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        CheatBreakerAPI.getInstance().changeServerRule(player, ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().changeServerRule(player, com.moonsworth.client.nethandler.obj.ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().setMinimapStatus(player, MinimapStatus.FORCED_OFF);
        CheatBreakerAPI.getInstance().sendWaypoint(player, new CBWaypoint("Spawn", player.getWorld().getSpawnLocation(), -1, true, true));
        if(team != null) team.sendWaypoint(player.getUniqueId());
        if (team.getFactionFocused() != null){
            CBWaypoint cbWaypoint = new CBWaypoint(team.getFactionFocused().getName() + "'s HQ", team.getFactionFocused().getHQ().getBlockX(), team.getFactionFocused().getHQ().getBlockY(), team.getFactionFocused().getHQ().getBlockZ(), team.getFactionFocused().getHQ().getWorld().getUID().toString(), Color.RED.getRed(), true, true);
            CheatBreakerAPI.getInstance().sendWaypoint(player, cbWaypoint);
        }
    }

    @EventHandler
    public void onModMode(ModModeEnterEvent event) {
        Player player = event.getPlayer();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) CheatBreakerAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player);
    }

    @EventHandler
    public void onModExit(ModModeExitEvent event) {
        Player player = event.getPlayer();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) CheatBreakerAPI.getInstance().resetNametag(onlinePlayer, player);
    }


    public List<String> fetchNametag(Player target, Player viewer) {
        String nameTag = (target.hasMetadata("invisible") ? ChatColor.GRAY + "*" : "") + new FoxtrotNametagProvider().fetchNametag(target, viewer).getPrefix() + target.getName();
        List<String> tag = new ArrayList<>();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(target);
        //FancyMessage dtrMessage = new FancyMessage(ChatColor.YELLOW + "DTR: " + getDTRColor(team) + Team.DTR_FORMAT.format(Team.getDTR()) + getDTRSuffix());

        if (team != null) {
            if (viewer.hasMetadata("modmode")) {
                tag.add(ChatColor.DARK_GRAY + "[" + team.getTeamColor().toString() + team.getName() + ChatColor.GRAY + CC.GRAY_LINE + getDTRColor(team) + Team.DTR_FORMAT.format(team.getDTR()) + getDTRSuffix(team) + ChatColor.DARK_GRAY + "]");
            } else {
                tag.add(ChatColor.DARK_GRAY + "[" + team.getTeamColor().toString() + team.getName(viewer) + ChatColor.GRAY + CC.GRAY_LINE + getDTRColor(Foxtrot.getInstance().getTeamHandler().getTeam(viewer)) + Team.DTR_FORMAT.format(Foxtrot.getInstance().getTeamHandler().getTeam(viewer).getDTR()) + getDTRSuffix(Foxtrot.getInstance().getTeamHandler().getTeam(viewer)) + ChatColor.DARK_GRAY + "]");
            }
        }
            if (target.hasMetadata("modmode")) tag.add(ChatColor.GRAY + "[Mod Mode]");
            tag.add(nameTag);
            return tag;
    }

    public static ChatColor getDTRColor(Team team) {
        ChatColor dtrColor = ChatColor.GREEN;

        if (team.getDTR() / team.getMaxDTR() <= 0.25) {
            if (team.isRaidable()) {
                dtrColor = ChatColor.DARK_RED;
            } else {
                dtrColor = ChatColor.YELLOW;
            }
        }

        return (dtrColor);
    }

    public static String getDTRSuffix(Team team) {
        if (DTRHandler.isRegenerating(team)) {
            if (team.getOnlineMemberAmount() == 0) {
                return (ChatColor.GRAY + "◀");
            } else {
                return (ChatColor.GREEN + "▲");
            }
        } else if (DTRHandler.isOnCooldown(team)) {
            return (ChatColor.RED + "■");
        } else {
            return (ChatColor.GREEN + "◀");
        }
    }
}
