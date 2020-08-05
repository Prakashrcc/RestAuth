package com.pks.demo.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestUserService {
	Configuration configuration = new Configuration().configure().addAnnotatedClass(RestUser.class);
			
	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();

	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	Logger logger = LoggerFactory.getLogger(ProductUtil.class);

	public RestUser getUser(String username) {
		logger.info("getUser() method started for  username: " + username);
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		RestUser restUser = null;
		restUser = (RestUser) session.get(RestUser.class, username);
		transaction.commit();
		session.close();
		if (restUser == null) {
			logger.warn("No User found with username: " + username);
		}
		logger.info("getUser() method completed for  username: " + username);

		return restUser;
	}

}
