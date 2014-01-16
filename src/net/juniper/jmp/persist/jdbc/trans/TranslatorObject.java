package net.juniper.jmp.persist.jdbc.trans;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.juniper.jmp.persist.utils.SqlLogger;

public abstract class TranslatorObject implements ITranslator {
    //定义特殊字符串
    private String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<", ">", "||", "&&", " ", "--", LINE_SEP, "\t" };

    private static final String LINE_SEP = "\r\n";

    protected String sourceSql;

    //定义特殊字符
    private String m_sSpecialChar = "-+()*=,? <>; " + "\t" + LINE_SEP;

    @Override
	public String getResultSql(String srcSql) throws SQLException {
    	this.sourceSql = srcSql;
    	if (srcSql == null || srcSql.equals(""))
    		return "";
    	
        long startTime = System.currentTimeMillis();
        try {
            String trimSql = trimPreChars(srcSql);
            if(trimSql.equals(""))
            	return null;
            String[] words = parseSql(sourceSql);
            String result = getSql(words);
            long endTime = System.currentTimeMillis();
            if(SqlLogger.isLogTranslatorEnabled())
            	SqlLogger.logTranslator((endTime - startTime), srcSql, result);
            return result;
        } 
        catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }
    
    protected abstract String getSql(String[] words);

	/**
     * trim sql start {blank, \t, \n}
     * @param sql
     * @return
     */
    private String trimPreChars(String sql) {
        if (sql == null || sql.length() < 1)
            return "";
        sql = sql.trim();
        int pos = 0;
        int sqlLength = sql.length();
        while(pos < sqlLength && (sql.charAt(pos) == '\t' || LINE_SEP.indexOf(sql.charAt(pos)) >= 0)) {
            pos ++;
        }
        return sql.substring(pos);
    }

    /**
     * 取得源SQL语句
     */
    public String getSourceSql() {
        return sourceSql;
    }

    private String[] parseSql(String sql) {
        Map<Integer, String> wordsMap = new HashMap<Integer, String>();
        //initialize counter
        int count = 0;
        int offset = 0;

        //find first word
        String word = parseWord(sql.substring(offset));

        while (word.length() > 0) {
            offset += word.length();
            if (word.trim().length() > 0) {
                //存入新单词
                String s = word;

                if (s.equalsIgnoreCase("join")) {
                    String stSql = wordsMap.get(new Integer(count - 1));

                    if (stSql == null) {
                        wordsMap.put(new Integer(count), "inner");
                        count ++;
                    } 
                    else {
                        if (!stSql.equalsIgnoreCase("inner") && !stSql.equalsIgnoreCase("outer")) {
                            String joinType = "inner";
                            if (stSql.equalsIgnoreCase("right") || stSql.equalsIgnoreCase("left")) {
                                joinType = "outer";
                            }
                            wordsMap.put(new Integer(count), joinType);
                            count ++;
                        }
                    }
                }

                if (count > 0) {
                    String st = wordsMap.get(new Integer(count - 1)).trim();

                    if (st.endsWith(".") || s.trim().startsWith(".")) {
                        wordsMap.put(new Integer(count - 1), st + s.trim());
                    }
                    else {
                        wordsMap.put(new Integer(count), s);
                        count ++;
                    }
                } 
                else {
                    wordsMap.put(new Integer(count), s.trim());
                    count++;
                }
            }
            word = parseWord(sql.substring(offset));

            //there is only an empty string, break
            String s = word.trim();
            if (s.length() == 0) {
                word = s;
            }
        }
        
        String[] words = new String[count];
        //get words in order
        for (int i = 0; i < count; i ++) {
            words[i] = wordsMap.get(i);
        }
        return words;
    }

