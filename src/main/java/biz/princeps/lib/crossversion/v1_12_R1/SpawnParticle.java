package biz.princeps.lib.crossversion.v1_12_R1;

import biz.princeps.lib.crossversion.CParticle;
import biz.princeps.lib.crossversion.ISpawnParticle;
import org.bukkit.Location;
import org.bukkit.Particle;

/**
 * Created by spatium on 28.07.17.
 */
public class SpawnParticle implements ISpawnParticle {


    @Override
    public void spawnParticle(Location loc, CParticle particle, int amount) {
        loc.getWorld().spawnParticle(Particle.valueOf(particle.getV19()), loc, amount);
    }
}
