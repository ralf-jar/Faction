package com.sa.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.sa.entitys.ArmorValues;
import com.sa.entitys.PlayerObject;
import com.sa.faccion.Chat;
import com.sa.faccion.Main;

public class EventsPowerLevel implements Listener {	

	private HashMap<String, PlayerObject> players;
	private HashMap<String, String> parameters;

	public EventsPowerLevel(HashMap<String, PlayerObject> players, HashMap<String, String> parameters, HashMap<String, String> messages, Main main) {
		this.players = players;
		this.parameters = parameters;
		timer(main);
	}
	
	public void timer(Main main){
        main.getServer().getScheduler().runTaskTimer(main, new Runnable(){
            public void run(){
            	for(Map.Entry<String, PlayerObject> entry : players.entrySet()) {
            		double powerDamage = getPowerDamage(Bukkit.getPlayer(UUID.fromString(entry.getKey())));
        			players.get(entry.getValue().getUuid()).setPower(powerDamage);
	            	Chat.sendPowerStatus(entry.getValue());
	             }
            }
        }, 0, 50);
    }

	private static double getMaterialDamageValue(ItemStack item) {
		boolean isAxe = false;
		boolean isDef = false;
		double valueDamage = 0;
		
		if(item.getType().toString().contains("_SWORD")) {
			valueDamage = 4;
			valueDamage += getEnchantmentDamageValue(item);
		}else if(item.getType().toString().contains("_AXE")) {
			valueDamage = 7;
			valueDamage += getEnchantmentDamageValue(item);
			isAxe = true;
		}else if(item.getType().toString().contains("_HELMET") || item.getType().toString().contains("_BOOTS") ||
				item.getType().toString().contains("_CHESTPLATE") || item.getType().toString().contains("_LEGGINGS")) {
			valueDamage += getEnchantmentDamageValue(item);
			valueDamage += ArmorValues.getValue(item.getType());
			isDef = true;
		}

		if(item.getType().toString().contains("STONE") ) {
			if(!isDef) {
				if(isAxe) {
					valueDamage += 2;
				}else {
					valueDamage += 1;
				}
			}else {
				
			}
			
		}else if(item.getType().toString().contains("IRON")) {
			if(!isDef) {
				valueDamage += 2;
			}else {
				
			}
				
		}else if(item.getType().toString().contains("DIAMOND") ||item.getType().toString().contains("NETHERITE") ) {
			if(!isDef) {
				if(isAxe) {
					valueDamage += 2;
				}else {
					valueDamage += 3;
				}
			}else {
				
			}
		}
		return valueDamage;
	}


	private static double getEnchantmentDamageValue(ItemStack item) {

		if(!item.getEnchantments().isEmpty() ) {
			if(item.getType().toString().contains("_SWORD")) {
				for(Entry<Enchantment, Integer> entry: item.getEnchantments().entrySet()) {
					if(entry.getKey().equals(Enchantment.DAMAGE_ALL)) {
						return (entry.getValue() * 0.5) + 1;
					}
				}
			}else if(item.getType().toString().contains("_AXE")) {
				for(Entry<Enchantment, Integer> entry: item.getEnchantments().entrySet()) {
					if(entry.getKey().equals(Enchantment.DAMAGE_ALL)) {
						return (entry.getValue() * 0.5);
					}
				}
			}else if(item.getType().toString().contains("_HELMET") || item.getType().toString().contains("_BOOTS") ||
					item.getType().toString().contains("_CHESTPLATE") || item.getType().toString().contains("_LEGGINGS")) {
				for(Entry<Enchantment, Integer> entry: item.getEnchantments().entrySet()) {
					if(entry.getKey().equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
						return entry.getValue() * 0.5;
					}
				}
			}
		}

		return 0;
	}

	public static double getPowerDamage(Player player) {
		double totalPower = 0;
		
		ArrayList<Double> power = new ArrayList<Double>();
		ArrayList<Double> defHealment = new ArrayList<Double>();
		ArrayList<Double> defChestplate = new ArrayList<Double>();
		ArrayList<Double> defLeggigns = new ArrayList<Double>();
		ArrayList<Double> defBoots = new ArrayList<Double>();
		
		for(ItemStack item : player.getInventory().getContents()) {
			if(item != null) {
				double calcule = 0;
				if(item.getType().toString().contains("_SWORD") || item.getType().toString().contains("_AXE")) {
					calcule = getMaterialDamageValue(item);
					power.add(calcule);
				}else if(item.getType().toString().contains("_HELMET")) {
					calcule = getMaterialDamageValue(item);
					defHealment.add(calcule);
				}else if(item.getType().toString().contains("_CHESTPLATE")) {
					calcule = getMaterialDamageValue(item);
					defChestplate.add(calcule);
				}else if(item.getType().toString().contains("_LEGGINGS")) {
					calcule = getMaterialDamageValue(item);
					defLeggigns.add(calcule);
				}else if(item.getType().toString().contains("_BOOTS")) {
					calcule = getMaterialDamageValue(item);
					defBoots.add(calcule);
				} 
			}
		}
		
		if(!power.isEmpty())
			totalPower += Collections.max(power);
		
		if(!defHealment.isEmpty())
			totalPower += Collections.max(defHealment);
		
		if(!defChestplate.isEmpty())
			totalPower += Collections.max(defChestplate);
		
		if(!defLeggigns.isEmpty())
			totalPower += Collections.max(defLeggigns);
		
		if(!defBoots.isEmpty())
			totalPower += Collections.max(defBoots);
		
		return totalPower;
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event){
		String uuid = event.getPlayer().getUniqueId().toString();
		if(players.get(uuid) != null) {
			double powerDamage = getPowerDamage(event.getPlayer());
			players.get(uuid).setPower(powerDamage);
			Chat.sendPowerStatus(players.get(uuid));
		}
	}
	
	
	@EventHandler
	public void onPlayerPick(EntityPickupItemEvent event){
		if(event.getEntity().getType() == EntityType.PLAYER) {
			String uuid = event.getEntity().getUniqueId().toString();
			if(players.get(uuid) != null) {
				double powerDamage = getPowerDamage((Player) event.getEntity());
				players.get(uuid).setPower(powerDamage);
				Chat.sendPowerStatus(players.get(uuid));
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		if(players.get(uuid) != null) {
			players.get(uuid).setPower(0);
			Chat.sendPowerStatus(players.get(uuid));
		}
	}
	
	@EventHandler
	public void onPlayerDamagedByMonster(EntityDamageByEntityEvent e) {
		if(e.getDamager().getType() != EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {
			String player  = ((Player) e.getEntity()).getUniqueId().toString();
			double maxPowerDamage = Double.parseDouble(parameters.get("maxpowerdamage"));
			maxPowerDamage = players.get(player).getPower() / maxPowerDamage;
			e.setDamage(e.getDamage() + (e.getDamage()*maxPowerDamage));
		}
	}

}

