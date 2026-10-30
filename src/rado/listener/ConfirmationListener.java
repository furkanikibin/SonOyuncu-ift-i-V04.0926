package rado.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import rado.ciftci.ConfirmationUI;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;
import rado.npc.CNPC;
import rado.utils.ConfirmationMSG;
import rado.utils.Vault.Economy;

public class ConfirmationListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;

        String title = event.getView().getTitle();
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        if (title.equals("Aktif ediliyor...")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            if (slot == 24) {
                p.closeInventory();
                ConfirmationMSG.sendCancel(p);
            }
            else if (slot == 30) {
                p.closeInventory();

                if (!Fixer.uygunMu(p)) return;

                if (Economy.buy(p, 5000.0)) {
                    CiftciManager.ciftciEkle(p.getLocation().getChunk(), new CiftciData());

                    CNPC.spawn(p.getLocation());

                    ConfirmationMSG.sendSuccess(p);
                } else {
                    ConfirmationMSG.sendNoMoney(p);
                }
            }
        }
    }
}