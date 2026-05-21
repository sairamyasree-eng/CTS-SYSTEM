package com.iispl.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.iispl.entity.Batch;
import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.util.HibernateUtil;


public class UserDao extends GenericDao<Batch, Long>{
	//findByUsername()
	public Optional<User> findActiveByUsername(String username) {

	    Session session = HibernateUtil
	            .getSessionFactory()
	            .openSession();

	    try {

	        String hql =
	                "FROM User u "
	              + "WHERE u.username = :username "
	              + "AND u.status = :status";

	        Query<User> query =
	                session.createQuery(hql, User.class);

	        query.setParameter(
	                "username",
	                username
	        );

	        query.setParameter(
	                "status",
	                Status.Active
	        );

	        User user = query.uniqueResult();

	        return Optional.ofNullable(user);

	    } finally {

	        session.close();
	    }
	}
}
