package biz.princeps.lib.crossversion;

/**
 * Created by spatium on 28.07.17.
 */
public enum CParticle {

    VILLAGERHAPPY("HAPPY_VILLAGER", "VILLAGER_HAPPY", 21),
    DRIPWATER("WATERDRIP", "DRIP_WATER", 18),
    DRIPLAVA("LAVADRIP", "DRIP_LAVA", 19),
    WITCHMAGIC("WITCHMAGIC", "SPELL_WITCH", 17);

    private final String v18;
    private final String v19;
    private final int id;

    CParticle(String v18, String v19, int id) {
        this.v18 = v18;
        this.v19 = v19;
        this.id = id;
    }

    public String getV18() {
        return v18;
    }

    public String getV19() {
        return v19;
    }

    public int getID() {
        return id;
    }
}
