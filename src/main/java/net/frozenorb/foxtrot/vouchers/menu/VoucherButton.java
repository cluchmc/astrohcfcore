package net.frozenorb.foxtrot.vouchers.menu;

import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.vouchers.Voucher;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@AllArgsConstructor
public class VoucherButton extends Button {

    private Voucher voucher;


    @Override
    public String getName(Player player){
        return ChatColor.DARK_RED + "Voucher ID:" + (voucher.getUuid().toString().split("-")[0]);
    }

    @Override
    public List<String> getDescription(Player player){
        List<String> toReturn = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        toReturn.add(CC.translate("&7&m-----------------------------"));
        toReturn.add(CC.translate(" &4\u00BB &cStatus&7: &f" + (voucher.isRedeemed() ? "&aClaimed" : "&cUnclaimed")));
        toReturn.add(CC.translate(" &4\u00BB &cValue&7: &f$" + voucher.getValue()));
        toReturn.add(CC.translate(" &4\u00BB &cCreation Date&7: &f" + simpleDateFormat.format(new Date(voucher.getTime())) + " EST"));

        if (!voucher.isRedeemed()) {
            toReturn.add(" ");
            toReturn.add(CC.translate(" &4\u00BB &cYou can &4left click &cto set this coupon to &aclaimed&c."));
        }

        toReturn.add(CC.translate("&7&m-----------------------------"));
        return toReturn;
    }

    @Override
    public Material getMaterial(Player player){
        return (voucher.isRedeemed() ? Material.EMERALD_BLOCK : Material.PAPER);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType){
        if (!voucher.isRedeemed()){
            voucher.setRedeemed(true);
        }
    }


}
