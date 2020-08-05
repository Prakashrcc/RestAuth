package com.pks.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pks.demo.model.ProductUtil;
import com.pks.demo.model.RestUser;

public class CheckToken {
	Configuration configuration = new Configuration().configure().addAnnotatedClass(RestUser.class);

	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();

	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	Logger logger = LoggerFactory.getLogger(ProductUtil.class);

	public boolean compareToken(String token, String username) throws ClassNotFoundException, SQLException {

		logger.info("compareToken() method accessed in CheckToken class with username: " + username);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		RestUser restUser = null;
		try {
			restUser = (RestUser) session.get(RestUser.class, username);
			transaction.commit();
			session.close();
		} catch (Exception e) {
			logger.warn("exception occured while checking Token in CheckToken class with username: " + username);
			return false;
		}

		if (restUser.getToken().equals(token)) {
			logger.info("compareToken() method completed in CheckToken class with username: " + username);
			return true;
		}

		logger.info("compareToken() method completed in CheckToken class with username: " + username);
		return false;
	}

}
