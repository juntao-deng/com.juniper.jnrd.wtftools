package net.juniper.jmp.persist.jdbc;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.jdbc.trans.ITranslator;


public class TranslatorObject implements ITranslator, DBConsts {

    /**
     * TranslatorObject 构造子注释。
     */
    public TranslatorObject(int dbType) {
        setDestDbType(dbType);
    }

    public int getDestDbType() {
        return m_iDestinationDatabaseType;
    }

    /**
     * 取得错误码
     */
    public int getErrorCode(int iErrorCode) {
        if (m_apiErrorList == null)
            return iErrorCode;
        for (int i = 0; i < m_apiErrorList.length; i++) {
            if (m_apiErrorList[i][0] == iErrorCode)
                return m_apiErrorList[i][1];
        }
        return iErrorCode;
    }

    public String getFunction(String sSourceFunction) {
        if (m_apsFunList == null)
            return sSourceFunction;
        for (int i = 0; i < m_apsFunList.length; i++) {
            String st = m_apsFunList[i][0];
            if (st.equalsIgnoreCase(sSourceFunction)) {
                return m_apsFunList[i][1];
            }
        }
        return sSourceFunction;
    }

    /**
     * 取得源SQL语句
     */
    public String getSourceSql() {
        return m_sResorceSQL;
    }

    /**
     * 取得源SQL语句，支持异常返回
     */
    public String getSql() throws Exception {
        return m_sResorceSQL;
    }

    /**
     * 取得源SQL语句异常
     */
    public java.sql.SQLException getSqlException() {
        if (m_eSqlExp == null)
            return null;
        if (m_apiErrorList == null)
            return m_eSqlExp;
        SQLException eSQL = new SQLException(m_eSqlExp.getMessage(), m_eSqlExp.getSQLState(), getErrorCode(m_eSqlExp.getErrorCode()));
        eSQL.setNextException(m_eSqlExp.getNextException());
        return eSQL;
    }

    /**
     * 取得语句种类
     */
    protected int getStatementType() throws Exception {
        int iType = 0;
        if (m_asSqlWords.length < 1) {
            return iType;
        }
        if (m_asSqlWords[0].equalsIgnoreCase("SELECT")) {
            iType = SQL_SELECT;
        } 
        else if (m_asSqlWords[0].equalsIgnoreCase("INSERT")) {
            iType = SQL_INSERT;
        } 
        else if (m_asSqlWords[0].equalsIgnoreCase("CREATE")) {
            if (m_asSqlWords.length > 1 && m_asSqlWords[1].equalsIgnoreCase("view")) {
                //create view
                iType = SQL_SELECT;
            } else {
                //create table
                iType = SQL_CREATE;
            }
        } else if (m_asSqlWords[0].equalsIgnoreCase("DROP")) {
            iType = SQL_DROP;
        } else if (m_asSqlWords[0].equalsIgnoreCase("DELETE")) {
            iType = SQL_DELETE;
        } else if (m_asSqlWords[0].equalsIgnoreCase("UPDATE")) {
            iType = SQL_UPDATE;
        } else if (m_asSqlWords[0].equalsIgnoreCase("EXPLAIN")) {
            iType = SQL_EXPLAIN;

        } else if (m_asSqlWords[0].equalsIgnoreCase("if") && m_asSqlWords[1].equalsIgnoreCase("exists")) {
            iType = 8;
        } else {
            iType = SQL_SELECT;
        }
        return iType;
    }

    public boolean isCompareOperator(String s) {
        for (int i = 0; i < m_asOperationStr.length; i++) {
            if (s.equals(m_asOperationStr[i]))
                return true;
        }
        return false;
    }

