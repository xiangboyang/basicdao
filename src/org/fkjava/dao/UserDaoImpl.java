package org.fkjava.dao;

import java.util.List;

import org.fkjava.bean.Student;
import org.fkjava.bean.User;

public class UserDaoImpl 
	extends BasicDaoImpl<User> 
	implements UserDao {

	@Override
	public User findWithLoginnameAndPassword(String loginname, String password) {
		// 准备sql
		String query = "select * from tb_user where loginname=? and password = ?";
		// 调用执行复杂操作的方法，返回集合
		List<User> list = this.query(query, loginname,password);
		// 因为loginname唯一，所以只取第一条数据
		return list.size() > 0 ? list.get(0) : null;
	}

}
