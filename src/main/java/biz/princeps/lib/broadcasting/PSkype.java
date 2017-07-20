package biz.princeps.lib.broadcasting;

import biz.princeps.lib.PrincepsLib;
import fr.delthas.skype.Group;
import fr.delthas.skype.Presence;
import fr.delthas.skype.Skype;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/**
 * Created by spatium on 18.06.17.
 */
class PSkype {

    private Skype skype;
    private String username;
    private String password;
    private String groupname;

    public PSkype(String username, String password, String groupname) {
        this.username = username;
        this.password = password;
        this.groupname = groupname;
    }

    public void login() {
        new BukkitRunnable() {

            @Override
            public void run() {
                skype = new Skype(username, password);
                try {
                    skype.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                skype.changePresence(Presence.ONLINE);
                if (skype.isConnected())
                    PrincepsLib.getPluginInstance().getLogger().info("Skype-Connection established!");
            }
        }.runTaskAsynchronously(PrincepsLib.getPluginInstance());
    }

    public void sendMessage(String message) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (skype != null)
                    if (skype.isConnected()) {
                        for (Group chat : skype.getGroups()) {
                            if (chat.getTopic().equals(groupname))
                                chat.sendMessage(message);
                        }
                    }
                cancel();
            }
        }.runTaskAsynchronously(PrincepsLib.getPluginInstance());
    }
}
