package net.frozenorb.foxtrot.tournaments;

import lombok.Getter;
import lombok.Setter;

public enum TournamentType {

    SUMO("SUMO", 0, "Sumo"),
    DIAMOND("DIAMOND", 1, "Diamond"),
    ARCHER("ARCHER", 2, "Archer"),
    ROGUE("ROGUE", 3, "Rogue"),
    AXE("AXE", 4, "Axe");

    private String name;

    TournamentType(String s, int n, String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
