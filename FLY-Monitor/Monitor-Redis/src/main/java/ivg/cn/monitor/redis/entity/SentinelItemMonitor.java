package ivg.cn.monitor.redis.entity;

import java.util.Date;

public class SentinelItemMonitor {
    private Long id;

    private String sentinelIp;

    private Integer sentinelPort;

    private Integer pingCost;

    private String remark;

    private String attr01;

    private String attr02;

    private String attr03;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSentinelIp() {
        return sentinelIp;
    }

    public void setSentinelIp(String sentinelIp) {
        this.sentinelIp = sentinelIp;
    }

    public Integer getSentinelPort() {
        return sentinelPort;
    }

    public void setSentinelPort(Integer sentinelPort) {
        this.sentinelPort = sentinelPort;
    }

    public Integer getPingCost() {
        return pingCost;
    }

    public void setPingCost(Integer pingCost) {
        this.pingCost = pingCost;
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
}