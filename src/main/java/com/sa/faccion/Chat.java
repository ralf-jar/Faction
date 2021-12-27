package com.sa.faccion;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sa.entitys.PlayerObject;
import com.sa.sql.Connection;

public class Chat {

	public static String styleDisplayName = "&@color&l@faction &r&@color@player";
	
	public static void msg(String msg, Player p) {
		for(Player player : Bukkit.getOnlinePlayers()) 
		{
			String displayName = "";
			displayName = p.getDisplayName();
			player.spigot().sendMessage(new net.md_5.bungee.api.chat.TextComponent(("&e" + displayName + "&7: &f" + msg).replace("&", "§")));
		}
	}

	public static void msg(String msg) {
		Bukkit.getConsoleSender().sendMessage("§6Servidor§7: §f" + msg.replace("&", "§"));
		for(Player player : Bukkit.getOnlinePlayers()) 
		{
			player.spigot().sendMessage(new net.md_5.bungee.api.chat.TextComponent("§8[§6Servidor§8]: §f" + msg.replace("&", "§")));	
		}
	}
	
	public static void recept(String msg, Player p) {
		p.spigot().sendMessage(new net.md_5.bungee.api.chat.TextComponent("§8[§7SA§8] §7" + msg.replace("&", "§")));
	}
	
	public static void console(String msg) {
		Bukkit.getConsoleSender().sendMessage("§4Console§7: §f" + msg.replace("&", "§"));
	}
	
	public static void console(Exception ex) {
		Bukkit.getConsoleSender().sendMessage("§4Console§7: §c" + ex.getMessage());
	}
	
	public static void sendPowerStatus(PlayerObject player) {
		String msg = ChatColor.GREEN + "Nivel de Poder: " + ChatColor.RED + "" + player.getPower();
		Player p = Bukkit.getPlayer(UUID.fromString(player.getUuid()));
		p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(msg));
	}
	
	public static void title(String title, String subTitle, Player player) {
		player.sendTitle(title, subTitle, 20, 60, 20);
	}
	
	public static HashMap<String, String> getMessages(String lenguageKey){
		HashMap<String, String> messages = null;
		
		try {
			ResultSet rs = Connection.Consult("CALL spMessageGet('@lenguageKey');"
					.replace("@lenguageKey",lenguageKey));
			while(rs.next()) {
				if(messages == null) messages = new HashMap<String, String>();
				messages.put(rs.getString("messageKey"), rs.getString("message"));
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		
		return messages;
	}
	
}
