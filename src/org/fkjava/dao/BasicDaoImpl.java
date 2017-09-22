package org.fkjava.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.fkjava.bean.User;
import org.fkjava.exception.DataAccessException;
import org.fkjava.util.ConnectionFactory;

/**
 * 父类接口的实现类，完成基本的增删改查操作
 * */
public class BasicDaoImpl<T> implements BasciDao<T> {
	
	private Class<?> entityClass;
	private String tableName;
	
	private Logger logger = Logger.getLogger(this.getClass());

	public BasicDaoImpl() {
		super();
		// 获得当前类型
		Class<?> c = this.getClass();
		/**
		 * getGenericSuperclass返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		 * 返回结果：org.fkjava.dao.BasicDaoImpl<org.fkjava.bean.User>
		 * 参数化类型：所有的泛型都是参数化类型
		 * */ 
		Type type = c.getGenericSuperclass();
		logger.debug("BasicDaoImpl: " + type);
		// 强转成参数化类型，目的是为了调用参数化类型中的方法
		ParameterizedType pType = (ParameterizedType)type;
		/**
		 * 返回表示此类型实际类型参数的 Type 对象的数组。
		 * org.fkjava.dao.BasicDaoImpl<org.fkjava.bean.User> 参数类型数组只有一个元素
		 * MyMap<Integer,String> 参数类型数组有两个元素
		 * */
		Type[] paramTypes = pType.getActualTypeArguments();
		entityClass = (Class)paramTypes[0];
		logger.debug("entityClass: " + entityClass);
		tableName = "tb_" + entityClass.getSimpleName();
	}

