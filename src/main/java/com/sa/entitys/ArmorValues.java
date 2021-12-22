package com.sa.entitys;

import org.bukkit.Material;

public enum ArmorValues {
	
	turtleHelmet(2),
	leatherHelmet(1),
	leatherChestplate(3),
	leatherLeggings(2),
	leatherBoots(1),
	goldenHelmet(2),
	goldenChestplate(5),
	goldenLeggings(3),
	goldenBoots(1),
	chainmailHelmet(2),
	chainmailChestplate(5),
	chainmailLeggings(4),
	chainmailBoots(1),
	ironHelmet(2),
	ironChestplate(6),
	ironLeggings(5),
	ironBoots(2),
	diamondHelmet(3),
	diamondChestplate(8),
	diamondLeggings(6),
	diamondBoots(3),
	netheriteHelmet(3),
	netheriteChestplate(8),
	netheriteLeggings(6),
	netheriteBoots(3);
	
	private final double value;       
	
    private ArmorValues(double b) {
    	value = b;
    }

    public double getValue() {
       return this.value;
    }
    
    public static double getValue(Material type) {
        
    	switch(type.toString().toLowerCase().replace("legacy_","")) {
	    	case "turtle_helmet":
	            return turtleHelmet.getValue();
		    case "leather_helmet":
		        return leatherHelmet.getValue();
		    case "leather_chestplate":
		        return leatherChestplate.getValue();
		    case "leather_leggings":
		            return leatherLeggings.getValue();
		    case "leather_boots":
		            return leatherBoots.getValue();
		    case "golden_helmet":
		            return goldenHelmet.getValue();
		    case "golden_chestplate":
		            return goldenChestplate.getValue();
		    case "golden_leggings":
		            return goldenLeggings.getValue();
		    case "golden_boots":
		            return goldenBoots.getValue();
		    case "chainmail_helmet":
		            return chainmailHelmet.getValue();
		    case "chainmail_chestplate":
		            return chainmailChestplate.getValue();
		    case "chainmail_leggings":
		            return chainmailLeggings.getValue();
		    case "chainmail_boots":
		            return chainmailBoots.getValue();
		    case "iron_helmet":
		            return ironHelmet.getValue();
		    case "iron_chestplate":
		            return ironChestplate.getValue();
		    case "iron_leggings":
		            return ironLeggings.getValue();
		    case "iron_boots":
		            return ironBoots.getValue();
		    case "diamond_helmet":
		            return diamondHelmet.getValue();
		    case "diamond_chestplate":
		            return diamondChestplate.getValue();
		    case "diamond_leggings":
		            return diamondLeggings.getValue();
		    case "diamond_boots":
		            return diamondBoots.getValue();
		    case "netherite_helmet":
		            return netheriteHelmet.getValue();
		    case "netherite_chestplate":
		            return netheriteChestplate.getValue();
		    case "netherite_leggings":
		            return netheriteLeggings.getValue();
		    case "netherite_boots":
		            return netheriteBoots.getValue();
    	}
    	
    	return 0;
     }
}
