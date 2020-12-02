package net.frozenorb.foxtrot.map.killstreaks.velttypes;

import net.frozenorb.foxtrot.map.killstreaks.Killstreak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PartnerPackages extends Killstreak {

    @Override
    public String getName() {
        return "3 Partner Packages";
    }

    @Override
    public int[] getKills() {
        return new int[] {
                25
        };
    }

    @Override
    public void apply(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "package give " + player.getName() + "3");
    }

}
