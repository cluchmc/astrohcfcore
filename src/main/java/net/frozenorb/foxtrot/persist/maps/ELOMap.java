package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class ELOMap extends PersistMap<Integer> {

    public ELOMap() {
        super("ELO", "ELO");
    }

    @Override
    public String getRedisValue(Integer elo) {
        return (String.valueOf(elo));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public Object getMongoValue(Integer elo) {
        return (elo);
    }

    public int getELO(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setELO(UUID update, int elo) {
        updateValueAsync(update, elo);
    }
}
