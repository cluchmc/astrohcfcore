package net.frozenorb.foxtrot.reclaim;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReclaimHandler {

    public List<Group> getGroups(){
        List<Group> groups = new ArrayList<Group>();
        for (String s : Foxtrot.getInstance().getReclaimConfig().getConfiguration().getConfigurationSection("groups").getKeys(false)){
            Group group = new Group();
            group.setName(s);
            group.setCommands(Foxtrot.getInstance().getReclaimConfig().getConfiguration().getStringList("groups." + s + ".commands"));
            groups.add(group);
        }
        return groups;
    }

    public boolean hasReclaimed(Player player){
        return Foxtrot.getInstance().getReclaimConfig().getConfiguration().getBoolean("reclaimed." + player.getUniqueId().toString());
    }

    public void setUsedReclaim(Player p, boolean used){
        Foxtrot.getInstance().getReclaimConfig().getConfiguration().set("reclaimed." + p.getUniqueId().toString(), used);
        Foxtrot.getInstance().getReclaimConfig().save();
    }
}