    public String[] parseSql(String sql) {
        if (sql == null || sql.trim().length() == 0)
            return null;
        String asKeyWords[] = null;
        java.util.Hashtable table = new java.util.Hashtable();
        //初始记数器
        int iCount = 0;
        int iOffSet = 0;

        //找到第一个单词
        String sWord = parseWord(sql.substring(iOffSet));

        //若单词长度大于0，则开始寻找其余单词
        while (sWord.length() > 0) {
            //计数加上单词的长度
            iOffSet += sWord.length();
            //去掉单词内的空格
            sWord = sWord.trim();
            //若单词长度大于0
            if (sWord.length() > 0) {
                //存入新单词
                String s = sWord;

                if (s.equalsIgnoreCase("join")) {
                    Object obj = table.get(new Integer(iCount - 1));

                    if (obj == null) {
                        table.put(new Integer(iCount), "inner");
                        iCount++;
                    } else {
                        String stSql = obj.toString();
                        if (!stSql.equalsIgnoreCase("inner") && !stSql.equalsIgnoreCase("outer")) {
                            String joinType = "inner";
                            if (stSql.equalsIgnoreCase("right") || stSql.equalsIgnoreCase("left")) {
                                joinType = "outer";
                            }
                            table.put(new Integer(iCount), joinType);
                            iCount++;
                        }
                    }
                }

                if (iCount > 0) {
                    String st = table.get(new Integer(iCount - 1)).toString().trim();

                    if (st.endsWith(".") || s.trim().startsWith(".")) {
                        table.put(new Integer(iCount - 1), st + s.trim());
                    }
                    //else if (st.endsWith("'") && s.trim().startsWith("'"))
                    //{
                    //table.put(new Integer(iCount - 1), st + s.trim());
                    //}
                    else {
                        //存入哈西表
                        table.put(new Integer(iCount), s.trim());
                        //计数加1
                        iCount++;
                    }
                } else {
                    //存入哈西表
                    table.put(new Integer(iCount), s.trim());
                    //计数加1
                    iCount++;
                }

            }
            //查找下一个单词
            sWord = parseWord(sql.substring(iOffSet));

            //若单词中仅含有空格则结束
            String s = sWord.trim();
            if (s.length() == 0) {
                sWord = s;
            }
        }
        //初始字符串数组
        asKeyWords = new String[iCount];
        //从哈西表中提取记录
        for (int i = 0; i < iCount; i++) {
            asKeyWords[i] = (String) table.get(new Integer(i));
        }
        return asKeyWords;
    }

