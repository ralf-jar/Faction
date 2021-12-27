package com.sa.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.sa.entitys.FactionObject;
import com.sa.entitys.FactionValues;
import com.sa.entitys.PlayerObject;
import com.sa.entitys.PlayerValues;
import com.sa.faccion.Chat;
import com.sa.faccion.Main;

public class EventsFaction implements Listener {
	
	private HashMap<String, String> parameters;
	private HashMap<String, PlayerObject> players;
	private HashMap<String, FactionObject> factions;
	private Inventory menuColors = null;
	private String titleMenu = "Color de la Facción";
	private HashMap<String, String> invitations; //id_Invitado, id_LiderFaccion
	private HashMap<String, String> messages; //messageKey, message
	
	public EventsFaction(HashMap<String, PlayerObject> players, HashMap<String, FactionObject> factions, HashMap<String, String> parameters, HashMap<String, String> messages) {
		setPlayers(players);
		setFactions(factions);
		generateColorsMenu();
		invitations = new HashMap<String, String>();
		this.parameters = parameters;
		this.messages = messages;
	}

	@EventHandler
	public void onPlayerPVP(EntityDamageByEntityEvent e) {
		
		if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {
			
			String playerDamager = ((Player) e.getDamager()).getUniqueId().toString();
			String player  = ((Player) e.getEntity()).getUniqueId().toString();

			String faccion = players.get(player).getUuidFaction();
			String faccionDamager = players.get(playerDamager).getUuidFaction();
			
			if((faccion != null && faccionDamager != null) && faccion.equals(faccionDamager)) {
				e.setCancelled(true);
				
			}else {
				double powerDiff = Double.parseDouble(parameters.get("powerdiff"));
				double diff = players.get(player).getPower() - players.get(playerDamager).getPower();
				
				if(diff < 0) diff *= -1;
				
				e.setCancelled( ! (diff >= 0 && diff <= powerDiff) );
			}
		}
	}
	
	private void factionAdd(String factionName, String uuidPlayer) {
		Player p = Bukkit.getPlayer(UUID.fromString(uuidPlayer));
		if(players.get(uuidPlayer).getUuidFaction() == null) {
			FactionObject faction = new FactionObject();
			if(factionName.length() <= 10 && factionName.length() >=3) {
				faction.setName(factionName.replace(" ", "_"));
				faction.setUuidPlayerOwner(uuidPlayer);
				faction.setColorPrefix("e");
				if(faction.postFaction()) {
					factions.put(faction.getUuid(), faction);
					players.get(p.getUniqueId().toString()).setUuidFaction(faction.getUuid());
					players.get(p.getUniqueId().toString()).setFactionOwner(true);
					String factionNameColor = faction.getName().replace("@color", faction.getColorPrefix()).replace("@player", p.getName());
					String formatName = Chat.styleDisplayName.replace("@faction", factionNameColor);
					p.setDisplayName(formatName);
					
					Chat.recept(messages.get("RFTC01").replace("@faction", factionName), p);
					
					Chat.recept(messages.get("RFTC02"), p);
				}else {
					Chat.recept(messages.get("RFTC03"), p);
				}
			}else {
				Chat.recept(messages.get("RFTC04"), p);
			}
		}else {
			Chat.recept(messages.get("RFTC05"), p);
		}
	}
	
	private void factionRename(String factionRename, String uuidPlayer) {
		Player player = Bukkit.getPlayer(UUID.fromString(uuidPlayer));
		String uuidFaction = players.get(uuidPlayer).getUuidFaction();
		
		if(!Main.isNullOrEmpty(uuidFaction)) {
			if(players.get(uuidPlayer).getFactionOwner()) {
				if(factionRename.length() <= 10 && factionRename.length() >=3) {
					boolean existe = false;
					for(Map.Entry<String, FactionObject> entry : factions.entrySet()) {
						if(!entry.getKey().equals(uuidFaction) && entry.getValue().getName().equals(factionRename)) {
							existe = true;
							break;
						}
					}
					
					if(!existe) {
						factions.get(uuidFaction).setName(factionRename);
						factions.get(uuidFaction).putFaction(FactionValues.name);
						
						FactionObject faction = factions.get(uuidFaction);
						ArrayList<String> members = FactionObject.getMembersUuid(faction.getUuid());
						for(String uuidMember : members) {
							Player p = Bukkit.getPlayer(UUID.fromString(uuidMember));
							if(p.isOnline()) {
								String displayName = Chat.styleDisplayName.replace("@faction", faction.getName()).replace("@color", faction.getColorPrefix()).replace("@player", p.getName());
								p.setDisplayName(displayName);
								
								Chat.recept(messages.get("RFTR01"), p);
							}
						}
					}else {
						Chat.recept(messages.get("RFTR02"), player);
					}
				}else {
					Chat.recept(messages.get("RFTC04"), player);
				}
			}else {
				Chat.recept(messages.get("RFTR03"), player);
			}
		}else {
			Chat.recept(messages.get("RFTR04"), player);
		}
	}
	
