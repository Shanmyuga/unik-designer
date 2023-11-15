/**
 * 
 */
package com.cognizant.unik.data.jdbc.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.unik.data.model.UserEntity;

/**
 * @author 238209
 *
 */
public interface UserRepository extends MongoRepository<UserEntity, BigInteger> {

	public List<UserEntity> findByUserName(String userName);
	
}
