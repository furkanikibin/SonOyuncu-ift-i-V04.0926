package rado.ciftci.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import rado.Main;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CiftciDatabase {

    private final File dosya;
    private final FileConfiguration config;

    public CiftciDatabase(Main plugin) {
        this.dosya = new File(plugin.getDataFolder(), "depo.yml");

        if (!dosya.exists()) {
            try {
                dosya.getParentFile().mkdirs();
                dosya.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(dosya);
    }

    public void verileriYukle() {
        if (!config.contains("ciftciler")) return;

        Set<String> anahtarkk = config.getConfigurationSection("ciftciler").getKeys(false);
        for (String chunkordinatabi : anahtarkk) {
            String yol = "ciftciler." + chunkordinatabi;
            CiftciData veri = new CiftciData();

            if (config.contains(yol + ".stok")) {
                Set<String> esyalar = config.getConfigurationSection(yol + ".stok").getKeys(false);
                for (String esya : esyalar) {
                    int miktar = config.getInt(yol + ".stok." + esya);
                    veri.miktarSet(esya, miktar);
                }
            }

            if (config.contains(yol + ".seviyeler")) {
                Set<String> sevKeys = config.getConfigurationSection(yol + ".seviyeler").getKeys(false);
                for (String sKey : sevKeys) {
                    int seviye = config.getInt(yol + ".seviyeler." + sKey);
                    veri.setUrunSeviye(sKey, seviye);
                }
            }

            CiftciManager.tumciftciler().put(chunkordinatabi, veri);
        }
    }

    public void verileriKaydet() {
        config.set("ciftciler", null);

        CiftciManager.tumciftciler().forEach((koordinat, veri) -> {
            String yol = "ciftciler." + koordinat;

            veri.getDepo().forEach((esya, miktar) -> {
                config.set(yol + ".stok." + esya, miktar);
            });

            veri.getSeviyeler().forEach((esya, seviye) -> {
                config.set(yol + ".seviyeler." + esya, seviye);
            });
        });

        try {
            config.save(dosya);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}