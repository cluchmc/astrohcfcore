package net.frozenorb.foxtrot.abilities;

import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.abilities.listeners.*;
import org.bukkit.plugin.PluginManager;

public class AbilityHandler {

    private Foxtrot plugin;

    public AbilityHandler(Foxtrot plugin){
        this.plugin = plugin;
        init();
    }

    private void init(){
        PluginManager pm = Foxtrot.getInstance().getServer().getPluginManager();
        pm.registerEvents(new Switcher(), plugin);
        pm.registerEvents(new Paralyser(), plugin);
        pm.registerEvents(new FakePearl(), plugin);
        pm.registerEvents(new RageSoup(), plugin);
        pm.registerEvents(new NinjaStar(), plugin);
        pm.registerEvents(new PotChecker(), plugin);
        pm.registerEvents(new LifeSaver(), plugin);
        pm.registerEvents(new TrickorTreat(), plugin);
    }
}
