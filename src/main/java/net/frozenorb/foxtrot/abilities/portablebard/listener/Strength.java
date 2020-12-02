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

public class Strength implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
        Player player = event.getPlayer();
        ItemStack strength = new ItemStack(Material.BLAZE_POWDER, player.getItemInHand().getAmount() - 1);
        ItemMeta meta = strength.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7 * &aTeam Members"));
        meta.setLore(lore);
        meta.setDisplayName(CC.translate("&4Strength II"));
        strength.setItemMeta(meta);
        if (!(player.getItemInHand().getType() == Material.BLAZE_POWDER)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.translate("&7 * &aTeam Members"))) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
                //player.sendMessage(CC.translate("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtil.formatTime((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getPortableBard().onCooldown(player)) {
                //player.sendMessage(CC.translate("&4&lPortable Bard&c is on cooldown for another&c&l " + TimeUtil.formatTime((int) Foxtrot.getInstance().getPortableBard().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            player.setItemInHand(strength);
            if (Foxtrot.getInstance().getTeamHandler().getTeam(player) == null) {
                player.sendMessage(CC.translate("&aApplied effect to yourself!"));
                Foxtrot.getInstance().getPortableBard().applyCooldown(player, 85 * 1000);
                Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                return;
            } else {
                senderTeam.getOnlineMembers().forEach(player1 -> player1.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1)));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                Foxtrot.getInstance().getPortableBard().applyCooldown(player, 30 * 1000);
                Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
                player.sendMessage(CC.translate("&aApplied strength 2 to your team!"));
                return;
            }
        }
    }
}
