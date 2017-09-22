package org.fkjava.bean;

/**
 * CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL auto_increment,
  `loginname` varchar(90) default NULL,
  `username` varchar(60) default NULL,
  `address` varchar(60) default NULL,
  `tel` varchar(60) default NULL,
  `email` varchar(150) default NULL,
  `PASSWORD` varchar(90) default NULL,
  PRIMARY KEY  (`id`)
)
 * */
public class User {
	
	private Integer id;
	private String loginname;
	private String username;
	private String address;
	private String tel;
	private String email;
	private String PASSWORD;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}
	
	

}
