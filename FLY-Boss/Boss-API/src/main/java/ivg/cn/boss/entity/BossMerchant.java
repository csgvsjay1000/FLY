package ivg.cn.boss.entity;

import ivg.cn.common.BaseEntity;

public class BossMerchant extends BaseEntity{

	private long id;
	
	private String name;
	
	private String aliasName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
	
}
