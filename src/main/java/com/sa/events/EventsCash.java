package com.sa.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.sa.entitys.FactionObject;
import com.sa.entitys.PlayerObject;
import com.sa.faccion.Chat;

import net.kyori.adventure.text.Component;

public class EventsCash implements Listener {


	private HashMap<String, String> parameters;
	private HashMap<String, PlayerObject> players;
	private HashMap<String, FactionObject> factions;
	private HashMap<String, String> messages;
	
	public EventsCash(HashMap<String, PlayerObject> players, HashMap<String, FactionObject> factions, HashMap<String, String> parameters, HashMap<String, String> messages) {
		this.parameters = parameters;
		this.players = players;
		this.factions = factions;
		this.messages = messages;
	}
	
	@EventHandler
	public void onServerCommand(ServerCommandEvent e) {
		if(e.getCommand().toLowerCase().equals("/errante")) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				Location loc = player.getLocation().clone();
				loc.setX(loc.getX()+2);
				
				int prize = Integer.parseInt(parameters.get("prizeitem01"));
				
				ItemStack item = new ItemStack(Material.NETHERITE_SWORD, 1);
				ItemStack itemCash = new ItemStack(Material.EMERALD, prize);
				ItemStack itemReward = new ItemStack(Material.EMERALD, 1);
				
				ItemMeta metaCash = itemCash.getItemMeta();
				metaCash.displayName(Component.text("&6SA Cash"));
				itemCash.setItemMeta(metaCash);
				
				ItemMeta metaReward = itemReward.getItemMeta();
				metaReward.displayName(Component.text("&7[&4Lord of Damage&7]"));
				itemReward.setItemMeta(metaReward);
				itemReward.addEnchantment(Enchantment.DAMAGE_ALL, 10);
				
				MerchantRecipe merchRec = new MerchantRecipe(item, 1);
				merchRec.addIngredient(new ItemStack(Material.NETHERITE_INGOT,1));
				merchRec.addIngredient(itemCash);
				merchRec.adjust(itemReward);

				WanderingTrader trader = (WanderingTrader) player.getWorld().spawnEntity(loc, EntityType.WANDERING_TRADER);
				trader.setRecipe(0, merchRec);
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		String uuidPlayer = e.getPlayer().getUniqueId().toString();
		String[] command = e.getMessage().split(" ");
		
		if(players.get(uuidPlayer) != null) {
			switch(command[0].toLowerCase()) {
			case "/baltop":
				
				break;
			case "/bal":
				
				break;
			case "/pay": //
				
				break;
			case "/trade": //Cash to Emerald
				
				break;
			case "/buy": //Virtual Chest 9x1
				
				break;
			case "/cash":
				if(command.length > 1) {
					switch(command[1].toLowerCase()) {
					case "reset":
						
						break;
					case "give":
						
						break;
					case "set":
						
						break;
					default:
						break;
					}
				}else {
					Chat.recept(messages.get("RFTCC01"), e.getPlayer());
				}
				break;
			default:
				break;
			}
		}
	}
	
}
