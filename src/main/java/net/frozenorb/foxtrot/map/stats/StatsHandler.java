package net.frozenorb.foxtrot.map.stats;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.kronos.helium.Helium;
import org.kronos.helium.hologram.Hologram;
import org.kronos.helium.hologram.Holograms;
import org.kronos.helium.profile.Profile;
import org.kronos.helium.util.NPC;
import net.frozenorb.foxtrot.team.commands.team.TeamTopCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.CustomTimerCreateCommand;
import net.frozenorb.foxtrot.map.stats.command.StatsTopCommand;
import net.frozenorb.foxtrot.map.stats.command.StatsTopCommand.StatsObjective;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import net.frozenorb.qlib.command.ParameterType;
import net.frozenorb.qlib.serialization.LocationSerializer;
import net.frozenorb.qlib.util.UUIDUtils;
import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.scheduler.BukkitTask;

public class StatsHandler implements Listener {

    private Map<UUID, StatsEntry> stats = Maps.newConcurrentMap();

    @Getter private Map<Location, Integer> leaderboardSigns = Maps.newHashMap();
    @Getter private Map<Location, Integer> leaderboardHeads = Maps.newHashMap();

    @Getter private Map<Location, StatsObjective> leaderboardHolos = Maps.newHashMap();
    @Getter private Map<NPC, StatsObjective> leaderboardNPCs = Maps.newHashMap();
    @Getter private List<Integer> npcTasks = new ArrayList<>();
    @Getter private Map<Location, StatsObjective> objectives = Maps.newHashMap();

    @Getter private Map<Integer, UUID> topKills = Maps.newConcurrentMap();

    @Getter private List<Hologram> activeHolograms = new ArrayList<>();

    private boolean firstUpdateComplete = false;

    public StatsHandler() {
        qLib.getInstance().runRedisCommand(redis -> {
            for (String key : redis.keys(Bukkit.getServerName() + ":" + "stats:*")) {
                UUID uuid = UUID.fromString(key.split(":")[2]);
                StatsEntry entry = qLib.PLAIN_GSON.fromJson(redis.get(key), StatsEntry.class);

                stats.put(uuid, entry);
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + stats.size() + " stats.");

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardSigns")) {
                List<String> serializedSigns = qLib.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardSigns"), new TypeToken<List<String>>() {}.getType());

                for (String sign : serializedSigns) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(sign.split("----")[0]));
                    int place = Integer.parseInt(sign.split("----")[1]);

                    leaderboardSigns.put(location, place);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + leaderboardSigns.size() + " leaderboard signs.");
            }

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardHeads")) {
                List<String> serializedHeads = qLib.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardHeads"), new TypeToken<List<String>>() {}.getType());

