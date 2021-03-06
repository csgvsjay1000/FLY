package ivg.cn.monitor.redis.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RedisInstMonitor {
    private Long id;

    private Integer itemId;

    private Integer masterItemId;

    private String redisIp;

    private Integer redisPort;

    private String role;

    private String mode;

    private Integer connections;

    private Long usedMemory;

    private Long maxMemory;

    private Long objCount;

    private Integer keyspaceHits;

    private Integer keyspaceMisses;

    private BigDecimal memFragmentationRatio;

    private BigDecimal qps;
    
    private BigDecimal cpuRatio;

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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getMasterItemId() {
        return masterItemId;
    }

    public void setMasterItemId(Integer masterItemId) {
        this.masterItemId = masterItemId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Long getObjCount() {
        return objCount;
    }

    public void setObjCount(Long objCount) {
        this.objCount = objCount;
    }

    public Integer getKeyspaceHits() {
        return keyspaceHits;
    }

    public void setKeyspaceHits(Integer keyspaceHits) {
        this.keyspaceHits = keyspaceHits;
    }

    public Integer getKeyspaceMisses() {
        return keyspaceMisses;
    }

    public void setKeyspaceMisses(Integer keyspaceMisses) {
        this.keyspaceMisses = keyspaceMisses;
    }

    public BigDecimal getMemFragmentationRatio() {
        return memFragmentationRatio;
    }

    public void setMemFragmentationRatio(BigDecimal memFragmentationRatio) {
        this.memFragmentationRatio = memFragmentationRatio;
    }

    public BigDecimal getQps() {
        return qps;
    }

    public void setQps(BigDecimal qps) {
        this.qps = qps;
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

	public BigDecimal getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(BigDecimal cpuRatio) {
		this.cpuRatio = cpuRatio;
	}
    
}