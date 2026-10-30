package rado.listener;

import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import rado.ciftci.database.manager.CiftciManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fixer implements Listener {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, Long> mesajCooldowns = new HashMap<>();

    public static boolean hizliMi(Player p) {
        long simdi = System.currentTimeMillis();
        long sonTiklama = cooldowns.getOrDefault(p.getUniqueId(), 0L);

        if (simdi - sonTiklama < 1000) {
            long sonMesaj = mesajCooldowns.getOrDefault(p.getUniqueId(), 0L);

            if (simdi - sonMesaj >= 2000) {
                p.sendMessage("§6TN §7§l> §4Çok hızlı tıklıyorsun!");
                mesajCooldowns.put(p.getUniqueId(), simdi);
            }
            return true;
        }

        cooldowns.put(p.getUniqueId(), simdi);
        return false;
    }

    public static boolean uygunMu(Player p) {
        if (hizliMi(p)) return false;

        Chunk chunk = p.getLocation().getChunk();
        Claim claim = ClaimHandler.getByLocation(p.getLocation());

        if (claim == null) {
            p.sendMessage("§6TN §7§l> §4Çiftçiyi sadece yönetim bölgesinde biryere taşıyabilirsin.");
            return false;
        }

        if (CiftciManager.ciftcikontrol(chunk)) {
            p.sendMessage("§6TN §7§l> §4Bu bölgede zaten bir çiftçi var.");
            return false;
        }

        return true;
    }
}