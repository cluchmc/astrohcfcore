package net.frozenorb.foxtrot.vouchers.menu;


import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.vouchers.Voucher;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class VoucherDisplayMenu extends Menu {

    private UUID uuid;

    @Override
    public String getTitle(Player player){
        return CC.translate("&4Vouchers");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player){
        Map<Integer, Button> buttons = new HashMap<>();

        List<Voucher> vouchers = Foxtrot.getInstance().getVoucherHandler().getVouchers(uuid);

        int size = (int) (Math.ceil(vouchers.size() / 9d) * 9) + 18;

        for (int i = 0; i < 9; i++){
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "§c"));
        }

        for (int i = (size - 9); i < size; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "§c"));
        }

        int i = 8;

        for (Voucher voucher : vouchers){
            i++;
            buttons.put(i, new VoucherButton(voucher));
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate(){
        return true;
    }
}
