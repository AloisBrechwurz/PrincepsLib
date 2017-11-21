package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.test.TestItem;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 11:48 AM
 */
public class ItemManager {

    private Map<String, Class<? extends AbstractItem>> items;

    public ItemManager() {
        items = new HashMap<>();
    }

    public void registerItem(String name, Class<TestItem> item) {
        items.put(name, item);
    }

    public AbstractItem getAbstractItem(ItemStack stack) {
        String customItemName = (String) PrincepsLib.crossVersion().getValueFromNBT(stack, "customItemName");

        try {
            Class<? extends AbstractItem> aClass = items.get(customItemName);

            return (AbstractItem) aClass.getConstructors()[0].newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("Custom item must implement empty constructor: " + e);
        }
        return null;
    }
}
