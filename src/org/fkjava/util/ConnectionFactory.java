package org.fkjava.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 本例将数据库连接直接写在类当中，修改的话可以直接修改代码
 * 如果需要做成更完善的功能，建议增加一个xml文件描述数据库参数信息
 * */
public class ConnectionFactory {

	private static DataSource ds;
	
	// 静态块给DataSource赋值
	static{
		//获取tomcat的连接池对象
		BasicDataSource bds = new BasicDataSource();
		//设置连接   参数
		bds.setDriverClassName("com.mysql.jdbc.Driver");
		bds.setUrl("jdbc:mysql://192.168.10.222:3306/reflect");
		bds.setUsername("root");
		bds.setPassword("root");
		bds.setMaxActive(100);//最大活动数3
		bds.setMaxIdle(50); //最大保存数2
		bds.setMaxWait(5000);//最大等待时间5000毫秒
		// 让DataSource变量指向BasicDataSource bds
		ds = bds;
	}
	
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close(Connection con,Statement stm,ResultSet rs){
		try {
			if(rs != null) rs.close();
			if(stm != null) stm.close();
			if(con != null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