	/**
	 * 1. public class UserDao extends BasicDaoImpl<User>
	 *   代码： UserDao userDao = new UserDao(); 
	 * 2. 在父类的构造方法中获取UserDao定义时传递的<User>
	 * 3. 获取到需要操作的对象的Class之后，通过反射自动构建sql语句
	 * 4. 执行sql，关闭资源
	 * */
	@Override
	public void remove(final Integer id) {
		logger.debug("remove");
		this.template(new CallbackListener() {
			@Override
			public Object doInCallback(Connection con, PreparedStatement pstm,
					ResultSet rs) throws Exception {
				logger.debug("template");
				/***************构建sql***********************/
				StringBuffer sql = new StringBuffer();
				sql.append("delete from ").append(tableName).append(" where id = ?");
				logger.info(sql.toString());
				/***************构建sql***********************/
				pstm = con.prepareStatement(sql.toString());
				pstm.setInt(1, id);
				pstm.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public T get(final Integer id) {
		return this.template(new CallbackListener<T>() {
			@Override
			public T doInCallback(Connection con, PreparedStatement pstm,
					ResultSet rs) throws Exception {
				/***************构建sql***********************/
				StringBuffer sql = new StringBuffer();
				sql.append("select * from ").append(tableName).append(" where id = ?");
				logger.info(sql.toString());
				/***************构建sql***********************/
				pstm = con.prepareStatement(sql.toString());
				pstm.setInt(1, id);
				rs = pstm.executeQuery();
				/***************
				 * 1. 如何确定对象 T
				 * 2. 如何找到数据列对应的T的属性  反射
				 * 本方法使用根据对象的所有字段查找表的所有列
				 * ***********************/
				if(rs.next()){
					// User user = new User();
					Object obj = entityClass.newInstance();
					// 获得对象的所有字段,返回数组
					Field[] fields = entityClass.getDeclaredFields();
					// 循环: id username ...
					for(Field field : fields){
						String columnName = field.getName(); // 字段的名称=表的列名
						// rs.getXXX("id");
						Object columnValue = rs.getObject(columnName); // 列的值
						// 判断访问权限
						if(!field.isAccessible())
							field.setAccessible(true);
						// 将列的值设置到对应的对象的字段当中
						field.set(obj, columnValue);
					}
					return (T)obj;
				}
				return null;
			}
		});
	}

	@Override
	public List<T> get() {
		return this.template(new CallbackListener<List<T>>() {
			@Override
			public List<T> doInCallback(Connection con, PreparedStatement pstm,
					ResultSet rs) throws Exception {
				List<T> list = new ArrayList<>();
				/***************构建sql***********************/
				StringBuffer sql = new StringBuffer();
				sql.append("select * from ").append(tableName);
				logger.info(sql.toString());
				/***************构建sql***********************/
				pstm = con.prepareStatement(sql.toString());
				rs = pstm.executeQuery();
				/***************
				 * 1. 如何确定对象 T
				 * 2. 如何找到数据列对应的T的属性  反射
				 * 本方法使用表的所有列查找对象对应的字段
				 * ***********************/
				// 获取ResultSetMetaData对象
				ResultSetMetaData rsmd = rs.getMetaData();
				// 循环结果集
				while(rs.next()){
					// User user = new User();
					Object obj = entityClass.newInstance();
					// 循环每条数据的所有列
					for(int i = 1; i <= rsmd.getColumnCount();i++){
						String columnName = rsmd.getColumnName(i);// 列名=对象的字段名
						Object columnValue = rs.getObject(columnName); // 列值
						// 根据列名(字段名)获取对应的字段
						// Field field = entityClass.getDeclaredField("id");
						Field field = entityClass.getDeclaredField(columnName);
						// 判断访问权限
						if(!field.isAccessible())
							field.setAccessible(true);
						// 将列的值设置到对应的对象的字段当中
						field.set(obj, columnValue);
					}
					// 添加到集合
					list.add((T)obj);
				}
				return list;
			}
		});
	}

	@Override
	public Serializable save(final T t) {
		return this.template(new CallbackListener<Serializable>() {
			@Override
			public Serializable doInCallback(Connection con,
					PreparedStatement pstm, ResultSet rs) throws Exception {
				/***************
				 * 构建sql
				 * insert into tb_user(name,sex...) values(?,?...);
				 * ***********************/
				StringBuffer sql = new StringBuffer();
				sql.append(" insert into ").append(tableName).append("(");
				// 获取对象的所有字段
				Field[] fields = entityClass.getDeclaredFields();
				for(Field field : fields){
					// mysql不需要插入id
					if(!field.getName().equals("id")){
						sql.append(field.getName()).append(",");
					}
				}
				// 去掉最后一个逗号
				sql.deleteCharAt(sql.length()-1);
				sql.append(" ) values( ");
				for(Field field : fields){
					// mysql不需要插入id
					if(!field.getName().equals("id")){
						sql.append("?,");
					}
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" ) ");
				logger.info(sql.toString());
				/***************构建sql***********************/
				pstm = con.prepareStatement(sql.toString());
				/**
				 * 给所有?号赋值,每个值就是列名对应的字段的值
				 * */
				int parameterIndex = 1;
				for(Field field : fields){
					// mysql不需要插入id
					if(!field.getName().equals("id")){
						if(!field.isAccessible())
							field.setAccessible(true);
						// 设置参数
						// pstm.setString(1,"");
						pstm.setObject(parameterIndex++, field.get(t));
					}
				}
				int result = pstm.executeUpdate();
				// 返回主键
				if(result > 0){
					rs = pstm.getGeneratedKeys();
					rs.next();
					return (Serializable)rs.getObject(1);
				}
				return null;
			}
		});
	}
	
	/**
	 * 回调接口
	 * @param <E>
	 */
	protected interface CallbackListener<E> {
		/**
		 * 回调方法，只处理实际的DAO操作，不必关心conn、pstm和rs的资源释放等问题
		 * 
		 * @param conn
		 * @param pstmt
		 * @param rs
		 * @return
		 * @throws Throwable
		 * 用户是save、delete、update或者select交给用户去完成
		 */
		E doInCallback(Connection con, PreparedStatement pstm, ResultSet rs) 
			throws Exception;
	}
	
	/**
	 * 数据操作的模板方法
	 * @param <E>
	 * @param callback接口
	 * @return 返回回调方法的返回值
	 * @throws DataAccessException
	 *             所有回调方法所抛出方法都会被包装成此运行时异常,该异常extends RuntimeException
	 * @see {@link Callback}
	 * 现阶段这个方法就是帮我们做了获取连接和关闭连接的动作,之后还可以处理事务操作
	 * <E> E 代表该方法是一个泛型方法
	 */
	protected <E> E template(CallbackListener<E> callback) throws DataAccessException {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			logger.debug("template 获取连接");
			// 执行真正的业务操作CRUD,留给用户实现
			return callback.doInCallback(con, pstm, rs);
		} catch (Exception se) {
			throw new DataAccessException(se);
		} finally {
			logger.debug("执行finally块");
			//关闭数据库连接
			ConnectionFactory.close(con,pstm, rs);
		}
	}

	@Override
	public List<T> query(final String query, final Object... params) {
		return this.template(new CallbackListener<List<T>>() {
			@Override
			public List<T> doInCallback(Connection con, PreparedStatement pstm,
					ResultSet rs) throws Exception {
				List<T> list = new ArrayList<>();
				pstm = con.prepareStatement(query);
				// 如果有参数，需要设置参数
				// List<User> list = this.query(query, loginname,password);
				int parameterIndex = 1;
				if(params != null && params.length > 0){
					for(Object param: params){
						pstm.setObject(parameterIndex++, param);
					}
				}
				rs = pstm.executeQuery();
				// 封装到对象返回
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){
					Object obj = entityClass.newInstance();
					for(int i = 1;i <= rsmd.getColumnCount();i++){
						String columnName = rsmd.getColumnName(i);
						Object columnValue = rs.getObject(columnName);
						Field field = entityClass.getDeclaredField(columnName);
						if(!field.isAccessible())
							field.setAccessible(true);
						field.set(obj, columnValue);
					}
					list.add((T)obj);
				}
				return list;
			}
		});
	}

	@Override
	public List<Map<String, Object>> queryList(final String query, final Object... params) {
		return this.template(new CallbackListener<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInCallback(Connection con,
					PreparedStatement pstm, ResultSet rs) throws Exception {
				List<Map<String, Object>> list = new ArrayList<>();
				pstm = con.prepareStatement(query);
				int parameterIndex = 1;
				if(params != null && params.length > 0){
					for(Object param: params){
						pstm.setObject(parameterIndex++, param);
					}
				}
				rs = pstm.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){
					Map<String, Object> row = new HashMap<String, Object>();
					for(int i = 1;i <= rsmd.getColumnCount();i++){
						String columnName = rsmd.getColumnName(i);
						Object columnValue = rs.getObject(columnName);
						row.put(columnName, columnValue);
					}
					list.add(row);
				}
				return list;
			}
		});
	}

	// 注意：返回的int值是执行dml语句所影响的行数
	@Override
	public int executeUpdate(final String query, final Object... params) {
		return this.template(new CallbackListener<Integer>() {

			@Override
			public Integer doInCallback(Connection con, PreparedStatement pstm,
					ResultSet rs) throws Exception {
				pstm = con.prepareStatement(query);
				for(int i = 0;i < params.length;i++){
					pstm.setObject(i+1, params[i]);
				}
				return pstm.executeUpdate();
			}
		});
	}


}
