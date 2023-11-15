/**
 * 
 */
package com.cognizant.unik.data.jdbc.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.unik.data.model.ConfigEntity;

/**
 * @author 238209
 *
 */
public interface ConfigRepository extends MongoRepository<ConfigEntity, BigInteger> {

	public List<ConfigEntity> findByConfigLanguage(String configLanguage);
	
	public List<ConfigEntity> findByConfigName(String configName);
	
	public List<ConfigEntity> findByDefaultConfig(boolean defaultConfig);

	public List<ConfigEntity> findByConfigLanguageAndDefaultConfig(String configLanguage, boolean defaultConfig);
}
