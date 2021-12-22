package com.sa.entitys;

public enum FactionValues {
    color ("color"),
    lastTransfer ("lastTransfer"),
    power ("power"),
    uuid ("uuid"),
    uuidPlayerOwner ("uuidPlayerOwner"),
    villagerKilled ("villagerKilled"),
    status ("status"),
    name ("name");

    private final String description;       

    private FactionValues(String s) {
    	description = s;
    }

    public boolean equalsName(String otherName) {
        return description.equals(otherName);
    }

    public String toString() {
       return this.description;
    }
}
