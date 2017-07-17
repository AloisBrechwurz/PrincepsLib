package biz.princeps.lib.manager;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class Manager {

    private JavaPlugin plugin = PrincepsLib.getPluginInstance();

    protected JavaPlugin getPlugin(){
        return plugin;
    }

    public abstract void saveAll();

    public abstract void loadAll();

}
