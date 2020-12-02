package net.frozenorb.foxtrot.events.killtheking.menu.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.killtheking.menu.Host;
import net.frozenorb.qlib.menu.Button;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;


@AllArgsConstructor
public class TournamentButton extends Button {

    private final Host host;

    @Override
    public String getName(Player player) {
        return host.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        description.addAll(host.getDescription());
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return host.getIcon();
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (Foxtrot.getInstance().getTournamentHandler().isCreated()) {
            player.sendMessage(ChatColor.RED + "There is an currently an active event.. ");
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
            return;
        }

        host.toggle(player);
    }
}
