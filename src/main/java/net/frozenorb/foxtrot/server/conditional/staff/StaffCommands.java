package net.frozenorb.foxtrot.server.conditional.staff;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class StaffCommands {

    @Command(names = {"amivis", "vis?", "v?"}, permission = "foxtrot.staff")
    public static void amivisCommand(Player sender) {
        boolean modMode = ModHandler.isModMode(sender);
        boolean vanished = ModHandler.isVanished(sender);

        sender.sendMessage(ChatColor.GOLD + "You are " + (modMode ? (ChatColor.GREEN + "in") : (ChatColor.RED + "not in")) + ChatColor.GOLD + " Mod Mode, and are " + (vanished ? (ChatColor.GREEN + "INVISIBLE") : (ChatColor.RED + "VISIBLE")) + ChatColor.GOLD + ".");
    }

    @Command(names = {"mm", "modmode", "staff", "v", "h"}, permission = "foxtrot.staff")
    public static void modModeCommand(Player sender, @Param(name = "player", defaultValue = "self") Player target) {
        if (sender != target && !sender.hasPermission("foxtrot.modmode.others")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to modmode others.");
            return;
        }

        boolean newState = ModHandler.toggleModMode(target);

        if (sender != target)
            sender.sendMessage(ChatColor.YELLOW + "Mod Mode has been " + (newState ? (ChatColor.GREEN + "enabled") : (ChatColor.RED + "disabled")) + ChatColor.YELLOW + " for " + target.getDisplayName());
    }

    @Command(names = {"hidestaff", "showstaff"}, permission = "foxtrot.staff")
    public static void hidestaff(Player sender){
        if (sender.hasMetadata("hidestaff")){
            sender.sendMessage(ChatColor.GREEN + "Successfully shown staff!");
            sender.removeMetadata("hidestaff", Foxtrot.getInstance());
            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()){
                // cant stack
                if (otherPlayer != sender){
                    if (otherPlayer.hasMetadata("modmode")){
                        sender.showPlayer(otherPlayer);
                    }
                }
            }
        } else {
            sender.setMetadata("hidestaff", new FixedMetadataValue(Foxtrot.getInstance(), true));
            sender.sendMessage(ChatColor.GREEN + "Successfully hidden staff!");
            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()){
                // cant stack them
                if (otherPlayer != sender){
                    if (otherPlayer.hasMetadata("modmode")){
                        sender.hidePlayer(otherPlayer);
                    }
                }
            }
        }
    }

}