package ivg.cn.monitor.redis.entity;

import java.util.Date;

public class MasterItemMonitor {
    private Long id;

    private String masterName;

    private String masterRedisIp;

    private Integer masterRedisPort;

    private Integer totalSlaves;

    private Integer currentSlaves;

    private Integer totalSentinels;

    private Integer currentSentinels;

    private Integer quorum;

    private Integer downAfterMilliseconds;

    private Integer failoverTimeout;

    private Integer parallelSyncs;

    private String enable;

    private String status;

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

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterRedisIp() {
        return masterRedisIp;
    }

    public void setMasterRedisIp(String masterRedisIp) {
        this.masterRedisIp = masterRedisIp;
    }

    public Integer getMasterRedisPort() {
        return masterRedisPort;
    }

    public void setMasterRedisPort(Integer masterRedisPort) {
        this.masterRedisPort = masterRedisPort;
    }

    public Integer getTotalSlaves() {
        return totalSlaves;
    }

    public void setTotalSlaves(Integer totalSlaves) {
        this.totalSlaves = totalSlaves;
    }

    public Integer getCurrentSlaves() {
        return currentSlaves;
    }

    public void setCurrentSlaves(Integer currentSlaves) {
        this.currentSlaves = currentSlaves;
    }

    public Integer getTotalSentinels() {
        return totalSentinels;
    }

    public void setTotalSentinels(Integer totalSentinels) {
        this.totalSentinels = totalSentinels;
    }

    public Integer getCurrentSentinels() {
        return currentSentinels;
    }

    public void setCurrentSentinels(Integer currentSentinels) {
        this.currentSentinels = currentSentinels;
    }

    public Integer getQuorum() {
        return quorum;
    }

    public void setQuorum(Integer quorum) {
        this.quorum = quorum;
    }

    public Integer getDownAfterMilliseconds() {
        return downAfterMilliseconds;
    }

    public void setDownAfterMilliseconds(Integer downAfterMilliseconds) {
        this.downAfterMilliseconds = downAfterMilliseconds;
    }

    public Integer getFailoverTimeout() {
        return failoverTimeout;
    }

    public void setFailoverTimeout(Integer failoverTimeout) {
        this.failoverTimeout = failoverTimeout;
    }

    public Integer getParallelSyncs() {
        return parallelSyncs;
    }

    public void setParallelSyncs(Integer parallelSyncs) {
        this.parallelSyncs = parallelSyncs;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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