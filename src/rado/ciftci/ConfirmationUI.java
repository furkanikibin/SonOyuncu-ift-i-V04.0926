package rado.ciftci;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationUI {

    public static void open(Player player) {
        String title = "Aktif ediliyor...";
        Inventory gui = Bukkit.getServer().createSingleInventory(player, title, 44);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, glass);
        }

        ItemStack cancel = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName("§cVazgeçtim");
            List<String> lore = new ArrayList<>();
            lore.add("§6Kararımdan vazgeçtim.");
            cancelMeta.setLore(lore);
            cancel.setItemMeta(cancelMeta);
        }
        gui.setItem(24, cancel);

        ItemStack ciftci = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta ciftciMeta = ciftci.getItemMeta();
        if (ciftciMeta != null) {
            ciftciMeta.setDisplayName("§eÇiftçi");
            List<String> lore = new ArrayList<>();
            lore.add("§7Fiyat: §65.000,0 Kinas");
            ciftciMeta.setLore(lore);
            ciftci.setItemMeta(ciftciMeta);
        }
        gui.setItem(16, ciftci);

        ItemStack confirm = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName("§aOnaylıyorum");
            List<String> lore = new ArrayList<>();
            lore.add("§6Aktif etmeyi kabul ediyorum");
            lore.add("§6ve paramın geri iade");
            lore.add("§6edilmeyeceğini biliyorum.");
            confirmMeta.setLore(lore);
            confirm.setItemMeta(confirmMeta);
        }
        gui.setItem(30, confirm);

        player.openInventory(gui);
    }
}