    public String parseWord(String s) {
        //注意此处不可用 s=s.trim();语句，否则会出错

        //若输入单词长度为0，则返回""
        if (s.length() == 0) {
            return "";
        }
        //标志:是否在''内,是否在""内,是否找到单词
        boolean bInSingle = false;
        boolean bInDouble = false;
        boolean bFound = false;
        //初始计数器
        int iOffSet = 0;
        //初始字符缓存
        char c;

        //过滤掉空格,'\t',与回车换行符。计数器保存着除去特定字符的开始位置,即第一个有效字符的位置
        while (//若计数小于输入字串的长度，且输入字串在计数位的字符串为空格
        (iOffSet < s.length() && s.charAt(iOffSet) == ' ') //若计数小于输入字串的长度，且输入字串在计数位的字符串为“Tab”
                || (iOffSet < s.length() && s.charAt(iOffSet) == '\t') //若计数小于输入字串的长度，且输入字串在计数位的字符串包含于换行符之内
                || (iOffSet < s.length() && m_sLineSep.indexOf(s.charAt(iOffSet)) >= 0)) {
            //计数器加1
            iOffSet++;
            //若计数大于输入字符串长度，则返回""
            if (iOffSet > s.length()) {
                return "";
            }
        }
        //若计数大于输入字符串长度，则返回""
        if (iOffSet >= s.length()) {
            return "";
        }
        //取得输入字符串在计数位置的字符
        c = s.charAt(iOffSet); //第一个有效字符

        /*//过滤掉()
         if (c == '(' && s.length() >= 2 && s.charAt(1) == ')') {
         s = s.substring(2, s.length());
         if (s.length() == 0)
         return "";
         }
         */

        //返回特殊字符串
        //计数器加1
        iOffSet++;
        //若计数小于输入字符串长度
        if (iOffSet < s.length()) {
            //取得输入字符串在计数位置2位的字符串
            String ss = "" + c + s.charAt(iOffSet);
            //依次比较是否为特殊字符串
            for (int i = 0; i < m_asSpecialStr.length; i++) {
                if (ss.equals(m_asSpecialStr[i])) {
                    //返回特殊字符串
                    return s.substring(0, iOffSet + 1);
                }
            }
        }
        //计数器减1
        iOffSet--;

        //查找并返回特殊字符
        for (int i = 0; i < m_sSpecialChar.length(); i++) {
            if (c == m_sSpecialChar.charAt(i)) {
                //if( (c=='-')
                //&& 
                //iOffSet>1 
                //&& (s.charAt(iOffSet-1)=='E' || s.charAt(iOffSet-1)=='e')
                //&&
                //(isNumber(s.substring(0,iOffSet-1)))
                //)
                if (!((c == '-') && iOffSet > 1 && (s.charAt(iOffSet - 1) == 'E' || s.charAt(iOffSet - 1) == 'e') && (isNumber(s.substring(0, iOffSet - 1)))))
                //{
                //iOffSet++;
                //}
                //else
                {
                    return s.substring(0, iOffSet + 1);
                }
            }
        }

        //若计数小于输入字符串的长度
        while (iOffSet < s.length()) {
            //取得输入字符串在计数位置的字符
            c = s.charAt(iOffSet);
            //若为单引号
            if (c == '\'') {
                //若不在双引号内
                if (!bInDouble) {
                    //若在单引号内
                    if (bInSingle) {
                        //解析''
                        //若计数加1小于输入字符串的长度，且输入字符串在计数加1位置的字符为单引号
                        if ((iOffSet + 1) < s.length() && s.charAt(iOffSet + 1) == '\'') {
                            //计数加1
                            iOffSet++;
                        } else {
                            //否则，计数加1，跳出循环
                            iOffSet++;
                            break;
                        }
                    }
                    //是否在单引号中设为真
                    bInSingle = true;
                }
            }

            //若为双引号
            if (c == '"') {
                //若不在单引号中
                if (!bInSingle) {
                    //若在双引号中
                    if (bInDouble) {
                        //计数加1，跳出循环
                        iOffSet++;
                        break;
                    }
                    //是否在双引号中设为真
                    bInDouble = true;
                }
            }

            //不在单引号内且不在双引号内
            if ((!bInDouble) && (!bInSingle)) {

                //计数加1
                iOffSet++;
                //若计数小于输入字符串的长度
                if (iOffSet < s.length()) {
                    //取得输入字符串在计数位置2位的字符串
                    String ss = "" + c + s.charAt(iOffSet);
                    //循环比较是否为特殊字符串
                    for (int i = 0; i < m_asSpecialStr.length; i++) {
                        //若找到，则跳出循环
                        if (ss.equals(m_asSpecialStr[i])) {
                            bFound = true;
                            break;
                        }
                    }
                }
                //计数减1
                iOffSet--;

                //循环查找是否为特殊字符
                for (int i = 0; i < m_sSpecialChar.length(); i++) {
                    if (c == m_sSpecialChar.charAt(i)) {
                        if (!((c == '-') && iOffSet > 1 && (s.charAt(iOffSet - 1) == 'E' || s.charAt(iOffSet - 1) == 'e') && (isNumber(s.substring(0,
                                iOffSet - 1))))) {
                            //若找到，则跳出循环
                            bFound = true;
                            break;
                        }
                    }
                }
                //若找到，则跳出循环
                if (bFound) {
                    break;
                }
            }
            //计数加1
            iOffSet++;
        }

        return s.substring(0, iOffSet);
    }

    public void setSql(String sql) {
        m_sResorceSQL = sql;
        setSqlArray(parseSql(m_sResorceSQL));
    }

    public void setSqlException(java.sql.SQLException e) {
        m_eSqlExp = e;
    }

    //ErrorCode对照表,列出sqlServer ErrorCode与其他数据库 ErrorCode的对应关系,
    protected int[][] m_apiErrorList = null;

    //函数对照表,列出sqlServer ErrorCode与其他数据库 函数的对应关系,
    protected String[][] m_apsFunList = null;

    //操作符
    String m_asOperationStr[] = { "=", "!=", "<>", "<", "<=", ">", ">=", "--", "\t" };

    //定义特殊字符串
    String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<", ">", "||", "&&", " ", "--", m_sLineSep, "\t" };

    //sql单词序列
    String[] m_asSqlWords = null;

    boolean m_bFinded = false; //词法分析标志

    java.sql.SQLException m_eSqlExp = null; //源SQLException

    int m_iBracket = 0; //函数是否结束

    int m_iDestinationDatabaseType = SQLSERVER; //目标数据库类型:缺省为sql server

    StringBuffer m_sbDestinationSql = null; //目标数据库类型sql语句

    String m_sLeftTable = ""; //左连接中用到的表(包含别名)

