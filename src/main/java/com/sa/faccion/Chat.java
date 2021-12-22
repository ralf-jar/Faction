package com.sa.faccion;

import java.sql.ResultSet;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sa.entitys.PlayerObject;
import com.sa.sql.Connection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class Chat {

	public static String styleDisplayName = "&@color&l@faction &r&@color@player";
	
	public static void msg(String msg, Player p) {
		for(Player player : Bukkit.getOnlinePlayers()) 
		{
			player.sendMessage(("&e" + p.displayName() + "&7: &f" + msg).replace("&", "§"));
		}
	}
	
	public static void msg(String msg) {
		Bukkit.getConsoleSender().sendMessage("§6Servidor§7: §f" + msg.replace("&", "§"));
		for(Player player : Bukkit.getOnlinePlayers()) 
		{
			player.sendMessage("§8[§6Servidor§8]: §f" + msg.replace("&", "§"));
		}
	}
	
	public static void recept(String msg, Player p) {
		p.sendMessage("§8[§7SA§8] §7" + msg.replace("&", "§"));
	}
	
	public static void console(String msg) {
		Bukkit.getConsoleSender().sendMessage("§4Console§7: §f" + msg.replace("&", "§"));
	}
	
	public static void console(Exception ex) {
		Bukkit.getConsoleSender().sendMessage("§4Console§7: §c" + ex.getMessage());
	}
	
	public static void sendPowerStatus(PlayerObject player) {
		Player p = Bukkit.getPlayer(UUID.fromString(player.getUuid()));
		p.sendActionBar(Component.text(ChatColor.GREEN + "Nivel de Poder: " + ChatColor.RED + "" + player.getPower()));
	}
	
	public static void title(String title, String subTitle, Player player) {
		Title.Times times = Title.Times.of(Duration.ofMillis(500), Duration.ofMillis(2500), Duration.ofMillis(1000));
		Component mainTitle = (Component)Component.text(title.replace("&", "§"));
		Component mainSubTitle = (Component)Component.text(subTitle.replace("&", "§"));
		Title ttitle = Title.title(mainTitle, mainSubTitle, times);
		player.showTitle(ttitle);
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
