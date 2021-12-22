package com.sa.events;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.sa.entitys.FactionObject;
import com.sa.entitys.PlayerObject;
import com.sa.entitys.PlayerValues;
import com.sa.faccion.Chat;
import com.sa.faccion.Main;

import net.kyori.adventure.text.Component;

import io.papermc.paper.event.player.AsyncChatEvent;

public class EventsSession implements Listener {
	
	private HashMap<String, PlayerObject> players;
	private HashMap<String, PlayerObject> playersOnLogin;
	private HashMap<String, ItemStack[]> inventorysOnLogin;
	private HashMap<String, String> messages;
	
	public EventsSession(HashMap<String, PlayerObject> players, HashMap<String, String> messages) {
		setPlayers(players);
		playersOnLogin = new HashMap<String, PlayerObject>();
		inventorysOnLogin = new HashMap<String, ItemStack[]>();
		this.messages = messages;
	}
	
	@EventHandler
    public void onPlayerChat(AsyncChatEvent e)
    {
		if(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null) {
			Chat.recept("&cInicie sesión para utilizar el chat.", e.getPlayer());	
		}else {
			Chat.msg(e.message().toString(), e.getPlayer());
		}
		
		e.setCancelled(true);
    }	
	
	@EventHandler
	public void onPlayerBreakItem(BlockBreakEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerEditBook(PlayerEditBookEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}

	@EventHandler
	public void onPlayerStartFish(PlayerFishEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}

	@EventHandler
	public void onPlayerShearSheep(PlayerShearEntityEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		e.setCancelled(playersOnLogin.get(e.getPlayer().getUniqueId().toString()) != null);
	}

	@EventHandler
	public void onOpenInventory(InventoryOpenEvent event) {
	    if (event.getInventory().getType() == InventoryType.PLAYER)
	    	event.setCancelled(true);
	}
	
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) 
	{
		try {
			String uuid = e.getPlayer().getUniqueId().toString();
			String[] cmd = e.getMessage().split(" ");
			
			if(playersOnLogin.get(uuid) != null) {
				
				PlayerObject player = playersOnLogin.get(uuid);
				
				if(player.getEncryptPass() == null) {
					//Registro
					if(cmd.length == 3 && cmd[0].toLowerCase().equals("/register")) {
						if(cmd[1].equals(cmd[2])) {
							//Creamos un nuevo jugador
							player = new PlayerObject();
							player.setUuid(uuid);
							
							//Registramos jugador
							String password = cmd[1];
							password = Main.encryptText(password);
							player.setEncryptPass(password);
							player.postPlayer();
							Chat.recept(messages.get("RSSR01"), e.getPlayer());
							Chat.msg(messages.get("GSSR01").replace("@player", e.getPlayer().getName()));
							
							//Obtenemos información del jugador
							player.getPlayer(uuid);
							
							//Lo agregamos al listado de jugadores
							players.put(uuid, player);
							players.get(uuid).setSessionStarted(true);
							
							//Lo liberamos del listado del login
							playersOnLogin.remove(uuid);
							
						}else {
							Chat.recept(messages.get("RSSR02"), e.getPlayer());
						}
					}else {
						Chat.recept(messages.get("RSSR03"), e.getPlayer());
					}
				}else{
					//Login
					if(cmd.length == 2 && cmd[0].toLowerCase().equals("/login")) {
						if(player.getEncryptPass().equals(Main.encryptText(cmd[1]))) {
							player.setSessionStarted(true);
							player.setSessionStart(new Date());
							players.put(uuid,player);
							playersOnLogin.remove(uuid);
							Chat.recept(messages.get("RSSL01").replace("@player", e.getPlayer().getName()), e.getPlayer());
							Chat.msg(messages.get("GSSL01").replace("@player", e.getPlayer().getName()));
							
							//Devuelve el inventario
							e.getPlayer().getInventory().setContents(inventorysOnLogin.get(uuid));		
							inventorysOnLogin.remove(uuid);
							
							//set DisplayName a Jugador
							if(player.getUuidFaction() != null) {
								FactionObject faction = new FactionObject();
								faction.getFaction(player.getUuidFaction());
								e.getPlayer().displayName(Component.text(Chat.styleDisplayName.replace("@faction", faction.getName()).replace("@color", faction.getColorPrefix()).replace("@player", e.getPlayer().getName())));
							}
							
						}else {
							Chat.recept(messages.get("RSSL02"), e.getPlayer());
							Chat.recept(messages.get("RSSL03"), e.getPlayer());
						}
					}else {
						Chat.recept(messages.get("RSSL03"), e.getPlayer());
					}
				}
			}else if(cmd[0].equals("/register") || cmd[0].equals("/login")){
				Chat.recept(messages.get("RSSL04"), e.getPlayer());
			}
			
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		try {
			String uuid = e.getPlayer().getUniqueId().toString();
			PlayerObject player = new PlayerObject();
			playersOnLogin.put(uuid, player);
			e.joinMessage(Component.text(""));
			
			if(player.getPlayer(uuid)) {
				Chat.recept(messages.get("RSSL01").replace("@player", e.getPlayer().getName()), e.getPlayer());
				Chat.recept(messages.get("RSSL03"), e.getPlayer());
				
				inventorysOnLogin.put(uuid, e.getPlayer().getInventory().getContents());
				e.getPlayer().getInventory().clear();
				
			}else {
				Chat.recept(messages.get("RSSR04").replace("@player", e.getPlayer().getName()), e.getPlayer());
				Chat.recept(messages.get("RSSR03"), e.getPlayer());
			}
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		PlayerObject player = players.get(e.getPlayer().getUniqueId().toString());
		if(player != null) {
			player.setLocation(e.getPlayer().getLocation());
			player.setSessionEnd(new Date());
			player.putPlayer(PlayerValues.powerLevel);
			
			players.remove(player.getUuid());
			Chat.msg(messages.get("GSSL02").replace("@player", e.getPlayer().getName()));
			e.quitMessage(Component.text(""));
		}else {
			e.getPlayer().getInventory().setContents(inventorysOnLogin.get(e.getPlayer().getUniqueId().toString()));
		}
	}
	
	@EventHandler
	public void onPlayerPVP(EntityDamageByEntityEvent e) {
		
		if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {
			
			String playerDamager = ((Player) e.getDamager()).getUniqueId().toString();
			String player = ((Player) e.getEntity()).getUniqueId().toString();

			e.setCancelled((playersOnLogin.get(player) != null || playersOnLogin.get(playerDamager) != null));
		}
	}
	
	public HashMap<String, PlayerObject> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, PlayerObject> players) {
		this.players = players;
	}

	
}
