package org.fkjava.test;

import org.fkjava.dao.StudentDao;
import org.fkjava.dao.StudentDaoImpl;
import org.fkjava.dao.UserDao;
import org.fkjava.dao.UserDaoImpl;


public class DaoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDao userDao = new UserDaoImpl();
//		userDao.remove(6);
//		User user = userDao.get(5);
//		System.out.println(user);
//		System.out.println(userDao.get());
		/*User user = new User();
		user.setAddress("gz");
		user.setEmail("mary@163.com");
		user.setLoginname("mary");
		user.setPASSWORD("123");
		user.setTel("123");
		user.setUsername("玛丽");
		System.out.println(userDao.save(user));*/
		System.out.println(userDao.findWithLoginnameAndPassword("tom", "123"));
		
		StudentDao sDao = new StudentDaoImpl();
//		sDao.remove(2);
//		Student stu = sDao.get(2);
//		System.out.println(stu);
//		System.out.println(sDao.get());
		/*Student stu = new Student();
		stu.setAddress("gz");
		stu.setClazz_id(1);
		stu.setEmail("aaa");
		stu.setStuname("小明");
		System.out.println(sDao.save(stu));*/
		System.out.println(sDao.getWithClass(1));

	
	}

}
