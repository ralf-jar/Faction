package com.sa.faccion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.sa.entitys.FactionObject;
import com.sa.entitys.ParameterObject;
import com.sa.entitys.PlayerObject;
import com.sa.events.EventsFaction;
import com.sa.events.EventsPeaceAgreement;
import com.sa.events.EventsPowerLevel;
import com.sa.events.EventsSession;

import net.kyori.adventure.text.Component;

public class Main extends JavaPlugin{
	
	private HashMap<String, PlayerObject> players = new HashMap<String, PlayerObject>();
	private HashMap<String, FactionObject> factions;
	private HashMap<String, String> parameters;
	private HashMap<String, String> messages;
	
	@Override
	public void onEnable() {
		parameters = ParameterObject.get();
		factions = FactionObject.getFactions();
		messages = Chat.getMessages(parameters.get("lenguage"));
		
		getServer().getPluginManager().registerEvents(new EventsSession(players, messages), this);
		getServer().getPluginManager().registerEvents(new EventsFaction(players, factions, parameters, messages), this);
		getServer().getPluginManager().registerEvents(new EventsPowerLevel(players, parameters, messages, this), this);
		getServer().getPluginManager().registerEvents(new EventsPeaceAgreement(players, parameters, messages, this), this);
		
		Chat.console("&6Facciones habilitadas.");
	}
	
	@Override
	public void onDisable() {
		
	}

	public static String encryptText(String password) {
        try{  
            MessageDigest m = MessageDigest.getInstance("MD5");  
              
            m.update(password.getBytes());  
              
            byte[] bytes = m.digest();  
              
            StringBuilder s = new StringBuilder();  
            for(int i=0; i< bytes.length ;i++)  
            {  
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));  
            }  
            return s.toString();
        }catch (NoSuchAlgorithmException e){  
            Chat.msg(e.getMessage());
            return "";
        }  
	}
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.replace(" ", "").length() == 0;
	}
	
	@SuppressWarnings("deprecation") 
	public static ItemStack getItem(Material material, String name) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		if(Main.isPaper()) {
			meta.displayName(Component.text(name.replace("&", "§")));	
		}else {
			meta.setDisplayName(name.replace("&", "§"));
		}
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static boolean isPaper() {
		boolean isPaper = false;
		try {
		    Class.forName("com.destroystokyo.paper.ParticleBuilder");
		    isPaper = true;
		} catch (ClassNotFoundException ignored) {
			
		}
		
		return isPaper;
	}
	
}
