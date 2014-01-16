package net.juniper.jmp.persist.exp;

import java.sql.SQLException;

public class JmpDbException extends Exception {
	private static final long serialVersionUID = 1L;
	protected int sqlErrorCode = 0;

    protected String sqlstate  = null;

    protected SQLException  realException;

    public JmpDbException(String msg, SQLException e) {
        super(msg,e);
        realException = e;
        sqlErrorCode = e.getErrorCode();
        sqlstate = e.getSQLState();
    }


    public JmpDbException(String msg) {
        super(msg);
        sqlErrorCode = -1;
        sqlstate  = null;
    }

    public JmpDbException(String msg, Throwable pt) {
        super(msg, pt);
        sqlErrorCode = -1;
        sqlstate  = null;
    }
    
    public int getSQLErrorCode() {
        return (sqlErrorCode);
    }


    public String getSQLState() {
        return (sqlstate );
    }

    public SQLException getRealException(){
        return realException;
    }
}

