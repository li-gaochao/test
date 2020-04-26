package org.akxy.zhky.manage.user.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.User;

/**
 * @ClassName: UserService.java
 * @Description:用户相关service
 * @date: 2018年5月30日
 */
public interface IUserService {
	/**
	 * @Title: findUserByName
	 * @Description: 根据用户名查找用户list
	 * @param: @param name
	 * @param: @return
	 * @return: List<User>
	 * @throws
	 */
	public List<User> findUserByName(String name);
	public List<User> findUserByExample(User user);
	public User findUserByNameAndPwd(String name,String pwd);
}
