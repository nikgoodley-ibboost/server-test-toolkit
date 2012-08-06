package org.test.toolkit.server.ssh.performance;

public abstract class Component {

	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	protected Component(String type) {
		super();
		this.type = type;
	}

}
