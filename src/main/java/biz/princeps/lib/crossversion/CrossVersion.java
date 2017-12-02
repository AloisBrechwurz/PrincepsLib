package biz.princeps.lib.crossversion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by spatium on 28.07.17.
 */
public class CrossVersion {

    private IActionBar bar;
    private ISpawnParticle particle;
    private IItem item;

    // Add stuff here which changed from 1.8 => 1.9
    private IStuff stuff;

    public CrossVersion() {
        String version = getVersion();

        switch (version) {

            case "v1_8_R3":
                bar = new biz.princeps.lib.crossversion.v1_8_R3.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_8_R3.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_8_R3.Item();
                stuff = new biz.princeps.lib.crossversion.v1_8_R3.Stuff();
                break;

            case "v1_9_R2":
                bar = new biz.princeps.lib.crossversion.v1_9_R2.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_9_R2.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_9_R2.Item();
                stuff = new biz.princeps.lib.crossversion.v1_9_R2.Stuff();
                break;

            case "v1_10_R1":
                bar = new biz.princeps.lib.crossversion.v1_10_R1.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_10_R1.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_10_R1.Item();
                stuff = new biz.princeps.lib.crossversion.v1_9_R2.Stuff();
                break;

            case "v1_11_R1":
                bar = new biz.princeps.lib.crossversion.v1_11_R1.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_11_R1.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_11_R1.Item();
                stuff = new biz.princeps.lib.crossversion.v1_9_R2.Stuff();
                break;

            case "v1_12_R1":
                bar = new biz.princeps.lib.crossversion.v1_12_R1.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_12_R1.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_12_R1.Item();
                stuff = new biz.princeps.lib.crossversion.v1_9_R2.Stuff();
                break;

            default:
                bar = new biz.princeps.lib.crossversion.v1_12_R1.ActionBar();
                particle = new biz.princeps.lib.crossversion.v1_12_R1.SpawnParticle();
                item = new biz.princeps.lib.crossversion.v1_12_R1.Item();
                stuff = new biz.princeps.lib.crossversion.v1_9_R2.Stuff();

        }

    }

    /**
     * just in case I ever want to upgrade this to a proper interface based system
     *
     * @param player
     * @param msg
     */
    public void sendActionBar(Player player, String msg) {
        bar.sendActionBar(player, msg);
    }

    public void spawnParticle(Location loc, CParticle cParticle, int amt) {
        particle.spawnParticle(loc, cParticle, amt);
    }

    public ItemStack addNBTTag(ItemStack stack, String key, Object value) {
        return item.addNBTTag(stack, key, value);
    }

    public Object getValueFromNBT(ItemStack stack, String key) {
        return item.getValueFromNBT(stack, key);
    }


    public boolean hasNBTTag(ItemStack stack, String customItem) {
        return item.hasNBTTag(stack, customItem);
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public boolean isOffHand(PlayerInteractEvent event) {
        return stuff.isOffHand(event);
    }
}
