package net.frozenorb.foxtrot.abilities.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ItemBuilder;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AbilityCommand {

    String prefix = "&4&lAstro&7 ┃ ";

    @Command(names = {"ability give"}, permission = "astro.ability")
    public static void giveability(Player sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "item") String item, @Param(name = "amount") int amount){
        if (item.equalsIgnoreCase("switcher")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.SNOW_BALL).name("&4&lSwitcher").lore(Arrays.asList(CC.translate("&7Switch locations with any"), CC.translate("&7enemy within 7 blocks of you"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " switchers!");
            return;
        }
        if (item.equalsIgnoreCase("paralyser")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.BLAZE_ROD).name("&4&lParalyser").lore(Arrays.asList(CC.translate("&7Hit a player 3 times in a row with this item"), CC.translate("&7to restrict them from building for 15 seconds!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " paralysers!");
            return;
        }
        if (item.equalsIgnoreCase("fakepearl")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.ENDER_PEARL).name("&4&lFake Pearl").lore(Arrays.asList(CC.translate("&7Right click this pearl"), CC.translate("&7to shoot a pearl to trick your enemies!"))).enchant(Enchantment.DURABILITY).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " fake pearls!");
        }
        if (item.equalsIgnoreCase("ragesoup")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.MUSHROOM_SOUP).name("&4&lRage Soup").lore(Arrays.asList(CC.translate("&7Right click this item to recieve"), CC.translate("&7Strength 2 and Resistance 3 for 10 seconds!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " rage soup!");
        }
        if (item.equalsIgnoreCase("ninjastar")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.NETHER_STAR).name("&4&lNinja Star").lore(Arrays.asList(CC.translate("&7Right click this item to"), CC.translate("&7teleport to the player that last hit you!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " ninja star!");
        }
        if (item.equalsIgnoreCase("instantinvis")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.INK_SACK).name("&4&lInstant Invis").lore(Arrays.asList(CC.translate("&7Right click this item to"), CC.translate("&7be invisible! Even your armor is invisible!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " ninja star!");
        }
        if (item.equalsIgnoreCase("potchecker")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.STICK).name("&d&lPot Checker").lore(Arrays.asList(CC.translate("&7Hit a player with this item to"), CC.translate("&7check how many potions they have left!"))).enchant(Enchantment.DURABILITY).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " pot checkers!");
        }
        if (item.equalsIgnoreCase("lifesaver")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.WATCH).name("&d&lLife Saver").lore(Arrays.asList(CC.translate("&7You will have 30 seconds to go down to 3 hearts"), CC.translate("&7during this cooldown if you go down to 2 hearts you will be healed fully!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " life savers!");
        }
        if (item.equalsIgnoreCase("trickortreat")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.PUMPKIN_PIE).name("&6&lTrick or Treat").lore(Arrays.asList(CC.translate("&7Upon use you will either recieve"), CC.translate("&7Weakness 2 or Strength 2 for 10 seconds!"))).amount(amount).build()}); }
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " trick or treats!");
    }

    @Command(names = {"ability reset"}, permission = "astro.ability")
    public static void resetability(Player sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "item") String item){
        if (item.equalsIgnoreCase("switcher")){
            if (Foxtrot.getInstance().getSwitcher().onCooldown(target)){
                Foxtrot.getInstance().getSwitcher().cooldownRemove(target);
                sender.sendMessage(ChatColor.GREEN + "You have removed the switcher cooldown from " + target.getName());
                return;
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " is not on switcher cooldown!");
            }
        }
    }

    @Command(names = {"ability list"}, permission = "astro.ability")
    public static void abilitylist(Player sender){
        sender.sendMessage("switcher");
        sender.sendMessage("paralyser");
        sender.sendMessage("fakepearl");
        sender.sendMessage("ragesoup");
        sender.sendMessage("ninjastar");
        sender.sendMessage("potchecker");
        sender.sendMessage("lifesaver");
    }
}
