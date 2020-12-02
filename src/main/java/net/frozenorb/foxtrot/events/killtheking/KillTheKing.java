package net.frozenorb.foxtrot.events.killtheking;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class KillTheKing {

    private Player activeKing;
    private boolean running;
}
