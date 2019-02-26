package ivg.cn.vesta.impl;

import ivg.cn.vesta.Id;
import ivg.cn.vesta.impl.populater.IdPopulator;
import ivg.cn.vesta.impl.populater.SyncIdPopulator;

public class IdServiceImpl extends AbstractIdServiceImpl{

	private IdPopulator idPopulator;
	
	@Override
	public void init() {
		super.init();
		initPopulator();
	}
	
	@Override
	protected void populateId(Id id) {
		// TODO Auto-generated method stub
		idPopulator.populateId(timer, id, idMeta);
	}
	
	private void initPopulator() {
		idPopulator = new SyncIdPopulator();
	}

}
