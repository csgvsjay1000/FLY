package ivg.cn.member.db;


import com.alibaba.dubbo.container.Main;

import ivg.cn.dbsplit.LogConfig;

public class MemberDBStartup {

	public static void main( String[] args )
    {
		LogConfig.getInstance().makeDebug();
		Main.main(args);
    }
	
}
