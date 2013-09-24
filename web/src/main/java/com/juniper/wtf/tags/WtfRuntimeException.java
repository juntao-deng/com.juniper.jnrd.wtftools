package com.juniper.wtf.tags;

public class WtfRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1290083717575328961L;

	public WtfRuntimeException() {
		super();
	}

	public WtfRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WtfRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public WtfRuntimeException(String message) {
		super(message);
	}

	public WtfRuntimeException(Throwable cause) {
		super(cause);
	}

}
