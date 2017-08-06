package biz.princeps.lib;

import biz.princeps.lib.crossversion.CrossVersion;
import biz.princeps.lib.storage.DatabaseAPI;
import biz.princeps.lib.storage.DatabaseType;
import biz.princeps.lib.test.TestRequests;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by spatium on 18.06.17.
 */
public class PrincepsLib extends JavaPlugin {

    private static JavaPlugin instance;
    private static CrossVersion crossVersion;

    @Override
    public void onEnable() {

        System.out.println("test");

      //  setPluginInstance(this);

        //DatabaseAPI api = new DatabaseAPI(DatabaseType.SQLite, new TestRequests(), "biz.princeps.lib.test");
    }

    public TextComponent getRandomText() {
        char[] text = new char[10];
        String characters = "EKRJEDaool,3idjapsjdxcmpaejrip";
        for (int i = 0; i < 9; i++) {
            text[i] = characters.charAt(new Random().nextInt(characters.length()));
        }
        TextComponent cp = new TextComponent(new String(text));
        cp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, new String(text)));
        return cp;
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
        PrincepsLib.crossVersion = new CrossVersion();
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

    public static CrossVersion crossVersion() {
        return crossVersion;
    }
}
