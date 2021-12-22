package com.sa.entitys;

import java.sql.ResultSet;
import java.util.Date;

import org.bukkit.Location;

import com.sa.faccion.Chat;
import com.sa.sql.Connection;

public class PlayerObject {
	
	private String uuid;
	private Date sessionStart;
	private Date sessionEnd;
	private String encryptPass;
	private int dailyDeaths;
	private boolean starterProtection;
	private double power;
	private int kills;
	private int deaths;
	private boolean factionOwner;
	private String uuidFaction;
	private boolean sessionStarted = false;
	
	private String displayName;
	private Boolean revenge;
	private Location location;
	private Boolean villagerKilled;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Boolean getRevenge() {
		return revenge;
	}
	public void setRevenge(Boolean revenge) {
		this.revenge = revenge;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Boolean getFactionOwner() {
		return factionOwner;
	}
	public void setFactionOwner(Boolean factionOwner) {
		this.factionOwner = factionOwner;
	}
	public Boolean getVillagerKilled() {
		return villagerKilled;
	}
	public void setVillagerKilled(Boolean villagerKilled) {
		this.villagerKilled = villagerKilled;
	}
	public String getEncryptPass() {
		return encryptPass;
	}
	public void setEncryptPass(String encryptPass) {
		this.encryptPass = encryptPass;
	}
	public String getUuidFaction() {
		return uuidFaction;
	}
	public void setUuidFaction(String uuidFaction) {
		this.uuidFaction = uuidFaction;
	}
	public Date getSessionEnd() {
		return sessionEnd;
	}
	public void setSessionEnd(Date sessionEnd) {
		this.sessionEnd = sessionEnd;
	}
	public Date getSessionStart() {
		return sessionStart;
	}
	public void setSessionStart(Date sessionStart) {
		this.sessionStart = sessionStart;
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getDailyDeaths() {
		return dailyDeaths;
	}
	public void setDailyDeaths(int dailyDeaths) {
		this.dailyDeaths = dailyDeaths;
	}
	public boolean isStarterProtection() {
		return starterProtection;
	}
	public void setStarterProtection(boolean starterProtection) {
		this.starterProtection = starterProtection;
	}
	
	
	public boolean getPlayer(String uuid) {
		try {
			
			ResultSet rs = Connection.Consult("CALL spPlayerSesionGet('@uuid')".replace("@uuid", uuid));
			if(rs.next()) {
				setUuid(rs.getString("uuid"));
				setSessionStart(new Date());
				setUuidFaction(rs.getString("uuidFaction"));
				setSessionEnd(rs.getDate("sessionEnd"));
				setFactionOwner(rs.getBoolean("factionOwner"));
				setDeaths(rs.getInt("deaths"));
				setKills(rs.getInt("kills"));
				setEncryptPass(rs.getString("password"));
				setDailyDeaths(rs.getInt("dailyDeaths"));
				setPower(rs.getInt("powerLevel"));
				setStarterProtection(rs.getBoolean("starterProtection"));
				return true;
			}else{
				return false;
			}
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
			return false;
		}
	}
	
	public boolean postPlayer() {
		try {
			//Crea
			ResultSet rs = Connection.Consult("CALL spPlayerSesionPost('@uuid', '@pass')".replace("@uuid", uuid).replace("@pass", this.getEncryptPass()));
			return (rs != null && rs.next());
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
			return false;
		}
	}
	
	public boolean putPlayer(PlayerValues value) {
		
		ResultSet rs = null;
		try {
			switch(value.toString()) {
			case "uuidPlayerOwner":
				
				break;
			case "uuidFaction":
				rs = Connection.Consult("CALL spPlayerPutUuidFaction('@uuidPlayer', '@uuidFaction')".replace("@uuidPlayer", getUuid()).replace("@uuidFaction", getUuidFaction()));
				return (rs != null);
			case "factionOwner":
				rs = Connection.Consult("CALL spPlayerPutFactionOwner('@uuidPlayer', @factionOwner)".replace("@uuidPlayer", getUuid()).replace("@factionOwner", getFactionOwner().toString()));
				return (rs != null);
			case "powerLevel":
				rs = Connection.Consult("CALL spPlayerPutPower('@uuidPlayer', @powerLevel)".replace("@uuidPlayer", getUuid()).replace("@powerLevel", getPower()+""));
				return (rs != null);
			default:
				break;
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		
		return (rs != null);
	}
	public boolean isSessionStarted() {
		return sessionStarted;
	}
	public void setSessionStarted(boolean sessionStarted) {
		this.sessionStarted = sessionStarted;
	}

}
