package ivg.cn.monitor.comp.dubbo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;

//@Service
public class RegistryContainerImpl implements RegistryContainer{

	@Reference
	private RegistryService registryService;
	
	public RegistryContainerImpl() {
		
	}
	
	@Override
	public void start() {
		
		URL subscribeUrl = new URL(Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0,"",
				Constants.INTERFACE_KEY, Constants.ANY_VALUE,
                Constants.GROUP_KEY, Constants.ANY_VALUE,
                Constants.VERSION_KEY, Constants.ANY_VALUE,
                Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
                Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY + ","
                + Constants.CONSUMERS_CATEGORY + ","
                + Constants.CONFIGURATORS_CATEGORY,
                Constants.CHECK_KEY, String.valueOf(false));
		
		registryService.subscribe(subscribeUrl, new NotifyListener() {
			
			@Override
			public void notify(List<URL> urls) {
				
				if (urls == null || urls.size() == 0) {
					return;
				}
				for (URL url : urls) {
					 String application = url.getParameter(Constants.APPLICATION_KEY);
					 System.out.println(application);
				}
				
			}
		});
		
	}
	
}
