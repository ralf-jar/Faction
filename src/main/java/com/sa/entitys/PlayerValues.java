package com.sa.entitys;

public enum PlayerValues {
	dailyDeaths ("dailyDeaths"),
	deaths ("deaths"),
	kills ("kills"),
	password ("password"),
	sessionStart ("sessionStart"),
	sessionEnd ("sessionEnd"),
	factionOwner ("factionOwner"),
	starterProtection ("starterProtection"),
	powerLevel ("powerLevel"),
	uuidFaction ("uuidFaction"),
	peace ("peace");
	
    private final String description;       

    private PlayerValues(String s) {
    	description = s;
    }

    public boolean equalsName(String otherName) {
        return description.equals(otherName);
    }

    public String toString() {
       return this.description;
    }

}