	private void factionDel(String uuidPlayer, String passwordEncrypt) {
		String uuidFaction = players.get(uuidPlayer).getUuidFaction();
		Player player = Bukkit.getPlayer(UUID.fromString(uuidPlayer)); 
		
		if(uuidFaction != null) {
			
			if(players.get(uuidPlayer).getFactionOwner()) {
				
				if(passwordEncrypt != null) {
					
					if(Main.encryptText(passwordEncrypt).equals(players.get(uuidPlayer).getEncryptPass())) {
						
						ArrayList<String> members = FactionObject.getMembersUuid(uuidFaction);
						if(factions.get(uuidFaction).putFaction(FactionValues.status)) {
							for(String uuidMember : members) {
								Player p = Bukkit.getPlayer(UUID.fromString(uuidMember));
								
								if(p.isOnline()) {
									players.get(uuidMember).setUuidFaction(null);
									p.setDisplayName(p.getName());
									
									if(!uuidPlayer.equals(uuidMember)) {
										Chat.recept(messages.get("RFTD01").replace("@player", player.getName()), p);
									}else {
										players.get(uuidMember).setFactionOwner(false);
										Chat.recept(messages.get("RFTD02"), p);
									}
								}
							}
						}
					}else{
						Chat.recept(messages.get("RFTD03"), player);
					}
				}else {
					Chat.recept(messages.get("RFTD04"), player);
				}
			}else {
				Chat.recept(messages.get("RFTD05"), player);
			}
		}else {
			Chat.recept(messages.get("RFTR04"), player);
		}
	}
	
	private void factionLeave(String uuidPlayer, String passwordEncrypt) {
		String uuidFaction = players.get(uuidPlayer).getUuidFaction();
		Player player = Bukkit.getPlayer(UUID.fromString(uuidPlayer)); 
		
		if(uuidFaction != null) {
			
			if(!players.get(uuidPlayer).getFactionOwner()) {
				
				if(passwordEncrypt != null) {
					
					if(Main.encryptText(passwordEncrypt).equals(players.get(uuidPlayer).getEncryptPass())) {
						players.get(uuidPlayer).setUuidFaction("");
						
						if(players.get(uuidPlayer).putPlayer(PlayerValues.uuidFaction)) {
							
							factions.get(uuidFaction).setMembersUuid(FactionObject.getMembersUuid(uuidFaction));
							player.setDisplayName(player.getName());
							
							Player p = Bukkit.getPlayer(UUID.fromString(factions.get(uuidFaction).getUuidPlayerOwner()));
							if(p.isOnline()) {
								Chat.recept(messages.get("RFTL00").replace("@player", player.getName()), p);
							}
							
							Chat.recept(messages.get("RFTL01"), player);
						}
						
					}else{
						Chat.recept(messages.get("RFTL02"), player);
					}
				}else {
					Chat.recept(messages.get("RFTL03"), player);
				}
			}else {
				//Proceso de reemplazo de lider de faccion
				if(FactionObject.getMembersUuid(uuidFaction).size() != 1) {
					Chat.recept(messages.get("RFTL04"), player);
				}else {
					Chat.recept(messages.get("RFTL05"), player);
				}
			}
		}else {
			Chat.recept(messages.get("RFTL06"), player);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(invitations.get(e.getPlayer().getUniqueId().toString()) != null) {
			invitations.remove(e.getPlayer().getUniqueId().toString());
			//Liberación de memoria
			Chat.console(messages.get("CSSL01").replace("@player", e.getPlayer().getName()));
		}
	}
	
	private void factionInvite(String uuidPlayerOwner, String namePlayerInvited) {
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuidPlayerOwner));
		Player playerInvited = Bukkit.getPlayer(namePlayerInvited);
		
