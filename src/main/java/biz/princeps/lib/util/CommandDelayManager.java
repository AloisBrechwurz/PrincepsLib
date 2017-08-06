package biz.princeps.lib.util;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.crossversion.CParticle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Created by spatium on 21.06.17.
 */
public class CommandDelayManager implements Listener {

    private HashMap<String, Integer> commandDelays;
    private String dontMove, youMoved, countdown;
    private boolean spawnParticles;

    private HashMap<UUID, Integer> playertasks;

    public CommandDelayManager(String dontMove, String youMoved, String countdown, boolean spawnParticles) {
        commandDelays = new HashMap<>();
        playertasks = new HashMap<>();

        this.dontMove = dontMove;
        this.youMoved = youMoved;
        this.countdown = countdown;

        this.spawnParticles = spawnParticles;

        PrincepsLib.getPluginInstance().getServer().getPluginManager().registerEvents(this, PrincepsLib.getPluginInstance());
    }

    public void delayCommand(String command, int time) {
        commandDelays.put(command, time);
    }

    /*
    public void sendCountdown(final Player p, final int delay) {
        new BukkitRunnable() {
            int countdown = delay;

            @Override
            public void run() {
                if (countdown == 0) {
                    PrincepsLib.crossVersion().sendActionBar(p, "");
                    cancel();
                } else {
                    PrincepsLib.crossVersion().sendActionBar(p, ChatColor.translateAlternateColorCodes('&', getCountdown()).replace("%countdown%", "" + countdown));
                    countdown--;
                }
            }
        }.runTaskTimer(PrincepsLib.getPluginInstance(), 0, 20L);
    }
    */

    public boolean isDelayedCommand(String s) {
        for (String s1 : commandDelays.keySet()) {
            if (s.equals(s1))
                return true;
        }
        return false;

        // return (commandDelays.keySet().contains(s));
    }

    public int getDelay(String s) {
        return commandDelays.get(s);
    }

    public void addPlayerTask(UUID id, int taskid) {
        playertasks.put(id, taskid);
    }

    public boolean hasTask(UUID id) {
        return playertasks.keySet().contains(id);
    }

    public void removeTask(UUID id) {
        playertasks.remove(id);
    }

    public int getTaskID(UUID id) {
        return playertasks.get(id);
    }

    public String getDontMove() {
        return dontMove;
    }

    public String getYouMoved() {
        return youMoved;
    }

    public String getCountdown() {
        return countdown;
    }


    /**
     * Stuff for listening to the actual command
     *
     * @param e
     */
    @EventHandler
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();

        String command = e.getMessage();
        //  command = command.substring(1, command.length());
        //   System.out.println("found command: " + command);
        if (this.isDelayedCommand(command.toLowerCase())) {
            //  System.out.println("found delayed cmd");
            if (!this.hasTask(p.getUniqueId())) {
                int delay = this.getDelay(command.toLowerCase());
                e.setCancelled(true);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getDontMove()).replace("%time%", "" + delay));
                // Utils.displayHelix(p, delay);

                int taskid = new BukkitRunnable() {
                    int countdown = delay * 4;
                    int var = 0;

                    @Override
                    public void run() {
                        if (countdown == 0) {
                            PrincepsLib.crossVersion().sendActionBar(p, "");
                            PrincepsLib.getPluginInstance().getServer().dispatchCommand(p, e.getMessage().substring(1, e.getMessage().length()));
                            removeTask(p.getUniqueId());
                            cancel();
                        } else {
                            if (countdown % 4 == 0)
                                PrincepsLib.crossVersion().sendActionBar(p, ChatColor.translateAlternateColorCodes('&', getCountdown()).replace("%countdown%", "" + countdown / 4));

                            if (spawnParticles) {
                                Location loc = p.getLocation();
                                List<Location> list = MathUtil.helix(loc, 1, 30, var);
                                for (Location location : list) {
                                    //         location.getWorld().playEffect(location, Effect.WITCH_MAGIC, 0);
                                    PrincepsLib.crossVersion().spawnParticle(location, CParticle.WITCHMAGIC, 2);
                                }
                            }
                            var++;

                            countdown--;
                        }
                    }
                }.runTaskTimer(PrincepsLib.getPluginInstance(), 0, 5L).getTaskId();

                this.addPlayerTask(p.getUniqueId(), taskid);
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (this.hasTask(p.getUniqueId())) {
                PrincepsLib.getPluginInstance().getServer().getScheduler().cancelTask(this.getTaskID(p.getUniqueId()));
                this.removeTask(p.getUniqueId());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getYouMoved()));
                PrincepsLib.crossVersion().sendActionBar(p, "");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        Player p = ev.getPlayer();
        if (this.hasTask(p.getUniqueId())) {
            PrincepsLib.getPluginInstance().getServer().getScheduler().cancelTask(this.getTaskID(p.getUniqueId()));
            this.removeTask(p.getUniqueId());
        }
    }


}