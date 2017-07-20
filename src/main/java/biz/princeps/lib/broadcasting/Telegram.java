package biz.princeps.lib.broadcasting;

import biz.princeps.lib.PrincepsLib;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.URL;

/**
 * Created by spatium on 20.07.17.
 */
public class Telegram {

    private String auth, chatid;

    public Telegram(String auth, String chatid) {
        this.auth = auth;
        this.chatid = chatid;
    }

    /**
     * Send a message via telegram to a defined chat.
     * Define your chat in the config.yml
     *
     * @param msg this is your message you want to send.
     */
    public void sendMessage(String msg){
        StringBuilder url = new StringBuilder();
        url.append("https://api.telegram.org/bot")
                .append(auth)
                .append("/sendMessage?chat_id=")
                .append(chatid)
                .append("&text=")
                .append(msg);

        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    new URL(url.toString()).openStream().close();
                } catch (IOException e) {
                    PrincepsLib.getPluginInstance().getLogger().warning("Could not send message via telegram: " + e);
                };
                cancel();
            }

        }.runTaskAsynchronously(PrincepsLib.getPluginInstance());
    }


}
