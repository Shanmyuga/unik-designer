/**
 * 
 */
package com.cognizant.unik.data.jdbc.dao;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.unik.data.model.ResourceEntity;

/**
 * @author 238209
 *
 */
public interface ResourceRepository extends MongoRepository<ResourceEntity, BigInteger> {

}
