package ua.dod.picload.dao;

import java.util.List;

import ua.dod.picload.domain.User;

public interface UserDAO {
	
	public void storeUser(User user);
	public void deleteUser(String id);
	public User getUser(String id);
	public List<User> getAllUsers();

}