package ivg.cn.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.comp.zk.vo.ZKNodeData;
import ivg.cn.monitor.dto.ZKDto;
import ivg.cn.monitor.dto.ZKNodeDeleteDto;
import ivg.cn.monitor.service.ZookeeperService;

@RestController
@RequestMapping("/zk")
public class ZkController {

	@Autowired
	private ZookeeperService zookeeperService;
	
	@ApiOperation("获取path子节点")
	@PostMapping("/children")
	public DreamResponse<String> listChildren(@RequestBody ZKDto zkDto){
		return zookeeperService.listChildren(zkDto.getPath());
	}
	
	
	@ApiOperation("获取path节点数据")
	@PostMapping("/data")
	public DreamResponse<ZKNodeData> queryNodeData(@RequestBody ZKDto zkDto){
		return zookeeperService.queryNodeData(zkDto.getPath());
	}
	
	@ApiOperation("删除节点")
	@PostMapping("/del")
	public DreamResponse<Integer> delete(@RequestBody ZKNodeDeleteDto deleteDto){
		return zookeeperService.delete(deleteDto);
	}
	
	@ApiOperation("递归删除子节点")
	@PostMapping("/recurdel")
	public DreamResponse<Integer> recursionDelete(@RequestBody ZKNodeDeleteDto deleteDto){
		return zookeeperService.recursionDelete(deleteDto.getPath());
	}
	
	
	
}
