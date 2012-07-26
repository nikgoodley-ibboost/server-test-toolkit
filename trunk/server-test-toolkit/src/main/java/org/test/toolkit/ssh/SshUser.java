package org.test.toolkit.ssh;

import org.test.toolkit.util.ValidationUtil;
 
/**
 * @author fu.jian
 * @date Jul 25, 2012
 */
public class SshUser {
	
	private String ip;
	private int port;
	private String userName;
	private String password;
 
	public SshUser(String ip, int port, String userName, String password) {
		ValidationUtil.effectiveStr(ip,userName,password);
		ValidationUtil.effectivePositive(port);
		
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
 	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SshUser [ip=");
		builder.append(ip);
		builder.append(", port=");
		builder.append(port);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}
}