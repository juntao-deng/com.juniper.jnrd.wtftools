package net.juniper.jmp.persist.jdbc;

import java.io.Reader;
import java.io.StringReader;

/**
 * Clob parameter. This db type is not recommended. Use Blob instead
 * @author juntaod
 *
 */
public class ClobParamType implements SQLParamType{
	private static final long serialVersionUID = -1449755460645693124L;
	private Reader reader;

    public ClobParamType(Reader reader) {
        this.reader = reader;
    }
    
    public ClobParamType(String str) {
		reader = new StringReader(str);
	}

	public Reader getReader() {
    	return reader;
    }
}
