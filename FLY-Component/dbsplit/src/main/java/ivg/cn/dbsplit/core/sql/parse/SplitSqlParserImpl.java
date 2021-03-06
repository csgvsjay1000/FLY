package ivg.cn.dbsplit.core.sql.parse;

import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.util.StringUtils;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.Token;

import ivg.cn.dbsplit.core.sql.parse.SplitSqlStructure.SqlType;

public class SplitSqlParserImpl implements SplitSqlParser{

	@SuppressWarnings("unchecked")
	Map<String/**sql*/, SplitSqlStructure> cache = new LRUMap(1000);
	
	@Override
	public SplitSqlStructure parse(String sql) {
		
		SplitSqlStructure splitSqlStructure = cache.get(sql);
		
		if (splitSqlStructure != null) {
			return splitSqlStructure;
		}
		splitSqlStructure = new SplitSqlStructure();
		
		String dbName = null;
		String tableName = null;
		boolean inProcess = false;

		boolean previous = true;
		boolean sebsequent = false;
		StringBuffer sbPreviousPart = new StringBuffer();
		StringBuffer sbSebsequentPart = new StringBuffer();

		// Need to opertimize for better performance

		Lexer lexer = new Lexer(sql);
		do {
			lexer.nextToken();
			Token tok = lexer.token();
			if (tok == Token.EOF) {
				break;
			}

			if (tok.name != null)
				switch (tok.name) {
				case "SELECT":
					splitSqlStructure.setSqlType(SqlType.SELECT);
					break;

				case "INSERT":
					splitSqlStructure.setSqlType(SqlType.INSERT);
					break;

				case "UPDATE":
					inProcess = true;
					splitSqlStructure.setSqlType(SqlType.UPDATE);
					break;

				case "DELETE":
					splitSqlStructure.setSqlType(SqlType.DELETE);
					break;

				case "INTO":
					if (SqlType.INSERT.equals(splitSqlStructure.getSqlType()))
						inProcess = true;
					break;

				case "FROM":
					if (SqlType.SELECT.equals(splitSqlStructure.getSqlType())
							|| SqlType.DELETE.equals(splitSqlStructure
									.getSqlType()))
						inProcess = true;
					break;
				}

			if (sebsequent)
				sbSebsequentPart.append(
						tok == Token.IDENTIFIER ? lexer.stringVal() : tok.name)
						.append(" ");

			if (inProcess) {
				if (dbName == null && tok == Token.IDENTIFIER) {
					dbName = lexer.stringVal();
					previous = false;
				} else if (dbName != null && tableName == null
						&& tok == Token.IDENTIFIER) {
					tableName = lexer.stringVal();

					inProcess = false;
					sebsequent = true;
				}
			}

			if (previous)
				sbPreviousPart.append(
						tok == Token.IDENTIFIER ? lexer.stringVal() : tok.name)
						.append(" ");

		} while (true);

		if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName))
			throw new RuntimeException("The split sql is not supported: "
					+ sql);

		splitSqlStructure.setDbName(dbName);
		splitSqlStructure.setTableName(tableName);

		splitSqlStructure.setPreviousPart(sbPreviousPart.toString());
		splitSqlStructure.setSebsequentPart(sbSebsequentPart.toString());
		
		synchronized (this) {
			if (!cache.containsKey(sql)) {
				cache.put(sql, splitSqlStructure);
			}
		}
		
		return splitSqlStructure;
	}

}
