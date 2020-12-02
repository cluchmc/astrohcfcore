package net.frozenorb.foxtrot.editor;

import net.frozenorb.foxtrot.editor.Editor;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.kronos.helium.util.menu.button.*;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 12:22 AM
 */

@AllArgsConstructor
public class EditorMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Choose A Kit!";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(12, Editor.PvP.toButton());
        buttons.put(13, Editor.BARD.toButton());
        buttons.put(14, Editor.ARCHER.toButton());
        buttons.put(21, Editor.BUILDER.toButton());
        buttons.put(22, Editor.ROGUE.toButton());

        buttons.put(35, Button.placeholder(Material.AIR));
        return buttons;
    }
}