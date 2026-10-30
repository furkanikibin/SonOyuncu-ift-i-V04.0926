package rado;

import com.shakur.claim.events.ClaimDeleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rado.ciftci.MainUI;
import rado.ciftci.database.CiftciDatabase;
import rado.ciftci.database.manager.CiftciManager;
import rado.ciftci.events.ItemBreakEvent;
import rado.ciftci.events.EntityDeathEvent;
import rado.listener.ConfirmationListener;
import rado.listener.MainUIListener;
import rado.listener.player.Sell;
import rado.listener.send.Packet;
import rado.listener.ui.FarmerListener;
import rado.listener.ui.UpgradeUI.UPConfirmationListener;
import rado.listener.ui.UpgradeUI.UpgradeUIListener;
import rado.listener.Fixer;
import rado.npc.CNPC;
import rado.npc.listener.CNPCListener;
import rado.utils.Vault.Economy;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private CiftciDatabase database;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (!Economy.setup()) {
            getLogger().severe("Taiga Çiftçi Down");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.database = new CiftciDatabase(this);
        this.database.verileriYukle();

        getCommand("ciftci").setExecutor(new MainUI());

        registerEvents();

        for (Player player : Bukkit.getOnlinePlayers()) {
            new Packet(player).inject();
            CNPC.sendSpawnPackets(player);
        }

        getLogger().info("TaigaCiftci Aktif");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new MainUIListener(), this);
        getServer().getPluginManager().registerEvents(new ConfirmationListener(), this);
        getServer().getPluginManager().registerEvents(new FarmerListener(), this);
        getServer().getPluginManager().registerEvents(new UpgradeUIListener(), this);
        getServer().getPluginManager().registerEvents(new UPConfirmationListener(), this);
        getServer().getPluginManager().registerEvents(new ItemBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new Sell(), this);
        getServer().getPluginManager().registerEvents(new Fixer(), this);
        getServer().getPluginManager().registerEvents(new CNPCListener(), this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new Packet(event.getPlayer()).inject();
        CNPC.sendSpawnPackets(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        new Packet(event.getPlayer()).uninject();
    }

    @EventHandler
    public void onClaimDelete(ClaimDeleteEvent event) {
        if (event.getClaim() != null && event.getClaim().getLocation() != null) {
            CiftciManager.ciftciSil(event.getClaim().getLocation().getChunk());
        }
        CNPC.removeForAll();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            new Packet(player).uninject();
        }

        if (this.database != null) {
            this.database.verileriKaydet();
        }
        getLogger().info("bb gidiyom kapattin beni");
    }

    public static Main getInstance() {
        return instance;
    }

    public CiftciDatabase getCiftciDatabase() {
        return database;
    }
}