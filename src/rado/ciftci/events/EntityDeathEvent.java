package rado.ciftci.events;

import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import org.bukkit.Chunk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import rado.Main;
import rado.ciftci.database.data.CiftciData;
import rado.ciftci.database.manager.CiftciManager;

import java.util.Iterator;

public class EntityDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Claim claim = ClaimHandler.getByLocation(entity.getLocation());
        if (claim == null) return;

        Chunk chunk = entity.getLocation().getChunk();
        CiftciData data = CiftciManager.veriGetir(chunk);

        if (data != null && data.isDurum()) {
            Iterator<ItemStack> iterator = event.getDrops().iterator();
            while (iterator.hasNext()) {
                ItemStack stack = iterator.next();
                String matName = stack.getType().name();
                short dur = stack.getDurability();

                String key = (dur > 0 || matName.equals("WOOL") || matName.equals("RAW_FISH"))
                        ? matName + ":" + dur : matName;

                if (Main.getPlugin(Main.class).getConfig().contains("fiyatlar." + key)) {
                    data.miktarEkle(key, stack.getAmount());
                    iterator.remove();
                }
            }
        }
    }
}