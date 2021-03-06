package ivg.cn.dbsplit;

public class RowBounds {

	private int page;
	
	private int limit;
	
	public RowBounds(int page, int limit) {
		super();
		this.page = page;
		this.limit = limit;
	}
	
	public RowBounds() {
		
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return page*limit;
	}

}
