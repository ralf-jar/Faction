package com.sa.entitys.items;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class ItemObject {

	private String uuid;
	private String itemKey;
	private String description;
	private String displayName;
	private String materialType;
	private int amount;
	private boolean special;
	private boolean dropeable;
	private ArrayList<ItemEnchantmentObject> enchants; 
	private ArrayList<ItemPriceObject> prices;
	private ArrayList<ItemRecipeObject> recipes;
	
	public ItemObject() {
		
	}
	
	public static ItemObject get() {
		
		return null;
	}
	
	public ItemStack makeItem() {
		
		return null;
	}

	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public boolean isDropeable() {
		return dropeable;
	}

	public void setDropeable(boolean dropeable) {
		this.dropeable = dropeable;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ArrayList<ItemEnchantmentObject> getEnchants() {
		return enchants;
	}

	public void setEnchants(ArrayList<ItemEnchantmentObject> enchants) {
		this.enchants = enchants;
	}

	public ArrayList<ItemPriceObject> getPrices() {
		return prices;
	}

	public void setPrices(ArrayList<ItemPriceObject> prices) {
		this.prices = prices;
	}

	public ArrayList<ItemRecipeObject> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<ItemRecipeObject> recipes) {
		this.recipes = recipes;
	}
	
}