    String m_sLeftWhere = " where "; //左连接中的where子句

    static String m_sLineSep = "\r\n";

    //System.getProperty("line.separator"); //换行符
    protected String m_sResorceSQL = null; //源sql语句 SQL Resorce

    //定义特殊字符
    String m_sSpecialChar = "-+()*=,? <>; " + "\t" + m_sLineSep;

    public String dateTypes[] = new String[] { "datetime", "smalldatetime", "timestamp", "UFDatabasedate", };

    String numberTypes[] = new String[] { "decimal", "float", "real", "int", "money", "numeric", "smallint", "smallmoney", "tinyint", "UFNumber5", "UFFactor",
            "UFPercent", "UFRate", "UFRebate", "UFTaxRate", "UFInterestRate", "UFInteger", "UFSeq", "UFIndex", "UFIndexInteger", "UFVersionNO", "UFFlag",
            "UFSeqshort", "UFDirection", "UFWaItem", "UFPeriod", "UFLevel", "UFWidth", "UFApproveStatus", "UFDefaultset", "UFInt", "UFStatus" };

    /**
     * 此处插入方法说明。
     * 创建日期：(2002-1-19 14:06:10)
     * @return java.lang.String[]
     * @param asWords java.lang.String
     * @param startIndex int
     * @param endIndex int
     */
    public String[] getFunParam(String[] asWords, int startIndex, int endIndex) {
        int iLBracket = 0;
        int iRBracket = 0;

        //int douHaoNum = 0;
        Vector vec = new Vector();
        String st = "";

        for (int iOff = startIndex; iOff < endIndex; iOff++) {
            //若当前字符串为左括号
            if (asWords[iOff].equals("("))
                iLBracket++;
            //若当前字符串为右括号
            if (asWords[iOff].equals(")"))
                iRBracket++;
            if (asWords[iOff].equals(",") && iLBracket == iRBracket) {
                vec.addElement(st);
                st = "";
            } else {
                st += " " + asWords[iOff];
            }
        }
        vec.addElement(st);
        String[] re = new String[vec.size()];
        vec.copyInto(re);
        return re;
    }

    public int getIndexOf(String source, String dest) {
        boolean find = false;
        int index = 0;
        while (!find) {
            index = source.indexOf(dest, index);

            if (index < 0 || (!inQuotation(source, index) && isSingleWord(source, dest, index))) {
                find = true;
            } else {
                index += dest.length();
            }
        }
        return index;
    }

    public String[] getLeftSql(String[] asSqlWords, int iOffSet) {
        boolean inLeft = true;
        Vector vec = new Vector();
        /*	
         while(iOffSet<asSqlWords.length && joinNotEnd(asSqlWords[iOffSet]) 
         && !asSqlWords[iOffSet].equalsIgnoreCase("and") 
         && !asSqlWords[iOffSet].equalsIgnoreCase("or")
         && inLeft)
         */
        while (iOffSet < asSqlWords.length && !asSqlWords[iOffSet].equalsIgnoreCase("and") && !asSqlWords[iOffSet].equalsIgnoreCase("or") && inLeft) {
            if (isBiJiaoFu(asSqlWords[iOffSet])) {
                inLeft = false;
            }
            if (inLeft && haveAloneSt(asSqlWords[iOffSet], ".")) {
                vec.addElement(asSqlWords[iOffSet]);
            }
            iOffSet++;
        }
        String leftSql[] = new String[vec.size()];
        vec.copyInto(leftSql);
        return leftSql;
    }

