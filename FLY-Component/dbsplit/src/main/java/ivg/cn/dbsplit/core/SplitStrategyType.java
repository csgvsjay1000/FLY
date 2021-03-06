package ivg.cn.dbsplit.core;

public enum SplitStrategyType {

	VERTICAL("vertical"), HORIZONTAL("horizontal");
	
	private String value;
	
	private SplitStrategyType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
