package net.frozenorb.foxtrot.tournaments.command;

import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.tournaments.Tournament;
import net.frozenorb.foxtrot.tournaments.TournamentState;
import net.frozenorb.foxtrot.tournaments.TournamentType;
import net.frozenorb.foxtrot.tournaments.config.TournamentFile;
import net.frozenorb.foxtrot.tournaments.handler.runnable.TournamentRunnable;
import net.frozenorb.foxtrot.tournaments.util.LocationUtils;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TournamentCommand {

    private static final TournamentFile file = TournamentFile.getConfig();


    @Command(names = {"tournament leave", "leave"}, permission = "")
    public static void leave(Player player) {
        final Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();
        if (tournament != null) {
            if (!Foxtrot.getInstance().getTournamentHandler().isInTournament(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are not participating in any event!");
            } else if (tournament.getHoster().getName().equalsIgnoreCase(player.getName())) {
                player.sendMessage(ChatColor.RED + "You cannot leave your own event!");
            } else {
                Foxtrot.getInstance().getTournamentHandler().leaveTournament(player);
            }
        } else {
            player.sendMessage(ChatColor.RED + "This event does not exist!");
        }
    }

    @Command(names = {"tournament join", "join"}, permission = "")
    public static void join(Player player) {
        Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();
        ItemStack[] contents;
        for (int length = (contents = player.getInventory().getContents()).length, i = 0; i < length; ++i) {
            ItemStack item = contents[i];
            if (item != null) {
                player.sendMessage(ChatColor.RED + "To enter the event you must have an empty inventory!");
                return;
            }
        }
        if (tournament == null) {
            return;
        }

        int countdown = Foxtrot.getInstance().getTournamentHandler().getTournament().getCooldown();
        if (tournament != null) {
            if (!DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                player.sendMessage(ChatColor.RED + "You must be in spawn to join tournaments");
                return;
            }

            if (player.hasMetadata("ModMode")) {
                player.sendMessage(ChatColor.RED + "You cannot use this while in mod mode.");
                return;
            }

            if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You cannot do this while your PVPTimer is active!");
                player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To remove your PvPTimer type '" + ChatColor.WHITE + "/pvp enable" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "'.");
                return;
            }

            if (Foxtrot.getInstance().getTournamentHandler().isInTournament(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are already in an event!");
                return;
            }
            //This code was to prevent players from each team only have 1 member
            //But made it so friendly fire is on only in tournaments and doesn't loose dtr
//            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
//            if(team.getMembers().size() >= 1 &&
//                    tournament.getType() == TournamentType.DIAMOND ||
//                    tournament.getType() == TournamentType.ARCHER ||
//                    tournament.getType() == TournamentType.AXE ||
//                    tournament.getType() == TournamentType.ROGUE) {
//                player.sendMessage(ChatColor.RED + "You may not join the FFA since you already have a team member in the event.");
//
//            }

            if (tournament.getPlayers().size() == tournament.getSize()) {
                player.sendMessage(ChatColor.RED + "The event is currently full.");
                return;
            }

            if (countdown == 0) {
                player.sendMessage(ChatColor.RED + "The event has already started!");
                return;
            }

            if (tournament.getTournamentState() == TournamentState.FIGHTING) {
                player.sendMessage(ChatColor.RED + "The event has already started!");
                return;
            }

            Foxtrot.getInstance().getTournamentHandler().joinTournament(player);
            tournament.saveInventory(player);
            if (player.getGameMode() != GameMode.SURVIVAL) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            if (player.isFlying()) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            tournament.teleport(player, "Spawn");
            tournament.giveItemWait(player);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEste evento no existe!"));
        }
    }


    @Command(names = {"tournament set"}, permission = "astro.tournament.set")
    public static void set(Player player, @Param(name = "type") String type, @Param(name = "sumo<spawn|first|second>") String tournament) {
        if (type.equalsIgnoreCase("spawn")) {
            file.set("Locations.Spawn", LocationUtils.getString(player.getLocation()));
            file.save();
            file.reload();
            player.sendMessage(ChatColor.GREEN + "Tournament Spawn location saved.");
        } else if (type.equalsIgnoreCase("sumo")) {
            if (tournament.equalsIgnoreCase("spawn")) {
                file.set("Locations.Sumo.Spawn", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Sumo Spawn location saved.");
            } else if (tournament.equalsIgnoreCase("first")) {
                file.set("Locations.Sumo.First", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Sumo First location saved.");
            } else if (tournament.equalsIgnoreCase("second")) {
                file.set("Locations.Sumo.Second", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Sumo Second location saved.");
            } else if (tournament.equalsIgnoreCase("spectate")) {
                file.set("Locations.Sumo.Spectate", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Sumo Spectate zone location saved.");
            } else {
                player.sendMessage(ChatColor.RED + "Tournament sub-command '" + tournament + "' not found.");
            }
        } else if (type.equalsIgnoreCase("ffa")) {
            if (tournament.equalsIgnoreCase("spawn")) {
                file.set("Locations.FFA.Spawn", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament FFA Spawn location saved.");
            } else {
                player.sendMessage(ChatColor.RED + "Tournament sub-command '" + tournament + "' not found.");
            }
        } else if (type.equalsIgnoreCase("axe")) {
            if (tournament.equalsIgnoreCase("spawn")) {
                file.set("Locations.Axe.Spawn", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Axe Spawn location saved.");
            } else {
                player.sendMessage(ChatColor.RED + "Tournament sub-command '" + tournament + "' not found.");
            }
        } else if (type.equalsIgnoreCase("archer")) {
            if (tournament.equalsIgnoreCase("spawn")) {
                file.set("Locations.Archer.Spawn", LocationUtils.getString(player.getLocation()));
                file.save();
                file.reload();
                player.sendMessage(ChatColor.GREEN + "Tournament Archer Spawn location saved.");
            } else {
                player.sendMessage(ChatColor.GREEN + "Tournament sub-command '" + tournament + "' not found.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Tournament sub-command '" + type + "' not found.");
        }
    }

    @Command(names = {"tournament create"}, permission = "astro.tournament.host")
    public static void create(Player player, @Param(name = "maxplayers") int maxplayers, @Param(name = "sumo|diamond|axe|archer|rogue") String tournamentType) {
        ItemStack[] contents;
        for (int length = (contents = player.getInventory().getContents()).length, i = 0; i < length; ++i) {
            ItemStack item = contents[i];
            if (item != null) {
                player.sendMessage(ChatColor.RED + "To create an event you must have an empty inventory!");
                return;
            }
        }

        if (player.hasMetadata("ModMode")) {
            player.sendMessage(ChatColor.RED + "You cannot use this while in mod mode.");
            return;
        }

        if (maxplayers < 1) {
            player.sendMessage(ChatColor.RED + "Invalid size.");
            return;
        }
        if (maxplayers > 50) {
            player.sendMessage(ChatColor.RED + "Maximum size is 50.");
            return;
        }

        try {
            TournamentType type = TournamentType.valueOf(tournamentType.toUpperCase());
            Foxtrot.getInstance().getTournamentHandler().createTournament(player, maxplayers, type, player);
            player.performCommand("tournament join");
            for (Player online : Bukkit.getOnlinePlayers()) {
                String name = player.getDisplayName();
                Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();
                FancyMessage message = new FancyMessage("");
                message.then(ChatColor.translateAlternateColorCodes('&', "&4&l" + type.getName() + " &7hosted by &r" + name + " &f(" + "&a" + tournament.getPlayers().size() + "&f/&a" + tournament.getSize() + "&f)"))
                        .tooltip(ChatColor.translateAlternateColorCodes('&', "&aClick to enter")).command("/tournament join").send(online);
            }
            new TournamentRunnable(Foxtrot.getInstance().getTournamentHandler().getTournament()).runAnnounce();
        } catch (Exception e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTournamentType not found."));
        }
    }

    @Command(names = {"tournament cancel", "tournament stop", "tournament end"}, permission = "Foxtrot.command.host")
    public static void stop(Player player) {
        if (!Foxtrot.getInstance().getTournamentHandler().isCreated()) {
            player.sendMessage(ChatColor.RED + "There is currently no active event.");
            return;
        }

        if (Foxtrot.getInstance().getTournamentHandler().getTournament().getHoster() != player) {
            player.sendMessage(ChatColor.RED + "You need to be the host to cancel the event!");
            return;
        }

        final Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();
        if (tournament != null) {
            Foxtrot.getInstance().getTournamentHandler().setCreated(false);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (Foxtrot.getInstance().getTournamentHandler().isInTournament(online.getUniqueId())) {
                    tournament.rollbackInventory(online);
                    Foxtrot.getInstance().getTournamentHandler().kickPlayer(online.getUniqueId());
                    online.sendMessage(ChatColor.RED + "You have been kicked from the event because it " + ChatColor.BOLD + "CANCELED" + ChatColor.RED + "!");
                    online.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
            }
        }
    }


    @Command(names = {"tournament status", "status"}, permission = "")
    public static void status(Player player) {
        Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();
        if (tournament == null) {
            player.sendMessage(ChatColor.RED + "There isn't an active tournament");
            return;
        }
        if (tournament.getTournamentState() == TournamentState.FIGHTING) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cType&7: &f" + WordUtils.capitalizeFully(tournament.getType().name())));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRound&7: &f" + tournament.getCurrentRound()));
            if (tournament.getType() == TournamentType.SUMO) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Current Fight:"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "   &4" + tournament.getFirstPlayer().getDisplayName() + " &7vs &2" + tournament.getSecondPlayer().getDisplayName()));
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNext Round&7: &f" + (tournament.getCurrentRound() + 1)));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayers&7: &f" + tournament.getPlayers().size() + "&7/&f" + tournament.getSize()));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHoster&7: &f" + tournament.getHoster().getName()));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------"));
        } else if (tournament.getTournamentState() == TournamentState.STARTING) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4The event is Starting..."));
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEvent&7: &f(&a" + tournament.getType().toString() + "&f)"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize()));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------"));
        }
    }
}