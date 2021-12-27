package com.sa.events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import com.sa.entitys.PeaceAgreementObject;
import com.sa.entitys.PlayerObject;
import com.sa.faccion.Main;
import com.sa.faccion.Chat;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class EventsPeaceAgreement implements Listener {
	private HashMap<String, PlayerObject> players;
	private ArrayList<PeaceAgreementObject> peaceAgreements;
	private HashMap<String, String> parameters;
	private HashMap<String, String> messages;
	
	public EventsPeaceAgreement(HashMap<String, PlayerObject> players, HashMap<String, String> parameters, HashMap<String, String> messages, Main main) {
		setPlayers(players);
		peaceAgreements = new ArrayList<PeaceAgreementObject>();
		this.parameters = parameters;
		this.messages = messages; 
		timer(main);
	}
	
	public void timer(Main main){
        main.getServer().getScheduler().runTaskTimer(main, new Runnable(){
            @SuppressWarnings("deprecation")
			public void run(){
            	for(int i = peaceAgreements.size()-1; i != -1; i--) {
            		
            		Player playerTarget = Bukkit.getPlayer(UUID.fromString(peaceAgreements.get(i).getPlayerKillerUUID()));
            		Player playerWithCompass = Bukkit.getPlayer(UUID.fromString(peaceAgreements.get(i).getPlayerUUID()));
    				
            		if(peaceAgreements.get(i).isActive()) {
            			if(playerWithCompass.isOnline()) {
        					for(int l = 0; playerWithCompass.getInventory().getSize() < l; l++) {
        						
        						String displayName = "";
        						if(Main.isPaper()) {
        							displayName = PlainTextComponentSerializer.plainText().serialize(playerWithCompass.getInventory().getItem(l).getItemMeta().displayName());
        						}else {
        							displayName = playerWithCompass.getInventory().getItem(l).getItemMeta().getDisplayName();
        						}
        						
        						if(playerWithCompass.getInventory().getItem(l) != null 
        								&& playerWithCompass.getInventory().getItem(l).getType() == Material.COMPASS 
        								&& displayName.startsWith(ChatColor.RED+"")) {
        								
    								CompassMeta compassMeta = (CompassMeta) playerWithCompass.getInventory().getItem(l).getItemMeta();
    		        				Location locationLodestone = compassMeta.getLodestone();
    		        				Bukkit.getWorld(playerTarget.getWorld().getUID()).setBlockData(locationLodestone, Bukkit.createBlockData(Material.BEDROCK));
    		        				
    		        				Location newLocation = playerTarget.getLocation();
    		        				newLocation.setY(-59);
    		        				compassMeta.setLodestone(newLocation);
    		        				playerWithCompass.getInventory().getItem(l).setItemMeta(compassMeta);
    		        				Bukkit.getWorld(playerTarget.getWorld().getUID()).setBlockData(newLocation, Bukkit.createBlockData(Material.LODESTONE));			
    								break;
        						}
        					}
        				}	
            		}else {
            			peaceAgreements.remove(i);
            			Chat.title(messages.get("TPAA01"),messages.get("SPAA01"),playerWithCompass);
            		}
            	}
            }
        }, 0, 60);
    }

	@EventHandler
	public void onPlayerDeaths(PlayerDeathEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			Player playerKiller = player.getKiller();
			
			if(player != playerKiller) {
				String uuidPlayer = player.getUniqueId().toString();
				String uuidPlayerKiller = playerKiller.getUniqueId().toString();

				boolean exists = false;
				for(int i = 0; i < peaceAgreements.size(); i++) {
					if(peaceAgreements.get(i).getPlayerKillerUUID().equals(uuidPlayer) 
							&& peaceAgreements.get(i).getPlayerUUID().equals(uuidPlayerKiller)) {
						peaceAgreements.remove(i);
						exists = true;
						Chat.title(messages.get("TPAA04"), messages.get("SPAA04"), playerKiller);
						
						break;
					}
				}
				
				//Si no fue una muerte por venganza entonces disparamos un tratado de paz.
				if(!exists) {
					PeaceAgreementObject peace = new PeaceAgreementObject();
					peace.setDateStart(LocalDateTime.now());
					peace.setDuration(Integer.parseInt(parameters.get("peacetime")));
					peace.setPlayerUUID(uuidPlayer);
					peace.setPlayerKillerUUID(uuidPlayerKiller);
					peaceAgreements.add(peace);	
				}				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event){
		String uuid = event.getPlayer().getUniqueId().toString();
		if(players.get(uuid) != null) {
			String displayName = "";
			if(Main.isPaper()) {
				displayName = PlainTextComponentSerializer.plainText().serialize(event.getItemDrop().getItemStack().getItemMeta().displayName());
			}else {
				displayName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
			}
			
			if(event.getItemDrop().getItemStack().getType() == Material.COMPASS && displayName.startsWith(ChatColor.RED + "")) {
				Chat.recept(messages.get("RPAD01"), event.getPlayer());
			}
		}
	}
	
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) 
	{
		try {
			if(e.getMessage().toLowerCase().equals("/revenge off")) {
				for(int i = 0; i < e.getPlayer().getInventory().getContents().length; i++) {
					ItemStack item = e.getPlayer().getInventory().getContents()[i];
					if(item != null && item.getType() == Material.COMPASS && PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()).startsWith(ChatColor.RED + "")) {
						e.getPlayer().getInventory().remove(item);
						break;
					}
				}
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}

	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		Player playerSpawned = event.getPlayer();
		String uuidPlayerSpawn = event.getPlayer().getUniqueId().toString();
		for(int i = 0; i < peaceAgreements.size(); i++) {
			//Validamos que haya sido asesinado por otro jugador, no un respawn por otro tipo de muerte.
			if(peaceAgreements.get(i).getPlayerUUID().equals(uuidPlayerSpawn)) {
				String uuidPlayerTarget = peaceAgreements.get(i).getPlayerKillerUUID();
				Player playerTarget = Bukkit.getPlayer(UUID.fromString(uuidPlayerTarget));
				
				Location location = playerTarget.getLocation();
				location.setY(-59);
				Bukkit.getWorld(playerTarget.getWorld().getUID()).setBlockData(location, Bukkit.createBlockData(Material.LODESTONE));
				
				ItemStack item = Main.getItem(Material.COMPASS, ChatColor.RED + "" + playerTarget.getName());
				CompassMeta compassMeta = (CompassMeta)item.getItemMeta();
				compassMeta.setLodestone(location);
				item.setItemMeta(compassMeta);

				playerSpawned.getInventory().setItem(0, item);

				Chat.title(messages.get("TPAA02"), messages.get("SPAA02"), playerSpawned);
				Chat.title(messages.get("TPAA03"), messages.get("SPAA03"), playerTarget);
				break;
			}
		}	
	}

	@EventHandler
	public void onPlayerFight(EntityDamageByEntityEvent event) {
		if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER) {
			Player playerDamager =  (Player) event.getDamager();
			Player player  = (Player) event.getEntity();
			
			for(int i = peaceAgreements.size()-1; i != -1; i--) {
				if(peaceAgreements.get(i).getPlayerUUID().equals(player.getUniqueId().toString())) {
					event.setCancelled(true);
					return;
				}else if(peaceAgreements.get(i).getPlayerUUID().equals(playerDamager.getUniqueId().toString())) {
					peaceAgreements.remove(i);
					Chat.title(messages.get("TPAA05"), messages.get("SPAA05"), playerDamager);
					return;
				}
			}
		}
	}
	
	public HashMap<String, PlayerObject> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, PlayerObject> players) {
		this.players = players;
	}
	
}
