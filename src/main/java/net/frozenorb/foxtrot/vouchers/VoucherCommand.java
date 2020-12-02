package net.frozenorb.foxtrot.vouchers;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.vouchers.menu.VoucherDisplayMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VoucherCommand {

    @Command(names = {"voucher add", "vouchers add"}, permission = "voucher.add", async = true)
    public static void addVoucher(CommandSender sender, @Param(name = "player") UUID player, @Param(name = "value") double value, @Param(name = "desc", wildcard = true) String desc){
        Foxtrot.getInstance().getVoucherHandler().getVouchers().add(new Voucher(player, desc, value));
        sender.sendMessage(ChatColor.GREEN + "Successfully added the voucher " + desc + " to the player " + UUIDUtils.name(player) + ".");
    }

    @Command(names = {"voucher check", "vouchers check"}, permission = "voucher.check", async = true)
    public static void checkVoucher(Player sender, @Param(name = "player") UUID player){
        new VoucherDisplayMenu(player).openMenu(sender);
    }
}
