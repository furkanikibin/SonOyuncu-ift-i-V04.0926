package rado.ciftci;

import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

import java.util.ArrayList;
import java.util.List;

public class MainUI implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        Chunk chunk = p.getLocation().getChunk();
        Claim claim = ClaimHandler.getByLocation(p.getLocation());
        String claimName = (claim != null) ? claim.getName() : "Sahipsiz";

        boolean isActive = CiftciManager.ciftcikontrol(chunk);

        String guiName = "Çiftçi (" + claimName + " : " + chunk.getX() + " " + chunk.getZ() + ")";
        Inventory gui = Bukkit.getServer().createSingleInventory(p, guiName, 33);

        ItemStack ciftciItem = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = ciftciItem.getItemMeta();

        if (meta != null) {
            List<String> lore = new ArrayList<>();
            if (!isActive) {
                meta.setDisplayName("§cÇiftçi");
                lore.add("");
                lore.add("§cÇiftçi aktif edilmedi.");
                lore.add("");
                lore.add("§7Çiftçiyi sadece yönetim");
                lore.add("§7bölgesinde aktif edebilirsin.");
                lore.add("§7Çiftçi aktif ettiğin koordinata");
                lore.add("§7gelecektir. Çiftçinin koordinatını");
                lore.add("§7sonradan değiştirebilirsin.");
                lore.add("");
                lore.add("§7Çiftçi hakkında detaylı bilgi");
                lore.add("§7almak için §e'/bilgi'");
                lore.add("");
                lore.add("§7Fiyat: §65.000,0 Kinas");
                lore.add("");
                lore.add("§eAktif etmek için tıkla");
            } else {
                meta.setDisplayName("§aÇiftçi");
                lore.add("");
                lore.add("§7Çiftçinin koordinatı");
                lore.add("§fX: " + p.getLocation().getBlockX() + "§f, Y: §f" + p.getLocation().getBlockY() + "§f, Z: §f" + p.getLocation().getBlockZ());
                lore.add("");
                lore.add("§eÇiftçiyi bulunduğun yere");
                lore.add("§etaşımak için tıkla");
            }
            meta.setLore(lore);
            ciftciItem.setItemMeta(meta);
        }
        gui.setItem(5, ciftciItem);

        ItemStack depoItem = new ItemStack(Material.CHEST);
        ItemMeta depoMeta = depoItem.getItemMeta();
        if (depoMeta != null) {
            depoMeta.setDisplayName("§aDepo");
            List<String> dLore = new ArrayList<>();
            dLore.add("§eAçmak için tıkla");
            depoMeta.setLore(dLore);
            depoItem.setItemMeta(depoMeta);
        }
        gui.setItem(14, depoItem);

        ItemStack upgradeItem = new ItemStack(Material.FIREWORK);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        if (upgradeMeta != null) {
            upgradeMeta.setDisplayName("§aYükseltme");
            List<String> uLore = new ArrayList<>();
            if (!isActive) {
                uLore.add("§cYükseltmeden önce");
                uLore.add("§cçiftçiyi aktif etmelisin");
            } else {
                uLore.add("§eAçmak için tıkla");
            }
            upgradeMeta.setLore(uLore);
            upgradeItem.setItemMeta(upgradeMeta);
        }
        gui.setItem(18, upgradeItem);

        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName("§b< §3Geri Dön");
            List<String> bLore = new ArrayList<>();
            bLore.add("§7Tıkla ve bir önceki menüye dön.");
            backMeta.setLore(bLore);
            backItem.setItemMeta(backMeta);
        }
        gui.setItem(22, backItem);

        if (isActive) {
            CiftciData data = CiftciManager.veriGetir(chunk);
            if (data != null) {
                short dyeColor = data.isDurum() ? (short) 10 : (short) 8;
                ItemStack aktiflikItem = new ItemStack(Material.INK_SACK, 1, dyeColor);
                ItemMeta aktiflikMeta = aktiflikItem.getItemMeta();

                if (aktiflikMeta != null) {
                    aktiflikMeta.setDisplayName("§aAktiflik");
                    List<String> aLore = new ArrayList<>();
                    aLore.add("");
                    aLore.add("§7Çiftçinin çalışmasını");
                    aLore.add("§7buradan durdurup");
                    aLore.add("§7tekrar başlatabilirsin.");
                    aLore.add("");

                    if (data.isDurum()) {
                        aLore.add("§7Durum: §aÇalışıyor");
                        aLore.add("");
                        aLore.add("§eDurdurmak için tıkla");
                    } else {
                        aLore.add("§7Durum: §cDurduruldu");
                        aLore.add("");
                        aLore.add("§eBaşlatmak için tıkla");
                    }

                    aktiflikMeta.setLore(aLore);
                    aktiflikItem.setItemMeta(aktiflikMeta);
                }
                gui.setItem(27, aktiflikItem);
            }
        }

        p.openInventory(gui);
        return true;
    }
}