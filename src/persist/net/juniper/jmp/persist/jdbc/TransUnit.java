package net.juniper.jmp.persist.jdbc;

public class TransUnit {
    private int iOffSet = 0;

    public java.lang.String sql = null;

    public java.lang.String[] sqlArray = null;

    private boolean dontHaveWhere = false;

    public TransUnit() {
        super();
    }

    public TransUnit(String[] newStArray, String newSql, int newOffset) {
        super();
        setSqlArray(newStArray);
        setSql(newSql);
        setIOffSet(newOffset);
    }

    public int getIOffSet() {
        return iOffSet;
    }

    public java.lang.String getSql() {
        return sql;
    }

    public java.lang.String[] getSqlArray() {
        return sqlArray;
    }

    public boolean isDontHaveWhere() {
        return dontHaveWhere;
    }

    public void setDontHaveWhere(boolean newDontHaveWhere) {
        dontHaveWhere = newDontHaveWhere;
    }

    public void setIOffSet(int newIOffSet) {
        iOffSet = newIOffSet;
    }

    public void setSql(java.lang.String newSql) {
        sql = newSql;
    }

    public void setSqlArray(java.lang.String[] newSqlArray) {
        sqlArray = newSqlArray;
    }
}