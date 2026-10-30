package rado.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import rado.ciftci.ConfirmationUI;
import rado.ciftci.farmer.Farmer;
import rado.ciftci.farmer.NotActiveUI;
import rado.ciftci.farmer.Upgrade.UpgradeUI;
import rado.ciftci.database.manager.CiftciManager;
import rado.ciftci.database.data.CiftciData;
import rado.npc.CNPC;

public class MainUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;

        String title = event.getView().getTitle();

        if (title.startsWith("Çiftçi (")) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            if (slot == 5 && event.getCurrentItem().getType() == Material.DIAMOND_HOE) {
                if (CiftciManager.ciftcikontrol(p.getLocation().getChunk())) {
                    p.closeInventory();

                    CNPC.spawn(p.getLocation());

                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 4.0f);
                } else {
                    ConfirmationUI.open(p);
                }
            }

            else if (slot == 14 && event.getCurrentItem().getType() == Material.CHEST) {
                if (CiftciManager.ciftcikontrol(p.getLocation().getChunk())) {
                    Farmer.open(p);
                } else {
                    NotActiveUI.open(p);
                }
            }

            else if (slot == 18 && event.getCurrentItem().getType() == Material.FIREWORK) {
                if (CiftciManager.ciftcikontrol(p.getLocation().getChunk())) {
                    UpgradeUI.open(p);
                } else {
                    p.sendMessage("§6TN §7§l> §4Yükseltmeden önce çiftçiyi aktif etmelisin!");
                }
            }

            else if (slot == 22 && event.getCurrentItem().getType() == Material.ARROW) {
                p.closeInventory();
            }

            else if (slot == 27 && event.getCurrentItem().getType() == Material.INK_SACK) {
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 4.0f);
                CiftciData data = CiftciManager.veriGetir(p.getLocation().getChunk());
                if (data != null) {
                    data.setDurum(!data.isDurum());
                    CiftciManager.ciftciEkle(p.getLocation().getChunk(), data);
                    p.performCommand("ciftci");
                }
            }
        }
    }
}