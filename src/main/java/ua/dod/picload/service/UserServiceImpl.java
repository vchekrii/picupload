package ua.dod.picload.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.dod.picload.dao.UserDAO;
import ua.dod.picload.domain.User;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserDAO userDao;

	@Transactional
	@Override
	public void storeUser(User user) {
		userDao.storeUser(user);
	}

	@Transactional
	@Override
	public void deleteUser(String id) {
		userDao.deleteUser(id);	
	}

	@Transactional
	@Override
	public User getUser(String id) {
		return userDao.getUser(id);
	}

	@Transactional
	@Override
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

}
