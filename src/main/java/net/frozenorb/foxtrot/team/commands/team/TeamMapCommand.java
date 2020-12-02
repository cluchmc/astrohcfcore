package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.claims.VisualClaim;
import net.frozenorb.foxtrot.team.claims.VisualClaimType;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamMapCommand {

    @Command(names={ "team map", "t map", "f map", "faction map", "fac map", "map" }, permission="")
    public static void teamMap(Player sender) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        (new VisualClaim(sender, VisualClaimType.MAP, false)).draw(false);
    }

//    @Command(names={ "team map surface", "t map surface", "f map surface", "faction map surface", "fac map surface", "map surface" }, permission="")
//    public static void teamMapSurface(Player sender) {
//        (new VisualClaim(sender, VisualClaimType.SURFACE_MAP, false)).draw(false);
//    }

}