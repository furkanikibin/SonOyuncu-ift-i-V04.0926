package rado.ciftci.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rado.npc.CNPC;

public class ClaimListenerDC implements Listener {

    @EventHandler
    public void onClaimDelete(ClaimDeleteEvent event) {
        CNPC.removeForAll();
    }
}