package rado.ciftci.database.manager;

import org.bukkit.Chunk;
import rado.ciftci.database.data.CiftciData;

import java.util.HashMap;
import java.util.Map;

public class CiftciManager {

    private static final Map<String, CiftciData> aktifCiftciler = new HashMap<>();

    public static String koordinatCoz(Chunk chunk) {
        return chunk.getWorld().getName() + "_" + chunk.getX() + "_" + chunk.getZ();
    }

    public static boolean ciftcikontrol(Chunk chunk) {
        return aktifCiftciler.containsKey(koordinatCoz(chunk));
    }

    public static CiftciData veriGetir(Chunk chunk) {
        return aktifCiftciler.get(koordinatCoz(chunk));
    }

    public static void ciftciEkle(Chunk chunk, CiftciData data) {
        aktifCiftciler.put(koordinatCoz(chunk), data);
    }

    public static void ciftciSil(Chunk chunk) {
        aktifCiftciler.remove(koordinatCoz(chunk));
    }

    public static void deleteevent(Chunk chunk) {
        String key = koordinatCoz(chunk);
        if (aktifCiftciler.containsKey(key)) {
            aktifCiftciler.remove(key);
        }
    }

    public static Map<String, CiftciData> tumciftciler() {
        return aktifCiftciler;
    }
}