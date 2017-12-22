package biz.princeps.lib.item;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * Project: PrincepsLib
 * Created by Alex D. (SpatiumPrinceps)
 * Date: 12/22/17
 */
public class DataStack {

    private Material mat;
    private byte data;

    public DataStack(String in) {
        String[] split = in.split(":");
        this.data = -1;

        if (split.length == 2) {
            this.data = Byte.parseByte(split[1]);
        }
        try {
            this.mat = Material.valueOf(split[0].toUpperCase());

        } catch (NumberFormatException ex) {
            PrincepsLib.getPluginInstance().getLogger().warning("Invalid Material detected!! Value: " + in);
        }
    }

    public DataStack(Material mat, byte data) {
        this.mat = mat;
        this.data = data;
    }

    public Material getMaterial() {
        return mat;
    }

    public byte getData() {
        return data;
    }

    public ItemStack getItemStack() {
        return new ItemStack(mat, 1, (short) 0, data);
    }

    public void place(World w, int x, int y, int z) {
        w.getBlockAt(x, y, z).setType(mat);
        if (data > -1) {
            w.getBlockAt(x, y, z).setData(data);
        }
    }

    public void place(World w, Location loc) {
        w.getBlockAt(loc).setType(mat);
        if (data > -1) {
            w.getBlockAt(loc).setData(data);
        }
    }
}