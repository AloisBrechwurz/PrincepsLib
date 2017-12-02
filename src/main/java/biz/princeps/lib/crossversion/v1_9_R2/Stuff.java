package biz.princeps.lib.crossversion.v1_9_R2;

import biz.princeps.lib.crossversion.IStuff;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Project: PrincepsLib
 * Created by Alex D. (SpatiumPrinceps)
 * Date: 12/2/17
 */
public class Stuff implements IStuff {

    @Override
    public boolean isOffHand(PlayerInteractEvent event) {
        return event.getHand() == EquipmentSlot.OFF_HAND;
    }
}