	private String parseWord(String sql) {
        if(sql.equals("")) {
        	return "";
        }
        
        //if in '', "", and if found the word
        boolean isInSingle = false;
        boolean isInDouble = false;
        boolean isFound = false;
        
        //initialize the counter
        int offset = 0;

        //filter blank , '\t', line sep
        int length = sql.length();
        while (offset < length && (sql.charAt(offset) == ' ') || sql.charAt(offset) == '\t' || LINE_SEP.indexOf(sql.charAt(offset)) >= 0) {
            offset++;
        }
        
        //no word
        if (offset >= length) {
            return "";
        }
        
        char c = sql.charAt(offset);
        offset++;
        if (offset < length) {
            //取得输入字符串在计数位置2位的字符串
            String ss = "" + c + sql.charAt(offset);
            //依次比较是否为特殊字符串
            for (int i = 0; i < m_asSpecialStr.length; i++) {
                if (ss.equals(m_asSpecialStr[i])) {
                    //返回特殊字符串
                    return sql.substring(0, offset + 1);
                }
            }
        }
        //计数器减1
        offset--;

        //查找并返回特殊字符
        for (int i = 0; i < m_sSpecialChar.length(); i++) {
            if (c == m_sSpecialChar.charAt(i)) {
                if (!((c == '-') && offset > 1 && (sql.charAt(offset - 1) == 'E' || sql.charAt(offset - 1) == 'e') && (isNumber(sql.substring(0, offset - 1))))){
                    return sql.substring(0, offset + 1);
                }
            }
        }

        //若计数小于输入字符串的长度
        while (offset < sql.length()) {
            //取得输入字符串在计数位置的字符
            c = sql.charAt(offset);
            //若为单引号
            if (c == '\'') {
                //若不在双引号内
                if (!isInDouble) {
                    //若在单引号内
                    if (isInSingle) {
                        //解析''
                        //若计数加1小于输入字符串的长度，且输入字符串在计数加1位置的字符为单引号
                        if ((offset + 1) < sql.length() && sql.charAt(offset + 1) == '\'') {
                            //计数加1
                            offset++;
                        } else {
                            //否则，计数加1，跳出循环
                            offset++;
                            break;
                        }
                    }
                    //是否在单引号中设为真
                    isInSingle = true;
                }
            }

            //若为双引号
            if (c == '"') {
                //若不在单引号中
                if (!isInSingle) {
                    //若在双引号中
                    if (isInDouble) {
                        //计数加1，跳出循环
                        offset++;
                        break;
                    }
                    //是否在双引号中设为真
                    isInDouble = true;
                }
            }

            //不在单引号内且不在双引号内
            if ((!isInDouble) && (!isInSingle)) {

                //计数加1
                offset++;
                //若计数小于输入字符串的长度
                if (offset < sql.length()) {
                    //取得输入字符串在计数位置2位的字符串
                    String ss = "" + c + sql.charAt(offset);
                    //循环比较是否为特殊字符串
                    for (int i = 0; i < m_asSpecialStr.length; i++) {
                        //若找到，则跳出循环
                        if (ss.equals(m_asSpecialStr[i])) {
                            isFound = true;
                            break;
                        }
                    }
                }
                //计数减1
                offset--;

                //循环查找是否为特殊字符
                for (int i = 0; i < m_sSpecialChar.length(); i++) {
                    if (c == m_sSpecialChar.charAt(i)) {
                        if (!((c == '-') && offset > 1 && (sql.charAt(offset - 1) == 'E' || sql.charAt(offset - 1) == 'e') && (isNumber(sql.substring(0,
                                offset - 1))))) {
                            //若找到，则跳出循环
                            isFound = true;
                            break;
                        }
                    }
                }
                //若找到，则跳出循环
                if (isFound) {
                    break;
                }
            }
            //计数加1
            offset++;
        }

        return sql.substring(0, offset);
    }

    private boolean isNumber(String st) {
        boolean isNumber = false;

//        if (st != null && st.trim().length() > 0) {
//            try {
//                com.thimda.lang.UFDouble ufd = new com.thimda.lang.UFDouble(st.trim());
//                if (ufd != null) {
//                    isNumber = true;
//                }
//            } catch (Exception e) {
//            }
//        }
        return isNumber;
    }


}