package net.frozenorb.foxtrot.abilities.portablebard;

import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.abilities.portablebard.command.PortableBardCommand;
import net.frozenorb.foxtrot.abilities.portablebard.listener.Strength;
import org.bukkit.plugin.PluginManager;

public class PortableBardHandler {

    private Foxtrot plugin;

    public PortableBardHandler(Foxtrot plugin){
        this.plugin = plugin;
        this.init();
    }

    private void init(){
        PluginManager pm = Foxtrot.getInstance().getServer().getPluginManager();
        pm.registerEvents(new Strength(), plugin);
        plugin.getCommand("portable").setExecutor(new PortableBardCommand());
    }
}
