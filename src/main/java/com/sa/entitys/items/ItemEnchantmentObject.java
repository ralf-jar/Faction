package com.sa.entitys.items;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class ItemEnchantmentObject {
	
	private String uuid;
	private String enchantmentName;
	private int value;
	
	public ItemEnchantmentObject() {
		
	}
	
	public static ItemEnchantmentObject get(String uuidItem) {
		return null;
	}
	
	public Enchantment enchantment() {
		Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
		return enchant;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEnchantmentName() {
		return enchantmentName;
	}

	public void setEnchantmentName(String enchantmentName) {
		this.enchantmentName = enchantmentName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	
}
