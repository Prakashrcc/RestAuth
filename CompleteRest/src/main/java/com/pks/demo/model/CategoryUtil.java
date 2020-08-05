package com.pks.demo.model;

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

public class CategoryUtil {
	Configuration configuration = new Configuration().configure().addAnnotatedClass(Product.class)
			.addAnnotatedClass(Category.class);
	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();

	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	Logger logger = LoggerFactory.getLogger(CategoryUtil.class);

	public List<Category> getCategories() {
		logger.info("getCategories() method accessed in categoryUtil class");
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Category");
		List<Category> categories = null;
		categories = query.list();

		transaction.commit();
		session.close();
		if (categories.isEmpty()) {
			logger.warn("No catogories found");
		}
		logger.info("getCategories() method completed in CategoryUtil class");
		return categories;

	}

	public Category getCategory(String id) {
		logger.info("getCategory() method accessed in CategoryUtil class for categoryId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Category category = null;
		try {
			category = (Category) session.get(Category.class, Integer.parseInt(id));

		} catch (Exception e) {
			logger.warn(e + " Exception occured in getCategory() method for categoryId: " + id);
		} finally {
			transaction.commit();
			session.close();
		}
		logger.info("getCategory() method completed in CategoryUtil class for categoryId: " + id);
		return category;
	}

	public boolean createCategory(Category category) {
		logger.info("createCategory() method accessed in CategoryUtil class");
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(category);

		} catch (Exception e) {
			logger.error("Exception occured while creating new category with categoryId: " + category.getCategoryId());
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		logger.info("createCategory() method completed in CategoryUtil class");
		return true;
	}

	public boolean updateCategory(Category category, String id) {
		logger.info("updateCategory() method accessed in CategoryUtil class with categoryId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(category);

		} catch (Exception e) {
			logger.error("Exception occured while updating product with productId: " + category.getCategoryId());
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		logger.info("updateCategory() method completed in CategoryUtil class with categoryId: " + id);
		return true;
	}

	public boolean deleteCategory(String id) {
		logger.info("deleteCategory() method accessed in CategoryUtil class with categoryId: " + id);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("delete Category where categoryId= :id");
		query.setParameter("id", Integer.parseInt(id));
		int count = 0;
		try {
			count = query.executeUpdate();

		} catch (Exception e) {
			logger.error("Exception occured while deleting category with categoryId: " + id);
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		if (count > 0) {
			logger.info("deleteCategory() method completed in CategoryUtil class with categoryId: " + id);
			return true;
		}
		logger.info(
				"deleteCategory() method was not able to delete the category in CategoryUtil class with categoryId: "
						+ id);
		return false;
	}

}
