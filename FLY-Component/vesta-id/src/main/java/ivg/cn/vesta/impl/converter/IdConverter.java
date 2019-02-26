package ivg.cn.vesta.impl.converter;

import ivg.cn.vesta.Id;
import ivg.cn.vesta.impl.bean.IdMeta;

public interface IdConverter {

	long convert(Id id, IdMeta idMeta);
	
	Id convert(long id, IdMeta idMeta);
}
