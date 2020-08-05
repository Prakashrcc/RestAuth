package com.pks.demo.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pks.demo.RestUserDetailsService;
import com.pks.demo.util.AuthUtil;

@Component
public class AuthFilter extends OncePerRequestFilter {
	@Autowired
	private RestUserDetailsService restUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Auth");
		
		if(authHeader==null) {
		Cookie[] cookies=request.getCookies();
		if (cookies != null) {
			 for (Cookie cookie : cookies) {
			   if (cookie.getName().equals("Auth")) {
				   authHeader=cookie.getValue();
				   break;
			    }
			  }
			}
		}
		
		
		String username = null;

		AuthUtil authUtil = new AuthUtil();
		if (authHeader != null) {
			username = authUtil.extractUsername(authHeader);
		}
		if (username != null) {
			UserDetails userDetails = restUserDetailsService.loadUserByUsername(username);
			try {
				if (authUtil.validateToken(authHeader)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		filterChain.doFilter(request, response);

	}

}
