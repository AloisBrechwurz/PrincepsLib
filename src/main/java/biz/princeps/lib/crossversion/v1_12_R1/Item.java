package biz.princeps.lib.crossversion.v1_12_R1;

import biz.princeps.lib.crossversion.IItem;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 11:19 AM
 */
public class Item implements IItem {

    @Override
    public ItemStack addNBTTag(ItemStack stack, String key, Object value) {
        //TODO implement 1.12.2 logic
        return stack;
    }

    @Override
    public Object getValueFromNBT(ItemStack stack, String key) {
        return null;
    }

    @Override
    public boolean hasNBTTag(ItemStack stack, String customItem) {
        return false;
    }
}