                for (String sign : serializedHeads) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(sign.split("----")[0]));
                    int place = Integer.parseInt(sign.split("----")[1]);

                    leaderboardHeads.put(location, place);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + leaderboardHeads.size() + " leaderboard heads.");
            }

            if (redis.exists(Bukkit.getServerName() + ":" + "objectives")) {
                List<String> serializedObjectives = qLib.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "objectives"), new TypeToken<List<String>>() {}.getType());

                for (String objective : serializedObjectives) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(objective.split("----")[0]));
                    StatsObjective obj = StatsObjective.valueOf(objective.split("----")[1]);

                    objectives.put(location, obj);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + objectives.size() + " objectives.");
            }

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardHolos")) {
                List<String> serializedHolos = qLib.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardHolos"), new TypeToken<List<String>>() {}.getType());

                for (String holo : serializedHolos) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(holo.split("----")[0]));
                    StatsObjective obj = StatsObjective.valueOf(holo.split("----")[1]);

                    //setupHologram(location, obj);
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + leaderboardHolos.size() + " leaderboard holos.");
            }

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardNPCs")) {
                List<String> serializedNPCs = qLib.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardNPCs"), new TypeToken<List<String>>() {}.getType());

                for (String holo : serializedNPCs) {
                    NPC npc = qLib.PLAIN_GSON.fromJson(holo.split("----")[0], NPC.class);
                    StatsObjective obj = StatsObjective.valueOf(holo.split("----")[1]);

                    setupNPC(npc, "");
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Foxtrot] Loaded " + leaderboardNPCs.size() + " leaderboard npcs.");
            }

            return null;
        });

        Bukkit.getPluginManager().registerEvents(this, Foxtrot.getInstance());

        FrozenCommandHandler.registerPackage(Foxtrot.getInstance(), "net.frozenorb.foxtrot.map.stats.command");

        FrozenCommandHandler.registerParameterType(StatsTopCommand.StatsObjective.class, new ParameterType<StatsTopCommand.StatsObjective>() {

            @Override
            public StatsTopCommand.StatsObjective transform(CommandSender sender, String source) {
                for (StatsTopCommand.StatsObjective objective : StatsTopCommand.StatsObjective.values()) {
                    if (source.equalsIgnoreCase(objective.getName())) {
                        return objective;
                    }

                    for (String alias : objective.getAliases()) {
                        if (source.equalsIgnoreCase(alias)) {
                            return objective;
                        }
                    }
                }

                sender.sendMessage(ChatColor.RED + "Objective '" + source + "' not found.");
                return null;
            }

            @Override
            public List<String> tabComplete(Player sender, Set<String> flags, String source) {
                List<String> completions = Lists.newArrayList();

                obj:
                for (StatsTopCommand.StatsObjective objective : StatsTopCommand.StatsObjective.values()) {
                    if (StringUtils.startsWithIgnoreCase(objective.getName().replace(" ", ""), source)) {
                        completions.add(objective.getName().replace(" ", ""));
                        continue;
                    }

                    for (String alias : objective.getAliases()) {
                        if (StringUtils.startsWithIgnoreCase(alias, source)) {
                            completions.add(alias);
                            continue obj;
                        }
                    }
                }

                return completions;
            }

        });

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Foxtrot.getInstance(), this::save, 30 * 20L, 30 * 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Foxtrot.getInstance(), this::updateTopKillsMap, 30 * 20L, 30 * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Foxtrot.getInstance(), this::updatePhysicalLeaderboards, 60 * 20L, 60 * 20L);
    }

    public void setupNPC(NPC npc, String obj) {

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {

            StatsEntry stats = get(StatsObjective.valueOf(obj), 1);
            npc.setName(StatsObjective.valueOf(obj).getName());

                if(stats != null) npc.grabSkin(UUIDUtils.name(stats.getOwner()));
                else npc.grabSkin("MHF_Question");


            npc.updateNPC();
        }, 0, 120);

        npcTasks.add(bukkitTask.getTaskId());
    }

    public void save() {
        qLib.getInstance().runRedisCommand(redis -> {
            List<String> serializedSigns = leaderboardSigns.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedHeads = leaderboardHeads.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedHolos = leaderboardHolos.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue().name()).collect(Collectors.toList());
            List<String> serializedNPCs = leaderboardNPCs.entrySet().stream().map(entry -> qLib.PLAIN_GSON.toJson(entry.getKey()) + "----" + entry.getValue().name()).collect(Collectors.toList());
            List<String> serializedObjectives = objectives.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue().name()).collect(Collectors.toList());

            redis.set(Bukkit.getServerName() + ":" + "leaderboardSigns", qLib.PLAIN_GSON.toJson(serializedSigns));
            redis.set(Bukkit.getServerName() + ":" + "leaderboardHeads", qLib.PLAIN_GSON.toJson(serializedHeads));
            redis.set(Bukkit.getServerName() + ":" + "leaderboardHolos", qLib.PLAIN_GSON.toJson(serializedHolos));
            redis.set(Bukkit.getServerName() + ":" + "leaderboardNPCs", qLib.PLAIN_GSON.toJson(serializedNPCs));
            redis.set(Bukkit.getServerName() + ":" + "objectives", qLib.PLAIN_GSON.toJson(serializedObjectives));

            // stats
            for (StatsEntry entry : stats.values()) {
                redis.set(Bukkit.getServerName() + ":" + "stats:" + entry.getOwner().toString(), qLib.PLAIN_GSON.toJson(entry));
            }
            return null;
        });
    }

    public void delete() {
        qLib.getInstance().runRedisCommand(redis -> {
            List<String> serializedHolos = leaderboardHolos.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue().name()).collect(Collectors.toList());
            redis.del(Bukkit.getServerName() + ":" + "leaderboardHolos");
            leaderboardHolos.clear();
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);

            return null;
        });
    }

    public void addHologramLeaderboard(Hologram hologram, Location location, StatsObjective objective) {
       leaderboardHolos.put(location, objective);
    }

    public void setupHologram(Location location, StatsObjective objective) {
        Hologram holo = Holograms.newHologram().at(location).updates().onUpdate(
                hologram -> {
                    List<String> lines = new ArrayList<>();
                    lines.add("§7§m---*-----------------*---");
                    lines.add("§b§l" + (objective != StatsObjective.HIGHEST_KILLSTREAK && objective != StatsObjective.TOP_FACTION  ? "Top " : "") + objective.getName());
                    lines.add("§7§m---*-----------------*---");
                    int index = 0;
                    for (Map.Entry<StatsEntry, String> entry : Foxtrot.getInstance().getMapHandler().getStatsHandler().getLeaderboards(objective, 5).entrySet()) {
                        index++;
                        lines.add(ChatColor.AQUA.toString() + "#" + index + " " + ChatColor.WHITE.toString() + (objective == StatsObjective.TOP_FACTION ? entry.getKey().getFaction() : UUIDUtils.name(entry.getKey().getOwner())) + ChatColor.GRAY + " | " + ChatColor.WHITE + entry.getValue());
                    }
                    if(index == 0) {
                        lines.add(ChatColor.RED.toString() + ChatColor.ITALIC + "No data yet...");
                    }
                    lines.add("§7§m---*-----------------*---");
                    hologram.setLines(lines);
                }
        ).interval(15, TimeUnit.SECONDS).build();
        holo.send();
        leaderboardHolos.put(location, objective);
        activeHolograms.add(holo);
    }
    public void removeHologram(int id) {
        Hologram holo = Helium.get().getHologramManager().getHolograms().get(id);
        holo.destroy();
    }

    public StatsEntry getStats(Player player) {
        return getStats(player.getUniqueId());
    }

    public StatsEntry getStats(String name) {
        return getStats(UUIDUtils.uuid(name));
    }

    public StatsEntry getStats(UUID uuid) {
        stats.putIfAbsent(uuid, new StatsEntry(uuid));
        return stats.get(uuid);
    }

    private void updateTopKillsMap() {
        UUID oldFirstPlace = this.topKills.get(1);
        UUID oldSecondPlace = this.topKills.get(2);
        UUID oldThirdPlace = this.topKills.get(3);
        UUID newFirstPlace = null;
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 1) != null) {
            newFirstPlace = this.get(StatsTopCommand.StatsObjective.KILLS, 1).getOwner();
        }
        UUID newSecondPlace = null;
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 2) != null) {
            newSecondPlace = this.get(StatsTopCommand.StatsObjective.KILLS, 2).getOwner();
        }
        UUID newThirdPlace = null;
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 3) != null) {
            newThirdPlace = this.get(StatsTopCommand.StatsObjective.KILLS, 3).getOwner();
        }
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 1) != null) {
            this.topKills.put(1, newFirstPlace);
        }
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 2) != null) {
            this.topKills.put(2, newSecondPlace);
        }
        if (this.get(StatsTopCommand.StatsObjective.KILLS, 3) != null) {
            this.topKills.put(3, newThirdPlace);
        }
        this.firstUpdateComplete = true;
    }

