package net.frozenorb.foxtrot.partnerpackages;

import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;

public class PackageListener implements Listener {

    @EventHandler
    public void rightclick(PlayerInteractEvent event) {
        Player player = Foxtrot.getInstance().getServer().getPlayer(event.getPlayer().getUniqueId());
        if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.ENDER_CHEST && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&4&lPartner Package")) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            Firework firework = (Firework) event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), (Class) Firework.class);
            FireworkMeta data = firework.getFireworkMeta();
            data.addEffects(new FireworkEffect[] { FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build() });
            data.setPower(1);
            firework.setFireworkMeta(data);
            String prefix = CC.prefix;
            Random object = new Random();
            for (int counter = 1; counter <= 1; ++counter){
                int abilityitem = 1 + object.nextInt(8);
                if (abilityitem == 1){
                    // switcher
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.SNOW_BALL).name("&4&lSwitcher").lore(Arrays.asList(CC.translate("&7Switch locations with any"), CC.translate("&7enemy within 7 blocks of you"))).amount(4).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 4 &4&lSwitcher&f's"));
                }
                else if (abilityitem == 2){
                    // anti build stick
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.BLAZE_ROD).name("&4&lParalyser").lore(Arrays.asList(CC.translate("&7Hit a player 3 times in a row with this item"), CC.translate("&7to restrict them from building for 15 seconds!"))).amount(3).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 4 &4&lParalyser&f's"));
                }
                else if (abilityitem == 3){
                    // fake pearl
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.ENDER_PEARL).name("&4&lFake Pearl").lore(Arrays.asList(CC.translate("&7Right click this pearl"), CC.translate("&7to shoot a pearl to trick your enemies!"))).enchant(Enchantment.DURABILITY).amount(4).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 4 &4&lFake Pearl&f's"));
                }
                else if (abilityitem == 4){
                    // rage soup
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.MUSHROOM_SOUP).name("&4&lRage Soup").lore(Arrays.asList(CC.translate("&7Right click this item to recieve"), CC.translate("&7Strength 2 and Resistance 3 for 10 seconds!"))).amount(3).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 3 &4&lRage Soup&f's"));
                }
                else if (abilityitem == 5){
                    // ninja star
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.NETHER_STAR).name("&4&lNinja Star").lore(Arrays.asList(CC.translate("&7Right click this item to"), CC.translate("&7teleport to the player that last hit you!"))).amount(2).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 2 &4&lNinja Star&f's"));
                }
                else if (abilityitem == 6){
                    // pot checker
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.STICK).name("&4&lPot Checker").lore(Arrays.asList(CC.translate("&7Hit a player with this item to"), CC.translate("&7check how many potions they have left!"))).enchant(Enchantment.DURABILITY).amount(4).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 4 &d&lPot Checkers&f's"));
                }
                else if (abilityitem == 7){
                    // give life saver
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.WATCH).name("&4&lLife Saver").lore(Arrays.asList(CC.translate("&7You will have 30 seconds to go down to 3 hearts"), CC.translate("&7during this cooldown if you go down to 2 hearts you will be healed fully!"))).amount(3).build()});
                    player.sendMessage(CC.translate(prefix + "&f You've won 3 &d&lLife Saver&f's"));
                }
                else if (abilityitem == 8){
                    // give trick or treat
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.PUMPKIN_PIE).name("&6&lTrick or Treat").lore(Arrays.asList(CC.translate("&7Upon use you will either recieve"), CC.translate("&7Weakness 2 or Strength 2 for 10 seconds!"))).amount(3).build()}); }
                    player.sendMessage(CC.translate(prefix + "&f You've won 3 &6&lTrick or Treat&f's"));
                }
            Random object2 = new Random();
            for (int counter2 =1; counter2 <=1; ++counter2){
                int randomitem = 1 + object2.nextInt(8);
                if (randomitem == 1){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.IRON_BLOCK).amount(32).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won 32 &f&lIron Blocks&f."));
                }
                else if (randomitem == 2){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_BLOCK).amount(32).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won 32 &b&lDiamond Blocks&f."));
                }
                else if (randomitem == 3){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.EMERALD_BLOCK).amount(32).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won 32 &a&lEmerald Blocks&f."));
                }
                else if (randomitem == 4){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).amount(1).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won a &b&lDiamond Helmet&f."));
                }
                else if (randomitem == 5){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).amount(1).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won &b&lDiamond Chestplate&f."));
                }
                else if (randomitem == 6){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).amount(1).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won &b&lDiamond Leggings&f."));
                }
                else if (randomitem == 7){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 4).amount(1).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won &b&lDiamond Boots&f."));
                }
                else if (randomitem == 8){
                    player.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.DURABILITY, 3).amount(1).build() });
                    player.sendMessage(CC.translate(prefix + "&fYou've won &b&lDiamond Helmet&f."));
                }
            }
            if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);

            player.getItemInHand().setAmount(player.getItemInHand().getAmount() -1);
        }
    }
}
