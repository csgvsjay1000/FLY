package ivg.cn.dbsplit.core.sql.parse;

public interface SplitSqlParser {

	
	
	SplitSqlStructure parse(String sql);
	
	SplitSqlParser INST = new SplitSqlParserImpl();
	
}
