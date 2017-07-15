package biz.princeps.lib;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by spatium on 18.06.17.
 */
public class PrincepsLib extends JavaPlugin {

    private static PrincepsLib instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static PrincepsLib getInstance() {
        return instance;
    }


}
