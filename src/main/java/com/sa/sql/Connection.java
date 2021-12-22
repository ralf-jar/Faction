package com.sa.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sa.faccion.Chat;

public class Connection {
	
	private static Statement connect() {
		try {  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			
			String host = "mysql.mcprohosting.com";
			String user = "server_1020914";
			String security ="VhBsQ6W71vuA$R*zd";
			String database = "server_1020914_11dc71f5";
			
			Statement connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, security).createStatement();  
			return connection;
		}
		catch(Exception ex) {
			Chat.console(ex.getMessage());
			return null;
		}  
		
	}
	
	public static ResultSet Consult(String procedure) {
		try {
			Statement conection = connect();
			if(conection != null){
				Chat.console(procedure);
				ResultSet result = conection.executeQuery(procedure.toString());
				if(result != null) {
					return result;
				}else {
					throw new Exception("No se logro conectar con el servidor.");
				}
			}else {
				throw new Exception("No se logro conectar con el servidor.");
			}
		}catch(Exception ex) {
			Chat.console(ex.getMessage());
			return null;
		}
	}

}
