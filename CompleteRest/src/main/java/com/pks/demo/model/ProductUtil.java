package com.pks.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ProductUtil {
	String url="jdbc:mysql://localhost:3306/test";
	String uname="root";
	String pass="Prakash1";
	public  List<Product> getProducts() throws ClassNotFoundException, SQLException {
		String query="select * from products";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		PreparedStatement st=con.prepareStatement(query);
		
		List<Product> products=new ArrayList<Product>();
		ResultSet resultSet=st.executeQuery();
		while(resultSet.next()) {
			Product myProduct=new Product(Integer.parseInt(resultSet.getString(1)),
											resultSet.getString(2),
											Integer.parseInt(resultSet.getString(3)),
											resultSet.getString(4));
			products.add(myProduct);
			
		}
		return products;
	}
	
	public Product getProduct(String id) throws ClassNotFoundException, SQLException {
		String query="select * from products where product_id=?";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1, id);
		ResultSet resultSet=st.executeQuery();
		if(resultSet.next()) {
			Product myProduct=new Product(Integer.parseInt(resultSet.getString(1)),
					resultSet.getString(2),
					Integer.parseInt(resultSet.getString(3)),
					resultSet.getString(4));
			return myProduct;
		}
		return null;
	}
	
	public boolean createProduct(Product product) throws Exception {
		String query="insert into products values(?,?,?,?)";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1,Integer.toString(product.getProductId()));
		st.setString(2, product.getProductName());
		st.setString(3, Integer.toString(product.getProductPrice()));
		st.setString(4, product.getProductDescription());
		int count=0;
		try {
		 count=st.executeUpdate();
		}
		catch (Exception e) {
			
		}
		if(count>0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean updateProduct(Product product, String id) throws ClassNotFoundException, SQLException {
		String query="update products set product_id=?, product_name=?, product_price=? and product_description=? where product_id=?";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,uname,pass);
		PreparedStatement st=con.prepareStatement(query);
		st.setString(1,Integer.toString(product.getProductId()));
		st.setString(2, product.getProductName());
		st.setString(3, Integer.toString(product.getProductPrice()));
		st.setString(4, product.getProductDescription());
		st.setString(5, id);
		
		int count=0;
		try {
			count=st.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(count>0) {
			return true;
		}
		
		return false;
	}

}
