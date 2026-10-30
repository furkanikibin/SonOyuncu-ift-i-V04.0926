package rado.npc.listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rado.ciftci.farmer.Farmer;

public class CNPCListener implements Listener {

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        if (event.getNPC().getName().contains("Çiftçi")) {
            Player player = event.getClicker();

            Farmer.open(player);
        }
    }
}