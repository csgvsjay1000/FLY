package ivg.cn.dbsplit.core;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class SplitNode {

	private JdbcTemplate masterTemplate;
	
	private List<JdbcTemplate> slaveTemplates;
	
	public SplitNode() {
		
	}

	public JdbcTemplate getMasterTemplate() {
		return masterTemplate;
	}

	public void setMasterTemplate(JdbcTemplate masterTemplate) {
		this.masterTemplate = masterTemplate;
	}

	public List<JdbcTemplate> getSlaveTemplates() {
		return slaveTemplates;
	}

	public void setSlaveTemplates(List<JdbcTemplate> slaveTemplates) {
		this.slaveTemplates = slaveTemplates;
	}
	
}
