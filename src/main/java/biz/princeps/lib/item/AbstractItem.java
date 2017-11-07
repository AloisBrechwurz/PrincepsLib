package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Project: PrincepsLib
 * Author: Alex D. (SpatiumPrinceps)
 * <p>
 * Date: 11/7/17 10:51 AM
 */
public abstract class AbstractItem {

    static {
        new ItemActionListener();
    }

    private ItemStack stack;
    private boolean glowing;

    public AbstractItem(ItemStack stack, boolean glowing) {
        this.stack = PrincepsLib.crossVersion().addNBTTag(stack, "customItem", true);
        setGlowing(glowing);
    }

    public abstract void onClick(Action action);

    public void setStackSize(int size) {
        stack.setAmount(size);
    }

    public void setGlowing(boolean glow) {
        this.glowing = glow;
        if (glowing) {
            ItemMeta itemMeta = stack.getItemMeta();
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(itemMeta);
        } else {
            ItemMeta itemMeta = stack.getItemMeta();
            itemMeta.removeEnchant(Enchantment.DAMAGE_ALL);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(itemMeta);
        }
    }

    public ItemStack getBukkitStack() {
        return stack;
    }

    public static boolean isCustomItem(ItemStack stack) {
        return PrincepsLib.crossVersion().hasNBTTag(stack, "customItem");
    }


    public void setNameToNBT(String nameToNBT) {
        PrincepsLib.crossVersion().addNBTTag(stack, "customItemName", nameToNBT);
    }
}
