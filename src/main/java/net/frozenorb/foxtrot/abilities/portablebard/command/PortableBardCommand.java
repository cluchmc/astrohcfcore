package net.frozenorb.foxtrot.abilities.portablebard.command;

import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PortableBardCommand implements CommandExecutor {

    String prefix = "&4&lAstro&7 ┃ ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("astro.ability")){
            if (args.length == 0){
                sender.sendMessage(CC.CHAT_BAR);
                sender.sendMessage(CC.translate("&4&lPortable Bard Help"));
                sender.sendMessage("");
                sender.sendMessage(CC.translate("&c/ability list&7 - displays the list of all ability items"));
                sender.sendMessage(CC.translate("&c/ability give <player> <type> <amount>&7- give a player an ability item"));
                sender.sendMessage("");
                sender.sendMessage(CC.CHAT_BAR);
                return true;
            }
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("list")){
                    //                    sender.sendMessage(CC.CHAT_BAR);
                    sender.sendMessage(CC.translate("&4&lPortable Bard List&7 - (Page: 1/1)"));
                    sender.sendMessage("");
                    sender.sendMessage(CC.translate("&cSwitcher &7(switcher)"));
                    sender.sendMessage(CC.translate("&cNinja Star&7 (ninjastar)"));
                    sender.sendMessage(CC.translate("&cSwitch Stick&7 (switchstick)"));
                    sender.sendMessage(CC.translate("&cFake Pearl&7 (fakepearl)"));
                    sender.sendMessage(CC.translate("&cDisarmer Axe&7 (disarmer)"));
                    sender.sendMessage(CC.translate("&cRocket&7 (rocket)"));
                    sender.sendMessage(CC.translate("&cSecond Chance&7 (second-chance)"));
                    sender.sendMessage("");
                    sender.sendMessage(CC.CHAT_BAR);
                    return true;
                } else {
                    return false;
                }
            }
            if (args.length < 4){
                sender.sendMessage(CC.translate("&cCommand Misuse"));
                return true;
            }
            if (args.length > 4){
                sender.sendMessage(CC.translate("&cCommand Misuse"));
                return true;
            }
            if (args.length == 4){
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(CC.translate(prefix + "&cThat player is offline!"));
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                }catch (NumberFormatException e){
                    sender.sendMessage(CC.translate(prefix + "&cThat is not a number!"));
                    return true;
                }
                if (args[2].equalsIgnoreCase("portable-bard")){
                    ItemStack switcher = new ItemStack(Material.INK_SACK, amount, (short) 14);
                    ItemMeta meta = switcher.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(CC.translate("&7Right click this item to receive"));
                    lore.add(CC.translate("&75 of the portable bard item you choose!"));
                    meta.setLore(lore);
                    meta.setDisplayName(CC.translate("&4&lPortable Bard"));
                    switcher.setItemMeta(meta);
                    target.sendMessage(CC.translate(prefix + "&fYou have been given &4" + args[2].toString() + " portable bard&f!"));
                    target.getInventory().addItem(switcher);
                    sender.sendMessage(CC.translate(prefix + "&fYou have given &4" + target.getDisplayName() + args[2].toString() + " portable bard&f!"));
                    return true;
                } else if (args[2].equalsIgnoreCase("strength")){
                    ItemStack buildstick = new ItemStack(Material.BLAZE_POWDER, amount);
                    ItemMeta meta = buildstick.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(CC.translate("&7 * &aTeam Members"));
                    meta.setLore(lore);
                    meta.setDisplayName(CC.translate("&cStrength II"));
                    buildstick.setItemMeta(meta);
                    target.sendMessage(CC.translate(prefix + "&fYou have been given &4" + args[3].toString() + " strength 2&f!"));
                    target.getInventory().addItem(buildstick);
                    sender.sendMessage(CC.translate(prefix + "&fYou have given &4" + target.getDisplayName() + args[3].toString() + " strength 2&f!"));
                    return true;
                } else if (args[2].equalsIgnoreCase("resistance")){
                    ItemStack resistance = new ItemStack(Material.IRON_INGOT, amount);
                    ItemMeta meta = resistance.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(CC.translate("&7 * &aTeam Members"));
                    meta.setLore(lore);
                    meta.setDisplayName(CC.translate("&cResistance II"));
                    resistance.setItemMeta(meta);
                    target.sendMessage(CC.translate(prefix + "&fYou have been given &4" + args[3].toString() + " resistance 2&f!"));
                    target.getInventory().addItem(resistance);
                    sender.sendMessage(CC.translate(prefix + "&fYou have given &4" + target.getDisplayName() + args[3].toString() + " resistance 2&f!"));
                    return true;
                }
            }
        }
        return false;
    }
}