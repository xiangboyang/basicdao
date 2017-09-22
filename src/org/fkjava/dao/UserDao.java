package org.fkjava.dao;

import org.fkjava.bean.User;

/**
 * BasciDao父类接口的实现类，完成基本的增删改查操作   
 * 子接口，定义的是个性化的操作
 * 
 * 父类接口： Collection 定义所有单值集合都有的方法
 * 子接口： List、Set  
 * */
public interface UserDao extends BasciDao<User> {

	User findWithLoginnameAndPassword(String loginname,String password);
	
}
