package net.frozenorb.foxtrot.events.killtheking.menu.button;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.killtheking.KillTheKing;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.menu.Button;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;


public class HostButton extends Button {

//    private static final int PRICE = 25;

    @Override
    public String getName(Player player) {
        return (ChatColor.DARK_RED + "KillTheKing Event");
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = Lists.newArrayList();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
//        boolean afford = CreditHandler.getPlayerCredits(player) >= PRICE;
//        if (!afford && Foxtrot.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//            lore.add(ChatColor.RED + "You must have at least " + PRICE + " credits");
//            lore.add("");
//            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To earn credits visit our store store." + Foxtrot.getInstance().getServerHandler().getNetworkWebsite());
//            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "or you can obtain credits by killing players.");
//        } else
        lore.add(ChatColor.GREEN + "Click to start this event!");
        lore.add("");
        lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Remember to prepare yourself.");
        // Spanish
//        if (!Foxtrot.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//            if (!afford && !Foxtrot.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//                lore.add(ChatColor.RED + "Debes tener al menos " + PRICE + " créditos");
//                lore.add("");
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Para ganar créditos visita nuestra tienda store." + Foxtrot.getInstance().getServerHandler().getNetworkWebsite());
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "puedes obtener créditos matando a otros jugadores.");
//            } else if (afford && !Foxtrot.getInstance().getLanguageMap().isNewLanguageToggle(player.getUniqueId())) {
//                lore.add(ChatColor.GREEN + "Click para comenzar el evento ");
//                lore.add("");
//                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Recuerda estar listo.");
//            }
//        }
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30));
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType.isRightClick() || clickType.isLeftClick()) {
            Team team = LandBoard.getInstance().getTeam(player.getLocation());
            if (team == null || !team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                player.sendMessage(ChatColor.RED + "You must be in Spawn to start the event!");
                return;
            }

//            if (CreditHandler.getPlayerCredits(player) < PRICE) {
//                player.sendMessage((value ? ChatColor.RED + "You do not have enough credits!" : ChatColor.RED + "No tienes suficientes creditos!"));
//                return;
//            }

            if (Foxtrot.getInstance().getKillTheKing() != null) {
                player.sendMessage(ChatColor.RED + "This event is currently active.");
                return;
            }

            Foxtrot.getInstance().setKillTheKing(new KillTheKing(player, true));
//            Foxtrot.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - PRICE);

            String[] messages;

            messages = new String[]{
                    ChatColor.RED + "███████",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█" + " " + ChatColor.GOLD + "[Event]",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.YELLOW + "KillTheKing",
                    ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.GREEN + "King: " + ChatColor.GRAY + Foxtrot.getInstance().getKillTheKing().getActiveKing().getName(),
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█",
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
}