//        if (!CustomTimerCreateCommand.isSOTWTimer()) {
//            if (firstUpdateComplete) {
//                if (newFirstPlace != oldFirstPlace && newFirstPlace != null) {
//                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newFirstPlace) + "&f has surpassed &6" + UUIDUtils.name(oldFirstPlace) + "&f for &6#1&f in kills!"));
//                }
//
//                if (newSecondPlace != oldSecondPlace && newSecondPlace != null) {
////                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newSecondPlace) + "&f has surpassed &6" + UUIDUtils.name(oldSecondPlace) + "&f for &6#2&f in kills!"));
//                }
//
//                if (newThirdPlace != oldThirdPlace && newThirdPlace != null) {
////                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newThirdPlace) + "&f has surpassed &6" + UUIDUtils.name(oldThirdPlace) + "&f for &6#3&f in kills!"));
//                }
//            }
//        }

    public void updatePhysicalLeaderboards() {
        Iterator<Map.Entry<Location, Integer>> iterator = leaderboardSigns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Location, Integer> entry = iterator.next();

            StatsObjective statsObjective = objectives.get(entry.getKey());
            StatsEntry stats = get(statsObjective, entry.getValue());

            String optionalStat = null;

            if(statsObjective == StatsObjective.TOP_FACTION) {
                for (Map.Entry<StatsEntry, String> entriesTopFactions : Foxtrot.getInstance().getMapHandler().getStatsHandler().getLeaderboards(statsObjective, 1).entrySet()) {
                    stats = entriesTopFactions.getKey();
                    optionalStat = entriesTopFactions.getValue();
                    break;
                }
            }

            if (stats == null) {
                continue;
            }

            if (!(entry.getKey().getBlock().getState() instanceof Sign)) {
                iterator.remove();
                continue;
            }

            Sign sign = (Sign) entry.getKey().getBlock().getState();

            sign.setLine(0, trim(ChatColor.RED.toString() + ChatColor.BOLD + (beautify(entry.getKey()))));
            sign.setLine(1, trim(ChatColor.AQUA.toString() + ChatColor.UNDERLINE + (statsObjective == StatsObjective.TOP_FACTION ? stats.getFaction() : UUIDUtils.name(stats.getOwner()))));

            if(optionalStat == null) {
                sign.setLine(3, ChatColor.DARK_GRAY.toString() + stats.get(objectives.get(entry.getKey())));
            } else {
                sign.setLine(3, ChatColor.DARK_GRAY.toString() + optionalStat);
            }

            sign.update();
        }

        Iterator<Map.Entry<Location, Integer>> headIterator = leaderboardHeads.entrySet().iterator();

        while (headIterator.hasNext()) {
            Map.Entry<Location, Integer> entry = headIterator.next();

            StatsObjective statsObjective = objectives.get(entry.getKey());

            StatsEntry stats = get(statsObjective, entry.getValue());

            if (stats == null) {
                continue;
            }

            if (!(entry.getKey().getBlock().getState() instanceof Skull)) {
                headIterator.remove();
                continue;
            }

            Skull skull = (Skull) entry.getKey().getBlock().getState();

            skull.setOwner(statsObjective == StatsObjective.TOP_FACTION ? "GoldBlock" : UUIDUtils.name(stats.getOwner()));
            skull.update();
        }
    }

    private String beautify(Location location) {
        StatsObjective objective = objectives.get(location);

        switch (objective) {
            case DEATHS:
                return "Top Deaths";
            case HIGHEST_KILLSTREAK:
                return "Top Killstreak";
            case TOP_FACTION:
                return "Top Faction";
            case KD:
                return "Top KDR";
            case KILLS:
                return "Top Kills";
            default:
                return "Error";

        }
    }

    private String trim(String name) {
        return name.length() <= 15 ? name : name.substring(0, 15);
    }

    public StatsEntry get(StatsObjective objective, int place) {
        Map<StatsEntry, Number> base = Maps.newHashMap();

        for (StatsEntry entry : stats.values()) {
            base.put(entry, entry.get(objective));
        }
        Foxtrot.getInstance().getTeamHandler().getTeams().stream().filter(team -> team.getPoints() != 0).forEach(team -> base.put(new StatsEntry(team.getName()), team.getPoints()));

        TreeMap<StatsEntry, Number> ordered = new TreeMap<>((Comparator<StatsEntry>) (first, second) -> {
            if (first.get(objective).doubleValue() >= second.get(objective).doubleValue()) {
                return -1;
            }
            return 1;
        });

        ordered.putAll(base);

        Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

        int index = 0;
        for (Map.Entry<StatsEntry, Number> entry : ordered.entrySet()) {

            if (entry.getKey().getDeaths() < 10 && objective == StatsObjective.KD) {
                continue;
            }

            leaderboards.put(entry.getKey(), entry.getValue() + "");

            index++;

            if (index == place + 1) {
                break;
            }
        }

        try {
            return Iterables.get(leaderboards.keySet(), place - 1);
        } catch (Exception e) {
            return null;
        }
    }

    public void clearAll() {
        stats.clear();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
    }

    public void clearLeaderboards() {
        leaderboardHeads.clear();
        leaderboardSigns.clear();
        leaderboardHolos.clear();
        leaderboardNPCs.clear();
        objectives.clear();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
    }

    public Map<StatsEntry, String> getLeaderboards(StatsTopCommand.StatsObjective objective, int range) {
        if(objective == StatsObjective.TOP_FACTION) {
            Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

            LinkedHashMap<Team, Integer> teamsSorted = TeamTopCommand.getSortedTeams();
            int index = 0;

            for (Team team : teamsSorted.keySet()) {
                int points = teamsSorted.get(team);
                if(points > 0){

                    StatsEntry se = new StatsEntry(team.getName());
                    leaderboards.put(se, points+"");
                    index++;
                    if (index == range) {
                        break;
                    }
                }
            }
            return leaderboards;
        }
        if (objective != StatsTopCommand.StatsObjective.KD) {
            Map<StatsEntry, Number> base = Maps.newHashMap();

            stats.values().stream().filter(statsEntry -> statsEntry.get(objective).intValue() != 0).forEach(statsEntry -> base.put(statsEntry, statsEntry.get(objective)));

            TreeMap<StatsEntry, Number> ordered = new TreeMap<>((Comparator<StatsEntry>) (first, second) -> {
                if (first.get(objective).doubleValue() >= second.get(objective).doubleValue()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Number> entry : ordered.entrySet()) {
                leaderboards.put(entry.getKey(), entry.getValue() + "");

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        } else {
            Map<StatsEntry, Double> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.getKD());
            }

            TreeMap<StatsEntry, Double> ordered = new TreeMap<>((Comparator<StatsEntry>) (first, second) -> {
                if (first.getKD() > second.getKD()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Double> entry : ordered.entrySet()) {
                if (entry.getKey().getDeaths() < 10) {
                    continue;
                }

                String kd = Team.DTR_FORMAT.format((double) entry.getKey().getKills() / (double) entry.getKey().getDeaths());

                leaderboards.put(entry.getKey(), kd);

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (leaderboardHeads.containsKey(event.getBlock().getLocation())) {
            leaderboardHeads.remove(event.getBlock().getLocation());
            player.sendMessage(ChatColor.YELLOW + "Removed this skull from leaderboards.");

            Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
        }

        if (leaderboardSigns.containsKey(event.getBlock().getLocation())) {
            leaderboardSigns.remove(event.getBlock().getLocation());
            player.sendMessage(ChatColor.YELLOW + "Removed this sign from leaderboards.");

            Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
        }
    }

}
