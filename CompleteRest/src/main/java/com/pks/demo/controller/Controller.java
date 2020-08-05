package com.pks.demo.controller;

import java.io.Console;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pks.demo.CompleteRestApplication;
import com.pks.demo.RestContent;
import com.pks.demo.Authtoken;
import com.pks.demo.RestUserDetailsService;
import com.pks.demo.exception.ProductNotFoundException;
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
	private RestUserDetailsService restUserDetailsService;
	
	 Logger logger = LoggerFactory.getLogger(Controller.class);


	@RequestMapping("/")
	public void exception() {
		throw new RestRequestException("No mapping/resource found for the end point {/}");
	}

	@RequestMapping("/a")
	public RestContent getContent() {
		RestContent restContent = new RestContent("Yes the content is shown");
		return restContent;
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public Authtoken authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response)
			throws Exception {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
	
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);

		try {
			authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		} catch (Exception e) {
			throw new Exception("Incorrect userName or password", e);
		}

		UserDetails userDetails = restUserDetailsService.loadUserByUsername(username);
		AuthUtil authUtil = new AuthUtil();
		String token = authUtil.generateToken(userDetails);
		Cookie cookie = new Cookie("Auth", token);
		response.addCookie(cookie);
		Authtoken authtoken = new Authtoken(token);
		return authtoken;
	}
	
	@RequestMapping("/signout")
	public String logout(HttpServletResponse response) {
		logger.info("logout method accessed");
		Cookie cookie = new Cookie("Auth", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		
		response.addCookie(cookie);
		logger.info("logout method completed");
		return "Log out Successfull";
	}

	@RequestMapping("/products")
	public List<Product> getProducts() throws ClassNotFoundException, SQLException {
		ProductUtil productUtil = new ProductUtil();
		List<Product> products = productUtil.getProducts();
		if (products.isEmpty()) {
			throw new ProductNotFoundException("No Product found ");
		}

		return products;

	}
	
	@RequestMapping(value = "/products", params = { "orderBy", "searchString", "maxRecord","startAt" }, method = RequestMethod.GET)
	public List<Product> getProductuse(@RequestParam("searchString") String searchString,
										@RequestParam("orderBy") String orderBy,
										@RequestParam("maxRecord") String maxRecord,
										@RequestParam("startAt") String startAt) throws Exception{
		
		ProductUtil productUtil=new ProductUtil();
		List<Product> products= productUtil.getProductsUsingParameter(searchString,orderBy, maxRecord,startAt);
		
		
		return products;
	}

	@RequestMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) throws ClassNotFoundException, SQLException {
		ProductUtil productUtil = new ProductUtil();
		Product product = productUtil.getProduct(id);
		if (product == null) {
			throw new ProductNotFoundException("No Product found for the id: " + id);
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

	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	public String updateProduct(@RequestBody Product product, @PathVariable String id)
			throws ClassNotFoundException, SQLException {
		ProductUtil productUtil = new ProductUtil();
		boolean bool = productUtil.updateProduct(product, id);
		if (bool) {
			return "Product updated and the new id is  " + product.getProductId();
		}

		throw new ProductNotFoundException("Product updating failed for the  id : " + id + "!!  No Product found with id : "+id);
	}
	
	@RequestMapping(value = "/products/{id}" , method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable String id) {
		ProductUtil productUtil= new ProductUtil();
		boolean bool = productUtil.deleteProduct(id);
		if(bool) {
			return "Product deleted with id : "+id;
		}
		else {
			throw new ProductNotFoundException("Product deletion failed for the  id : " + id + "!!  No Product found with id : "+id);
		}
	}

}
