package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 11:48 AM
 */
public class ItemManager {

    private Map<String, AbstractItem> items;

    public ItemManager() {
        items = new HashMap<>();
    }

    public void registerItem(String name, AbstractItem item) {
        items.put(name, item);
        item.setNameToNBT(name);
    }

    public AbstractItem getAbstractItem(ItemStack stack) {
        ItemStack customItemName = PrincepsLib.crossVersion().getNBTTag(stack, "customItemName");
        return items.get(customItemName);
    }
}
