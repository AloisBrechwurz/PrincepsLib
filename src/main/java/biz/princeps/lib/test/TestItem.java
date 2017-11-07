package biz.princeps.lib.test;

import biz.princeps.lib.item.AbstractItem;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 4:03 PM
 */
public class TestItem extends AbstractItem {

    /**
     * Used to initially create a custom item stack
     */
    public TestItem() {
        super("testitem", new ItemStack(Material.ANVIL), false);
    }

    @Override
    public void onClick(Action action) {
        System.out.println(action.name());
    }
}
