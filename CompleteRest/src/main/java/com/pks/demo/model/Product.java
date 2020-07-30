package com.pks.demo.model;

public class Product {
	private int productId;
	private String productName;
	private int productPrice;
	private String productDescription;
	
	
	public Product(int productId, String productName, int productPrice, String productDescription) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	

}
