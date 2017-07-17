package biz.princeps.lib.manager;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.storage.DatabaseAPI;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class Manager {

    protected JavaPlugin plugin;
    protected DatabaseAPI api;


    public Manager(DatabaseAPI api) {
        this.plugin = PrincepsLib.getPluginInstance();
        this.api = api;
    }

    // Load strategy (like loadAll on startup, or load on join)
    public abstract void saveAll();

    public abstract void loadAll();


}
