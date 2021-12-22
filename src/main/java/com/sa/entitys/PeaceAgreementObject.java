package com.sa.entitys;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.sa.faccion.Chat;
import com.sa.sql.Connection;

public class PeaceAgreementObject {

	private LocalDateTime dateStart;
	private int duration;
	private String playerKillerUUID;
	private String playerUUID;
	
	public LocalDateTime getDateStart() {
		return dateStart;
	}
	public void setDateStart(LocalDateTime dateStart) {
		this.dateStart = dateStart;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getPlayerKillerUUID() {
		return playerKillerUUID;
	}
	public void setPlayerKillerUUID(String playerKillerUUID) {
		this.playerKillerUUID = playerKillerUUID;
	}
	public String getPlayerUUID() {
		return playerUUID;
	}
	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}
	public boolean isActive() {
		//La fecha actual es anterior a la fecha limite?
		return LocalDateTime.now().isBefore(dateStart.plusSeconds(duration));
	}
	
	public boolean put(PeaceAgreementValues propiertie) {
		ResultSet rs = null;
		switch(propiertie.toString()) {
		case "dateStart":
			rs = Connection.Consult(replaceValues("CALL spPeaceAgreementPutDateStart('@playerUUID', @dateStart);"));
			break;
		case "duration":
			rs = Connection.Consult(replaceValues("CALL spPeaceAgreementPutDuration('@playerUUID', @duration);"));
			break;
		case "peace":
			rs = Connection.Consult(replaceValues("CALL spPeaceAgreementPutPeace('@playerUUID', @peace);"));
			break;
		case "playerKillerUUID":
			rs = Connection.Consult(replaceValues("CALL spPeaceAgreementPutPlayerKillerUUID('@playerUUID', '@playerKillerUUID');"));
			break;
		case "playerUUID":
			rs = Connection.Consult(replaceValues("CALL spPeaceAgreementPutPlayerUUID('@playerUUID');"));
			break;
		}
		
		return rs != null;
	}
	
	public static boolean post() {
		ResultSet rs = null;
		try {
			rs = Connection.Consult("CALL spPeaceAgreementPost();");
		}catch(Exception ex) {
			Chat.console(ex);
		}
		return rs != null;
	}
	
	public static PeaceAgreementObject get(String uuidPlayer) {
		PeaceAgreementObject peaceAgreement = new PeaceAgreementObject();
		 try {
			 
			 
			 
			 
		 }catch(Exception ex) {
			 Chat.console(ex);
		 }
		return peaceAgreement;
	}
	
	public String replaceValues(String text) {
		text = text.replace("@dateStart", dateStart.toString());
		text = text.replace("@duration", duration + "");
		text = text.replace("@playerKillerUUID", playerKillerUUID);
		text = text.replace("@playerUUID", playerUUID);
		return text;
	}
	
}
