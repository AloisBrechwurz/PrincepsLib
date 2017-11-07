package biz.princeps.lib.crossversion.v1_10_R1;

import biz.princeps.lib.crossversion.IItem;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 11:19 AM
 */
public class Item implements IItem{

    @Override
    public ItemStack addNBTTag(ItemStack stack, String key, Object value) {
        net.minecraft.server.v1_10_R1.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        tag.setBoolean("isCustomItem", true);
        nmsstack.save(tag);
        stack = CraftItemStack.asBukkitCopy(nmsstack);
        return stack;
    }

    @Override
    public boolean hasNBTTag(ItemStack stack, String customItem) {
        net.minecraft.server.v1_10_R1.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsstack.getTag();
        return tag.get(customItem) != null;
    }
}
