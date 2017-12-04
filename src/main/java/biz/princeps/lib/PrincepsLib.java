package biz.princeps.lib;

import biz.princeps.lib.chat.MultiPagedMessage;
import biz.princeps.lib.crossversion.CParticle;
import biz.princeps.lib.crossversion.CrossVersion;
import biz.princeps.lib.item.ItemManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by spatium on 18.06.17.
 */
public class PrincepsLib extends JavaPlugin implements Listener {

    private static JavaPlugin instance;
    private static CrossVersion crossVersion;
    private static ItemManager itemManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {

            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    crossVersion.spawnParticle(p, p.getLocation().add(0, 0.1, 0), CParticle.VILLAGERHAPPY, 10);

                }
            }
        }.runTaskTimer(this, 20, 20);
    }

    @Override
    public void onEnable() {
        setPluginInstance(this);

        // DatabaseAPI api = new DatabaseAPI(DatabaseType.SQLite, new TestRequests(), "biz.princeps.lib.test");

        // PrincepsLib.getItemManager().registerItem(TestItem.name, TestItem.class);

        // TestItem item = new TestItem();

        getServer().getPluginManager().registerEvents(this, this);

        new BukkitRunnable() {

            @Override
            public void run() {
                Player p = Bukkit.getOnlinePlayers().iterator().next();


            }
        }.runTaskLater(this, 100L);


        getCommand("msgtest").setExecutor((commandSender, command, s, strings) -> {
            List<String> list = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                if (i % 2 == 0)
                    list.add(ChatColor.RED + getRandomText().getText());
                else{
                    list.add(ChatColor.AQUA + getRandomText().getText());

                }
            }
            MultiPagedMessage msg = new MultiPagedMessage.Builder(5, list).setHeaderString("header")
                    .setNextString("NEXT ==>")
                    .setPreviousString("<== PREVIOUS ")
                    .setCommand("msgtest", strings).build();
            commandSender.spigot().sendMessage(msg.create());
            return true;
        });
    }

    public TextComponent getRandomText() {
        char[] text = new char[70];
        String characters = "EKRJEDaool,3idjapsjdxcmpaejrip";
        for (int i = 0; i < text.length; i++) {
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
        PrincepsLib.instance = instance;
        PrincepsLib.crossVersion = new CrossVersion();
        PrincepsLib.itemManager = new ItemManager();
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

    public static ItemManager getItemManager() {
        return itemManager;
    }
}
