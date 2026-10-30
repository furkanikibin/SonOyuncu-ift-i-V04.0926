package rado.utils.Vault;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Economy {

    private static net.milkbowl.vault.economy.Economy vault = null;

    public static boolean setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp =
                Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (rsp == null) {
            return false;
        }

        vault = rsp.getProvider();
        return vault != null;
    }

    public static boolean buy(Player p, double miktar) {
        if (vault == null) return false;

        if (vault.has(p, miktar)) {
            EconomyResponse response = vault.withdrawPlayer(p, miktar);
            return response.transactionSuccess();
        }
        return false;
    }

    public static void deposit(Player p, double miktar) {
        if (vault != null) {
            vault.depositPlayer(p, miktar);
        }
    }

    public static boolean has(Player p, double miktar) {
        return vault != null && vault.has(p, miktar);
    }

    public static net.milkbowl.vault.economy.Economy getAPI() {
        return vault;
    }
}