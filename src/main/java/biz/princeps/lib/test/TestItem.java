package biz.princeps.lib.test;

import biz.princeps.lib.item.AbstractItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * Date: 11/7/17 4:03 PM
 */
public class TestItem extends AbstractItem {

    public static final String name = "testitem";

    /**
     * Used to initially create a custom item stack
     */
    public TestItem() {
        super(name, new ItemStack(Material.STICK), false, false);
    }


    @Override
    public void onClick(Action action, Player player, Location loc) {
        System.out.println(action);
    }
}
