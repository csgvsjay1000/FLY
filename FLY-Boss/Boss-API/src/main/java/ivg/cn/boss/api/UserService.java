package ivg.cn.boss.api;

import ivg.cn.common.DreamResponse;

public interface UserService {

	/** 删除用户 */
	DreamResponse<Integer> delete(long id);
	
}
