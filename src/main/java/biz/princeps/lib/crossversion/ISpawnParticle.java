package biz.princeps.lib.crossversion;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by spatium on 28.07.17.
 */
public interface ISpawnParticle {


    void spawnParticle(Location loc, CParticle particle, int amount);

    void spawnParticle(Player p, Location loc, CParticle particle, int amount);
}
