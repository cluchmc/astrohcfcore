package net.frozenorb.foxtrot.vouchers;

import lombok.Getter;
import lombok.Setter;

import org.bson.Document;
import java.util.UUID;

@Getter @Setter
public class Voucher {

    private UUID uuid;
    private UUID playerUuid;
    private String desc;
    private double value;
    private long time;
    private boolean redeemed;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public String getDesc() {
        return this.desc;
    }

    public double getValue() {
        return this.value;
    }

    public long getTime() {
        return this.time;
    }

    public boolean isRedeemed() {
        return this.redeemed;
    }

    public Voucher(Document document){
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.playerUuid = UUID.fromString(document.getString("playerUuid"));
        this.desc = document.getString("desc");
        this.value = document.getDouble("value");
        this.time = document.getLong("time");
        this.redeemed = document.getBoolean("redeemed");
    }

    public Voucher(UUID playerUuid, String desc, double value) {
        this.uuid = UUID.randomUUID();
        this.playerUuid = playerUuid;
        this.desc = desc;
        this.value = value;
        this.time = System.currentTimeMillis();
        this.redeemed = false;
    }
}

