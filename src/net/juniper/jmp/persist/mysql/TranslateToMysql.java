package net.juniper.jmp.persist.mysql;

import net.juniper.jmp.persist.jdbc.trans.TranslatorObject;

/**
 * Mysql sql translator
 * @author juntaod
 *
 */
public class TranslateToMysql extends TranslatorObject {
	private StringBuffer resultSql;
	public TranslateToMysql() {
    }

    @Override
	protected String getSql(String[] words) {
        translateSql(words);

        if (resultSql == null)
            return null;

        String result = resultSql.toString();

        if (result.endsWith(";"))
            result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * translate mysql sqls to more efficient ones
     * @param words
     */
    private void translateSql(String[] words) {
        if (words == null) {
            return;
        }

        resultSql = new StringBuffer();
        int offset = 0;
        while (offset < words.length) {
            String word = words[offset];
            if (offset > 0) {
            	resultSql.append(" ");
            }
            resultSql.append(word);
            offset ++;
        }
    }
}