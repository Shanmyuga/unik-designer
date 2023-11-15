package com.cognizant.unik.service.model.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognizant.unik.data.jdbc.dao.UserRepository;
import com.cognizant.unik.data.model.User;
import com.cognizant.unik.data.model.UserBean;
import com.cognizant.unik.data.model.UserEntity;
import com.cognizant.unik.service.model.data.Response;

@Controller
@RequestMapping("/api")
public class DataModelService {

	@Autowired
	private UserRepository userDao;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public Response authenticate(@RequestBody User userRequest) {

		Response response = new Response();
		
		List<UserEntity> users = userDao.findByUserName(userRequest.getUserName());
		if (users != null && users.isEmpty() == false) {
			UserEntity user = users.get(0);
			if (StringUtils.equals(userRequest.getPassword(), user.getPassword())) {
				response.setSuccess(true);
				response.setUser(user);
			} else {
				response.setMessage("Invalid Password. Please try again !!");
			}
		} else {
			response.setMessage("Username / Password does not exist");
		}
		
		return response;
	}
	
	@RequestMapping(value = "/allUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<UserBean> getAllUsers() {
		List<UserBean> retVal = new ArrayList<UserBean>();
		
		List<UserEntity> usersList = userDao.findAll();
		UserBean bean = null;
		for (UserEntity userEntity : usersList) {
			bean = new UserBean();
			BeanUtils.copyProperties(userEntity, bean);
			
			if (userEntity.getId() != null) {
				bean.setId(userEntity.getId().toString());
			}
			
			retVal.add(bean);
		}
		
		return retVal;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	public UserEntity addUser(@RequestBody UserEntity userRequest) {
		return userDao.save(userRequest);
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public UserEntity updateUser(@RequestBody UserEntity userRequest) {
		return userDao.save(userRequest);
	}
	
	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void deleteUser(@PathVariable String id) {
		userDao.deleteById(new BigInteger(id));
	}
}
