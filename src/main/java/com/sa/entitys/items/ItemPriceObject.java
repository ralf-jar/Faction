package com.sa.entitys.items;

public class ItemPriceObject {
	
	private String uuid;
	private int itemRecipeSlot; 
	private int requerimentPowerLevel; 
	private int maxSalableAmount;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getItemRecipeSlot() {
		return itemRecipeSlot;
	}
	public void setItemRecipeSlot(int itemRecipeSlot) {
		this.itemRecipeSlot = itemRecipeSlot;
	}
	public int getRequerimentPowerLevel() {
		return requerimentPowerLevel;
	}
	public void setRequerimentPowerLevel(int requerimentPowerLevel) {
		this.requerimentPowerLevel = requerimentPowerLevel;
	}
	public int getMaxSalableAmount() {
		return maxSalableAmount;
	}
	public void setMaxSalableAmount(int maxSalableAmount) {
		this.maxSalableAmount = maxSalableAmount;
	}
	
	
	
}
