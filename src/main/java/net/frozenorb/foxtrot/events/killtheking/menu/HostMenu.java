package net.frozenorb.foxtrot.events.killtheking.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.killtheking.menu.button.HostButton;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;


@AllArgsConstructor
public class HostMenu extends Menu {

    {
        setPlaceholder(true);
        setAutoUpdate(true);

    }

    @Override
    public String getTitle(Player player) {
        return (ChatColor.RED + "Select an event...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(1, Host.DIAMOND.toButton());
        buttons.put(4, new HostButton());
        buttons.put(7, Host.SUMO.toButton());
        return buttons;
    }
}