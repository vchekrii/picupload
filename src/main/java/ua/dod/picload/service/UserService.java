package ua.dod.picload.service;

import java.util.List;

import ua.dod.picload.domain.User;

public interface UserService {
	
	public void storeUser(User user);
	public void deleteUser(String id);
	public User getUser(String id);
	public List<User> getAllUsers();

}