package ivg.cn.boss.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InsertMerchantDTO implements Serializable{

	private String name;  //商户名称
	
	private String aliasName;  //商户别名
	
	private String username;  //登录用户名
	
	private String password;  //登录密码

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
