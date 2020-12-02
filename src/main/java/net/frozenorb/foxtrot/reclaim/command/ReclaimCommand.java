package net.frozenorb.foxtrot.reclaim.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.reclaim.Group;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kronos.helium.profile.Profile;
import org.kronos.helium.rank.Rank;

import java.util.ArrayList;
import java.util.List;

public class ReclaimCommand {

    @Command(names = {"reclaim", "claim"}, permission = "")
    public static void reclaim(CommandSender sender){
        Player p = (Player) sender;
        Profile user = Profile.getByUuid(p.getUniqueId());
        assert user != null;
        Rank rank = user.getActiveRank();
        String group = rank.getDisplayName();

        boolean needsReclaim = false;

        for (Group definedGroup : Foxtrot.getInstance().getReclaimHandler().getGroups()){
            if (definedGroup.getName().equalsIgnoreCase(group)){
                needsReclaim = true;
            }
        }

        if (needsReclaim){

            if (Foxtrot.getInstance().getReclaimHandler().hasReclaimed(p)){
                p.sendMessage(CC.translate(CC.prefix + "&c You have already reclaimed this map!"));
                return;
            }

            List<String> commands = new ArrayList<>();

            for (Group definedGroup : Foxtrot.getInstance().getReclaimHandler().getGroups()){
                if (definedGroup.getName().equalsIgnoreCase(group)){
                    for (String command1 : definedGroup.getCommands()){
                        commands.add(command1);
                    }
                }
            }

            for (String c : commands){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", p.getName()));
            }

            Foxtrot.getInstance().getReclaimHandler().setUsedReclaim(p, true);
        } else {
            p.sendMessage(CC.translate(CC.prefix + "&c You are not eligible to reclaim!"));
        }
    }

    @Command(names = {"reclaimreset", "claimreset"}, permission = "astro.reclaim.reset")
    public static void reclaimReset(CommandSender sender, @Param(name = "player") Player target){
        Player player = (Player) sender;
        sender.sendMessage(CC.translate(CC.prefix + " " + player.getDisplayName() + "&f reclaim has been reset and now can use their reclaim."));
        Foxtrot.getInstance().getReclaimHandler().setUsedReclaim(target, false);
    }
}
