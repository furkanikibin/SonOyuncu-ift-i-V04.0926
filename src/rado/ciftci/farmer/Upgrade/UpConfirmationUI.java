package rado.ciftci.farmer.Upgrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpConfirmationUI {

    public static void open(Player p, Material mat, short dur, int mevcutSeviye) {
        String title = "Yükseltiliyor...";
        Inventory gui = Bukkit.getServer().createSingleInventory(p, title, 44);

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

        double fiyat = 5150.0 + (mevcutSeviye * 350.0);

        ItemStack upItem = new ItemStack(mat, 1, dur);
        ItemMeta upMeta = upItem.getItemMeta();
        if (upMeta != null) {
            List<String> lore = new ArrayList<>();
            lore.add("§7Fiyat: §6" + String.format("%.1f", fiyat).replace(".", ",") + " Kinas");
            upMeta.setLore(lore);
            upItem.setItemMeta(upMeta);
        }
        gui.setItem(16, upItem);

        ItemStack confirm = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName("§aOnaylıyorum");
            List<String> lore = new ArrayList<>();
            lore.add("§6Satın almayı onaylıyorum");
            lore.add("§6ve paramın geri iade");
            lore.add("§6edilmeyeceğini biliyorum.");
            confirmMeta.setLore(lore);
            confirm.setItemMeta(confirmMeta);
        }
        gui.setItem(30, confirm);

        p.openInventory(gui);
    }
}