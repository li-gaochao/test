package org.akxy.zhky.manage.user.service.impl;

import java.util.List;

import org.akxy.zhky.manage.dao.mapper.UserMapper;
import org.akxy.zhky.manage.pojo.User;
import org.akxy.zhky.manage.pojo.UserExample;
import org.akxy.zhky.manage.pojo.UserExample.Criteria;
import org.akxy.zhky.manage.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> findUserByName(String name) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<User> list = userMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<User> findUserByExample(User user) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(user.getName());
		criteria.andPwdEqualTo(user.getPwd());
		List<User> list = userMapper.selectByExample(example);
		return list;
	}

	@Override
	public User findUserByNameAndPwd(String name, String pwd) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andPwdEqualTo(pwd);
		List<User> list = userMapper.selectByExample(example);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}

	}

}
