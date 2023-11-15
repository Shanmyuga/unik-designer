/**
 * 
 */
package com.cognizant.unik.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author 238209
 *
 */
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int arg1) throws SQLException {
		User user = new User();
		user.setUserName(rs.getString("user_name"));
		user.setPassword(rs.getString("password"));
		user.setFullName(rs.getString("full_name"));
		
		return user;
	}

}
