package ivg.cn.monitor.redis.entity;

import java.util.Date;

public class RedisItem {
    private Integer id;

    private Long clusterId;

    private String redisIp;

    private Integer redisPort;

    private Integer status;

    private Date statusUpdate;
    
    private Integer slowlogLogSlowerThan;
    
    private Integer slowlogMaxLen;

    private String remark;

    private String attr01;

    private String attr02;

    private String attr03;

    private Date createDate;

    private Date updateDate;

    private Long creater;

    private Long updater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getRedisIp() {
        return redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(Date statusUpdate) {
        this.statusUpdate = statusUpdate;
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

	public Integer getSlowlogLogSlowerThan() {
		return slowlogLogSlowerThan;
	}

	public void setSlowlogLogSlowerThan(Integer slowlogLogSlowerThan) {
		this.slowlogLogSlowerThan = slowlogLogSlowerThan;
	}

	public Integer getSlowlogMaxLen() {
		return slowlogMaxLen;
	}

	public void setSlowlogMaxLen(Integer slowlogMaxLen) {
		this.slowlogMaxLen = slowlogMaxLen;
	}
    
}