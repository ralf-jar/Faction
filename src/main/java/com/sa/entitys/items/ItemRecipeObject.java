package com.sa.entitys.items;

public class ItemRecipeObject {
	
	private String uuid; 
	
	private String materialTypeOne; 
	private int amountOne;
	private boolean cashOne; 
	private ItemPriceObject priceOne;
	
	private String materialTypeTwo;
	private int amountTwo;
	private boolean cashTwo;
	private ItemPriceObject priceTwo;
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getMaterialTypeOne() {
		return materialTypeOne;
	}
	public void setMaterialTypeOne(String materialTypeOne) {
		this.materialTypeOne = materialTypeOne;
	}
	public int getAmountOne() {
		return amountOne;
	}
	public void setAmountOne(int amountOne) {
		this.amountOne = amountOne;
	}
	public boolean isCashOne() {
		return cashOne;
	}
	public void setCashOne(boolean cashOne) {
		this.cashOne = cashOne;
	}
	public ItemPriceObject getPriceOne() {
		return priceOne;
	}
	public void setPriceOne(ItemPriceObject priceOne) {
		this.priceOne = priceOne;
	}
	public String getMaterialTypeTwo() {
		return materialTypeTwo;
	}
	public void setMaterialTypeTwo(String materialTypeTwo) {
		this.materialTypeTwo = materialTypeTwo;
	}
	public int getAmountTwo() {
		return amountTwo;
	}
	public void setAmountTwo(int amountTwo) {
		this.amountTwo = amountTwo;
	}
	public boolean isCashTwo() {
		return cashTwo;
	}
	public void setCashTwo(boolean cashTwo) {
		this.cashTwo = cashTwo;
	}
	public ItemPriceObject getPriceTwo() {
		return priceTwo;
	}
	public void setPriceTwo(ItemPriceObject priceTwo) {
		this.priceTwo = priceTwo;
	}
	
	
	
}
