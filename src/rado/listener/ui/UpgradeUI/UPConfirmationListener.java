package rado.listener.ui.UpgradeUI;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;
import rado.ciftci.farmer.Upgrade.UpgradeUI;

public class UPConfirmationListener implements Listener {

    private static Economy econ = null;

    public UPConfirmationListener() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) econ = rsp.getProvider();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;
        if (!event.getView().getTitle().equals("Yükseltiliyor...")) return;

        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        CiftciData data = CiftciManager.veriGetir(p.getLocation().getChunk());
        if (data == null) return;

        if (slot == 24) {
            UpgradeUI.open(p);
            return;
        }

        if (slot == 30) {
            ItemStack upItem = event.getInventory().getItem(16);
            if (upItem == null) return;

            String key = (upItem.getDurability() > 0 || upItem.getType() == Material.WOOL || upItem.getType() == Material.RAW_FISH)
                    ? upItem.getType().name() + ":" + upItem.getDurability() : upItem.getType().name();

            int mevcutSeviye = data.getUrunSeviye(key);
            double fiyat = 5150.0 + (mevcutSeviye * 350.0);

            if (econ != null && econ.has(p, fiyat)) {
                econ.withdrawPlayer(p, fiyat);

                int yeniSeviye = mevcutSeviye + 1;
                data.setUrunSeviye(key, yeniSeviye);
                CiftciManager.ciftciEkle(p.getLocation().getChunk(), data);

                p.sendMessage("§6TN §7§l> §aSeviye yükseldildi: §e" + yeniSeviye);
                UpgradeUI.open(p);
            } else {
                p.closeInventory();
                p.sendMessage("§6TN §7§l> §4Bunun ücretini karşılamıyorsun.");
            }
        }
    }
}