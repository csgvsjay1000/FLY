package ivg.cn.print.service.consumer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import ivg.cn.kclient.MessageHandler;
import ivg.cn.print.service.iner.InerPrintService;

@Component
public class PrintEpcConsumer implements MessageHandler{

	private static Logger log = LoggerFactory.getLogger(PrintEpcConsumer.class);
	
	@Autowired
	private InerPrintService inerPrintService;
	
	@Override
	public void execute(String topic, String key, String message) {
		long value = -1;
		Date date = null;
		try {
			JSONObject jsonObject = JSONObject.parseObject(message);
			value = jsonObject.getLong("id");
			date = jsonObject.getDate("date");
		} catch (Exception e) {
			log.error("receive message error, message={}",message);
			log.error("receive message error.",e);
			return;
		}
		inerPrintService.receivePrintEpcMessage(date, value);
	}

}
