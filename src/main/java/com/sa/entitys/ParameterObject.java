package com.sa.entitys;

import java.sql.ResultSet;
import java.util.HashMap;

import com.sa.faccion.Chat;
import com.sa.sql.Connection;

public class ParameterObject {
	
	private String parameter;
	private String value;

	public ParameterObject(String parameter, String value) {
		this.parameter = parameter;
		this.value = value;
	}

	public String getParameter() {
		return parameter;
	}

	public String getValue() {
		return value;
	}
	
	public static HashMap<String, String> get() {
		HashMap<String, String> parameters = null;
		// String Parameter, String Value
		try {
			ResultSet rs = Connection.Consult("CALL spParameterGet();");
			while(rs.next()) {
				if(parameters == null) {
					parameters = new HashMap<String, String>();
				}
				parameters.put(rs.getString("parameter"), rs.getString("value"));
			}
		}catch(Exception ex) {
			Chat.console(ex);
		}
		return parameters;
	}
	
	public static boolean put(String parameter, String value) {
		try {
			ResultSet rs = Connection.Consult("CALL spParameterPut('@parameter','@value');"
					.replace("@parameter", parameter)
					.replace("@value", value));
			
			return (rs != null && rs.next());
		
		}catch(Exception ex) {
			Chat.console(ex);
		}
		return false;
	}

}
