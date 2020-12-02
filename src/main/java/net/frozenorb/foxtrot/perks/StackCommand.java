package net.frozenorb.foxtrot.perks;

import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class StackCommand {

    @Command(names = {"stack"}, permission = "foxtrot.stack")
    public static void stack(CommandSender sender) {
        Player player = ((Player) sender);
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int done = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current != null) {
                for (int i2 = i + 1; i2 < contents.length; i2++) {
                    ItemStack current2 = contents[i2];
                    if (current.isSimilar(current2)) {
                        int allowed = current.getMaxStackSize() - current.getAmount();
                        if (allowed > 0) {
                            int left = current2.getAmount() - allowed;
                            if (left > 0) {
                                current2.setAmount(left);
                                current.setAmount(current.getMaxStackSize());
                            } else {
                                done++;
                                current.setAmount(current.getAmount() + current2.getAmount());
                                contents[i2] = null;
                            }
                        }
                    }
                }
            }
        }
        inventory.setContents(contents);
        player.updateInventory();
        sender.sendMessage(done == 0 ? ChatColor.RED + "You don't have any items to stack." : ChatColor.GOLD + "You've stacked " + ChatColor.GREEN + done + ChatColor.GOLD + " item" + (done != 1 ? "s" : ""));
    }
}