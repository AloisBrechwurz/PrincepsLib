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
    protected String name;

    /**
     * Used to initially create a custom item stack
     *
     * @param name    name, which should be saved inside nbt and will also be used to get the instance of this class
     * @param stack   the item stack, which should be wrapped
     * @param glowing if the item should be glowing or not
     */
    public AbstractItem(String name, ItemStack stack, boolean glowing) {
        this.name = name;
        PrincepsLib.crossVersion().addNBTTag(stack, "customItemName", name);
        this.stack = PrincepsLib.crossVersion().addNBTTag(stack, "customItem", true);
        setGlowing(glowing);
        PrincepsLib.getItemManager().registerItem(this);
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
}
