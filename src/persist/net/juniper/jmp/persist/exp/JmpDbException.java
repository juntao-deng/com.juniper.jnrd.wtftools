package net.juniper.jmp.persist.exp;

import java.sql.SQLException;

public class JmpDbException extends Exception {
	private static final long serialVersionUID = 1L;
	protected int sqlErrorCode = 0;

    protected String sqlstage  = null;

    protected SQLException  realException;

    public JmpDbException(String msg, SQLException e) {
        super(msg,e);
        realException = e;
        sqlErrorCode = e.getErrorCode();
        sqlstage = e.getSQLState();
    }


    public JmpDbException(String msg) {
        super(msg);
        sqlErrorCode = -1;
        sqlstage  = null;
    }


    public int getSQLErrorCode() {
        return (sqlErrorCode);
    }


    public String getSQLState() {
        return (sqlstage );
    }

    public SQLException getRealException(){
        return realException;
    }
}

