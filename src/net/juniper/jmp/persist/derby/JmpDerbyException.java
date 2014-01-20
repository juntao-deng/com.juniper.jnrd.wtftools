package net.juniper.jmp.persist.derby;

import java.sql.SQLException;

import net.juniper.jmp.persist.exp.JmpDbException;

public class JmpDerbyException extends JmpDbException {
	private static final long serialVersionUID = 3557832474993483389L;
	public JmpDerbyException(String msg) {
		super(msg);
	}
	public JmpDerbyException(String msg, SQLException e) {
		super(msg, e);
	}
	public JmpDerbyException(String msg, Throwable pt) {
		super(msg, pt);
	}
}
