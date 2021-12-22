package com.sa.entitys;

public enum PeaceAgreementValues {
	dateStart ("dateStart"),
	duration ("duration"), 
	playerKillerUUID ("playerKillerUUID"),
	playerUUID ("playerUUID");
	
	private final String value;
	
	private PeaceAgreementValues(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}

}
