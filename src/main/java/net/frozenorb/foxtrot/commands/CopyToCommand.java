package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.qLib;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CopyToCommand {

    @Command(names = {"cpto", "copyinvto", "copyto"}, permission = "foxtrot.copyto")
    public static void copyinvto(Player sender,@Param(name="player") Player receiver){
        if (receiver == null){
            sender.sendMessage(CC.translate("&cThat player is not online!"));
            return;
        }

        if (receiver == sender){
            sender.sendMessage(CC.translate("&cWhat is copying your inventory to yourself going to do?"));
            return;
        }

        receiver.getInventory().setContents(sender.getInventory().getContents());
        receiver.getInventory().setArmorContents(sender.getInventory().getArmorContents());

        sender.sendMessage(CC.translate("&aYou have copied your inventory to " + receiver.getDisplayName()));
    }
}
