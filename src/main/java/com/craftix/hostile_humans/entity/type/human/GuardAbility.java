package com.craftix.hostile_humans.entity.type.human;

import com.craftix.hostile_humans.entity.HumanAbility;
import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class GuardAbility extends HumanAbility {

    private static final short GUARD_TICK = 20 * 60;

    public GuardAbility(HumanEntity humanEntity, Level level) {
        super(humanEntity, level);
    }

    private void guardTick() {
        if (!level.isClientSide && ticker++ >= GUARD_TICK) {
            ticker = 0;
        }
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide && this.neutralMob != null) {
            this.neutralMob.updatePersistentAnger((ServerLevel) this.level, true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        guardTick();
    }
}
