package ivg.cn.print.db.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Service;

import ivg.cn.dbsplit.CommonDaoImpl;
import ivg.cn.print.dao.PrintDao;

@Repository
@Service
public class PrintDaoImpl extends CommonDaoImpl implements PrintDao{

}
