package ivg.cn.vesta;

import java.util.Date;

public interface IdService {

    public long genId();

    public Id expId(long id);
    
    Date transTime(long time);

}
