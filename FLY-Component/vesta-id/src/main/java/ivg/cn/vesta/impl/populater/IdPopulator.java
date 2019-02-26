package ivg.cn.vesta.impl.populater;

import ivg.cn.vesta.Id;
import ivg.cn.vesta.impl.bean.IdMeta;
import ivg.cn.vesta.impl.timer.Timer;

/**
 * Id构造者
 * */
public interface IdPopulator {

	void populateId(Timer timer, Id id, IdMeta idMeta);
	
}