    public TransUnit getRightSql(String[] asSqlWords, int iOffSet) {
        boolean inRight = false;
        String rightSql = "";
        int leftKuoHao = 0;
        int rightKouHao = 0;
        /*
         && 
         (
         (!asSqlWords[iOffSet].equals(",") && joinNotEnd(asSqlWords[iOffSet]))
         || 
         (asSqlWords[iOffSet].equals(",") && leftKuo!=rightKuo)
         )
         */
        while (iOffSet < asSqlWords.length
                && ((!asSqlWords[iOffSet].equals(",") && joinNotEnd(asSqlWords[iOffSet])) || (asSqlWords[iOffSet].equals(",") && leftKuoHao != rightKouHao))
                && !asSqlWords[iOffSet].equalsIgnoreCase("and") && !asSqlWords[iOffSet].equalsIgnoreCase("or") //&& leftKuoHao <= rightKouHao
        ) {
            if (asSqlWords[iOffSet].equals("(")) {
                leftKuoHao++;
            }
            if (asSqlWords[iOffSet].equals(")")) {
                rightKouHao++;
            }

            if (inRight && leftKuoHao <= rightKouHao) {
                rightSql += " " + asSqlWords[iOffSet];
            }
            if (isBiJiaoFu(asSqlWords[iOffSet])) {
                inRight = true;
                if (asSqlWords[iOffSet].equalsIgnoreCase("is") && iOffSet < asSqlWords.length - 1 && asSqlWords[iOffSet + 1].equalsIgnoreCase("not")) {
                    iOffSet++;
                }
            }
            iOffSet++;
        }
        if (rightKouHao > leftKuoHao) {
            iOffSet--;
        }
        return new TransUnit(null, rightSql, iOffSet);
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2002-1-17 17:16:50)
     * @return nc.bs.mw.sqltrans.TransUnit
     * @param asSqlWords java.lang.String[]
     * @param iOffSet int
     */
    public int getStartIndex(String[] asSqlWords, int iOffSet) {
        boolean inRight = false;
        String rightSql = "";
        int leftKuoHao = 0;
        int rightKouHao = 0;

        while (iOffSet > 0 && !asSqlWords[iOffSet - 1].equalsIgnoreCase("on") && !asSqlWords[iOffSet - 1].equalsIgnoreCase("and")
                && !asSqlWords[iOffSet - 1].equalsIgnoreCase("or")) {
            iOffSet--;
        }
        return iOffSet;
    }

    public TransUnit getSubSql(String[] asSqlWords, String leftWord, String rightWord, int iOffSet) {
        int left = 1;
        int right = 0;

        Vector vec = new Vector();

        vec.addElement(asSqlWords[iOffSet]);

        while (iOffSet < asSqlWords.length && left != right) {
            iOffSet++;

            if (asSqlWords[iOffSet].equalsIgnoreCase(leftWord)) {
                left++;
            }
            if (asSqlWords[iOffSet].equalsIgnoreCase(rightWord)) {
                right++;
            }

            vec.addElement(asSqlWords[iOffSet]);

        }

        String newCaseSql[] = new String[vec.size()];
        vec.copyInto(newCaseSql);

        return new TransUnit(newCaseSql, null, iOffSet);
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2002-1-17 13:59:39)
     * @return boolean
     * @param source java.lang.String
     * @param dest java.lang.String
     */
    public boolean haveAloneSt(String source, String dest) {
        boolean have = false;

        while (source.indexOf(dest) >= 0) {
            int singleNum = 0;
            for (int i = 0; i < source.indexOf(dest); i++) {
                char ch = source.charAt(i);
                if (ch == '\'') {
                    singleNum++;
                }
            }
            if (singleNum % 2 == 0) {
                have = true;
                break;
            } else {
                source = source.substring(source.indexOf(dest) + 1);
            }
        }
        return have;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2001-12-19 10:00:22)
     * @return boolean
     * @param sql java.lang.String
     * @param sTableName java.lang.String
     * @param sTableAlias java.lang.String
     */
    public boolean haveOtherTable(String sql_old, Vector vecTable) {
        boolean haveOtherTable = false;

        if (vecTable != null && vecTable.size() > 0) {
            int size = vecTable.size();

            for (int i = 0; i < size; i++) {
                Object obj = vecTable.elementAt(i);
                String sql = sql_old;

                if (obj != null) {
                    String table = obj.toString();

                    while (sql.indexOf(".") >= 0) {
                        if (table.trim().length() > 0
                                && ((sql.indexOf(table + ".") == 0) || (sql.indexOf(table + ".") > 0 && (sql.charAt(sql.indexOf(table + ".") - 1) == ' '
                                        || sql.charAt(sql.indexOf(table + ".") - 1) == '+' || sql.charAt(sql.indexOf(table + ".") - 1) == '-'
                                        || sql.charAt(sql.indexOf(table + ".") - 1) == '*' || sql.charAt(sql.indexOf(table + ".") - 1) == '/' || sql.charAt(sql
                                        .indexOf(table + ".") - 1) == '|')))) {
                            haveOtherTable = true;
                            //return haveOtherTable;
                            break;
                        } else {
                            sql = sql.substring(sql.indexOf(".") + 1);
                        }
                    }
                }
            }
        }

        return haveOtherTable;

    }

