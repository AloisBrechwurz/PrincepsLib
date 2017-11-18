package biz.princeps.lib.gui;

import biz.princeps.lib.gui.simple.AbstractGUI;
import biz.princeps.lib.gui.simple.Icon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spatium on 21.07.17.
 */
public class MultiPagedGUI extends AbstractGUI {

    private List<Icon> icons;
    private int rowsPerSite;

    private int siteNumber = 0;

    /**
     * Creates a new MultiPagedGUI with navigation elements
     *
     * @param player      the player which want to see the menu
     * @param rowsPerSite a value between 0 and 5
     * @param title       the name of the menu - ChatColor allowed!
     * @param icons       a list of icons which should be displayed on more pages
     * @param main        the superior main menu
     */
    public MultiPagedGUI(Player player, int rowsPerSite, String title, List<Icon> icons, AbstractGUI main) {
        super(player, rowsPerSite * 9 + 9, title, main);
        this.icons = icons;
        this.rowsPerSite = rowsPerSite;
    }

    /**
     * Creates a new MultiPagedGUI with navigation elements
     * You can add icons later with addIcon
     *
     * @param player      the player which want to see the menu
     * @param rowsPerSite a value between 0 and 5
     * @param title       the name of the menu - ChatColor allowed!
     */
    public MultiPagedGUI(Player player, int rowsPerSite, String title) {
        this(player, rowsPerSite, title, new ArrayList<>(), null);
    }

    /**
     * Adds an new Icon to the and of the list
     *
     * @param icon a icon
     * @return itself to add more buttons builder-pattern alike
     */
    public MultiPagedGUI addIcon(Icon icon) {
        icons.add(icon);
        return this;
    }

    public void removeIcon(Icon icon) {
        icons.remove(icon);
    }

    public List<Icon> filter(String filter) {
        List<Icon> filtered = new ArrayList<>();
        icons.stream().filter(icon -> icon.itemStack.getItemMeta().getDisplayName().contains(filter)).forEach(filtered::add);
        return filtered;
    }

    /**
     * This method must be called in order to open the inventory
     *
     * @return the opened inventory
     */
    @Override
    public Inventory display() {
        create();
        updateTitle();
        this.inventory = this.getInventory();
        this.player.openInventory(this.inventory);
        return this.inventory;
    }


    /**
     * Creates the site which is needed next
     */
    @Override
    protected void create() {
        this.clearIcons();
        for (int i = 0; i < rowsPerSite * 9; i++) {
            if (i + rowsPerSite * siteNumber * 9 < icons.size()) {
                this.setIcon(i, icons.get(i + rowsPerSite * siteNumber * 9));
                int erg = i + rowsPerSite * siteNumber * 9;
                System.out.println("Created an item on spot " + i + " @ array pos " + erg);
            } else {
                this.setIcon(i, new Icon(new ItemStack(Material.AIR)));
            }
        }

        if (siteNumber > 0) {
            ItemStack previous = new ItemStack(Material.ARROW);

            this.setIcon(rowsPerSite * 9 + 3,
                    new Icon(previous)
                            .setName(ChatColor.GREEN + "Previous Page")
                            .addClickAction((p) -> {
                                siteNumber--;
                                updateTitle();
                            }));
        } else
            this.setIcon(rowsPerSite * 9 + 3, new Icon(new ItemStack(Material.AIR)));


        if (siteNumber + 1 < Math.ceil((double) icons.size() / (double) (rowsPerSite * 9))) {
            ItemStack next = new ItemStack(Material.ARROW);
            this.setIcon(rowsPerSite * 9 + 5,
                    new Icon(next)
                            .setName(ChatColor.GREEN + "Next Page")
                            .addClickAction((p) -> {
                                siteNumber++;
                                updateTitle();
                            }));
        } else
            this.setIcon(rowsPerSite * 9 + 5, new Icon(new ItemStack(Material.AIR)));

        if (this.mainMenu != null) {
            this.setIcon(rowsPerSite * 9 + 4,
                    new Icon(new ItemStack(Material.NETHER_STAR))
                            .setName(org.bukkit.ChatColor.GOLD + mainMenu.getTitle())
                            .addClickAction((player) -> mainMenu.display()));
        }

    }

    /**
     * Updates the title accordingly to the site number
     */
    private void updateTitle() {
        this.setTitle(this
                .rawTitle.replace("%site%", "" + (siteNumber + 1))
                .replace("%maxsite%", (int) Math.ceil((double) icons.size() / (double) (rowsPerSite * 9)) + ""));
        if (this.inventory != null)
            refresh();
    }

    public int getRowsPerSite() {
        return rowsPerSite;
    }

    public int getSiteNumber() {
        return siteNumber;
    }
}
