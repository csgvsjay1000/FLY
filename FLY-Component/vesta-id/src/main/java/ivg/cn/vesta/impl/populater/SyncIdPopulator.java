package ivg.cn.vesta.impl.populater;

import ivg.cn.vesta.Id;
import ivg.cn.vesta.impl.bean.IdMeta;
import ivg.cn.vesta.impl.timer.Timer;

public class SyncIdPopulator extends BasePopulator{

	@Override
	public void populateId(Timer timer, Id id, IdMeta idMeta) {
		synchronized (this) {
			doPopulateId(timer, id, idMeta);
		}
	}
	
}
