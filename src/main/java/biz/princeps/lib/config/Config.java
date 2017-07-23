package biz.princeps.lib.config;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by spatium on 22.07.17 / 13:35.
 */
public class Config {

    private File file;
    private FileConfiguration config;

    private List<String> entries;

    public Config(String folder, String file) {
        this.file = new File(folder + "/" + file);
        config = new YamlConfiguration();
        entries = new ArrayList<>();

        File fold = new File(folder);
        if (!fold.exists())
            fold.mkdirs();

        if (!this.file.exists()) {
            PrincepsLib.getPluginInstance().saveResource(file, false);
        }
        try {
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        mapFile();
    }

    private void mapFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(s -> entries.add(s));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        entries.forEach(System.out::println);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }




}
