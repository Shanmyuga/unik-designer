/**
 * 
 */
package com.cognizant.unik.data.jdbc.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.unik.data.model.ComponentEntity;

/**
 * @author 238209
 *
 */
public interface ComponentRepository extends MongoRepository<ComponentEntity, BigInteger> {
	
	public List<ComponentEntity> findByComponentName(String componentName);
}
