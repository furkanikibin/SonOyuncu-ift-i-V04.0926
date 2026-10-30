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
import rado.ciftci.farmer.Upgrade.UpConfirmationUI;

public class UpgradeUIListener implements Listener {

    private static Economy econ = null;

    public UpgradeUIListener() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) econ = rsp.getProvider();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getView().getTitle() == null) return;

        String title = event.getView().getTitle();
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();

        if (title.startsWith("Çiftçi Yükseltme (")) {
            event.setCancelled(true);
            if (item == null || item.getType() == Material.AIR) return;

            if (slot == 77) {
                p.performCommand("ciftci");
                return;
            }

            if (slot == 5) return;

            if (slot >= 23 && slot <= 68) {
                CiftciData data = CiftciManager.veriGetir(p.getLocation().getChunk());
                if (data == null) return;

                String key = (item.getDurability() > 0 || item.getType() == Material.WOOL || item.getType() == Material.RAW_FISH)
                        ? item.getType().name() + ":" + item.getDurability() : item.getType().name();

                int mevcutSeviye = data.getUrunSeviye(key);

                if (mevcutSeviye >= 50) {
                    p.sendMessage("§6TN §7§l> §4Daha fazla yükseltme yapamazsın.");
                    return;
                }

                UpConfirmationUI.open(p, item.getType(), item.getDurability(), mevcutSeviye);
            }
        }
    }
}