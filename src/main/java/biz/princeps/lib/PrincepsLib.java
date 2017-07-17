package biz.princeps.lib;

import biz.princeps.lib.storage.*;
import biz.princeps.lib.test.TestRequests;
import biz.princeps.lib.test.TestTable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spatium on 18.06.17.
 */
public class PrincepsLib extends JavaPlugin {

    private static JavaPlugin instance;

    @Override
    public void onEnable() {

        setPluginInstance(this);

        DatabaseAPI api = new DatabaseAPI(DatabaseType.MySQL, new TestRequests(), "biz.princeps.lib.test");

        TestTable tab = new TestTable("bllll", 24, 6L, 2.1F, true);
        api.req(TestRequests.class).saveTab(tab);

        // retrieving
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("NAME_COLUMN", "bllll");
        List<Object> toGet = api.retrieveObjects(TestTable.class, conditions);
        System.out.println("got list size: " + toGet.size());

        for (Object o : toGet) {
            System.out.println(((TestTable) o).getCount());
        }
    }


    /**
     * @return your own plugin instance, which you set before
     */
    public static JavaPlugin getPluginInstance() {
        return instance;
    }


    /**
     * You need to call this method in order to assign your own plugin instance to this api
     *
     * @param instance
     */
    public static void setPluginInstance(JavaPlugin instance) {
        PrincepsLib.instance = instance;
    }

    /**
     * Generates a myqsl-data file in your plugin folder
     *
     * @return the specific fileconfig
     */
    public static FileConfiguration prepareDatabaseFile() {
        File file = new File(getPluginInstance().getDataFolder(), "MySQL.yml");

        if (!file.exists())
            try {
                getPluginInstance().getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        config.addDefault("MySQL.Hostname", "localhost");
        config.addDefault("MySQL.Port", 3306);
        config.addDefault("MySQL.Database", "minecraft");
        config.addDefault("MySQL.User", "root");
        config.addDefault("MySQL.Password", "passy");
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }
}
