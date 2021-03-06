package net.frozenorb.foxtrot.abilities.listeners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class RageSoup implements Listener {

    @EventHandler
    public void rightclick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, player.getItemInHand().getAmount() - 1);
        ItemMeta meta = pearl.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7Right click this item to recieve"));
        lore.add(CC.translate("&7Strength 2 and Resistance 3 for 10 seconds!"));
        meta.setLore(lore);
        meta.setDisplayName(CC.translate("&4&lRage Soup"));
        pearl.setItemMeta(meta);
        if (!(player.getItemInHand().getType() == Material.MUSHROOM_SOUP)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.translate("&7Strength 2 and Resistance 3 for 10 seconds!")))
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
                player.sendMessage(CC.translate("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getRageSoup().onCooldown(player)) {
                player.sendMessage(CC.translate("&4&lRage Soup&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getRageSoup().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 2));

            ArrayList<String> message = new ArrayList<>();
            message.add("");
            message.add(CC.translate("&4\u00BB &cYou have used your rage soup!"));
            message.add(CC.translate("&4\u00BB &cYou have strength 2 and resistance 3 for 7 seconds!"));
            message.add("");
            message.forEach(player::sendMessage);

            if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);

            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

            Foxtrot.getInstance().getRageSoup().applyCooldown(player, 60 * 1000);
            Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
        }
    }
}
