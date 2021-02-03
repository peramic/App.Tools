package havis.custom.harting.tools.model;

import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;

public enum Operation {
	READ("READ", 0), WRITE("WRITE", 1), KILL("KILL", 2), LOCK("LOCK", 3);

	private String op;
	private int index;
	private final static ConstantsResource resource = ConstantsResource.INSTANCE;

	private Operation(String op, int index) {
		this.op = op;
		this.index = index;
	}

	public int getOperationIndex() {
		return index;
	}

	@Override
	public String toString() {
		return op;
	}

	public String label() {
		String item;

		switch (this) {
		case READ:
			item = resource.readOPLabel();
			break;
		case WRITE:
			item = resource.writeOPLabel();
			break;
		case LOCK:
			item = resource.lockOPLabel();
			break;
		case KILL:
			item = resource.killOPLabel();
			break;
		default:
			item = "";
			break;
		}

		return item;
	}
}
