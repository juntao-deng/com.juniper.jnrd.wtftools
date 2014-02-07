package net.juniper.jmp.persist.jdbc.trans;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.juniper.jmp.persist.utils.SqlLogger;
/**
 * Translating original sql into words array, and then join them according to special database.
 * @author root
 *
 */
public abstract class TranslatorObject implements ITranslator {
    private String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<", ">", "||", "&&", " ", "--", LINE_SEP, "\t" };

    private static final String LINE_SEP = "\r\n";

    protected String sourceSql;

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
            String ss = "" + c + sql.charAt(offset);
            for (int i = 0; i < m_asSpecialStr.length; i++) {
                if (ss.equals(m_asSpecialStr[i])) {
                    return sql.substring(0, offset + 1);
                }
            }
        }
        offset--;

        for (int i = 0; i < m_sSpecialChar.length(); i++) {
            if (c == m_sSpecialChar.charAt(i)) {
                if (!((c == '-') && offset > 1 && (sql.charAt(offset - 1) == 'E' || sql.charAt(offset - 1) == 'e') && (isNumber(sql.substring(0, offset - 1))))){
                    return sql.substring(0, offset + 1);
                }
            }
        }

        while (offset < sql.length()) {
            c = sql.charAt(offset);
            if (c == '\'') {
                if (!isInDouble) {
                    if (isInSingle) {
                        if ((offset + 1) < sql.length() && sql.charAt(offset + 1) == '\'') {
                            offset++;
                        } else {
                            offset++;
                            break;
                        }
                    }
                    isInSingle = true;
                }
            }

            if (c == '"') {
                if (!isInSingle) {
                    if (isInDouble) {
                        offset++;
                        break;
                    }
                    isInDouble = true;
                }
            }

            if ((!isInDouble) && (!isInSingle)) {

                offset++;
                if (offset < sql.length()) {
                    String ss = "" + c + sql.charAt(offset);
                    for (int i = 0; i < m_asSpecialStr.length; i++) {
                        if (ss.equals(m_asSpecialStr[i])) {
                            isFound = true;
                            break;
                        }
                    }
                }
                offset--;

                for (int i = 0; i < m_sSpecialChar.length(); i++) {
                    if (c == m_sSpecialChar.charAt(i)) {
                        if (!((c == '-') && offset > 1 && (sql.charAt(offset - 1) == 'E' || sql.charAt(offset - 1) == 'e') && (isNumber(sql.substring(0,
                                offset - 1))))) {
                            isFound = true;
                            break;
                        }
                    }
                }
                if (isFound) {
                    break;
                }
            }
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