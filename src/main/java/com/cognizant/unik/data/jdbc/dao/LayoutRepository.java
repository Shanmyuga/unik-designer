/**
 * 
 */
package com.cognizant.unik.data.jdbc.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.unik.data.model.LayoutEntity;

/**
 * @author 238209
 *
 */
public interface LayoutRepository extends MongoRepository<LayoutEntity, BigInteger> {

	
	public List<LayoutEntity> findByType(String type);
	
	public List<LayoutEntity> findByLayoutName(String layoutName);
	
	public List<LayoutEntity> findByLayoutNameAndType(String layoutName, String type);
	
}
