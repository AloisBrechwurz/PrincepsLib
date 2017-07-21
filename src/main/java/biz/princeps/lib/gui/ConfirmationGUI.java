package biz.princeps.lib.gui;

import biz.princeps.lib.gui.simple.AbstractGUI;
import biz.princeps.lib.gui.simple.ClickAction;
import biz.princeps.lib.gui.simple.Icon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by spatium on 21.07.17.
 */
public class ConfirmationGUI extends AbstractGUI {

    private ClickAction onAccept, onDecline;

    public ConfirmationGUI(Player player, String msg, ClickAction onAccept, ClickAction onDecline, AbstractGUI mainMenu) {
        super(player, 9, msg, mainMenu);
        this.onAccept = onAccept;
        this.onDecline = onDecline;
    }

    @Override
    protected void create() {
        ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Confirm!");
        item.setItemMeta(meta);
        this.setIcon(0, new Icon(item).addClickAction(onAccept));

        ItemStack item2 = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(ChatColor.RED + "Decline!");
        item2.setItemMeta(meta2);
        this.setIcon(8, new Icon(item2).addClickAction(onDecline));
    }
}
