package ivg.cn.vesta.impl.bean;

public enum IdType {

	SECONDS("seconds"), MILLISECONDS("milliseconds"), SHORTID("short_id");;
	
	private String name;
	
	private IdType(String name) {
		this.name = name;
	}
	
	public long value() {
        switch (this) {
            case SECONDS:
                return 0;
            case MILLISECONDS:
                return 1;
            case SHORTID:
                return 2;
            default:
                return 0;
        }
    }
	
	@Override
    public String toString() {
        return this.name;
    }
}
