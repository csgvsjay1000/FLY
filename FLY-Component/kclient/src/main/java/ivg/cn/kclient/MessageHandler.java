package ivg.cn.kclient;

public interface MessageHandler {

	void execute(String topic,String key,String message);
	
}
