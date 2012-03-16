package ua.dod.picload.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.dod.picload.domain.User;

@Repository
public class UserDAOImpl implements UserDAO{
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	PictureDAO pictureDao;

	@Override
	public void storeUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public void deleteUser(String id) {
		System.out.println("deleteUser");
		User user = getUser(id);
		if (null != user) {
			sessionFactory.getCurrentSession().delete(user);
		}		
	}

	@Override
	public User getUser(String id) {
		return (User) sessionFactory.getCurrentSession().get(User.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		return sessionFactory.getCurrentSession().createSQLQuery("SELECT * FROM users").addEntity(User.class).list();
	}

}
