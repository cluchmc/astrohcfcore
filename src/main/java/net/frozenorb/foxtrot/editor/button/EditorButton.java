package net.frozenorb.foxtrot.editor.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.editor.Editor;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/**
 * Created by InspectMC
 * Date: 7/6/2020
 * Time: 12:26 AM
 */

@AllArgsConstructor
public class EditorButton extends Button {

    private final Editor editor;

    @Override
    public String getName(Player player) {
        return editor.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add("");
        description.addAll(editor.getDescription());
        description.add("");
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return editor.getIcon();
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        editor.toggle(player);
    }
}
