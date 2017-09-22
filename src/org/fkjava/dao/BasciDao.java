package org.fkjava.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 父类接口，所有的DAO都要实现这个接口
 * 提供基本的增删改查方法
 * 
 * 个性化方法则在子接口当中提供：
 * tb_user  根据登录名和密码查询用户
 * tb_book  根据作者或者价格区间查询书籍
 * 
 * 实现功能的前提条件：
 * ORM（对象  关系  映射）
 * User      id  username
 * tb_user   id  username
 * 对象名和表名差别只在于"tb_"
 * 
 * 对象的属性必须和表的列名一样
 * 如果需要做成更完善的功能，建议增加一个xml文件描述对象和关系的映射信息
 * */
public interface BasciDao<T> {
	
	/**
	 * 根据id删除数据
	 * 思路：
	 * 所有表的删除语句区别只在于表名
	 * delete from tb_user where id = " + id
	 * delete from tb_student where id = " + id
	 * */
	void remove(Integer id);
	
	/**
	 * 根据id查询数据
	 * @param id
	 * @return 对象
	 * */
	T get(Integer id);
	
	/**
	 * 查询所有数据
	 * @return 封装所有对象的集合
	 * */
	List<T> get();
	
	/**
	 * 插入一条数据
	 * @return 返回插入数据生成的id主键值
	 * 
	 * 如何返回不同数据库的主键，每个数据库写一套不同的basicdao
	 * */
	Serializable save(T t);
	
	/**
	 * 复杂的个性化的select语句
	 * @param String query 要执行的sql语句
	 *        Object... params  可变参数
	 * @return 查询的结果的list集合
	 * 
	 * UserDaoImpl:
	 *  select * from tb_user where loginname=? and password = ?
	 *  select * from tb_student where class_id = ?
	 *  
	 *  思路：
	 *  1. pstm = con.prepareStatement(query);
	 *  2. 
	 *  int index = 1;
	 *  for(Object p : params){
	 *  	pstm.setObject(index++,p);
	 *  }
	 *  3. rs = pstm.executeQuery();
	 *  while(rs.next){
	 *  }
	 * */
	List<T> query(String query,Object... params);
	
	/**
	 * 复杂的select语句,一般是多表查询
	 * @param String query 要执行的sql语句
	 *        Object... params  可变参数
	 * @return 查询的结果的list集合(其中每一行数据就是一个Map,列名为key,值为value)
	 * List{map{id=1,name=jack},map{id=2,name=rose}}
	 * */
	List<Map<String,Object>> queryList(String query,Object... params);
	
	/**
	 * 复杂的DML语句
	 * delete from tb_user where adderss = ? and sex = ?
	 * @param String query 要执行的sql语句
	 *        Object... params  可变参数
	 * @return 查询的结果的list集合
	 * */
	int executeUpdate(String query,Object... params);
	

}
