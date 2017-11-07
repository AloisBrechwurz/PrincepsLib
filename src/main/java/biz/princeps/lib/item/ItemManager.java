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

    public void registerItem(AbstractItem item) {
        items.put(item.name, item);
    }

    public AbstractItem getAbstractItem(ItemStack stack) {
        String customItemName = (String) PrincepsLib.crossVersion().getValueFromNBT(stack, "customItemName");
        return items.get(customItemName);
    }
}
