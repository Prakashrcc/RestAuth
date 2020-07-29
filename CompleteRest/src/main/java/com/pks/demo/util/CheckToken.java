package com.pks.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckToken {
	String url="jdbc:mysql://localhost:3306/test";
	String uname="root";
	String pass="Prakash1";
	public boolean compareToken(String token, String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		String query ="select username from users where token=? and username=?";
		PreparedStatement st=con.prepareStatement(query);
		
		st.setString(1, token);
		st.setString(2, username);
		ResultSet resultSet=st.executeQuery();
		if(resultSet.next()) {
			return true;
		}
		
		return false;
	}

}
