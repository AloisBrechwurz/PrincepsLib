package biz.princeps.lib.crossversion.v1_8_R3;

import biz.princeps.lib.crossversion.IActionBar;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by spatium on 28.07.17.
 */
public class ActionBar implements IActionBar {

    @Override
    public void sendActionBar(Player player, String msg) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(msg), (byte)2);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