    public boolean inQuotation(String formula, int endIndex) {
        int quotationNum = 0;

        int left = 0;
        int right = 0;

        for (int i = 0; i < endIndex; i++) {
            if (formula.charAt(i) == '\'') {
                quotationNum++;
            }
            if (formula.charAt(i) == '(' && quotationNum % 2 == 0) {
                left++;
            }
            if (formula.charAt(i) == ')' && quotationNum % 2 == 0) {
                right++;
            }
        }
        if (quotationNum % 2 == 0 && left == right) {
            return false;
        } 
        else {
            return true;
        }
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2001-12-29 11:18:23)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isBiJiaoFu(String st) {
        boolean isOprater = false;

        //没有遇到as， 也没有遇到on,别名
        if (st.equals("=") //
                || st.equals("<=") || st.equals(">=") || st.equals("<") || st.equals(">") || st.equals("<>") || st.equals("!=") || st.equalsIgnoreCase("is")) {
            isOprater = true;
        }
        return isOprater;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2005-01-05 17:04:03)
     * @return boolean
     * @param dataType java.lang.String
     */
    public boolean isCharType(String dataType) {
        return isType(charTypes, dataType);
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2002-1-18 17:04:03)
     * @return boolean
     * @param dataType java.lang.String
     */
    public boolean isDateType(String dataType) {
        return isType(dateTypes, dataType);
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2001-12-28 19:43:29)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isFunctionName(String sWord, String nextWord) {
        boolean isFunc = false;

        if (((sWord.equalsIgnoreCase("left") && !nextWord.equalsIgnoreCase("outer"))/** 1 **/
        || (sWord.equalsIgnoreCase("right") && !nextWord.equalsIgnoreCase("outer"))/** 2 **/
        || sWord.equalsIgnoreCase("square") //或当前单词为“square”
                || sWord.equalsIgnoreCase("cast") //或当前单词为“cast”
                || sWord.equalsIgnoreCase("coalesce") || sWord.equalsIgnoreCase("ltrim") || sWord.equalsIgnoreCase("rtrim")
                || sWord.equalsIgnoreCase("patindex") || sWord.equalsIgnoreCase("len") || sWord.equalsIgnoreCase("round") || sWord.equalsIgnoreCase("convert")
                || sWord.equalsIgnoreCase("dateadd") || sWord.equalsIgnoreCase("datediff")) //且下一个单词是“(”
                && nextWord.equals("(")) {
            isFunc = true;
        }
        return isFunc;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2001-12-29 10:12:17)
     * @return boolean
     * @param first java.lang.String
     * @param second java.lang.String
     * @param third java.lang.String
     */
    public boolean isInnerJoin(String first, String second) {
        boolean isInnerJoin = false;
        if (first.equalsIgnoreCase("inner") && second.equalsIgnoreCase("join")) {
            isInnerJoin = true;
        }
        return isInnerJoin;
    }

    public boolean isNumber(String st) {

        if (st != null && st.trim().length() > 0) {
            try {
               Double.valueOf(st);
               return true;
            } 
            catch (NumberFormatException e) {
            }
        }
        return false;
    }

    public boolean isNumberType(String dataType) {
        return isType(numberTypes, dataType);
    }

    public boolean isOuterJoin(String first, String second, String third) {
        boolean isOuterJoin = false;
        if ((first.equalsIgnoreCase("left") || first.equalsIgnoreCase("right")) && second.equalsIgnoreCase("outer") && third.equalsIgnoreCase("join")) {
            isOuterJoin = true;
        }
        return isOuterJoin;
    }

    public boolean isSingleWord(String source, String dest, int index) {
        boolean isSingleWord = false;

        if (index <= 0 || source.charAt(index - 1) == ' ') {
            if (index + dest.length() >= source.length() - 1 || source.charAt(index + dest.length()) == ' ') {
                isSingleWord = true;
            }
        }
        return isSingleWord;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2001-12-29 11:18:23)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isTableOtherName(String st) {
        boolean isTableOtherName = false;

        //没有遇到as， 也没有遇到on,别名
        if (!st.equalsIgnoreCase("on") && !st.equalsIgnoreCase("where") && !st.equalsIgnoreCase("inner") && !st.equalsIgnoreCase("left")
                && !st.equalsIgnoreCase("right") && !st.equalsIgnoreCase(",")) {
            isTableOtherName = true;
        }
        return isTableOtherName;
    }

