package biz.princeps.lib.crossversion.v1_11_R1;

import biz.princeps.lib.crossversion.IItem;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 11:19 AM
 */
public class Item implements IItem {

    @Override
    public ItemStack addNBTTag(ItemStack stack, String key, Object value) {
        net.minecraft.server.v1_11_R1.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag() == null ? new NBTTagCompound() : nmsstack.getTag();
        tag.setBoolean("isCustomItem", true);
        nmsstack.setTag(tag);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsstack);
        stack.setItemMeta(meta);

        return stack;
    }

    @Override
    public Object getValueFromNBT(ItemStack stack, String key) {
        net.minecraft.server.v1_11_R1.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        return tag.get(key);
    }

    @Override
    public boolean hasNBTTag(ItemStack stack, String customItem) {
        net.minecraft.server.v1_11_R1.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        return tag.hasKey(customItem);
    }
}
