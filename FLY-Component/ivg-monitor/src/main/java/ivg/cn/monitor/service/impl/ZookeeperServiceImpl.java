package ivg.cn.monitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.comp.zk.ZKApi;
import ivg.cn.monitor.comp.zk.ZKConnection;
import ivg.cn.monitor.comp.zk.ZKSendCmdResult;
import ivg.cn.monitor.comp.zk.vo.ZKNodeData;
import ivg.cn.monitor.dto.ZKNodeDeleteDto;
import ivg.cn.monitor.service.ZookeeperService;

@Service
public class ZookeeperServiceImpl implements ZookeeperService{

	@Autowired
	private ZKConnection zkConnection;
	
	@Autowired
	private ZKApi zkApi;
	
	/**
	 * 查看zk性能参数，和连接上的客户端
	 * */
	public void stat() {
		ZKSendCmdResult result = zkConnection.sendCmd("stat");
	}

	@Override
	public DreamResponse<String> listChildren(String path) {
		List<String> cList = zkApi.listChildren(path);
		DreamResponse<String> response = DreamResponse.createOKResponse();
		response.setData(cList);
		return response;
	}

	@Override
	public DreamResponse<ZKNodeData> queryNodeData(String path) {
		ZKNodeData nodeData = zkApi.queryNodeData(path);
		DreamResponse<ZKNodeData> response = DreamResponse.createOKResponse();
		response.setObjData(nodeData);
		return response;
	}
	
	@Override
	public DreamResponse<Integer> delete(ZKNodeDeleteDto deleteDto) {
		zkApi.delete(deleteDto.getPath(), deleteDto.getVersion());
		return DreamResponse.createOKResponse();
	}
	
	@Override
	public DreamResponse<Integer> recursionDelete(String path) {
		zkApi.recursionDelete(path);
		return DreamResponse.createOKResponse();
	}
	
}
