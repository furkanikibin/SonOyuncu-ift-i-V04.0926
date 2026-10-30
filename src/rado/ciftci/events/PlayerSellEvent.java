package rado.ciftci.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSellEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String itemKey;
    private final int miktar;

    public PlayerSellEvent(Player player, String itemKey, int miktar) {
        this.player = player;
        this.itemKey = itemKey;
        this.miktar = miktar;
    }

    public Player getPlayer() { return player; }
    public String getItemKey() { return itemKey; }
    public int getMiktar() { return miktar; }

    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}