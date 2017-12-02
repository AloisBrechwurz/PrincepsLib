package biz.princeps.lib.crossversion.v1_8_R3;

import biz.princeps.lib.crossversion.IStuff;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Project: PrincepsLib
 * Created by Alex D. (SpatiumPrinceps)
 * Date: 12/2/17
 */
public class Stuff implements IStuff {


    @Override
    public boolean isOffHand(PlayerInteractEvent event) {
        return false;
    }
}
