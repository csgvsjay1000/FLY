package ivg.cn.print.db.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Service;

import ivg.cn.dbsplit.CommonDaoImpl;
import ivg.cn.print.dao.PrintDetailEpcDao;

@Repository
@Service(version="1.0.0")
public class PrintDetailEpcDaoImpl extends CommonDaoImpl implements PrintDetailEpcDao{

}
