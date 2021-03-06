package ivg.cn.monitor.service;

import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.comp.zk.vo.ZKNodeData;
import ivg.cn.monitor.dto.ZKNodeDeleteDto;

public interface ZookeeperService {

	DreamResponse<String> listChildren(String path);
	
	DreamResponse<ZKNodeData> queryNodeData(String path);
	
	DreamResponse<Integer> delete(ZKNodeDeleteDto deleteDto);
	
	DreamResponse<Integer> recursionDelete(String path);
}
