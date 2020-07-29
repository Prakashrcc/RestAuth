package com.pks.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreToken {
	String url="jdbc:mysql://localhost:3306/test";
	String uname="root";
	String pass="Prakash1";
	public boolean saveToken(String token, String username) throws ClassNotFoundException, SQLException {
		
		String query="update users set token=? where username=?";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		PreparedStatement st=con.prepareStatement(query);
		
		st.setString(1, token);
		st.setString(2, username);
		
		
		int count=st.executeUpdate();
		if(count>0) {
			return true;
		}
		
		
		return false;
		
	}

}
