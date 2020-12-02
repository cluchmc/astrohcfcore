package net.frozenorb.foxtrot.util;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.UUID;

public class DiscordLogger {
    private Foxtrot foxtrot;

    public DiscordLogger(Foxtrot foxtrot) { this.foxtrot = foxtrot; }


    public void logRefund(String refunded, String refunder, String reason){
        UUID uuid = Bukkit.getPlayer(refunded).getUniqueId();

        Webhook webhook = new Webhook("https://discordapp.com/api/webhooks/765286308657496094/GC82v5d3FEYo8TXRj_sNC7KQrLo58v9U-hFkK89b2yDn1e2jyB8eg4CmQgOnw7K-0qn7");
        webhook.addEmbed(new Webhook.EmbedObject()
                .setAuthor("Refund", null, null)
                .setColor(Color.RED)
                .addField("Player Refunded", refunded, false)
                .addField("Refunded by", refunder, false)
                .addField("Reason", reason, true)

        );
    }

}
