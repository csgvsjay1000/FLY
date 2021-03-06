package ivg.cn.replenish.entity;

import java.util.Date;

public class Replenish {
    private Long id;

    private String name;

    private String num;

    private Short status;

    private Date statusUpdate;

    private String batchNum;

    private Integer calculateOnsaleQty;

    private Integer actualOnsaleQty;

    private Integer actualOffsaleQty;

    private Long merchantId;

    private String remark;

    private String attr01;

    private String attr02;

    private String attr03;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(Date statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public Integer getCalculateOnsaleQty() {
        return calculateOnsaleQty;
    }

    public void setCalculateOnsaleQty(Integer calculateOnsaleQty) {
        this.calculateOnsaleQty = calculateOnsaleQty;
    }

    public Integer getActualOnsaleQty() {
        return actualOnsaleQty;
    }

    public void setActualOnsaleQty(Integer actualOnsaleQty) {
        this.actualOnsaleQty = actualOnsaleQty;
    }

    public Integer getActualOffsaleQty() {
        return actualOffsaleQty;
    }

    public void setActualOffsaleQty(Integer actualOffsaleQty) {
        this.actualOffsaleQty = actualOffsaleQty;
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
}