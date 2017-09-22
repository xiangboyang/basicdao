package org.fkjava.bean;

/**
 * CREATE TABLE `tb_student` (
  `id` int(11) NOT NULL auto_increment,
  `stuname` varchar(18) default NULL,
  `address` varchar(18) default NULL,
  `email` varchar(18) default NULL,
  `clazz_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
) 
 * */
public class Student {
	
	private Integer id;
	private String stuname;
	private String address;
	private String email;
	private Integer clazz_id;
	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStuname() {
		return stuname;
	}
	public void setStuname(String stuname) {
		this.stuname = stuname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getClazz_id() {
		return clazz_id;
	}
	public void setClazz_id(Integer clazz_id) {
		this.clazz_id = clazz_id;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", stuname=" + stuname + "]";
	}
	

}
