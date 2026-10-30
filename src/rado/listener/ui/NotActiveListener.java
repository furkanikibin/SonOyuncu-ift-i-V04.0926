package rado.listener.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class NotActiveListener implements Listener {

    private final String menuTitle = "Çiftçi Deposu";

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null || !event.getView().getTitle().equals(menuTitle)) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() != null) {
            event.getClickedInventory().getContents();
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equals(menuTitle)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (p.getOpenInventory().getTitle().equals(menuTitle)) {
            event.setCancelled(true);
        }
    }
}