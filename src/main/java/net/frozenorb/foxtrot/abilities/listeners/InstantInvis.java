package net.frozenorb.foxtrot.abilities.listeners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InstantInvis implements Listener {

    public static HashMap<Player, ItemStack[]> armorContents = new HashMap<Player, ItemStack[]>();

    @EventHandler
    public void rightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!(event.getItem().getType() == Material.INK_SACK)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.translate("&7be invisible! Even your armor is invisible!")))
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){

            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
                player.sendMessage(CC.translate("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getInstantInvis().onCooldown(player)) {
                player.sendMessage(CC.translate("&4&lInstant Invis&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getInstantInvis().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            

        }
    }


}
