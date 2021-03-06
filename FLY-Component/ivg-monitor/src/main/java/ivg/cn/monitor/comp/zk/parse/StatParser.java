package ivg.cn.monitor.comp.zk.parse;

import ivg.cn.monitor.comp.zk.vo.StatVO;

public class StatParser {

	private static final String FiledZookeeperVersion = "Zookeeper version";
	private static final String FiledClients = "Clients";
	private static final String child = " /";
	
	public static void parse(String result) {
		String[] lines = result.split("\n");
		
		StatVO statVO = new StatVO();
		
		String parent = null;
		for (String line : lines) {
			if (FiledZookeeperVersion.equals(line)) {
				String value = line.substring(FiledZookeeperVersion.length()+2);
				statVO.setZookeeperVersion(value);
			}else if (FiledClients.equals(line)) {
				parent = FiledClients;
			}else if (child.equals(line)) {
				
			}
		}
	}
	
}
