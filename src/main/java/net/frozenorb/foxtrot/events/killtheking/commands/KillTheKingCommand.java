package net.frozenorb.foxtrot.events.killtheking.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.killtheking.KillTheKing;
import net.frozenorb.foxtrot.events.killtheking.menu.HostMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.frozenorb.foxtrot.util.ItemBuilder;
import org.bukkit.scheduler.BukkitRunnable;

public class KillTheKingCommand {

    @Command(names = {"host"}, permission = "event.ktk")
    public static void host(Player player){
        if (Foxtrot.getInstance().getMapHandler().isKitMap()){
            new HostMenu().openMenu(player);
        } else {
            player.sendMessage(ChatColor.RED + "You can only use this command on Kitmap!");
        }
    }

    @Command(names = {"killtheking stop", "ktk stop"}, permission = "event.ktk")
    public static void ktkstop(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target){
        target.getInventory().clear();
        target.removePotionEffect(PotionEffectType.SPEED);
        target.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        target.getInventory().setArmorContents(null);
        target.sendMessage(ChatColor.RED + "Kill The King has ended!");
        Foxtrot.getInstance().setKillTheKing(null);
    }

    @Command(names = {"killtheking start", "ktk start"}, permission = "event.ktk")
    public static void ktkstart(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target){
        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
        Foxtrot.getInstance().setKillTheKing(new KillTheKing(target, true));

        target.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());
        target.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).build());

        target.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 10).build());
        target.getInventory().setItem(1, new ItemBuilder(Material.ENDER_PEARL, 64).build());
        target.getInventory().setItem(8, new ItemBuilder(Material.GOLDEN_APPLE, 64).build());
        for (int i = 2; i < 5; i++) {
            ItemStack itemStack = target.getInventory().getItem(i);
            if (itemStack != null) {
                continue;
            }
            ItemStack HP = new ItemStack(Material.POTION, 64, (short) 16421);
            target.getInventory().setItem(i, HP);
        }
        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5));
        String[] messages;

        messages = new String[]{
                ChatColor.RED + "███████",
                ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " " + ChatColor.DARK_RED + "[Event]",
                ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED + "KillTheKing",
                ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " " + ChatColor.GREEN + "King: " + ChatColor.GRAY + Foxtrot.getInstance().getKillTheKing().getActiveKing().getName(),
                ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████",
                ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█",
                ChatColor.RED + "███████"
        };

        final String[] messagesFinal = messages;

        new BukkitRunnable() {

            public void run() {
                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.sendMessage(messagesFinal);
                }
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());
    }
}
