package com.pks.demo.model;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pks.demo.exception.RestRequestException;

public class ProductUtil {
	Configuration configuration = new Configuration().configure().addAnnotatedClass(Product.class)
			.addAnnotatedClass(Category.class);
	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();

	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	Logger logger = LoggerFactory.getLogger(ProductUtil.class);

	public List<Product> getProducts() {
		logger.info("getProducts() method accessed in ProductUtil class");
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Product");
		List<Product> products = null;
		products = query.list();
		transaction.commit();
		session.close();
		if (products.isEmpty()) {
			logger.warn("No Product found");
		}
		logger.info("getProduct() method completed in ProductUtil class");
		return products;

	}

	public Product getProduct(String id) throws ClassNotFoundException, SQLException {
		logger.info("getProduct() method accessed in ProductUtil class for productId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Product product = null;
		product = (Product) session.get(Product.class, Integer.parseInt(id));
		transaction.commit();
		session.close();
		if (product == null) {
			logger.warn("No Product found for productId: " + id);
		}
		logger.info("getProduct() method completed in ProductUtil class for productId: " + id);
		return product;
	}

	public boolean createProduct(Product product) throws SQLIntegrityConstraintViolationException {
		logger.info("createProduct() method accessed in ProductUtil class");
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(product);

		} catch (Exception e) {
			logger.error("Exception occured while creating new product with productId: " + product.getProductId());
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		logger.info("createProduct() method completed in ProductUtil class");
		return true;

	}

	public boolean updateProduct(Product product, String id) throws ClassNotFoundException, SQLException {
		logger.info("updateProduct() method accessed in ProductUtil class with productId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(product);

		} catch (Exception e) {
			logger.error("Exception occured while updating product with productId: " + product.getProductId());
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		logger.info("updateProduct() method completed in ProductUtil class with productId: " + id);
		return true;
	}

	public boolean deleteProduct(String id) {
		logger.info("deleteProduct() method accessed in ProductUtil class with productId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("delete Product where productId= :id");
		query.setParameter("id", Integer.parseInt(id));
		System.out.println(query);
		int count = 0;
		try {
			count = query.executeUpdate();

		} catch (Exception e) {
			logger.error("Exception occured while deleting product with productId: " + id);
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		if (count > 0) {
			logger.info("deleteProduct() method completed in ProductUtil class with productId: " + id);
			return true;
		}

		logger.info(
				"deleteProduct() method was not able to delete the product in ProductUtil class with productId: " + id);
		return false;
	}

	public List<Product> getProductsUsingParameter(String searchString, String orderBy, String maxRecord,
			String startAt) throws Exception {
		logger.info("getProductsUsingParameter() method accessed in ProductUtil class");

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = null;
		if (searchString != null && orderBy != null && maxRecord != null && startAt != null) {
			query = session.createQuery("from Product where productName like ?  order by productName " + orderBy);
			query.setParameter(0, "%" + searchString + "%");
			// query.setParameter("order", orderBy);
			if (maxRecord.length() != 0 && startAt.length() != 0 && Integer.parseInt(maxRecord) > -1
					&& Integer.parseInt(startAt) > -1) {
				query.setFirstResult(Integer.parseInt(startAt));
				query.setMaxResults(Integer.parseInt(maxRecord));
			} else {
				throw new RestRequestException("Invalid value for maxRecord or startAt");
			}
		}

		List<Product> products = null;

		products = query.list();
		logger.info("method completed");

		return products;
	}

}
