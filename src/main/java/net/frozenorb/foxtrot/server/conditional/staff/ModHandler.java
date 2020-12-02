package net.frozenorb.foxtrot.server.conditional.staff;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBNotification;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.kronos.helium.profile.Profile;
import org.kronos.helium.rank.Rank;
import org.kronos.helium.util.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.nametag.FrozenNametagHandler;
import net.frozenorb.qlib.visibility.FrozenVisibilityHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModHandler {

    private static Map<UUID, ModCache> modCacheMap = new HashMap<>();

    public static boolean isModMode(Player player) {
        return player.hasMetadata("modmode");
    }
    public static boolean canSeeStaff(Player player){
        return player.hasMetadata("nostaff");
    }

    public static boolean isVanished(Player player) {
        return player.hasMetadata("invisible");
    }

    public static boolean toggleModMode(Player player) {
        return toggleModMode(player, false);
    }

    public static boolean toggleModMode(Player player, boolean silent) {
        boolean newState = !isModMode(player);

        if (!silent)
            player.sendMessage(ChatColor.DARK_RED + "Mod Mode: " + (newState ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));

        if (newState) {
            player.setMetadata("modmode", new FixedMetadataValue(Foxtrot.getInstance(), true));

            ModCache cache = new ModCache(player);
            modCacheMap.put(player.getUniqueId(), cache);

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            if (player.hasPermission("foxtrot.gamemode"))
                player.setGameMode(GameMode.CREATIVE);
            else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            setVanished(player, true);

            player.getInventory().setItem(0, StaffItems.COMPASS);
            player.getInventory().setItem(1, StaffItems.INSPECT_BOOK);

            if (player.hasPermission("worldedit.wand")) {
                player.getInventory().setItem(2, StaffItems.WAND);
                player.getInventory().setItem(3, StaffItems.CARPET);
            } else
                player.getInventory().setItem(2, StaffItems.CARPET);

            player.getInventory().setItem(7, StaffItems.ONLINE_STAFF);
            player.getInventory().setItem(8, StaffItems.GO_VIS);

            CheatBreakerAPI.getInstance().giveAllStaffModules(player);
            CheatBreakerAPI.getInstance().sendNotification(player,new CBNotification("&fStaff modules enabled", 2, TimeUnit.SECONDS, CBNotification.Level.INFO));
            Bukkit.getPluginManager().callEvent(new ModModeEnterEvent(player));
        } else {
            player.removeMetadata("modmode", Foxtrot.getInstance());
            setVanished(player, false);

            ModCache cache = modCacheMap.remove(player.getUniqueId());
            if (cache != null)
                cache.restore(player);

            if (player.getGameMode() != GameMode.CREATIVE)
                player.setAllowFlight(false);
            CheatBreakerAPI.getInstance().disableAllStaffModules(player);
            CheatBreakerAPI.getInstance().sendNotification(player,new CBNotification("&fStaff modules disabled", 2, TimeUnit.SECONDS, CBNotification.Level.INFO));
            Bukkit.getPluginManager().callEvent(new ModModeExitEvent(player));
        }

        player.updateInventory();
        FrozenNametagHandler.reloadPlayer(player);
        FrozenVisibilityHandler.update(player);

        return newState;
    }

    public static void enableModMode(Player player) {
        if (isModMode(player)) return;
        toggleModMode(player);
    }

    public static void disableModMode(Player player) {
        if (!isModMode(player)) return;
        toggleModMode(player);
    }

    public static void setVanished(Player player, boolean state) {
        if (state)
            player.setMetadata("invisible", new FixedMetadataValue(Foxtrot.getInstance(), true));
        else
            player.removeMetadata("invisible", Foxtrot.getInstance());

        if (isModMode(player))
            player.getInventory().setItem(8, state ? StaffItems.GO_VIS : StaffItems.GO_INVIS);

        player.spigot().setCollidesWithEntities(!state);
        player.updateInventory();

        FrozenNametagHandler.reloadPlayer(player);
        FrozenVisibilityHandler.update(player);
    }

    @Getter
    @AllArgsConstructor
    protected static class ModCache {
        private ItemStack[] inventory;
        private ItemStack[] armor;
        private GameMode gameMode;

        public ModCache(Player player) {
            this.inventory = player.getInventory().getContents();
            this.armor = player.getInventory().getArmorContents();
            this.gameMode = player.getGameMode();
        }

        public void restore(Player player) {
            player.getInventory().setContents(inventory);
            player.getInventory().setArmorContents(armor);
            player.setGameMode(gameMode);
        }
    }

    public static class StaffItems {
        public static ItemStack COMPASS = build(Material.COMPASS, ChatColor.RED + "Compass");
        public static ItemStack INSPECT_BOOK = build(Material.BOOK, ChatColor.RED + "Inspection Book");
        public static ItemStack WAND = build(Material.WOOD_AXE, ChatColor.RED + "WorldEdit Wand");

        public static ItemStack GO_VIS = build(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData(), ChatColor.RED + "Become Visible");
        public static ItemStack GO_INVIS = build(Material.INK_SACK, 1, DyeColor.LIME.getDyeData(), ChatColor.RED + "Become Invisible");

        public static ItemStack ONLINE_STAFF = build(Material.SKULL_ITEM, ChatColor.RED + "Online Staff");

        public static ItemStack LAST_DAMAGE = build(Material.EMERALD, ChatColor.RED + "Teleport to Last Damage");
        public static ItemStack CARPET = build(Material.CARPET, 1, DyeColor.RED.getWoolData(), " ");

        public static ItemStack build(Material type, String displayName) {
            return build(type, 1, (byte)0, displayName);
        }

        public static ItemStack build(Material type, int amount, byte data, String displayName) {
            ItemStack stack = new ItemStack(type, amount, (short)1, data);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            stack.setItemMeta(meta);
            return stack;
        }
    }

    public static void openOnlineStaff(Player player){
        new Menu(){
            @Override
            public String getTitle(Player player){
                return ChatColor.RED + "Online Staff";
            }

            @Override
            public Map<Integer, Button> getButtons(Player player){
                Map<Integer, Button> buttons = new HashMap<>();

                List<Profile> profileList = new ArrayList<>();

                for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers().stream().filter(other -> Profile.getByUuid(other.getUniqueId()).getActiveRank().getWeight() >=
                        Rank.getRankByDisplayName("Trial-Mod").getWeight()).collect(Collectors.toList())){
                    profileList.add(Profile.getByUuid(otherPlayer.getUniqueId()));
                }

                profileList.sort(Comparator.comparingInt(profile -> profile.getActiveRank().getWeight()));
                Collections.reverse(profileList);

                for (Profile profile : profileList){
                    buttons.put(buttons.size(), new Button() {
                        @Override
                        public String getName(Player player) {
                            return CC.translate(profile.getActiveRank().getPrefix() + profile.getName());
                        }

                        @Override
                        public List<String> getDescription(Player player) {
                            List<String> toReturn = new ArrayList<>();

                            Player bukkitPlayer = Foxtrot.getInstance().getServer().getPlayer(profile.getUuid());

                            long playTime = Foxtrot.getInstance().getPlaytimeMap().getPlaytime(profile.getUuid());

                            if (bukkitPlayer != null && player.canSee(bukkitPlayer)){
                                playTime += Foxtrot.getInstance().getPlaytimeMap().getCurrentSession(bukkitPlayer.getUniqueId()) / 1000;
                            }

                            toReturn.add("&7&m--------------------------");
                            toReturn.add("&cRank: " + profile.getActiveRank().getDisplayName());
                            toReturn.add("&cPlaytime " + profile.getActiveRank().getDisplayName());
                            toReturn.add("&7&m--------------------------");

                            return CC.translate(toReturn);
                        }

                        @Override
                        public Material getMaterial(Player player) {
                            return Material.SKULL_ITEM;
                        }
                    });
                }

                return buttons;
            }
        }.openMenu(player);
    }

}