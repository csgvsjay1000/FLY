package ivg.cn.print.dto;

import java.io.Serializable;

/** 制单DTO */
@SuppressWarnings("serial")
public class InsertPrintDTO implements Serializable{

	private String name;  // 单名称

    private String batchNum;  // 批次号

    private Long merchantId;  // 商户ID
    
    private Long creater;  // 操作人

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}
	
}
