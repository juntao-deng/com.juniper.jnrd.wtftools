package net.juniper.jmp.persist.exp;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CrossDBSQLException extends SQLException {
    private static final long serialVersionUID = -1857782841397778688L;
    Throwable cause;
    
    /**
     *
     * @param message - the reason
     * @param state - the SQL state
     * @param cause - the exception that was the reason for this exception
     */
    public CrossDBSQLException(String message,String state,Throwable cause) {
        super(message,state);
        this.cause=cause;
    }

    /**
     *
     */
    public void printStackTrace() {
        super.printStackTrace();
        if(cause!=null) {
            cause.printStackTrace();
        }
    }
    /**
     * Prints the stack trace to the specified print writer.
     *
     * @param s - the print writer
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if(cause!=null) {
            cause.printStackTrace(s);
        }
    }
    /**
     *
     * @param s - the print stream
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if(cause!=null) {
            cause.printStackTrace(s);
        }
    }
}
