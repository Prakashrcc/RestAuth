package com.pks.demo.util;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUtil {

	public String generateToken(UserDetails userDetails) throws Exception {
		String username = userDetails.getUsername();
		RandomToken randomToken = new RandomToken();

		String token = randomToken.getRandomToken(username);
		StoreToken storeToken=new StoreToken();
		if(storeToken.saveToken(token, username)) {
			return token;
		}
		else {
			throw new Exception("Token Not saved Some error try again");
		}
		
	}
	
	public boolean validateToken(String token) throws ClassNotFoundException, SQLException {
		Base64.Decoder decoder=Base64.getDecoder();
		String tokenString=new String(decoder.decode(token));
		String username=null;
		String date=null;
		int startIndex=0;
		for(int i=0;i<tokenString.length();i++) {
			if(tokenString.charAt(i)== '@' && startIndex==0) {
				username=tokenString.substring(startIndex, i);
				startIndex=i;
			}
			else if(tokenString.charAt(i)== '@' ) {
				date=tokenString.substring(startIndex+1, i);
				break;
			}
		}
		if(datevalidator(date)) {
			CheckToken checkToken=new CheckToken();
			if(checkToken.compareToken(token, username)) {
				return true;
			}
			
		}
		
		return false;
		
	}
	public String extractUsername(String token) {
		Base64.Decoder decoder=Base64.getDecoder();
		String tokenString=new String(decoder.decode(token));
		String username=null;
		
		for(int i=0;i<tokenString.length();i++) {
			if(tokenString.charAt(i)== '@' ) {
				username=tokenString.substring(0, i);
				return username;
			}
			
		}
		return null;
	}

	private boolean datevalidator(String date) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime expiryDate= (LocalDateTime.parse(date)).plusHours(4);
		if(currentDateTime.compareTo(expiryDate)<0 || currentDateTime.compareTo(expiryDate)==0 ) {
			return true;
		}
		return false;
	}

}
