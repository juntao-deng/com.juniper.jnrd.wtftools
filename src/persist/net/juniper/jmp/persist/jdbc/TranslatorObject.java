package net.juniper.jmp.persist.jdbc;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.juniper.jmp.persist.constant.DBConsts;
import net.juniper.jmp.persist.jdbc.trans.ITranslator;


public class TranslatorObject implements ITranslator, DBConsts {

    /**
     * TranslatorObject ������ע�͡�
     */
    public TranslatorObject(int dbType) {
        setDestDbType(dbType);
    }

    public int getDestDbType() {
        return m_iDestinationDatabaseType;
    }

    /**
     * ȡ�ô�����
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
     * ȡ��ԴSQL���
     */
    public String getSourceSql() {
        return m_sResorceSQL;
    }

    /**
     * ȡ��ԴSQL��䣬֧���쳣����
     */
    public String getSql() throws Exception {
        return m_sResorceSQL;
    }

    /**
     * ȡ��ԴSQL����쳣
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
     * ȡ���������
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
        //��ʼ������
        int iCount = 0;
        int iOffSet = 0;

        //�ҵ���һ������
        String sWord = parseWord(sql.substring(iOffSet));

        //�����ʳ��ȴ���0����ʼѰ�����൥��
        while (sWord.length() > 0) {
            //�������ϵ��ʵĳ���
            iOffSet += sWord.length();
            //ȥ�������ڵĿո�
            sWord = sWord.trim();
            //�����ʳ��ȴ���0
            if (sWord.length() > 0) {
                //�����µ���
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
                        //���������
                        table.put(new Integer(iCount), s.trim());
                        //������1
                        iCount++;
                    }
                } else {
                    //���������
                    table.put(new Integer(iCount), s.trim());
                    //������1
                    iCount++;
                }

            }
            //������һ������
            sWord = parseWord(sql.substring(iOffSet));

            //�������н����пո������
            String s = sWord.trim();
            if (s.length() == 0) {
                sWord = s;
            }
        }
        //��ʼ�ַ�������
        asKeyWords = new String[iCount];
        //�ӹ���������ȡ��¼
        for (int i = 0; i < iCount; i++) {
            asKeyWords[i] = (String) table.get(new Integer(i));
        }
        return asKeyWords;
    }

    public String parseWord(String s) {
        //ע��˴������� s=s.trim();��䣬��������

        //�����뵥�ʳ���Ϊ0���򷵻�""
        if (s.length() == 0) {
            return "";
        }
        //��־:�Ƿ���''��,�Ƿ���""��,�Ƿ��ҵ�����
        boolean bInSingle = false;
        boolean bInDouble = false;
        boolean bFound = false;
        //��ʼ������
        int iOffSet = 0;
        //��ʼ�ַ�����
        char c;

        //���˵��ո�,'\t',��س����з��������������ų�ȥ�ض��ַ��Ŀ�ʼλ��,����һ����Ч�ַ���λ��
        while (//������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ�ո�
        (iOffSet < s.length() && s.charAt(iOffSet) == ' ') //������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ��Tab��
                || (iOffSet < s.length() && s.charAt(iOffSet) == '\t') //������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ��������ڻ��з�֮��
                || (iOffSet < s.length() && m_sLineSep.indexOf(s.charAt(iOffSet)) >= 0)) {
            //��������1
            iOffSet++;
            //���������������ַ������ȣ��򷵻�""
            if (iOffSet > s.length()) {
                return "";
            }
        }
        //���������������ַ������ȣ��򷵻�""
        if (iOffSet >= s.length()) {
            return "";
        }
        //ȡ�������ַ����ڼ���λ�õ��ַ�
        c = s.charAt(iOffSet); //��һ����Ч�ַ�

        /*//���˵�()
         if (c == '(' && s.length() >= 2 && s.charAt(1) == ')') {
         s = s.substring(2, s.length());
         if (s.length() == 0)
         return "";
         }
         */

        //���������ַ���
        //��������1
        iOffSet++;
        //������С�������ַ�������
        if (iOffSet < s.length()) {
            //ȡ�������ַ����ڼ���λ��2λ���ַ���
            String ss = "" + c + s.charAt(iOffSet);
            //���αȽ��Ƿ�Ϊ�����ַ���
            for (int i = 0; i < m_asSpecialStr.length; i++) {
                if (ss.equals(m_asSpecialStr[i])) {
                    //���������ַ���
                    return s.substring(0, iOffSet + 1);
                }
            }
        }
        //��������1
        iOffSet--;

        //���Ҳ����������ַ�
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