    public boolean isType(String[] dataTypes, String type) {
        type = type.trim();
        boolean isType = false;

        for (int i = 0; i < dataTypes.length; i++) {
            if (type.equalsIgnoreCase(dataTypes[i]) //|| dataTypes[i].toLowerCase().startsWith(type.toLowerCase())
            ) {
                isType = true;
                break;
            }
        }
        return isType;
    }

    public boolean joinNotEnd(String st) {
        boolean joinNotEnd = false;
        if (!st.equalsIgnoreCase("left") && !st.equalsIgnoreCase("right") && !st.equalsIgnoreCase("where") && !st.equalsIgnoreCase("order")
                && !st.equalsIgnoreCase("group") && !st.equalsIgnoreCase("inner") && !st.equalsIgnoreCase("union") && !st.equalsIgnoreCase("on")
                && !st.equalsIgnoreCase(",")) {
            joinNotEnd = true;
        }
        return joinNotEnd;
    }

    public void setDestDbType(int dbType) {
        if (dbType >= 0 && dbType <= 3)
            m_iDestinationDatabaseType = dbType;
        else
            m_iDestinationDatabaseType = SQLSERVER; //缺省为sql server
    }

    /**
     * 设置SQL语句
     */
    public void setSqlArray(String[] sqlArray) {
        m_asSqlWords = sqlArray;
    }

    /**
     * 设置SQL语句
     */
    public String[] getSqlArray() {
        return m_asSqlWords;
    }

    /**
     * 设置SQL语句
     */
    public String[] getTableNames() {
        String[] tablename = null;
        Vector v = new Vector();
        Hashtable ht = new Hashtable();
        if (m_asSqlWords == null)
            return null;
        if (!m_asSqlWords[0].equalsIgnoreCase("where"))
            return null;
        for (int i = 0; i < m_asSqlWords.length; i++) {
            if (Character.isLetter(m_asSqlWords[i].charAt(0))) {
                if (m_asSqlWords[i].indexOf(".") > 0 && m_asSqlWords[i].indexOf("'") < 0 && m_asSqlWords[i].indexOf("\"") < 0) {
                    //v.addElement();
                    ht.put(m_asSqlWords[i].substring(0, m_asSqlWords[i].indexOf(".")), m_asSqlWords[i].substring(0, m_asSqlWords[i].indexOf(".")));
                }
            }
        }
        if (!ht.isEmpty()) {
            Enumeration em = ht.elements();
            while (em.hasMoreElements()) {
                String table = (String) em.nextElement();
                v.addElement(table);
            }
            if (v.size() > 0) {
                tablename = new String[v.size()];
                v.copyInto(tablename);
            }
        }
        return tablename;
    }


