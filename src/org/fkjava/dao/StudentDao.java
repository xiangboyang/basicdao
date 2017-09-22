package org.fkjava.dao;

import java.util.List;

import org.fkjava.bean.Student;

public interface StudentDao extends BasciDao<Student> {
	
	List<Student> getWithClass(Integer id);

}
