package rado.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rado.Main;
import rado.ciftci.events.PlayerSellEvent;
import rado.utils.Vault.Economy;

public class Sell implements Listener {

    @EventHandler
    public void onFarmerSell(PlayerSellEvent event) {
        Player p = event.getPlayer();
        String key = event.getItemKey();
        int miktar = event.getMiktar();

        double birimFiyat = Main.getPlugin(Main.class).getConfig().getDouble("fiyatlar." + key, 0.0);

        if (birimFiyat <= 0) return;

        double hamKazanc = miktar * birimFiyat;
        double komisyon = hamKazanc * 0.25;
        double netKazanc = hamKazanc - komisyon;

        if (netKazanc > 0) {
            Economy.deposit(p, netKazanc);

            p.sendMessage("§6TN §7§l> §eToplam §6" + miktar + " §eürün satıldı.");
            p.sendMessage("§6TN §7§l> §eÇiftçi kendine §6" + String.format("%.1f", komisyon).replace(".", ",") + " Kinas §epay aldı.");
            p.sendMessage("§6TN §7§l> §6+" + String.format("%.1f", netKazanc).replace(".", ",") + " Kinas");
        }
    }
}