    /**
     * 此处插入方法说明。
     * 创建日期：(2004-6-3 15:10:44)
     * @return java.lang.String[]
     */
    public String[] parseTable(String[] asWords, String sTableName, String sTableAlias) {
        int iOffSet = 0;
        String othtable = "";
        String trueName = "";
        String[] sTable = null;
        boolean isOth = false;
        Vector vecTable = new Vector();
        while (iOffSet < asWords.length) {
            if (iOffSet < asWords.length && asWords[iOffSet].equalsIgnoreCase("from")) {
                int leftCount = 1;
                int rightCount = 0;
                String subfromSt = "";
                Vector tabVec = new Vector();
                iOffSet++;
                while (iOffSet < asWords.length && !asWords[iOffSet].equalsIgnoreCase("where")) {

                    if (asWords[iOffSet].equalsIgnoreCase("(")) {
                        subfromSt = "(";
                        while ((leftCount != rightCount) && (iOffSet < asWords.length)) {
                            iOffSet++;

                            if (asWords[iOffSet].equalsIgnoreCase("(")) {
                                leftCount++;
                            } else if (asWords[iOffSet].equalsIgnoreCase(")")) {
                                rightCount++;
                            }

                            subfromSt += " " + asWords[iOffSet];

                        }

                        tabVec.addElement(subfromSt);
                        iOffSet++;
                    }
                    tabVec.addElement(asWords[iOffSet]);
                    iOffSet++;
                }

                for (int newIndex = 0; newIndex < tabVec.size(); newIndex++) {
                    othtable = tabVec.elementAt(newIndex).toString();
                    trueName = othtable;
                    isOth = false;

                    if (!othtable.equalsIgnoreCase(sTableName)) {
                        isOth = true;
                        vecTable.addElement(othtable);
                    }
                    newIndex++;
                    if (newIndex < tabVec.size()) {
                        othtable = tabVec.elementAt(newIndex).toString();
                        if (othtable.equalsIgnoreCase("as")) {
                            newIndex++;
                            othtable = tabVec.elementAt(newIndex).toString();
                        }

                        if (!othtable.equalsIgnoreCase(",")) {
                            if (isOth) {
                                vecTable.addElement(othtable);
                            } else {
                                if (sTableAlias != null && sTableAlias.trim().length() > 0) {
                                    if (!othtable.equalsIgnoreCase(sTableAlias)) {
                                        vecTable.addElement(trueName);
                                        vecTable.addElement(othtable);
                                    }
                                }
                            }
                            newIndex++;
                        }
                    }

                }
            }
            iOffSet++;
        }
        sTable = new String[vecTable.size()];
        //从哈西表中提取记录
        for (int i = 0; i < vecTable.size(); i++) {
            sTable[i] = (String) vecTable.elementAt(i);
        }
        return sTable;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2004-6-2 16:02:34)
     * @return boolean
     */
    public boolean haveTab(String Sql, String[] v_table) {
        boolean MasterTab = false;
        int dotIndex = 0;
        int i = 0;
        String s = "";
        while (i < v_table.length) {
            s = v_table[i];
            dotIndex = Sql.indexOf(s.trim() + ".");
            if (dotIndex > 0) {
                //				if (Sql.substring(dotIndex+1,dotIndex+2).equalsIgnoreCase(".")) {
                MasterTab = true;
                return MasterTab;
                //				}
            }
            i++;
        }
        return MasterTab;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2004-6-2 16:02:34)
     * @return boolean
     */
    public boolean haveTab(String Sql, String Table) {
        boolean MasterTab = false;
        if (Table.trim() == null || Table.trim().length() == 0) {
            return MasterTab;
        }

        int dotIndex = Sql.indexOf(Table + ".");
        if (dotIndex > 0) {
            //		if (Sql.substring(dotIndex+1,dotIndex+2).equalsIgnoreCase(".")) {
            MasterTab = true;
            //		}
        }

        return MasterTab;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2004-5-25 19:42:34)
     * @return boolean
     */
    public boolean isMasterTab(String[] v_table, String sql) {
        boolean MasterTab = false;
        int i = 0;
        int dotIndex = 0;
        String s = "";
        dotIndex = sql.indexOf(".");
        if (dotIndex >= 0) {
            String tabName = sql.substring(0, dotIndex).trim().toLowerCase();
            while (i < v_table.length) {
                s = v_table[i];
                if (s.trim().equalsIgnoreCase(tabName)) {
                    MasterTab = true;
                    return MasterTab;
                }
                i++;
            }
        }
        return MasterTab;
    }

    /**
     * 此处插入方法说明。
     * 创建日期：(2004-5-25 19:42:34)
     * @return boolean
     */
    public boolean isMasterTab(String Sql, String Table) {
        boolean MasterTab = false;

        int dotIndex = Sql.indexOf(".");
        if (dotIndex >= 0) {
            String tabName = Sql.substring(0, dotIndex).trim().toLowerCase();
            if (tabName.equalsIgnoreCase(Table)) {
                MasterTab = true;

            }
        }

        return MasterTab;
    }

    public String charTypes[] = new String[] { "char", "varchar" };

    //当前的连接
    private java.sql.Connection m_con = null;

    //数据库版本
    private int m_DbVersion = 0;

    /**
     * 此处插入方法说明。 
     * 功能：取得目标数据库版本 
     * 创建日期：(2005-01-05 11:44:11)
     */
    public int getDbVersion() {
        return m_DbVersion;
    }

    /**
     * 此处插入方法说明。
     * 功能：设置目标数据库的连接
     * 创建日期：(2005-01-25 09:44:00)
     */
    public void setDbVersion(int dbVersion) {
        m_DbVersion = dbVersion;
    }
}