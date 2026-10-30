package rado.ciftci.events;

import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import org.bukkit.Chunk;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import rado.Main;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

public class ItemBreakEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(ItemSpawnEvent event) {
        if (event.getEntity().getPickupDelay() > 39) {
            return;
        }

        Item itemEntity = event.getEntity();
        ItemStack stack = itemEntity.getItemStack();
        String matName = stack.getType().name();
        short dur = stack.getDurability();

        String key = (dur > 0 || matName.equals("WOOL") || matName.equals("RAW_FISH"))
                ? matName + ":" + dur : matName;

        if (!Main.getPlugin(Main.class).getConfig().contains("fiyatlar." + key)) return;

        Claim claim = ClaimHandler.getByLocation(itemEntity.getLocation());
        if (claim == null) return;

        Chunk masterChunk = claim.getLocation().getChunk();

        CiftciData data = CiftciManager.veriGetir(masterChunk);

        if (data == null) {
            masterChunk = itemEntity.getLocation().getChunk();
            data = CiftciManager.veriGetir(masterChunk);
        }

        if (data != null) {
            if (!data.isDurum()) return;

            int mevcut = data.miktarAl(key);
            int kapasite = data.getUrunKapasite(key);

            if (mevcut < kapasite) {
                data.miktarEkle(key, stack.getAmount());
                event.setCancelled(true);
            }
        }
    }
}