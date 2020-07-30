package com.pks.demo.controller;

import java.io.Console;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pks.demo.CompleteRestApplication;
import com.pks.demo.MyContent;
import com.pks.demo.MyToken;
import com.pks.demo.MyUserDetailsService;
import com.pks.demo.exception.RestRequestException;
import com.pks.demo.model.Product;
import com.pks.demo.model.ProductUtil;
import com.pks.demo.util.AuthUtil;
import com.pks.demo.util.AuthenticationRequest;

@RestController
public class Controller {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@RequestMapping("/")
	public void exception() {
		throw new RestRequestException("No mapping/resource found for the end point {/}");
	}

	@RequestMapping("/a")
	public MyContent getContent() {
		MyContent myContent = new MyContent("Yes the content is shown");
		return myContent;
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public MyToken authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response)
			throws Exception {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		String encodedPassword = new Base64().encodeAsString(password.getBytes());

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, encodedPassword);

		try {
			authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		} catch (Exception e) {
			throw new Exception("Incorrect userName or password", e);
		}

		UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
		AuthUtil authUtil = new AuthUtil();
		String token = authUtil.generateToken(userDetails);
		Cookie cookie = new Cookie("My-Auth", token);
		response.addCookie(cookie);
		MyToken myToken = new MyToken(token);
		return myToken;
	}

	@RequestMapping("/products")
	public List<Product> getProducts() throws ClassNotFoundException, SQLException {
		ProductUtil productUtil = new ProductUtil();
		List<Product> products = productUtil.getProducts();
		return products;

	}

	@RequestMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) throws ClassNotFoundException, SQLException {
		ProductUtil productUtil = new ProductUtil();
		Product product = productUtil.getProduct(id);
		if (product == null) {
			throw new RestRequestException("No Product found for the id: " + id);
		}
		return product;
	}

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public String createProduct(@RequestBody Product product) throws Exception {
		ProductUtil productUtil = new ProductUtil();
		boolean bool = productUtil.createProduct(product);

		if (bool) {
			return product.getProductName() + " Product added to the database with id : " + product.getProductId();
		} else {
			throw new RestRequestException(
					"Product adding failed maybe another product has the same id : " + product.getProductId() + "!!");

		}

	}
	
	@RequestMapping(value="/products/{id}",method = RequestMethod.PUT)
	public String updateProduct(@RequestBody Product product, @PathVariable String id ) throws ClassNotFoundException, SQLException {
		ProductUtil productUtil=new ProductUtil();
		boolean bool= productUtil.updateProduct(product,id);
		if(bool) {
			return "Product updated and the new id is  "+product.getProductId();
		}
		
		throw new RestRequestException(
				"Product updating failed for the  id : " + id + "!!");
	}

}
