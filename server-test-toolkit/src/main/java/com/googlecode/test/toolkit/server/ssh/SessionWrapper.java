package com.googlecode.test.toolkit.server.ssh;

import com.googlecode.test.toolkit.server.common.user.ServerUser;
import com.jcraft.jsch.Session;

public class SessionWrapper {

	private Session session;
	private ServerUser serverUser;

	private int usingChannelNumber;
	private int maxChannelNumber;

	public ServerUser getServerUser() {
		return serverUser;
	}


	public void setServerUser(ServerUser serverUser) {
		this.serverUser = serverUser;
	}


	public void setUsingChannelNumber(int usingChannelNumber) {
		this.usingChannelNumber = usingChannelNumber;
	}



	public SessionWrapper(Session session, ServerUser serverUser, int maxChannelNumber) {
		super();
		this.session = session;
		this.maxChannelNumber = maxChannelNumber;
	}


	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		this.session = session;
	}


	public int getUsingChannelNumber() {
		return usingChannelNumber;
	}


	public void setChannelNumber(int channelNumber) {
		this.usingChannelNumber = channelNumber;
	}


	public void increaseUsingChannelNumber() {
		this.usingChannelNumber += 1;
	}

	public void decreaseUsingChannelNumber() {
		this.usingChannelNumber -= 1;
	}

	public int getMaxChannelNumber() {
		return maxChannelNumber;
	}


	public void setMaxChannelNumber(int maxChannelNumber) {
		this.maxChannelNumber = maxChannelNumber;
	}

	public boolean isSessionAvailable() {
		return this.usingChannelNumber < maxChannelNumber;
	}

	public void disconnect() {
 			 this.session.disconnect();
 	}

	public String getHost() {
		return this.session.getHost();
	}

	@Override
	public String toString() {
		return "SessionWrapper [session=" + session + ", channelNumber="
				+ usingChannelNumber + ", maxChannelNumber=" + maxChannelNumber
				+ "]";
	}

}
