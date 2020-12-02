package net.frozenorb.foxtrot.reclaim;

import lombok.Data;

import java.util.List;

@Data
public class Group {

    private String name;
    private List<String> commands;
}
