package net.juniper.jmp.persist.mysql;

import java.sql.SQLException;

import net.juniper.jmp.persist.exp.JmpDbException;

public class JmpMySQLException extends JmpDbException {

	private static final long serialVersionUID = -5713524414094113849L;
	public JmpMySQLException(String msg) {
		super(msg);
	}


	public JmpMySQLException(String msg, SQLException e) {
		super(msg, e);
	}

}
