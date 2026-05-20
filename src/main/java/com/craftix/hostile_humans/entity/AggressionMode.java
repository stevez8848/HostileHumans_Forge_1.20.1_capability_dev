package com.craftix.hostile_humans.entity;

public enum AggressionMode {

    PASSIVE, AGGRESSIVE_MONSTER, AGGRESSIVE_ALL;

    public static AggressionMode get(String aggressionLevel) {
        if (aggressionLevel == null || aggressionLevel.isEmpty()) {
            return AggressionMode.PASSIVE;
        }
        try {
            return AggressionMode.valueOf(aggressionLevel);
        } catch (IllegalArgumentException e) {
            return AggressionMode.PASSIVE;
        }
    }

    public AggressionMode getNext() {
        return values()[(ordinal() + 1) % values().length];
    }
}
