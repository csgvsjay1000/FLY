package ivg.cn.replenish.entity;

import java.io.Serializable;

/**
 * 补货时验证EPC有效性DTO
 * */
@SuppressWarnings("serial")
public class ValidateEpcDTO implements Serializable{

	private String epc;

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}
	
}
