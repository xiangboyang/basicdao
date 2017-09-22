package org.fkjava.dao;

import java.util.List;

import org.fkjava.bean.Student;

public class StudentDaoImpl 
	extends BasicDaoImpl<Student> 
	implements StudentDao {

	@Override
	public List<Student> getWithClass(Integer id) {
		// 准备sql
		String query = "select * from tb_student where clazz_id = ?";
		// 调用执行复杂操作的方法，返回集合
		return this.query(query, id);
	}

	
	

}
