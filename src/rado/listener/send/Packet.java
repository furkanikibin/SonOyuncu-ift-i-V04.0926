package rado.listener.send;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rado.Main;
import rado.ciftci.farmer.Farmer;
import rado.npc.CNPC;

import java.lang.reflect.Field;
import java.util.List;

public class Packet {

    private final Player player;

    public Packet(Player player) {
        this.player = player;
    }

    public void inject() {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().playerConnection.networkManager.channel;

        if (channel.pipeline().get("PacketInjector") != null) return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext ctx, PacketPlayInUseEntity packet, List<Object> out) throws Exception {
                out.add(packet);

                readPacket(packet);
            }
        });
    }

    public void uninject() {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

    private void readPacket(PacketPlayInUseEntity packet) {
        try {
            Field field = packet.getClass().getDeclaredField("a");
            field.setAccessible(true);
            int id = field.getInt(packet);

            if (id == CNPC.NPC_ID) {
                if (packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {

                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        Farmer.open(player);
                    });
                }
            }
        } catch (Exception ignored) {
        }
    }
}