package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

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
        new ItemActionListener(this);
    }

    public void registerItem(String name, Class<? extends AbstractItem> item) {
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

    public static ItemStack stack(String string) {
        String[] split = string.split(":");
        byte b = -1;
        if (split.length == 2) {
            b = Byte.parseByte(split[1]);
        }
        try {
            Material material = Material.valueOf(split[0].toUpperCase());
            ItemStack stack = new ItemStack(material);
            stack.setTypeId(b);
            return stack;
        }catch(NumberFormatException ex){
            return null;
        }
    }
}
