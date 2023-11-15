package com.cognizant.unik.data.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cognizant.unik.data.model.User;
import com.cognizant.unik.data.model.UserRowMapper;
@Repository
public class UserDAOImpl implements UserDAO {

	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public User getUser(String userName) {
		
		User user = null;
		try {
			user = this.jdbcTemplate.queryForObject(
			        "select * from user_info where user_name = ?", new Object[] {userName}, new UserRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	@Override
	public String addUser(User newUser) {
		
		User user = getUser(newUser.getUserName());
		if (user != null) {
			return "User already exists !!";
		}
		
		try {
			this.jdbcTemplate.update(
			        "insert into user_info (user_name, password, full_name) values (?, ?, ?)",
			        newUser.getUserName(), newUser.getPassword(), newUser.getFullName());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			return "Failed !!";
		}
		
		return "Success !!";
	}

	@Override
	public String updateUser(User updateUser) {
		User user = getUser(updateUser.getUserName());
		if (user == null) {
			return addUser(updateUser);
		}
		
		try {
			this.jdbcTemplate.update(
			        "update user_info set password = ?, full_name = ? where user_name = ?",
			        updateUser.getPassword(), updateUser.getFullName(), updateUser.getUserName());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			return "Failed !!";
		}
		
		return "Success !!";
	}
}
