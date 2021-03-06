package ivg.cn.merchant.entity;

import java.io.Serializable;

import ivg.cn.common.BaseEntity;

@SuppressWarnings("serial")
public class MerchantUser extends BaseEntity implements Serializable{

	private long id;
	
	private String username;
	
	private String password;
	
	private String aliasName;
	
	private long merchantId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	
}
