package com.craftix.hostile_humans.entity;

import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.level.Level;

import java.util.Random;

public class HumanAbility {

    protected final Random random = new Random();

    protected short ticker = 0;
    protected Level level;
    protected HumanEntity humanEntity;
    protected NeutralMob neutralMob;

    public HumanAbility(HumanEntity humanEntity, Level level) {

        this.level = level;
        this.humanEntity = humanEntity;

        if (humanEntity instanceof NeutralMob neutralMobCast) {
            this.neutralMob = neutralMobCast;
        }

        this.ticker = (short) this.random.nextInt(0, 25);
    }

    protected void aiStep() {

    }

    protected void tick() {

    }
}
