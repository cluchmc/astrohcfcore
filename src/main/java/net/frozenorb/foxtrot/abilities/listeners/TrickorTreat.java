package net.frozenorb.foxtrot.abilities.listeners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class TrickorTreat implements Listener {

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (!p.getItemInHand().hasItemMeta()) return;
        if (p.getItemInHand().getType() != Material.PUMPKIN_PIE) return;
        if (!p.getItemInHand().getItemMeta().getLore().contains(CC.translate("&7Weakness 2 or Strength 2 for 10 seconds!"))) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (Foxtrot.getInstance().getTrickOrTreat().onCooldown(p)) {
                    event.getPlayer().sendMessage(CC.translate("&6&lTrick or Treat&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getTrickOrTreat().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                    p.updateInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
    
    @EventHandler
    public void rightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!player.getItemInHand().hasItemMeta()) return;
        if (player.getItemInHand().getType() != Material.PUMPKIN_PIE) return;
        if (!player.getItemInHand().getItemMeta().getLore().contains(CC.translate("&7Weakness 2 or Strength 2 for 10 seconds!"))) return;

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())){
            player.sendMessage(CC.translate(CC.prefix + " &fYou cannot use &4ability&f items in &aSpawn&f's territory!"));
            return;
        }

        if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)){
            player.sendMessage(CC.translate("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingInt(event.getPlayer()) / 1000) + "&c!"));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (Foxtrot.getInstance().getTrickOrTreat().onCooldown(player)){
            event.getPlayer().sendMessage(CC.translate("&6&lTrick or Treat&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getTrickOrTreat().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
            player.updateInventory();;
            event.setCancelled(true);
            return;
        }

        Foxtrot.getInstance().getTrickOrTreat().applyCooldown(player, 80 * 1000);
        Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);

        if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);

        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

        Random object = new Random();
        for (int counter = 1; counter <= 1; ++counter){
            int chance = 1 + object.nextInt(100);
            if (chance > 50){
                // give strength
                ArrayList<String> message = new ArrayList<>();
                message.add("");
                message.add(CC.translate("&4\u2764 &cYour &6&lTrick or Treat&c has been used!"));
                message.add(CC.translate("&4\u2764 &cYou have receive  strength 2 and resistance 2!"));
                message.add("");
                message.forEach(player::sendMessage);

                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 2));
            } else {
                // give weakness
                ArrayList<String> message = new ArrayList<>();
                message.add("");
                message.add(CC.translate("&4\u2764 &cYour &6&lTrick or Treat&c has been used!"));
                message.add(CC.translate("&4\u2764 &cYou have receive  weakness 2 and mining fatigue!"));
                message.add("");
                message.forEach(player::sendMessage);

                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 1));
            }
        }
    }
}
