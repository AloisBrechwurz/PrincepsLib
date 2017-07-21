package biz.princeps.lib.gui.simple;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.gui.MainMenuGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spatium on 21.07.17.
 */
public abstract class AbstractGUI implements InventoryHolder {

    static {
        // Listener to prohibt taking out items and executing ClickActions
        new InventoryClickListener();
    }

    private final Map<Integer, Icon> icons;
    private final int size;
    protected String title, rawTitle;
    protected MainMenuGUI mainMenu;

    protected Player player;
    private Inventory inv;

    /**
     * Creates a new main menu
     *
     * @param player the player which want to see the menu
     * @param size   the size of the inventory. must be a multiple of 8
     * @param title  the name of the menu - ChatColor allowed!
     */
    public AbstractGUI(Player player, int size, String title) {
        this.player = player;
        this.icons = new HashMap<>();
        this.size = size;
        this.title = format(title);
        this.rawTitle = format(title);
    }

    /**
     * Creates a new main menu
     *
     * @param player   the player which want to see the menu
     * @param size     the size of the inventory. must be a multiple of 8
     * @param title    the name of the menu - ChatColor allowed!
     * @param mainMenu The superior menu
     */
    public AbstractGUI(Player player, int size, String title, MainMenuGUI mainMenu) {
        this(player, size, title);
        this.mainMenu = mainMenu;
    }

    /**
     * Formats a string to chatcolor
     *
     * @param toFormat the string which will be formatted
     * @return a formatted string
     */
    private String format(String toFormat) {
        return ChatColor.translateAlternateColorCodes('&', toFormat);
    }

    /**
     * Sets on the given position an icon
     *
     * @param position the position where the icon should be placed
     * @param icon     the icon itself
     * @return itself to allow builder-like object creation
     */
    public AbstractGUI setIcon(int position, Icon icon) {
        this.icons.put(position, icon);
        if (inv != null) {
            inv.setItem(position, icon.itemStack);
        }
        return this;
    }

    /**
     * Get an icon, which is on the given position. May return null
     *
     * @param position the wanted position
     * @return the Icon which is located at the given position. May return null
     */
    public Icon getIcon(int position) {
        return this.icons.get(position);
    }

    /**
     * Sets the title of the GUI
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Inventory getInventory() {
        inv = Bukkit.createInventory(this, size, title);

        for (Map.Entry<Integer, Icon> entry : this.icons.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().itemStack);
        }

        return inv;
    }

    public void refresh() {
        player.closeInventory();
        player.openInventory(getInventory());
    }

    public abstract Inventory display();


    static class InventoryClickListener implements Listener {

        public InventoryClickListener() {
            PrincepsLib.getPluginInstance().getServer().getPluginManager().registerEvents(this, PrincepsLib.getPluginInstance());
        }

        @EventHandler
        public void onClick(InventoryClickEvent event) {
            if (event.getView().getTopInventory().getHolder() instanceof AbstractGUI) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack itemStack = event.getCurrentItem();
                    if (itemStack == null || itemStack.getType() == Material.AIR) return;

                    AbstractGUI customHolder = (AbstractGUI) event.getView().getTopInventory().getHolder();

                    Icon icon = customHolder.getIcon(event.getRawSlot());
                    if (icon == null) return;
                    for (ClickAction clickAction : icon.getClickActions()) {
                        clickAction.execute(player);
                    }
                }
            }
        }
    }
}
