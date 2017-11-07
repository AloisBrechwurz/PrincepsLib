package biz.princeps.lib.crossversion.v1_9_R2;

import biz.princeps.lib.crossversion.IItem;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
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
        net.minecraft.server.v1_9_R2.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        tag.setBoolean("isCustomItem", true);
        nmsstack.save(tag);
        stack = CraftItemStack.asBukkitCopy(nmsstack);
        return stack;
    }

    @Override
    public Object getValueFromNBT(ItemStack stack, String key) {
        net.minecraft.server.v1_9_R2.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        return tag.get(key);
    }

    @Override
    public boolean hasNBTTag(ItemStack stack, String customItem) {
        net.minecraft.server.v1_9_R2.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        return tag.hasKey(customItem);
    }
}