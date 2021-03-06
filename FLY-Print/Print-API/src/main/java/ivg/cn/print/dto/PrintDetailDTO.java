package ivg.cn.print.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 打印详情DTO
 * */
@SuppressWarnings("serial")
public class PrintDetailDTO implements Serializable{

	private long printId;  // 打印单Id
	
	private String num;

    private Long merchantId;
    
    private Long operatorId;
	
	private List<InerDetail> detailItems;
	
	public long getPrintId() {
		return printId;
	}

	public void setPrintId(long printId) {
		this.printId = printId;
	}

	public List<InerDetail> getDetailItems() {
		return detailItems;
	}

	public void setDetailItems(List<InerDetail> detailItems) {
		this.detailItems = detailItems;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public static class InerDetail implements Serializable{
		
		private Long printId;

	    private Long commodityAssetId;

	    private String commodityAssetBarcode;

	    private String commodityName;

	    private String num;

	    private String assertUnit;

	    private Integer printQty;

		public Long getPrintId() {
			return printId;
		}

		public void setPrintId(Long printId) {
			this.printId = printId;
		}

		public Long getCommodityAssetId() {
			return commodityAssetId;
		}

		public void setCommodityAssetId(Long commodityAssetId) {
			this.commodityAssetId = commodityAssetId;
		}

		public String getCommodityAssetBarcode() {
			return commodityAssetBarcode;
		}

		public void setCommodityAssetBarcode(String commodityAssetBarcode) {
			this.commodityAssetBarcode = commodityAssetBarcode;
		}

		public String getCommodityName() {
			return commodityName;
		}

		public void setCommodityName(String commodityName) {
			this.commodityName = commodityName;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getAssertUnit() {
			return assertUnit;
		}

		public void setAssertUnit(String assertUnit) {
			this.assertUnit = assertUnit;
		}

		public Integer getPrintQty() {
			return printQty;
		}

		public void setPrintQty(Integer printQty) {
			this.printQty = printQty;
		}
	    
	}
	
}
