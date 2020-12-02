package net.frozenorb.foxtrot.abilities.portablebard.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PortableBard implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
        Player player = event.getPlayer();
        ItemStack portablebard = new ItemStack(Material.INK_SACK, player.getItemInHand().getAmount() - 1);
        ItemMeta meta = portablebard.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7Right click this item to receive"));
        lore.add(CC.translate("&75 of the portable bard item you choose!"));
        meta.setLore(lore);
        meta.setDisplayName(CC.translate("&4&lPortable Bard"));
        portablebard.setItemMeta(meta);
        if (!(player.getItemInHand().getType() == Material.IRON_INGOT)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.translate("&75 of the portable bard item you choose!"))) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // make gui thing here
            // tired rn so ima do it later
            // TODO: 16/09/2020
        }
    }
}
