package rado.ciftci.database.data;

import java.util.HashMap;
import java.util.Map;

public class CiftciData {

    private final Map<String, Integer> depo;
    private final Map<String, Integer> seviyeler;
    private boolean durum;

    public CiftciData() {
        this.depo = new HashMap<>();
        this.seviyeler = new HashMap<>();
        this.durum = true;
    }

    public int getUrunSeviye(String anahtar) {
        return seviyeler.getOrDefault(anahtar, 1);
    }

    public void setUrunSeviye(String anahtar, int seviye) {
        if (seviye > 50) seviye = 50;
        seviyeler.put(anahtar, seviye);
    }

    public int getUrunKapasite(String anahtar) {
        return getUrunSeviye(anahtar) * 20000;
    }

    public int miktarAl(String anahtar) {
        return depo.getOrDefault(anahtar, 0);
    }

    public void miktarEkle(String anahtar, int miktar) {
        if (!durum) return;

        int mevcut = miktarAl(anahtar);
        int yeniMiktar = mevcut + miktar;
        int maxKapasite = getUrunKapasite(anahtar);

        if (yeniMiktar > maxKapasite) {
            yeniMiktar = maxKapasite;
        }

        depo.put(anahtar, yeniMiktar);
    }

    public void miktarSet(String anahtar, int miktar) {
        depo.put(anahtar, miktar);
    }

    public Map<String, Integer> getDepo() {
        return depo;
    }

    public Map<String, Integer> getSeviyeler() {
        return seviyeler;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }
}