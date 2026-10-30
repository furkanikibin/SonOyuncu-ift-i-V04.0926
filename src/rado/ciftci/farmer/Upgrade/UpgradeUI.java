package rado.ciftci.farmer.Upgrade;

import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

import java.util.ArrayList;
import java.util.List;

public class UpgradeUI {

    public static void open(Player p) {
        Chunk chunk = p.getLocation().getChunk();
        Claim claim = ClaimHandler.getByLocation(p.getLocation());
        CiftciData data = CiftciManager.veriGetir(chunk);

        if (data == null) return;

        String claimName = (claim != null) ? claim.getName() : "Sahipsiz";
        String guiName = "Çiftçi Yükseltme (" + claimName + " : " + chunk.getX() + " " + chunk.getZ() + ")";

        Inventory gui = Bukkit.getServer().createSingleInventory(p, guiName, 88);

        gui.setItem(5, createSimpleItem(Material.PAPER, (short) 0, "§aBilgi", "§7Burada deponu", "§7büyütebilirsin.", "§7Max Seviye: §e50"));
        gui.setItem(77, createSimpleItem(Material.ARROW, (short) 0, "§9< §3Geri Dön", "§7Tıkla ve bir önceki menüye dön."));

        gui.setItem(23, createItem(data, Material.MELON, (short) 0));
        gui.setItem(24, createItem(data, Material.CACTUS, (short) 0));
        gui.setItem(25, createItem(data, Material.PUMPKIN, (short) 0));
        gui.setItem(26, createItem(data, Material.PRISMARINE_SHARD, (short) 0));
        gui.setItem(27, createItem(data, Material.PRISMARINE_CRYSTALS, (short) 0));
        gui.setItem(28, createItem(data, Material.RAW_FISH, (short) 0));
        gui.setItem(29, createItem(data, Material.RAW_FISH, (short) 1));
        gui.setItem(30, createItem(data, Material.RAW_FISH, (short) 3));
        gui.setItem(31, createItem(data, Material.COOKED_FISH, (short) 0));
        gui.setItem(34, createItem(data, Material.COOKED_FISH, (short) 1));
        gui.setItem(35, createItem(data, Material.COOKED_CHICKEN, (short) 0));
        gui.setItem(36, createItem(data, Material.COOKED_MUTTON, (short) 0));
        gui.setItem(37, createItem(data, Material.GRILLED_PORK, (short) 0));
        gui.setItem(38, createItem(data, Material.COOKED_BEEF, (short) 0));
        gui.setItem(39, createItem(data, Material.LEATHER, (short) 0));
        gui.setItem(40, createItem(data, Material.FEATHER, (short) 0));
        gui.setItem(41, createItem(data, Material.STRING, (short) 0));
        gui.setItem(42, createItem(data, Material.SPIDER_EYE, (short) 0));
        gui.setItem(45, createItem(data, Material.BONE, (short) 0));
        gui.setItem(46, createItem(data, Material.ARROW, (short) 0));
        gui.setItem(47, createItem(data, Material.ROTTEN_FLESH, (short) 0));
        gui.setItem(48, createItem(data, Material.IRON_INGOT, (short) 0));
        gui.setItem(49, createItem(data, Material.WOOL, (short) 0));
        gui.setItem(50, createItem(data, Material.WOOL, (short) 1));
        gui.setItem(51, createItem(data, Material.WOOL, (short) 2));
        gui.setItem(52, createItem(data, Material.WOOL, (short) 3));
        gui.setItem(53, createItem(data, Material.WOOL, (short) 4));
        gui.setItem(56, createItem(data, Material.WOOL, (short) 5));
        gui.setItem(57, createItem(data, Material.WOOL, (short) 6));
        gui.setItem(58, createItem(data, Material.WOOL, (short) 7));
        gui.setItem(59, createItem(data, Material.WOOL, (short) 8));
        gui.setItem(60, createItem(data, Material.WOOL, (short) 9));
        gui.setItem(61, createItem(data, Material.WOOL, (short) 10));
        gui.setItem(62, createItem(data, Material.WOOL, (short) 11));
        gui.setItem(63, createItem(data, Material.WOOL, (short) 12));
        gui.setItem(64, createItem(data, Material.WOOL, (short) 13));
        gui.setItem(67, createItem(data, Material.WOOL, (short) 14));
        gui.setItem(68, createItem(data, Material.WOOL, (short) 15));

        p.openInventory(gui);
    }

    private static ItemStack createItem(CiftciData data, Material mat, short dur) {
        String key = (dur > 0 || mat == Material.WOOL || mat == Material.RAW_FISH)
                ? mat.name() + ":" + dur : mat.name();

        int seviye = data.getUrunSeviye(key);

        ItemStack item = new ItemStack(mat, seviye, dur);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            int kapasite = data.getUrunKapasite(key);
            double fiyat = 5150.0 + ((seviye - 1) * 350.0);

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Seviye: §e" + seviye);
            lore.add("§7Kapasite: §e" + kapasite);
            lore.add("§7Fiyat: §6" + String.format("%.1f", fiyat).replace(".", ",") + " Kinas");
            lore.add("");
            lore.add("§eYükseltmek için tıkla");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createSimpleItem(Material mat, short dur, String name, String... loreLines) {
        ItemStack item = new ItemStack(mat, 1, dur);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            for (String s : loreLines) lore.add(s);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}