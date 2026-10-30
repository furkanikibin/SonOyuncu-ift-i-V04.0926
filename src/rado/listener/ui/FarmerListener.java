package rado.listener.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;
import rado.ciftci.events.PlayerSellEvent;
import rado.listener.Fixer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FarmerListener implements Listener {

    private final Map<UUID, Long> stokMesajCooldown = new HashMap<>();
    private static final Map<UUID, Long> verifyHafizasi = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void tiklama(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;

        String baslik = event.getView().getTitle();

        if (baslik.equals("Çiftçi Deposu")) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();


            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) return;

            if (Fixer.hizliMi(p)) return;

            CiftciData veri = CiftciManager.veriGetir(p.getLocation().getChunk());
            if (veri == null) return;

            short dur = item.getDurability();
            String matName = item.getType().name();
            String key = (dur > 0 || matName.equals("WOOL") || matName.equals("RAW_FISH"))
                    ? matName + ":" + dur : matName;

            int miktar = veri.miktarAl(key);
            long simdi = System.currentTimeMillis();

            if (miktar <= 0) {
                if (stokMesajCooldown.getOrDefault(p.getUniqueId(), 0L) < simdi) {
                    p.sendMessage("§6TN §7§l> §4Hiç stok yok.");
                    stokMesajCooldown.put(p.getUniqueId(), simdi + 3000);
                }
                return;
            }

            if (event.getClick() == ClickType.LEFT) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage("§cEnvanterinde yer yok!");
                    return;
                }
                int cekilecek = Math.min(miktar, 64);
                veri.miktarSet(key, miktar - cekilecek);
                p.getInventory().addItem(new ItemStack(item.getType(), cekilecek, dur));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                p.sendMessage("§6TN §7§l> §aToplam §6" + cekilecek + " §aadet aldın.");
            }
            else if (event.getClick() == ClickType.SHIFT_LEFT) {
                int toplamCekilen = 0;
                int anlikMiktar = miktar;
                for (int i = 0; i < 36; i++) {
                    if (anlikMiktar <= 0) break;
                    ItemStack slot = p.getInventory().getItem(i);
                    if (slot == null || slot.getType() == Material.AIR) {
                        int eklenecek = Math.min(anlikMiktar, 64);
                        p.getInventory().setItem(i, new ItemStack(item.getType(), eklenecek, dur));
                        anlikMiktar -= eklenecek;
                        toplamCekilen += eklenecek;
                    }
                }
                if (toplamCekilen > 0) {
                    veri.miktarSet(key, miktar - toplamCekilen);
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    p.sendMessage("§6TN §7§l> §aToplam §6" + toplamCekilen + " §aadet aldın.");
                } else {
                    p.sendMessage("§cEnvanterinde boş yer yok!");
                }
            }
            else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                PlayerSellEvent sellEvent = new PlayerSellEvent(p, key, miktar);
                Bukkit.getPluginManager().callEvent(sellEvent);
                veri.miktarSet(key, 0);
                p.updateInventory();
            }
        }
    }

    private boolean pinGerekiyorMu(Player p) {
        if (!verifyHafizasi.containsKey(p.getUniqueId())) return true;
        long sonOnay = verifyHafizasi.get(p.getUniqueId());
        return (System.currentTimeMillis() - sonOnay) > 300000;
    }

    public static void pinOnaylandi(Player p) {
        verifyHafizasi.put(p.getUniqueId(), System.currentTimeMillis());
    }
}