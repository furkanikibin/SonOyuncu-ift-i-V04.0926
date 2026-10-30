package rado.ciftci.farmer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NotActiveUI {

    public static void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, 45, "Çiftçi Deposu");

        p.openInventory(gui);
    }
}