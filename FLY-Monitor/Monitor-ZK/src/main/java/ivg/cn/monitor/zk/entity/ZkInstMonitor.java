package ivg.cn.monitor.zk.entity;

import java.util.Date;

public class ZkInstMonitor {
    private Long id;

    private Long itemId;

    private String zkIp;

    private Integer zkPort;

    private String mode;

    private Integer connections;

    private Integer nodeCount;

    private Long sendPacketQty;

    private Long recvPacketQty;

    private Integer minLatency;
    
    private Integer avgLatency;
    
    private Integer maxLatency;

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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getZkIp() {
        return zkIp;
    }

    public void setZkIp(String zkIp) {
        this.zkIp = zkIp;
    }

    public Integer getZkPort() {
        return zkPort;
    }

    public void setZkPort(Integer zkPort) {
        this.zkPort = zkPort;
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

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public Long getSendPacketQty() {
        return sendPacketQty;
    }

    public void setSendPacketQty(Long sendPacketQty) {
        this.sendPacketQty = sendPacketQty;
    }

    public Long getRecvPacketQty() {
        return recvPacketQty;
    }

    public void setRecvPacketQty(Long recvPacketQty) {
        this.recvPacketQty = recvPacketQty;
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

	public Integer getMinLatency() {
		return minLatency;
	}

	public void setMinLatency(Integer minLatency) {
		this.minLatency = minLatency;
	}

	public Integer getAvgLatency() {
		return avgLatency;
	}

	public void setAvgLatency(Integer avgLatency) {
		this.avgLatency = avgLatency;
	}

	public Integer getMaxLatency() {
		return maxLatency;
	}

	public void setMaxLatency(Integer maxLatency) {
		this.maxLatency = maxLatency;
	}
    
}