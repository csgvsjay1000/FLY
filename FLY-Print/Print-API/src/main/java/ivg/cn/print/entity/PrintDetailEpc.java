package ivg.cn.print.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class PrintDetailEpc implements Serializable{
    private Long id;

    private Long printId;

    private Long commodityAssetId;

    private String commodityAssetBarcode;

    private String commodityName;

    private String assertUnit;

    private String epc;

    private Long merchantId;

    private String remark;

    private String attr01;

    private String attr02;

    private String attr03;

    private Date detailCreateDate;
    
    private Date createDate;

    private Date updateDate;

    private Long creater;

    private Long updater;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getAssertUnit() {
        return assertUnit;
    }

    public void setAssertUnit(String assertUnit) {
        this.assertUnit = assertUnit;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttr01() {
        return attr01;
    }

    public void setAttr01(String attr01) {
        this.attr01 = attr01;
    }

    public String getAttr02() {
        return attr02;
    }

    public void setAttr02(String attr02) {
        this.attr02 = attr02;
    }

    public String getAttr03() {
        return attr03;
    }

    public void setAttr03(String attr03) {
        this.attr03 = attr03;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

	public Date getDetailCreateDate() {
		return detailCreateDate;
	}

	public void setDetailCreateDate(Date detailCreateDate) {
		this.detailCreateDate = detailCreateDate;
	}
    
}