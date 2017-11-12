package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * Date: 11/7/17 11:42 AM
 */
public class ItemActionListener implements Listener {

    public ItemActionListener() {
        PrincepsLib.getPluginInstance().getServer().getPluginManager().registerEvents(this, PrincepsLib.getPluginInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        System.out.println("tirgger");
        if (item != null) {
            if (AbstractItem.isCustomItem(item)) {
                System.out.println("jo");
                AbstractItem abstractItem = new ItemManager().getAbstractItem(item);
                abstractItem.onClick(event.getAction());
            }
        }
    }
}
