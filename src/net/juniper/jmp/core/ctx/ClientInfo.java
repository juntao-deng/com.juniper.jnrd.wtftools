package net.juniper.jmp.core.ctx;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	private static final long serialVersionUID = 2424687032331510700L;
	private String clientIp;
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
