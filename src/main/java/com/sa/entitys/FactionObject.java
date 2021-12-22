package com.sa.entitys;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.sa.faccion.Chat;
import com.sa.sql.Connection;

public class FactionObject {
	
	private String uuid;
	private String name;
	private String colorPrefix;
	private boolean villagerKilled;
	private Date lastTransfer;
	private String uuidPlayerOwner;
	private double power;
	private ArrayList<String> membersUuid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		try {
			if(name.length() > 20) {
				throw new Exception("El nombre de la facción es mas grande de lo permitido");
			}else if(name.length() == 0){
				throw new Exception("El nombre de la facción debe de tener al menos 1 caracter.");
			}else {
				this.name = name;
			}	
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public String getColorPrefix() {
		return colorPrefix;
	}
	public void setColorPrefix(String colorPrefix) {
		this.colorPrefix = colorPrefix;
	}
	public boolean isVillagerKilled() {
		return villagerKilled;
	}
	public void setVillagerKilled(boolean villagerKilled) {
		this.villagerKilled = villagerKilled;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getLastTransfer() {
		return lastTransfer;
	}
	public void setLastTransfer(Date lastTransfer) {
		this.lastTransfer = lastTransfer;
	}
	public String getUuidPlayerOwner() {
		return uuidPlayerOwner;
	}
	public void setUuidPlayerOwner(String uuidPlayerOwner) {
		this.uuidPlayerOwner = uuidPlayerOwner;
	}
	
	public void getFaction(String search) { //Consult
		try {
			ResultSet rs = Connection.Consult("CALL spFactionGet('@search')".replace("@search", search));
			if(rs.next()) {
				setUuid(rs.getString("uuid"));
				setName(rs.getString("name"));
				setVillagerKilled(rs.getBoolean("villagerKilled"));
				setUuidPlayerOwner(rs.getString("uuidPlayerOwner"));
				setColorPrefix(rs.getString("color"));
				setLastTransfer(rs.getDate("lastTransfer"));
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
	}
	
	public static HashMap<String, FactionObject> getFactions(){
		
		HashMap<String, FactionObject> factions = new HashMap<String, FactionObject>();
		try {
			ResultSet rs = Connection.Consult("CALL spFactionsGet()");
			while(rs.next()) {
				FactionObject faction = new FactionObject();
				faction.setUuid(rs.getString("uuid"));
				faction.setName(rs.getString("name"));
				faction.setVillagerKilled(rs.getBoolean("villagerKilled"));
				faction.setUuidPlayerOwner(rs.getString("uuidPlayerOwner"));
				faction.setColorPrefix(rs.getString("color"));
				faction.setLastTransfer(rs.getDate("lastTransfer"));
				factions.put(faction.getUuid(), faction);
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		
		return factions;
	}
	
	public static ArrayList<String> getMembersUuid(String uuid){
		ArrayList<String> members = new ArrayList<String>();
		try {
			ResultSet rs = Connection.Consult("CALL spPlayerFactionUuidGet('@uuidFaction')".replace("@uuidFaction", uuid));
			while(rs.next()) {
				members.add(rs.getString("uuidPlayer"));
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		return members;
	}
	
	public boolean postFaction() { //Crea
		try {
			ResultSet rs = Connection.Consult("CALL spFactionPost('@name','@color','@uuidPlayerOwner')".replace("@name", getName()).replace("@color", getColorPrefix()).replace("@uuidPlayerOwner", getUuidPlayerOwner()));
			if(rs.next()) {
				setUuid(rs.getString("uuidFaction"));
				getFaction(getUuid());
				return true;
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		return false;
	}
	
	public boolean putFaction(FactionValues value) { //Modifica
		ResultSet rs = null;
		
		try {
			switch(value.toString()){
			case "color":
				rs = Connection.Consult("CALL spFactionPutColor('@color','@uuid')".replace("@color", getColorPrefix()).replace("@uuid", getUuid()));
				return rs.next();
			case "name":
				rs = Connection.Consult("CALL spFactionPutName('@name','@uuid')".replace("@name", getName()).replace("@uuid", getUuid()));
				return rs.next();
			case "uuidPlayerOwner":
				rs = Connection.Consult("CALL spFactionPutUuidPlayerOwner('@uuidPlayerOwner','@uuid')".replace("@uuidPlayerOwner", getUuidPlayerOwner()).replace("@uuid", getUuid()));
				rs.next();
			case "status":
				rs = Connection.Consult("CALL spFactionPutStatus('@uuid')".replace("@uuid", getUuid()));
				rs.next();
				break;
			default:
				break;
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		
		return rs != null;
	}
	
	public ArrayList<String> getMembersUuid() {
		return membersUuid;
	}
	public void setMembersUuid(ArrayList<String> membersUuid) {
		this.membersUuid = membersUuid;
	}
	

}
