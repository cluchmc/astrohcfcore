package net.frozenorb.foxtrot.events.killtheking.listener;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

public class KillTheKingListener implements Listener {

    private final String prefix = ChatColor.RED + "Kill the King" + ChatColor.DARK_GRAY + " » ";

    @EventHandler
    public void on(PlayerDeathEvent e) {
        if (Foxtrot.getInstance().getKillTheKing() == null) {
            return;
        }
        Player player = Foxtrot.getInstance().getKillTheKing().getActiveKing();
        Player killer = e.getEntity().getKiller();
        if (e.getEntity().equals(player)) {
            e.getDrops().clear();
            if (killer == null) {
                Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has been killed.");
                Foxtrot.getInstance().setKillTheKing(null);
                return;
            }
            Foxtrot.getInstance().setKillTheKing(null);
            Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has been killed by " + killer.getName() + ".");
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        if (Foxtrot.getInstance().getKillTheKing() == null) {
            return;
        }
        Player player = Foxtrot.getInstance().getKillTheKing().getActiveKing();
        if (e.getPlayer().equals(player)) {
            Foxtrot.getInstance().setKillTheKing(null);
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            Bukkit.broadcastMessage(prefix + ChatColor.WHITE + player.getName() + " has disconnected!");
        }
    }
}