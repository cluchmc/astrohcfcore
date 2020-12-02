package net.frozenorb.foxtrot.partnerpackages;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ItemBuilder;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PackageCommand {

    @Command(names = {"package give"}, permission = "astro.partnerpackage.give")
    public static void packagegive(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "amount") int amount){
        target.getInventory().addItem(new ItemStack[]{getPartnerPackage(amount)});
        sender.sendMessage(CC.translate(CC.prefix + "&f You have given " + target.getDisplayName() + " " + amount + " &4&lPartner Packages&f!"));
        target.sendMessage(CC.translate(CC.prefix + "&f You have been given " + amount + " &4&lPartner Packages&f!"));
    }


    @Command(names = {"package all"}, permission = "astro.partnerpackage.all")
    public static void packageall(CommandSender sender, @Param(name = "amount") int amount){
        sender.sendMessage(CC.translate("&aYou have given all players &aa &d&lPartner Package&a."));
        for (Player online : Bukkit.getServer().getOnlinePlayers()){
            online.getInventory().addItem(new ItemStack[] { getPartnerPackage(amount) });
        }
    }

    public static ItemStack getPartnerPackage(int amount) {
        return new ItemBuilder(Material.ENDER_CHEST).name("&4&lPartner Package").lore(Arrays.asList(CC.translate("&7Purchase at shop.astro.rip &c\u2764"), CC.translate("&7Make sure you have an open inventory slot!"))).amount(amount).build();
    }
}
