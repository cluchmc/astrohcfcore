package net.frozenorb.foxtrot.map.kits.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitBypassCommand {

	@Command(names = "kitbypass", permission = "op")
	public static void kitBypass(Player sender, @Param(name = "kitname") String name) {
		if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
			sender.sendMessage("§cThis is a KitMap only command.");
			return;
		}

		if (Foxtrot.getInstance().getMapHandler().getKitManager().getUseAll().contains(name.toLowerCase())) {
			Foxtrot.getInstance().getMapHandler().getKitManager().getUseAll().remove(name.toLowerCase());
			sender.sendMessage(ChatColor.RED + "Removed " + name + " from bypass list!");
		} else {
			Foxtrot.getInstance().getMapHandler().getKitManager().getUseAll().add(name.toLowerCase());
			sender.sendMessage(ChatColor.GREEN + "Added " + name + " to bypass list!");
		}
	}

}