package com.craftix.hostile_humans.entity.entities;

public enum HumanTier {
    LEVEL1(1), LEVEL2(2), ROAMER(9), RONIN(10), SAMURAI1(11), SAMURAI2(12), BANDIT(13), MERCENARY(14);
    public final int id;

    HumanTier(int value) {
        this.id = value;
    }

    public static HumanTier byId(int id) {
        for (HumanTier b : HumanTier.values()) {
            if (b.id == (id)) {
                return b;
            }
        }
        return null;
    }
}
