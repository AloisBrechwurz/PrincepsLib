package biz.princeps.lib.crossversion.v1_12_R1;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.crossversion.CParticle;
import biz.princeps.lib.crossversion.ISpawnParticle;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by spatium on 28.07.17.
 */
public class SpawnParticle implements ISpawnParticle {


    @Override
    public void spawnParticle(Location loc, CParticle particle, int amount) {
        loc.getWorld().spawnParticle(Particle.valueOf(particle.getV19()), loc, amount);
    }

    @Override
    public void spawnParticle(Player p, Location loc, CParticle particle, int amount) {

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getParticles().write(0, EnumWrappers.Particle.getById(particle.getID()));
        packet.getBooleans().write(0, false);
        // Coordinates
        packet.getFloat().write(0, (float) loc.getX()).write(1, (float) loc.getY()).write(2, (float) loc.getZ());
        // Offset
        packet.getFloat().write(3, .0F).write(4, .0F).write(5, .0F);

        // particle data. No clue whats that
        packet.getFloat().write(6, .0F);

        // amount
        packet.getIntegers().write(0, amount);

        packet.getIntegerArrays().write(0, new int[0]);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            PrincepsLib.getPluginInstance().getLogger().warning("Packet SpawnParticle could not be sent to " + p.getName());
        }
    }
}
