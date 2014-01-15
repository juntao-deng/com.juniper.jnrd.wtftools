package net.juniper.jmp.persist.jdbc;

import java.io.UnsupportedEncodingException;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

/**
 * Clob parameter. This db type is not recommended. Use Blob instead
 * @author juntaod
 *
 */
public class ClobParamType implements SQLParamType{
	private static final long serialVersionUID = -1449755460645693124L;
	private static final String CHARSET = "iso8859-1";
	private String str = null;
    private int length = 0;

    public ClobParamType(String str) {
        try {
            this.str = str;
            length = str.getBytes(CHARSET).length;
        } 
        catch (UnsupportedEncodingException e) {
        	throw new JmpDbRuntimeException(e.getMessage(), e);
        }
    }

    public String getString() {
    	return str;
    }
    
    public int getLength() {
        return length;
    }


}
