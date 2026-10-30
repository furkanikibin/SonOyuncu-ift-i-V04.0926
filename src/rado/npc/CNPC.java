package rado.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shakur.claim.Claim;
import com.shakur.claim.ClaimHandler;
import com.shakur.claim.member.ClaimMember;
import com.shakur.claim.member.ClaimMemberPermission;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rado.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class CNPC {

    public static final int NPC_ID = 999100;
    private static String textureValue = "";
    private static String textureSignature = "";
    private static EntityPlayer npcInstance = null;

    public static void spawn(Location loc) {
        if (textureValue.isEmpty()) loadSkinData("tristanjv");

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();

        GameProfile npcProfile = new GameProfile(UUID.randomUUID(), "§aÇiftçi");
        if (!textureValue.isEmpty()) {
            npcProfile.getProperties().put("textures", new Property("textures", textureValue, textureSignature));
        }

        npcInstance = new EntityPlayer(nmsServer, nmsWorld, npcProfile, new PlayerInteractManager(nmsWorld));
        npcInstance.d(NPC_ID);
        npcInstance.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnPackets(player);
        }
    }

    public static void sendSpawnPackets(Player player) {
        if (npcInstance == null) return;

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npcInstance));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npcInstance));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npcInstance, (byte) (npcInstance.yaw * 256 / 360)));

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npcInstance));
        }, 50L);

        startLookTask(player, npcInstance, npcInstance.getBukkitEntity().getLocation());
    }

    private static void loadSkinData(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
            JsonObject json = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            textureValue = json.get("value").getAsString();
            textureSignature = json.get("signature").getAsString();
        } catch (Exception ignored) {}
    }

    private static void startLookTask(Player player, EntityPlayer npc, Location npcLoc) {
        new BukkitRunnable() {
            float currentBodyYaw = npcLoc.getYaw();
            float currentHeadYaw = npcLoc.getYaw();

            @Override
            public void run() {
                if (!player.isOnline()) { this.cancel(); return; }
                if (!player.getWorld().equals(npcLoc.getWorld()) || player.getLocation().distance(npcLoc) > 10) return;

                Location playerLoc = player.getEyeLocation();
                double dX = playerLoc.getX() - npcLoc.getX();
                double dY = playerLoc.getY() - (npcLoc.getY() + 1.62);
                double dZ = playerLoc.getZ() - npcLoc.getZ();
                double dXZ = Math.sqrt(dX * dX + dZ * dZ);

                float targetYaw = (float) (Math.toDegrees(Math.atan2(dZ, dX))) - 90;
                float targetPitch = (float) (-Math.toDegrees(Math.atan2(dY, dXZ)));

                PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

                float headDiff = targetYaw - currentHeadYaw;
                while (headDiff < -180) headDiff += 360;
                while (headDiff > 180) headDiff -= 360;
                currentHeadYaw += headDiff * 0.50f;

                conn.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (currentHeadYaw * 256 / 360)));

                float bodyDiff = targetYaw - currentBodyYaw;
                while (bodyDiff < -180) bodyDiff += 360;
                while (bodyDiff > 180) bodyDiff -= 360;
                currentBodyYaw += bodyDiff * 0.35f;

                conn.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(
                        npc.getId(),
                        (byte) (currentBodyYaw * 256 / 360),
                        (byte) (targetPitch * 256 / 360),
                        true
                ));
            }
        }.runTaskTimer(Main.getInstance(), 0L, 2L);
    }

    public static void handleInteract(Player player) {
        if (npcInstance == null) return;
        Location loc = npcInstance.getBukkitEntity().getLocation();

        if (ClaimHandler.has(loc)) {
            Claim claim = ClaimHandler.getByLocation(loc);
            boolean isOwner = claim.getOwner().equals(player.getUniqueId());
            boolean isOp = player.isOp();
            ClaimMember member = claim.getMember(player.getUniqueId());
            boolean hasPerm = (member != null && member.hasPermission(ClaimMemberPermission.SPAWNER_BREAK));

            if (isOwner || isOp || hasPerm) {
                rado.ciftci.farmer.Farmer.open(player);
            } else {
                player.sendMessage("§6SM §7§l > §4Bu çiftçiyi yönetmek için yetkiniz yok.");
            }
        } else {
            player.sendMessage("§6SM §7§l > §4Bu özellik sadece claimli alanlarda kullanılabilir.");
        }
    }

    public static void removeForAll() {
        if (npcInstance == null) return;
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(NPC_ID);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyPacket);
        }
        npcInstance = null;
    }
}