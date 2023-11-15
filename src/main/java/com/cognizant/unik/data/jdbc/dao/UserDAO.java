package com.cognizant.unik.data.jdbc.dao;

import com.cognizant.unik.data.model.User;

public interface UserDAO {
	
	public User getUser(String userName);
	
	public String addUser(User newUser);
	
	public String updateUser(User updateUser);
}
