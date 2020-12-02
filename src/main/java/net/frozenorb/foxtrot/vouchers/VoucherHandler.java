package net.frozenorb.foxtrot.vouchers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class VoucherHandler {

    private MongoCollection<Document> collection = Foxtrot.getInstance().getMongoPool().getDatabase(Foxtrot.MONGO_DB_NAME).getCollection("vouchers");

    private List<Voucher> voucher = new ArrayList<>();

    public VoucherHandler() {
        for (Document document : collection.find()) {
            voucher.add(new Voucher(document));
        }

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Foxtrot.getInstance(), this::save, (20*60)*15, (20*60)*15);
    }

    public void save() {
        for (Voucher voucher : voucher) {
            Document document = new Document();

            document.put("uuid", voucher.getUuid().toString());
            document.put("playerUuid", voucher.getPlayerUuid().toString());
            document.put("desc", voucher.getDesc());
            document.put("value", voucher.getValue());
            document.put("time", voucher.getTime());
            document.put("redeemed", voucher.isRedeemed());

            collection.replaceOne(Filters.eq("uuid", voucher.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        }
    }

    public List<Voucher> getVouchers() {
        return this.voucher;
    }

    public List<Voucher> getVouchers(UUID uuid) {
        return voucher.stream().filter(coupon -> coupon.getPlayerUuid().toString().equalsIgnoreCase(uuid.toString())).collect(Collectors.toList());
    }

}