        //������С�������ַ����ĳ���
        while (iOffSet < s.length()) {
            //ȡ�������ַ����ڼ���λ�õ��ַ�
            c = s.charAt(iOffSet);
            //��Ϊ������
            if (c == '\'') {
                //������˫������
                if (!bInDouble) {
                    //���ڵ�������
                    if (bInSingle) {
                        //����''
                        //��������1С�������ַ����ĳ��ȣ��������ַ����ڼ�����1λ�õ��ַ�Ϊ������
                        if ((iOffSet + 1) < s.length() && s.charAt(iOffSet + 1) == '\'') {
                            //������1
                            iOffSet++;
                        } else {
                            //���򣬼�����1������ѭ��
                            iOffSet++;
                            break;
                        }
                    }
                    //�Ƿ��ڵ���������Ϊ��
                    bInSingle = true;
                }
            }

            //��Ϊ˫����
            if (c == '"') {
                //�����ڵ�������
                if (!bInSingle) {
                    //����˫������
                    if (bInDouble) {
                        //������1������ѭ��
                        iOffSet++;
                        break;
                    }
                    //�Ƿ���˫��������Ϊ��
                    bInDouble = true;
                }
            }

            //���ڵ��������Ҳ���˫������
            if ((!bInDouble) && (!bInSingle)) {

                //������1
                iOffSet++;
                //������С�������ַ����ĳ���
                if (iOffSet < s.length()) {
                    //ȡ�������ַ����ڼ���λ��2λ���ַ���
                    String ss = "" + c + s.charAt(iOffSet);
                    //ѭ���Ƚ��Ƿ�Ϊ�����ַ���
                    for (int i = 0; i < m_asSpecialStr.length; i++) {
                        //���ҵ���������ѭ��
                        if (ss.equals(m_asSpecialStr[i])) {
                            bFound = true;
                            break;
                        }
                    }
                }
                //������1
                iOffSet--;

                //ѭ�������Ƿ�Ϊ�����ַ�
                for (int i = 0; i < m_sSpecialChar.length(); i++) {
                    if (c == m_sSpecialChar.charAt(i)) {
                        if (!((c == '-') && iOffSet > 1 && (s.charAt(iOffSet - 1) == 'E' || s.charAt(iOffSet - 1) == 'e') && (isNumber(s.substring(0,
                                iOffSet - 1))))) {
                            //���ҵ���������ѭ��
                            bFound = true;
                            break;
                        }
                    }
                }
                //���ҵ���������ѭ��
                if (bFound) {
                    break;
                }
            }
            //������1
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

    //ErrorCode���ձ�,�г�sqlServer ErrorCode���������ݿ� ErrorCode�Ķ�Ӧ��ϵ,
    protected int[][] m_apiErrorList = null;

    //�������ձ�,�г�sqlServer ErrorCode���������ݿ� �����Ķ�Ӧ��ϵ,
    protected String[][] m_apsFunList = null;

    //������
    String m_asOperationStr[] = { "=", "!=", "<>", "<", "<=", ">", ">=", "--", "\t" };

    //���������ַ���
    String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<", ">", "||", "&&", " ", "--", m_sLineSep, "\t" };

    //sql��������
    String[] m_asSqlWords = null;

    boolean m_bFinded = false; //�ʷ�������־

    java.sql.SQLException m_eSqlExp = null; //ԴSQLException

    int m_iBracket = 0; //�����Ƿ����

    int m_iDestinationDatabaseType = SQLSERVER; //Ŀ�����ݿ�����:ȱʡΪsql server

    StringBuffer m_sbDestinationSql = null; //Ŀ�����ݿ�����sql���

    String m_sLeftTable = ""; //���������õ��ı�(��������)

    String m_sLeftWhere = " where "; //�������е�where�Ӿ�

    static String m_sLineSep = "\r\n";

    //System.getProperty("line.separator"); //���з�
    protected String m_sResorceSQL = null; //Դsql��� SQL Resorce

    //���������ַ�
    String m_sSpecialChar = "-+()*=,? <>; " + "\t" + m_sLineSep;

    public String dateTypes[] = new String[] { "datetime", "smalldatetime", "timestamp", "UFDatabasedate", };

    String numberTypes[] = new String[] { "decimal", "float", "real", "int", "money", "numeric", "smallint", "smallmoney", "tinyint", "UFNumber5", "UFFactor",
            "UFPercent", "UFRate", "UFRebate", "UFTaxRate", "UFInterestRate", "UFInteger", "UFSeq", "UFIndex", "UFIndexInteger", "UFVersionNO", "UFFlag",
            "UFSeqshort", "UFDirection", "UFWaItem", "UFPeriod", "UFLevel", "UFWidth", "UFApproveStatus", "UFDefaultset", "UFInt", "UFStatus" };

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2002-1-19 14:06:10)
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
            //����ǰ�ַ���Ϊ������
            if (asWords[iOff].equals("("))
                iLBracket++;
            //����ǰ�ַ���Ϊ������
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
     * �˴����뷽��˵����
     * �������ڣ�(2002-1-17 17:16:50)
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
     * �˴����뷽��˵����
     * �������ڣ�(2002-1-17 13:59:39)
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
     * �˴����뷽��˵����
     * �������ڣ�(2001-12-19 10:00:22)
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
     * �˴����뷽��˵����
     * �������ڣ�(2001-12-29 11:18:23)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isBiJiaoFu(String st) {
        boolean isOprater = false;

        //û������as�� Ҳû������on,����
        if (st.equals("=") //
                || st.equals("<=") || st.equals(">=") || st.equals("<") || st.equals(">") || st.equals("<>") || st.equals("!=") || st.equalsIgnoreCase("is")) {
            isOprater = true;
        }
        return isOprater;
    }

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2005-01-05 17:04:03)
     * @return boolean
     * @param dataType java.lang.String
     */
    public boolean isCharType(String dataType) {
        return isType(charTypes, dataType);
    }

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2002-1-18 17:04:03)
     * @return boolean
     * @param dataType java.lang.String
     */
    public boolean isDateType(String dataType) {
        return isType(dateTypes, dataType);
    }

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2001-12-28 19:43:29)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isFunctionName(String sWord, String nextWord) {
        boolean isFunc = false;

        if (((sWord.equalsIgnoreCase("left") && !nextWord.equalsIgnoreCase("outer"))/** 1 **/
        || (sWord.equalsIgnoreCase("right") && !nextWord.equalsIgnoreCase("outer"))/** 2 **/
        || sWord.equalsIgnoreCase("square") //��ǰ����Ϊ��square��
                || sWord.equalsIgnoreCase("cast") //��ǰ����Ϊ��cast��
                || sWord.equalsIgnoreCase("coalesce") || sWord.equalsIgnoreCase("ltrim") || sWord.equalsIgnoreCase("rtrim")
                || sWord.equalsIgnoreCase("patindex") || sWord.equalsIgnoreCase("len") || sWord.equalsIgnoreCase("round") || sWord.equalsIgnoreCase("convert")
                || sWord.equalsIgnoreCase("dateadd") || sWord.equalsIgnoreCase("datediff")) //����һ�������ǡ�(��
                && nextWord.equals("(")) {
            isFunc = true;
        }
        return isFunc;
    }

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2001-12-29 10:12:17)
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
     * �˴����뷽��˵����
     * �������ڣ�(2001-12-29 11:18:23)
     * @return boolean
     * @param st java.lang.String
     */
    public boolean isTableOtherName(String st) {
        boolean isTableOtherName = false;

        //û������as�� Ҳû������on,����
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
            m_iDestinationDatabaseType = SQLSERVER; //ȱʡΪsql server
    }

    /**
     * ����SQL���
     */
    public void setSqlArray(String[] sqlArray) {
        m_asSqlWords = sqlArray;
    }

    /**
     * ����SQL���
     */
    public String[] getSqlArray() {
        return m_asSqlWords;
    }

    /**
     * ����SQL���
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
     * �˴����뷽��˵����
     * �������ڣ�(2004-6-3 15:10:44)
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
        //�ӹ���������ȡ��¼
        for (int i = 0; i < vecTable.size(); i++) {
            sTable[i] = (String) vecTable.elementAt(i);
        }
        return sTable;
    }

    /**
     * �˴����뷽��˵����
     * �������ڣ�(2004-6-2 16:02:34)
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
     * �˴����뷽��˵����
     * �������ڣ�(2004-6-2 16:02:34)
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
     * �˴����뷽��˵����
     * �������ڣ�(2004-5-25 19:42:34)
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
     * �˴����뷽��˵����
     * �������ڣ�(2004-5-25 19:42:34)
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

    //��ǰ������
    private java.sql.Connection m_con = null;

    //���ݿ�汾
    private int m_DbVersion = 0;

    /**
     * �˴����뷽��˵���� 
     * ���ܣ�ȡ��Ŀ�����ݿ�汾 
     * �������ڣ�(2005-01-05 11:44:11)
     */
    public int getDbVersion() {
        return m_DbVersion;
    }

    /**
     * �˴����뷽��˵����
     * ���ܣ�����Ŀ�����ݿ������
     * �������ڣ�(2005-01-25 09:44:00)
     */
    public void setDbVersion(int dbVersion) {
        m_DbVersion = dbVersion;
    }
}