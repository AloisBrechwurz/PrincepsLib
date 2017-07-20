package biz.princeps.lib.broadcasting;

/**
 * Created by spatium on 20.07.17.
 */
public class BroadcastingAPI {

    private static Discord discord;
    private static PSkype skype;
    private static Telegram telegram;

    public static Discord getDiscord() {
        return discord;
    }

    public static PSkype getSkype() {
        return skype;
    }

    public static Telegram getTelegram() {
        return telegram;
    }

    public static Discord createDiscord(String token, String channelid, String gamename) {
        discord = new Discord(token, channelid, gamename);
        return discord;
    }

    public static PSkype createSkype(String username, String password, String groupname) {
        skype = new PSkype(username, password, groupname);
        return skype;
    }

    public static Telegram createTelegram(String auth, String chatid) {
        telegram = new Telegram(auth, chatid);
        return telegram;
    }

}
