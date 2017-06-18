package biz.princeps.lib.broadcasting;

import biz.princeps.lib.PrincepsLib;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by spatium on 18.06.17.
 */
public class Discord {

    private String token;
    private String channelid;
    private String gamename;
    private DiscordAPI discord;

    public Discord(String token, String channelid, String gamename) {
        this.token = token;
        this.channelid = channelid;
        this.gamename = gamename;
    }

    public void connect() {
        discord = Javacord.getApi(token, true);
        discord.connect(new FutureCallback<DiscordAPI>() {

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSuccess(DiscordAPI api) {
                discord = api;
                discord.setGame(gamename);
            }
        });
    }

    public void sendMessage(String message) {
        new BukkitRunnable(){

            @Override
            public void run() {
                discord.getChannelById(channelid).sendMessage(message);
                cancel();
            }
        }.runTaskAsynchronously(PrincepsLib.getInstance());
    }

    public void disconnect(){
        discord.disconnect();
    }


}
