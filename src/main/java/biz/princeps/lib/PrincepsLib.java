package biz.princeps.lib;

import biz.princeps.lib.storage.DatabaseAPI;
import biz.princeps.lib.storage.MySQL;
import biz.princeps.lib.test.MySQLDB;
import biz.princeps.lib.test.TestTable;
import org.bukkit.plugin.java.JavaPlugin;

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

        MySQLDB db = new MySQLDB(getLogger(), "localhost", 3306, "minecraft", "morses", "1234");
        DatabaseAPI api = getDatabaseAPI(db, "biz.princeps.lib.test");

        TestTable tab = new TestTable("bllll", 23, 2L, 2F, false);
        // api.saveObject(tab);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("count", 23);
        List<Object> toGet = api.retrieveObjects(TestTable.class, conditions);
        System.out.println("got list size: " + toGet.size());

        for (Object o : toGet) {
            System.out.println(((TestTable) o).getCount());
        }
    }

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
     * @param db          the database object the api is supposed to work with
     * @param packageName the packagename your database table classes will be located in
     * @return A new instance of a DatabaseAPI object
     */
    public static DatabaseAPI getDatabaseAPI(MySQL db, String packageName) {
        DatabaseAPI api = new DatabaseAPI(db);
        api.scan(packageName);

        return api;
    }

}
