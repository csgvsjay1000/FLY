package ivg.cn.boss.entity;

import ivg.cn.common.BaseEntity;

public class BossUser extends BaseEntity{

	private long id;
	
	private String username;
	
	private String password;
	
	private String aliasName;
	
	private long merchantId;  // 商户id

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

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	
}
