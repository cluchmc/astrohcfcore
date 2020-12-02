package net.frozenorb.foxtrot.scoreboard;

import net.frozenorb.foxtrot.listener.ClientListener;
import net.frozenorb.foxtrot.tournaments.Tournament;
import net.frozenorb.foxtrot.tournaments.TournamentState;
import net.frozenorb.foxtrot.tournaments.TournamentType;
import net.frozenorb.foxtrot.tournaments.handler.TournamentHandler;
import net.frozenorb.foxtrot.util.DurationFormatter;
import org.kronos.helium.Helium;
import org.kronos.helium.profile.Profile;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.CustomTimerCreateCommand;
import net.frozenorb.foxtrot.commands.EOTWCommand;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.conquest.game.ConquestGame;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.listener.GoldenAppleListener;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ArcherClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.BardClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ScoutClass;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.server.conditional.staff.ModHandler;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.commands.team.TeamStuckCommand;
import net.frozenorb.foxtrot.util.Logout;
import net.frozenorb.qlib.autoreboot.AutoRebootHandler;
import net.frozenorb.qlib.scoreboard.ScoreFunction;
import net.frozenorb.qlib.scoreboard.ScoreGetter;
import net.frozenorb.qlib.util.LinkedList;
import net.frozenorb.qlib.util.TimeUtils;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class FoxtrotScoreGetter implements ScoreGetter {

    public void getScores(LinkedList<String> scores, Player player) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        String spawnTagScore = getSpawnTagScore(player);
        String enderpearlScore = getEnderpearlScore(player);
        String pvpTimerScore = getPvPTimerScore(player);
        String archerMarkScore = getArcherMarkScore(player);
        String bardEffectScore = getBardEffectScore(player);
        String bardEnergyScore = getBardEnergyScore(player);
        String scoutSpeedScore = getScoutSpeedScore(player);
        String scoutGrapplingScore = getScoutGrapplingScore(player);
        String fstuckScore = getFStuckScore(player);
        String logoutScore = getLogoutScore(player);
        String homeScore = getHomeScore(player);
        String appleScore = getAppleScore(player);

        TournamentHandler tournamentManager = Foxtrot.getInstance().getTournamentHandler();
        Tournament tournament = Foxtrot.getInstance().getTournamentHandler().getTournament();

        String deathbanScore = getDeathbanScore(player);

        if (Setting.SCOREBOARD_STAFF_BOARD.isEnabled(player) && ModHandler.isModMode(player)) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            Helium Helium = org.kronos.helium.Helium.get();
            String chat = "&ePublic";
            if (profile.getStaffOptions().adminChatModeEnabled()) chat = "&cAdmin";
            if (profile.getStaffOptions().staffChatModeEnabled()) chat = "&9Staff";
            if (Helium.getChat().isPublicChatDelayed()) chat = "&ePublic &7(" + Helium.getChat().getDelayTime() + ")";
            if (Helium.getChat().isPublicChatMuted()) chat = "&ePublic &7(Muted)";
            scores.add("&4\u00BB &fVanish&7: " + (ModHandler.isVanished(player) ? "&aEnabled" : "&cDisabled"));
            //scores.add("&4\u00BB &fPlayers&7: &a" + Bukkit.getOnlinePlayers().size());
            scores.add("&4\u00BB &fChat&7: " + chat);
        }

        String sectionColor = Foxtrot.getInstance().getServerHandler().getSbSectionColor();
        String infoColor = Foxtrot.getInstance().getServerHandler().getSbTimeColor();

        if (Foxtrot.getInstance().getMapHandler().isKitMap() || Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());
            if (!ModHandler.isModMode(player)) {
                scores.add("&4&lKills&7: &f" + stats.getKills() + " " + (stats.getKD() > 0 ? "&7(&c" + Team.DTR_FORMAT.format(stats.getKD()) + "&7)" : ""));
                if (stats.getKillstreak() > 0) {
                    scores.add("&4&lKillstreak&7: &f" + stats.getKillstreak());
                }
                scores.add("&4&lDeaths&7: &f" + stats.getDeaths());
                //scores.add("&4&lBalance&7: " + infoColor + "$" + FrozenEconomyHandler.getBalance(player.getUniqueId()));
            }
        }

        if (spawnTagScore != null) {
            scores.add("&c&lSpawn Tag&7: &c" + spawnTagScore);
        }

        if (homeScore != null) {
            scores.add("&9&lHome§7: &9" + homeScore);
        }

        if (appleScore != null) {
            scores.add("&6&lApple&7: **&6" + appleScore);
        }

        if (enderpearlScore != null) {
            scores.add("&e&lEnderpearl&7: &c" + enderpearlScore);
        }


        if (team != null){
            if (team.getFactionFocused() != null){
                Team focusedTeam = team.getFactionFocused();
                scores.add("&4&c&f");
                scores.add("&4&lTeam&7: &f" + focusedTeam.getName());
                scores.add("&4&lDTR&7: &f" + ClientListener.getDTRColor(focusedTeam) + Team.DTR_FORMAT.format(focusedTeam.getDTR()) + ClientListener.getDTRSuffix(focusedTeam));
                scores.add("&4&lOnline&7: &f" + focusedTeam.getOnlineMemberAmount());
            }
        }

        if (Foxtrot.getInstance().getKillTheKing() != null){
            Player king = Foxtrot.getInstance().getKillTheKing().getActiveKing();
            scores.add("&4&lKill The King");
            scores.add(" &cKing&7: &f " + king.getName());
//          scores.add(" &cHealth&7: &f " + Math.round(king.getHealth()) / 2 + "&4❤");
            scores.add(" &cLocation:&f " + king.getLocation().getBlockX() + ", " + king.getLocation().getBlockZ());
        }

        if (tournamentManager.isInTournament(player) && !player.hasMetadata("frozen")) {
            int announceCountdown = tournament.getDesecrentAnn();
            scores.add("&4&l" + tournament.getType().getName() + " Event");
            if (tournament.getType() == TournamentType.SUMO) {
                scores.add(" &cPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                if (announceCountdown > 0) {
                    scores.add(" &cRound Begins&7: &f" + announceCountdown + "s");
                }
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    scores.add(" &cState&7: &fWaiting...");
                } else if (tournament.getTournamentState() == TournamentState.FIGHTING) {
                    scores.add(" &cState&7: &fFighting...");
                } else {
                    scores.add(" &cState&7: &fSelecting...");
                }
            } else if (tournament.getType() == TournamentType.DIAMOND ||
                    tournament.getType() == TournamentType.AXE ||
                    tournament.getType() == TournamentType.ARCHER ||
                    tournament.getType() == TournamentType.ROGUE) {
                scores.add(" &cPlayers&7: &f" + tournament.getPlayers().size() + "/" + tournament.getSize());
                if (announceCountdown > 0) {
                    scores.add(" &cFFA Begins&7: &f" + announceCountdown + "s");
                }
                if (tournament.getTournamentState() == TournamentState.WAITING) {
                    scores.add(" &cState&7: &fWaiting...");
                } else if (tournament.isActiveProtection()) {
                    scores.add(" &cState&7: &fImmune");
                    scores.add(" &cImmunity&7: &f" + (tournament.getProtection() / 1000) + "s");
                } else {
                    scores.add(" &cState&7: &fFighting...");
                    scores.add(" &cImmunity&7: &fDeactivated");
                }
            }
        }

        if (deathbanScore != null) {
            scores.add("&c&lDeathban&7: &c" + deathbanScore);
        } else if (pvpTimerScore != null) {
            if (Foxtrot.getInstance().getStartingPvPTimerMap().get(player.getUniqueId())) {
                scores.add("&a&lStarting Timer&7: &c" + pvpTimerScore);
            } else {
                scores.add("&a&lPvP Timer&7: &c" + pvpTimerScore);
            }
        }

        Iterator<Map.Entry<String, Long>> iterator = CustomTimerCreateCommand.getCustomTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> timer = iterator.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                iterator.remove();
                continue;
            }

            if (timer.getKey().equals("&4&lSOTW")) {
                if (CustomTimerCreateCommand.hasSOTWEnabled(player.getUniqueId())) {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&4&l&mSOTW &4&mends in &4&l&m" + getTimerScore(timer)));
                } else {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&4&lSOTW &4ends in &4&l" + getTimerScore(timer)));
                }
            } else if (timer.getKey().equals("&4&lSale")) {
                scores.add(ChatColor.translateAlternateColorCodes('&', "&4&lSale &4ends in &4&l" + getTimerScore(timer)));
            } else {
                scores.add(ChatColor.translateAlternateColorCodes('&', timer.getKey()) + "&7: &c" + getTimerScore(timer));
            }
        }

        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (!event.isActive() || event.isHidden()) {
                continue;
            }

            String displayName;

            switch (event.getName()) {
                case "EOTW":
                    displayName = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW";
                    break;
                case "Citadel":
                    displayName = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Citadel";
                    break;
                case "Biohazard":
                    displayName = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Biohazard";
                    break;
                case "Mars":
                    displayName = ChatColor.GOLD.toString() + ChatColor.BOLD + "Mars";
                    break;
                default:
                    displayName = ChatColor.BLUE.toString() + ChatColor.BOLD + event.getName();
                    break;
            }

            if (event.getType() == EventType.DTC) {
                scores.add(displayName + "&7: &c" + ((DTC) event).getCurrentPoints());
            } else {
                scores.add(displayName + "&7: " + (event.getName() == "Mars" ? "&6" : "&c") + ScoreFunction.TIME_SIMPLE.apply((float) ((KOTH) event).getRemainingCapTime()));
            }
        }

        if (EOTWCommand.isFfaEnabled()) {
            long ffaEnabledAt = EOTWCommand.getFfaActiveAt();
            if (System.currentTimeMillis() < ffaEnabledAt) {
                long difference = ffaEnabledAt - System.currentTimeMillis();
                scores.add("&4&lFFA&7: &c" + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            }
        }


        if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)){
            scores.add("&d&lPartner Item&7:&c " + DurationFormatter.getRemaining(Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player), true, true));
        }

        if (Foxtrot.getInstance().getRampageHandler().isRampageActive()) {
            scores.add("&c&lRampage Active");
        }

        if (archerMarkScore != null) {
            scores.add("&6&lArcher Mark&7: &c" + archerMarkScore);
        }

        if (bardEffectScore != null) {
            scores.add("&a&lBard Effect&7: &c" + bardEffectScore);
        }

        if (bardEnergyScore != null) {
            scores.add("&b&lBard Energy&7: &c" + bardEnergyScore);
        }

        if (scoutSpeedScore != null) {
            scores.add("&a&lScout Speed&7: &c" + scoutSpeedScore);
        }

        if (scoutGrapplingScore != null) {
            scores.add("&6&lScout Grapple&7: &c" + scoutGrapplingScore);
        }

        if (fstuckScore != null) {
            scores.add("&4&lStuck&7: &c" + fstuckScore);
        }

        if (logoutScore != null) {
            scores.add("&4&lLogout&7: &c" + logoutScore);
        }

        ConquestGame conquest = Foxtrot.getInstance().getConquestHandler().getGame();

        if (conquest != null) {
            if (scores.size() != 0) {
                scores.add("&c&7&m--------------------");
            }

            scores.add("&6&lConquest:");
            int displayed = 0;

            for (Map.Entry<ObjectId, Integer> entry : conquest.getTeamPoints().entrySet()) {
                Team resolved = Foxtrot.getInstance().getTeamHandler().getTeam(entry.getKey());

                if (resolved != null) {
                    scores.add("  " + resolved.getName(player) + "&7: &f" + entry.getValue());
                    displayed++;
                }

                if (displayed == 3) {
                    break;
                }
            }

            if (displayed == 0) {
                scores.add("  &7No scores yet");
            }
        }

        if (AutoRebootHandler.isRebooting()) {
            scores.add("&4&lRebooting: " + TimeUtils.formatIntoMMSS(AutoRebootHandler.getRebootSecondsRemaining()));
        }

        if (!scores.isEmpty()) {
            // 'Top' and bottom.
            scores.addFirst("&a&7&m--------------------");
            scores.add("&c&l");
            scores.add("&7&oastro.rip");
            scores.add("&b&7&m--------------------");
        }
    }

    public String getDeathbanScore(Player player) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
            float diff = Foxtrot.getInstance().getDeathbanMap().getDeathban(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getAppleScore(Player player) {
        if (GoldenAppleListener.getCrappleCooldown().containsKey(player.getUniqueId()) && GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) >= System.currentTimeMillis()) {
            float diff = GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getHomeScore(Player player) {
        if (ServerHandler.getHomeTimer().containsKey(player.getName()) && ServerHandler.getHomeTimer().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = ServerHandler.getHomeTimer().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getFStuckScore(Player player) {
        if (TeamStuckCommand.getWarping().containsKey(player.getName())) {
            float diff = TeamStuckCommand.getWarping().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getLogoutScore(Player player) {
        Logout logout = ServerHandler.getTasks().get(player.getName());

        if (logout != null) {
            float diff = logout.getLogoutTime() - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getSpawnTagScore(Player player) {
        if (SpawnTagHandler.isTagged(player)) {
            float diff = SpawnTagHandler.getTag(player);

            if (diff >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getEnderpearlScore(Player player) {
        if (EnderpearlCooldownHandler.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getPvPTimerScore(Player player) {
        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            int secondsRemaining = Foxtrot.getInstance().getPvPTimerMap().getSecondsRemaining(player.getUniqueId());

            if (secondsRemaining >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply((float) secondsRemaining));
            }
        }

        return (null);
    }

    public String getTimerScore(Map.Entry<String, Long> timer) {
        long diff = timer.getValue() - System.currentTimeMillis();

        if (diff > 0) {
            return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
        } else {
            return (null);
        }
    }

    public String getArcherMarkScore(Player player) {
        if (ArcherClass.isMarked(player)) {
            long diff = ArcherClass.getMarkedPlayers().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEffectScore(Player player) {
        if (BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = BardClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEnergyScore(Player player) {
        if (BardClass.getEnergy().containsKey(player.getName())) {
            float energy = BardClass.getEnergy().get(player.getName());

            if (energy > 0) {
                // No function here, as it's a "raw" value.
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }

    public String getScoutSpeedScore(Player player) {
        if (ScoutClass.getLastSpeedUsage().containsKey(player.getName())) {
            float diff = ScoutClass.getLastSpeedUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getScoutGrapplingScore(Player player) {
        if (ScoutClass.getLastGrapplingUsage().containsKey(player.getName())) {
            float diff = ScoutClass.getLastGrapplingUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

}