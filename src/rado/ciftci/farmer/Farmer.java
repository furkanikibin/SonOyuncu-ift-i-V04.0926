package rado.ciftci.farmer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import rado.Main;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Farmer {

    private static final Map<String, Map<String, Integer>> sonStoklar = new HashMap<>();

    public static void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, 45, "Çiftçi Deposu");
        CiftciData data = CiftciManager.veriGetir(p.getLocation().getChunk());

        if (data == null) return;

        Map<String, Integer> oyuncuStok = sonStoklar.computeIfAbsent(p.getName(), k -> new HashMap<>());
        setItems(gui, data, "§7Üretim: §cHesaplanıyor...");
        p.openInventory(gui);

        new BukkitRunnable() {
            int frame = 0;

            @Override
            public void run() {
                if (p.getOpenInventory() == null || !p.getOpenInventory().getTitle().equals("Çiftçi Deposu")) {
                    this.cancel();
                    return;
                }

                String dots;
                if (frame == 0) dots = "...";
                else if (frame == 1) dots = "..";
                else dots = ".";

                frame = (frame + 1) % 3;

                for (int i = 0; i < gui.getSize(); i++) {
                    ItemStack item = gui.getItem(i);
                    if (item == null || item.getType() == Material.AIR) continue;

                    ItemMeta meta = item.getItemMeta();
                    if (meta == null) continue;

                    short dur = item.getDurability();
                    String matName = item.getType().name();
                    String key = (dur > 0 || matName.equals("WOOL") || matName.equals("RAW_FISH"))
                            ? matName + ":" + dur : matName;

                    int guncelStok = data.miktarAl(key);
                    int urunKapasite = data.getUrunKapasite(key);

                    if (!oyuncuStok.containsKey(key)) oyuncuStok.put(key, guncelStok);
                    int eskiStok = oyuncuStok.get(key);

                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add("§7Mevcut stok: §c" + guncelStok + "§6/" + urunKapasite);

                    if (guncelStok > eskiStok) {
                        int fark = guncelStok - eskiStok;
                        lore.add("§7Ortalama Dakikalık Üretim: §a" + (fark * 60));
                        lore.add("§7Ortalama Saatlik Üretim: §a" + (fark * 3600));
                        lore.add("§7Ortalama Günlük Üretim: §a" + (fark * 86400));
                    } else {
                        lore.add("§7Üretim: §cHesaplanıyor" + dots);
                    }

                    lore.add("");
                    lore.add("§e1 Stack almak için [Sol Tık]");
                    lore.add("§eEnvanterini doldurmak için [Shift + Sol Tık]");
                    lore.add("§eHepsini satmak [Shift + Sağ Tık]");
                    lore.add("");
                    lore.add("§dÖnemli Not: Shift + Sağ Tık'ı kullanarak");
                    lore.add("§dhepsini markete satarsan, çiftçi satılan");
                    lore.add("§düründen §c%25 §dkendine pay alır.");

                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.setAmount(Math.max(1, Math.min(guncelStok, 64)));
                    gui.setItem(i, item);
                }
                p.updateInventory();
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    private static void setItems(Inventory gui, CiftciData data, String uretimText) {
        gui.setItem(0, createItem(data, Material.MELON, (short) 0, uretimText));
        gui.setItem(1, createItem(data, Material.CACTUS, (short) 0, uretimText));
        gui.setItem(2, createItem(data, Material.PUMPKIN, (short) 0, uretimText));
        gui.setItem(3, createItem(data, Material.PRISMARINE_SHARD, (short) 0, uretimText));
        gui.setItem(4, createItem(data, Material.PRISMARINE_CRYSTALS, (short) 0, uretimText));
        gui.setItem(5, createItem(data, Material.RAW_FISH, (short) 0, uretimText));
        gui.setItem(6, createItem(data, Material.RAW_FISH, (short) 1, uretimText));
        gui.setItem(7, createItem(data, Material.RAW_FISH, (short) 3, uretimText));
        gui.setItem(8, createItem(data, Material.COOKED_FISH, (short) 0, uretimText));
        gui.setItem(9, createItem(data, Material.COOKED_FISH, (short) 1, uretimText));
        gui.setItem(10, createItem(data, Material.COOKED_CHICKEN, (short) 0, uretimText));
        gui.setItem(11, createItem(data, Material.COOKED_MUTTON, (short) 0, uretimText));
        gui.setItem(12, createItem(data, Material.COOKED_BEEF, (short) 0, uretimText));
        gui.setItem(13, createItem(data, Material.LEATHER, (short) 0, uretimText));
        gui.setItem(14, createItem(data, Material.FEATHER, (short) 0, uretimText));
        gui.setItem(15, createItem(data, Material.STRING, (short) 0, uretimText));
        gui.setItem(16, createItem(data, Material.SPIDER_EYE, (short) 0, uretimText));
        gui.setItem(17, createItem(data, Material.BONE, (short) 0, uretimText));
        gui.setItem(18, createItem(data, Material.ARROW, (short) 0, uretimText));
        gui.setItem(19, createItem(data, Material.ROTTEN_FLESH, (short) 0, uretimText));
        gui.setItem(20, createItem(data, Material.IRON_INGOT, (short) 0, uretimText));
        for (int i = 0; i <= 15; i++) {
            gui.setItem(21 + i, createItem(data, Material.WOOL, (short) i, uretimText));
        }
    }

    private static ItemStack createItem(CiftciData data, Material material, short durability, String uretimText) {
        String key = (durability > 0 || material == Material.WOOL || material == Material.RAW_FISH)
                ? material.name() + ":" + durability : material.name();

        int stock = data.miktarAl(key);
        int urunKapasite = data.getUrunKapasite(key);

        ItemStack item = new ItemStack(material, Math.max(1, Math.min(stock, 64)), durability);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Mevcut stok: §c" + stock + "§6/" + urunKapasite);
            lore.add(uretimText);
            lore.add("");
            lore.add("§e1 Stack almak için [Sol Tık]");
            lore.add("§eEnvanterini doldurmak için [Shift + Sol Tık]");
            lore.add("§eHepsini satmak [Shift + Sağ Tık]");
            lore.add("");
            lore.add("§dÖnemli Not: Shift + Sağ Tık'ı kullanarak");
            lore.add("§dhepsini markete satarsan, çiftçi satılan");
            lore.add("§düründen §c%25 §dkendine pay alır.");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}