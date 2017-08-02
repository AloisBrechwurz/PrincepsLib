package biz.princeps.lib.config;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
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

    public void updateConfig() {
        try {
            // New Config
            InputStream streamNew = getClass().getClassLoader().getResourceAsStream(file.getName());
            BufferedReader newCfgReader = new BufferedReader(new InputStreamReader(streamNew));

            //Old config
            File oldFile = file;
            //   BufferedReader oldCfgReader = new BufferedReader(new FileReader(oldFile));

            List<String> lines = Files.readAllLines(oldFile.toPath());
            List<String> newLines = new ArrayList<>();

            // check
            newCfgReader.lines().forEach(s -> {

                int index = -1;
                for (String line : lines) {
                    if (line.equals(s)) {
                        index = lines.indexOf(line);
                    } else if (line.split(":")[0].equals(s.split(":")[0])) {
                        //System.out.println("Found " + s.split("=")[0]);
                        index = lines.indexOf(line);
                    }
                }
                if (index <= 0) {
                    newLines.add(s);
                } else {
                    newLines.add(lines.get(index));
                }

            });
            Files.write(oldFile.toPath(), newLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
