package rado.ciftci.events;

import com.shakur.claim.Claim;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClaimDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Claim claim;

    public ClaimDeleteEvent(Claim claim) {
        this.claim = claim;
    }

    public Claim getClaim() {
        return this.claim;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}