		if(players.get(uuidPlayerOwner).getFactionOwner()) {
			
			if(playerInvited != null && playerInvited.isOnline()) {
				
				FactionObject faction = factions.get(players.get(uuidPlayerOwner).getUuidFaction());
				String uuidPlayerInvited = playerInvited.getUniqueId().toString();
				
				PlayerObject playerInvitedObj = new PlayerObject();
				playerInvitedObj.getPlayer(uuidPlayerInvited);
				
				int maxMembers = Integer.parseInt(parameters.get("maxmembers"));
				
				if(FactionObject.getMembersUuid(faction.getUuid()).size() <= maxMembers) {
					if(playerInvitedObj.getUuidFaction() == null) {
						
						if(invitations.get(uuidPlayerInvited) == null) {
							invitations.put(uuidPlayerInvited, uuidPlayerOwner);
						}else {
							invitations.replace(uuidPlayerInvited, uuidPlayerOwner);
						}
						
						Chat.recept(messages.get("RFTI01").replace("@faction", faction.getName()).replace("@player", player.getName()), playerInvited);
						Chat.recept(messages.get("RFTI02").replace("@player", playerInvited.getName()), player);
					
					}else {
						Chat.recept(messages.get("RFTI03"), player);
					}
				}else {
					Chat.recept(messages.get("RFTI04"), player);
				}
			}else {
				Chat.recept(messages.get("RFTI05"), player);
			}
		}else {
			Chat.recept(messages.get("RFTI06"), player);
		}
	}
	
	private void factionJoin(String uuidPlayer) {
		try {
			int maxMembers = Integer.parseInt(parameters.get("maxmembers"));
			Player player = Bukkit.getPlayer(UUID.fromString(uuidPlayer));
			
			if(invitations.get(uuidPlayer) != null) {
				String uuidFaction = players.get(invitations.get(uuidPlayer)).getUuidFaction();
				if(uuidFaction != null) {
					List<String> members = FactionObject.getMembersUuid(uuidFaction);
					if(members.size() < maxMembers) {
						
						players.get(uuidPlayer).setUuidFaction(uuidFaction);
						if(players.get(uuidPlayer).putPlayer(PlayerValues.uuidFaction)) {
							
							String displayName = Chat.styleDisplayName.replace("@faction", factions.get(uuidFaction).getName()).replace("@color", factions.get(uuidFaction).getColorPrefix()).replace("@player", player.getName());
							player.setDisplayName(displayName);
							
							Chat.recept(messages.get("RFTJ01").replace("@faction", factions.get(uuidFaction).getName()), player);
							for(String member : members) {
								Player playerMember = Bukkit.getPlayer(UUID.fromString(member));
								if(playerMember != null && playerMember.isOnline()) {
									Chat.recept(messages.get("RFTJ02").replace("@player", player.getName()), playerMember);
								}	
							}
						}else {
							Chat.recept(messages.get("RFTJ03"), player);
						}
					}else {
						Chat.recept(messages.get("RFTJ04"), player);
						Player playerOwner = Bukkit.getPlayer(UUID.fromString(factions.get(uuidFaction).getUuidPlayerOwner()));
						if(playerOwner != null && playerOwner.isOnline()) {
							Chat.recept(messages.get("RFTJ05").replace("@player", player.getName()), playerOwner);
						}
					}
				}else {
					Chat.recept(messages.get("RFTJ06"), player);
				}
			}else {
				Chat.recept(messages.get("RFTJ07"), player);
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}
	
	private void factionKick(String uuidPlayer, String playerNameKicked) {
		try {
			Player playerOwner = Bukkit.getPlayer(UUID.fromString(uuidPlayer));
			
			if(players.get(uuidPlayer).getFactionOwner()) {	
				
				Player playerKicked = Bukkit.getPlayer(playerNameKicked);
				String uuidPlayerKicked = playerKicked.getUniqueId().toString();
				String uuidFaction = players.get(uuidPlayer).getUuidFaction();
				if(players.get(uuidPlayer).getUuidFaction().equals(players.get(uuidPlayerKicked).getUuidFaction())) {
				
					players.get(uuidPlayerKicked).setUuidFaction("");
					if(players.get(uuidPlayerKicked).putPlayer(PlayerValues.uuidFaction)) {	
						factions.get(uuidFaction).setMembersUuid(FactionObject.getMembersUuid(uuidFaction));
						playerKicked.setDisplayName(playerKicked.getName());
						
						Chat.recept(messages.get("RFTK01").replace("@player", playerKicked.getName()), playerOwner);
						Chat.recept(messages.get("RFTK02").replace("@player", playerOwner.getName()), playerKicked);
					}else {
						Chat.recept(messages.get("RFTK03"), playerKicked);
					}
				}else {
					Chat.recept(messages.get("RFTK04"), playerOwner);
				}
			}else {
				Chat.recept(messages.get("RFTK05"), playerOwner);
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}
	
	private void factionLeader(String uuidPlayer, String playerNameNewLeader, String password) {
		try {
			PlayerObject playerLastOwnerObj = players.get(uuidPlayer);
			Player playerLastOwner = Bukkit.getPlayer(UUID.fromString(uuidPlayer));
			if(playerLastOwnerObj != null && playerLastOwnerObj.getFactionOwner()) {
				
				Player playerNewOwner = Bukkit.getPlayer(playerNameNewLeader);
				if(playerNewOwner != null) {
					String uuidPlayerNewOwner = playerNewOwner.getUniqueId().toString();
					PlayerObject playerNewOwnerObj = players.get(uuidPlayerNewOwner);
					if(playerNewOwnerObj.getUuidFaction() != null && playerLastOwnerObj.getUuidFaction().equals(playerNewOwnerObj.getUuidFaction())) {
						if(Main.encryptText(password).equals(playerLastOwnerObj.getEncryptPass())) {
							
							players.get(uuidPlayer).setFactionOwner(false);
							if(players.get(uuidPlayer).putPlayer(PlayerValues.factionOwner)) {
								players.get(uuidPlayerNewOwner).setFactionOwner(true);
								if(players.get(uuidPlayerNewOwner).putPlayer(PlayerValues.factionOwner)) {
									factions.get(playerNewOwnerObj.getUuidFaction()).setUuidPlayerOwner(uuidPlayerNewOwner);
									
									List<String> members = FactionObject.getMembersUuid(playerNewOwnerObj.getUuidFaction());
									for(String member: members) {
										Player playerMember = Bukkit.getPlayer(UUID.fromString(member));
										if(playerMember.isOnline()) {
											Chat.recept(messages.get("RFTE01").replace("@player", playerNewOwner.getName()), playerMember);	
										}
									}
									
								}else {
									Chat.recept(messages.get("RFTE02"), playerLastOwner);
								}
							}else {
								Chat.recept(messages.get("RFTE03"), playerLastOwner);
							}
						}else {
							Chat.recept(messages.get("RFTE04"), playerLastOwner);
						}
					}else {
						Chat.recept(messages.get("RFTE05"), playerLastOwner);
					}
				}else {
					Chat.recept(messages.get("RFTE06"), playerLastOwner);
				}
			}else {
				Chat.recept(messages.get("RFTE07"), playerLastOwner);
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}
	
	private void factionList(Player player) {
		int index = 1;
		Chat.recept(messages.get("RFTT01"), player);
		
		for(Map.Entry<String, FactionObject> entry : factions.entrySet()) {
			Chat.recept(messages.get("RFTT02").replace("@index", index+"").replace("@color", entry.getValue().getColorPrefix()).replace("@faction", entry.getValue().getName()), player);
			index += 1;
			if(index == 20) {
				Chat.recept(messages.get("RFTT03").replace("@index", index+""), player);
				break;
			}
		}
	}
	
	private void factionMembers(Player player, String factionName) {
		
		FactionObject faction = new FactionObject();
		
		if(factionName == null) {
			PlayerObject playerObj =  players.get(player.getUniqueId().toString());
			String uuidFaction = playerObj.getUuidFaction();
			if(uuidFaction != null) {
				faction = factions.get(uuidFaction);
			}else {
				Chat.recept(messages.get("RFTM01"), player);
				return;
			}
		}else {
			faction.getFaction(factionName);	
		}
		
		if(faction.getUuid() != null && !faction.getUuid().isEmpty()) {
			int index = 1;
			Chat.recept(messages.get("RFTM02").replace("@color", faction.getColorPrefix()).replace("@faction", faction.getName()), player);
			List<String> uuidMembers = FactionObject.getMembersUuid(faction.getUuid());
			for(String uuidPlayerMember : uuidMembers) {
				Player playerMember = Bukkit.getPlayer(UUID.fromString(uuidPlayerMember));
				Chat.recept(messages.get("RFTM03").replace("@index", index+"").replace("@color", faction.getColorPrefix()).replace("@player", playerMember.getName()), player);
				index += 1;
			}
			Chat.recept(messages.get("RFTM04"), player);
		}else {
			Chat.recept(messages.get("RFTM05"), player);
		}
	}
	
	
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) 
	{
		try {
			
			String[] cmd = e.getMessage().split(" ");
			PlayerObject player = players.get(e.getPlayer().getUniqueId().toString());
			
			if(player != null && cmd[0].toLowerCase().startsWith("/faction")) {
				switch(cmd[0].toLowerCase()) {
				case "/factionadd":
					factionAdd(cmd[1], e.getPlayer().getUniqueId().toString());
					break;
				case "/factioncolor":
					if(!Main.isNullOrEmpty(player.getUuidFaction())) {
						if(player.getFactionOwner()) {
							e.getPlayer().openInventory(menuColors);
						}else {
							Chat.recept(messages.get("RFTO01"), e.getPlayer());
						}
					}else {
						Chat.recept(messages.get("RFTO02"), e.getPlayer());
					}
					break;
				case "/factionrename":
					factionRename(cmd[1], e.getPlayer().getUniqueId().toString());
					break;
				case "/factiondel":
					factionDel(e.getPlayer().getUniqueId().toString(), cmd[1]);
					break;
				case "/factionleave":
					factionLeave(e.getPlayer().getUniqueId().toString(), cmd[1]);
					break;
				case "/factionjoin":
					factionJoin(e.getPlayer().getUniqueId().toString());
					break;
				case "/factioninvite":
					factionInvite(e.getPlayer().getUniqueId().toString(), cmd[1]);
					break;
				case "/factionkick":
					factionKick(e.getPlayer().getUniqueId().toString(), cmd[1]);
					break;
				case "/factionleader":
					factionLeader(e.getPlayer().getUniqueId().toString(), cmd[1], cmd[2]);
					break;
				case "/factionlist":
					factionList(e.getPlayer());
					break;
				case "/factionmembers":
					factionMembers(e.getPlayer(), cmd[1]);
					break;
				default:
					break;
				}
			}
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
			Chat.recept(messages.get("RFTCC01"), e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerClickInventory(InventoryClickEvent e){
		if(e.getView().getTitle().toString().equals(titleMenu)) {
			if(e.getCurrentItem() != null && e.getCurrentItem().getType().name().contains("WOOL")) {
				String uuid = e.getWhoClicked().getUniqueId().toString();
				
				String color = "";
				color = e.getCurrentItem().getItemMeta().getDisplayName().charAt(1) + "";
			
				factions.get(players.get(uuid).getUuidFaction()).setColorPrefix(color);
				
				FactionObject faction = factions.get(players.get(uuid).getUuidFaction());
				Player p = (Player) e.getWhoClicked();
				p.setDisplayName(Chat.styleDisplayName.replace("@faction", faction.getName()).replace("@color", faction.getColorPrefix()).replace("@player", p.getName()));
				
				ArrayList<String> members = FactionObject.getMembersUuid(faction.getUuid());
				for(String uuidMember : members) {
					Player member = Bukkit.getPlayer(UUID.fromString(uuidMember));
					if(member.isOnline()) {
						String displayName = Chat.styleDisplayName.replace("@faction", faction.getName()).replace("@color", faction.getColorPrefix()).replace("@player", member.getName());
						member.setDisplayName(displayName);
						Chat.recept(messages.get("RFTO03").replace("@color", color), p);
					}
				}
				
				e.setCancelled(true);
				p.closeInventory();
				faction.putFaction(FactionValues.color);
			}
		}
	}
	
	public HashMap<String, PlayerObject> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, PlayerObject> players) {
		this.players = players;
	}

	public HashMap<String, FactionObject> getFactions() {
		return factions;
	}

	public void setFactions(HashMap<String, FactionObject> factions) {
		this.factions = factions;
	}
	
	private void generateColorsMenu() {
		menuColors = Bukkit.createInventory(null, 36, titleMenu);
		
		menuColors.setItem(10, Main.getItem(Material.GREEN_WOOL, "&2Verde Obscuro"));
		menuColors.setItem(11, Main.getItem(Material.CYAN_WOOL, "&3Aqua Obscuro"));
		menuColors.setItem(12, Main.getItem(Material.PURPLE_WOOL, "&5Morado"));
		menuColors.setItem(13, Main.getItem(Material.YELLOW_WOOL, "&6Dorado"));
		menuColors.setItem(14, Main.getItem(Material.LIGHT_GRAY_WOOL, "&7Gris Claro"));
		menuColors.setItem(15, Main.getItem(Material.GRAY_WOOL, "&8Gris Obscuro"));
		menuColors.setItem(16, Main.getItem(Material.BLUE_WOOL, "&9Azul"));
		
		menuColors.setItem(20, Main.getItem(Material.LIME_WOOL, "&aVerde Claro"));
		menuColors.setItem(21, Main.getItem(Material.LIGHT_BLUE_WOOL, "&bAqua Claro"));
		menuColors.setItem(22, Main.getItem(Material.ORANGE_WOOL, "&cNaranja"));
		menuColors.setItem(23, Main.getItem(Material.MAGENTA_WOOL, "&dRosa"));
		menuColors.setItem(24, Main.getItem(Material.SAND, "&eAmarillo Claro"));
		
	}

}
