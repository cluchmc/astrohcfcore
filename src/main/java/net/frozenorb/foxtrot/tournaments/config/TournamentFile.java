package net.frozenorb.foxtrot.tournaments.config;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class TournamentFile extends YamlConfiguration {

    private static TournamentFile config;
    private final Plugin plugin;
    private final java.io.File configFile;

    public TournamentFile() {
        this.plugin = main();
        this.configFile = new java.io.File(this.plugin.getDataFolder(), "tournament.yml");
        saveDefault();
        reload();
    }

    public static TournamentFile getConfig() {
        if (config == null) {
            config = new TournamentFile();
        }
        return config;
    }

    private Plugin main() {
        return Foxtrot.getInstance();
    }

    public void reload() {
        try {
            super.load(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        this.plugin.saveResource("tournament.yml", false);
    }
}
