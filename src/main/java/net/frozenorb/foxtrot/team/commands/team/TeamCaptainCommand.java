package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.UUIDUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCaptainCommand {

    @Command(names={ "team captain add", "t captain add", "t mod add", "team mod add", "f mod add", "fac mod add", "faction mod add", "f captain add", "fac captain add", "faction captain add"}, permission="")
    public static void captainAdd(Player sender, @Param(name = "player") UUID promote) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(org.bukkit.ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if( team == null ) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if(!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only team co-leaders can execute this command.");
            return;
        }

        if(!team.isMember(promote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if(team.isOwner(promote) || team.isCaptain(promote) || team.isCoLeader(promote)) {
            sender.sendMessage(ChatColor.RED + "This player is already a captain (or above) of your team.");
            return;
        }

        team.removeCoLeader(promote);
        team.addCaptain(promote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + UUIDUtils.name(promote) + " has been promoted to Captain!");
    }

    @Command(names={ "team captain remove", "t captain remove", "t mod remove", "team mod remove", "f mod remove", "fac mod remove", "faction mod remove", "f captain remove", "fac captain remove", "faction captain remove" }, permission="")
    public static void captainRemove(Player sender, @Param(name = "player") UUID demote) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender.getUniqueId());
        if( team == null ) {
            sender.sendMessage(ChatColor.RED + "You must be in a team to execute this command.");
            return;
        }

        if(!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only team co-leaders can execute this command.");
            return;
        }

        if(!team.isMember(demote)) {
            sender.sendMessage(ChatColor.RED + "This player must be a member of your team.");
            return;
        }

        if(!team.isCaptain(demote)) {
            sender.sendMessage(ChatColor.RED + "This player is not a captain of your team.");
            return;
        }

        team.removeCoLeader(demote);
        team.removeCaptain(demote);
        team.sendMessage(org.bukkit.ChatColor.DARK_AQUA + UUIDUtils.name(demote) + " has been demoted to a member!");
    }

}
