package biz.princeps.lib.test;

import biz.princeps.lib.gui.MultiPagedGUI;
import biz.princeps.lib.gui.simple.AbstractGUI;
import biz.princeps.lib.gui.simple.Icon;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Project: PrincepsLib
 * Created by Alex D. (SpatiumPrinceps)
 * Date: 11/18/17
 */
public class TestGUI extends MultiPagedGUI {

    public TestGUI(Player player, List<Icon> icons, AbstractGUI main) {
        super(player, 2, "Seiten: %site%/%maxsite%", icons, main);
    }


}
