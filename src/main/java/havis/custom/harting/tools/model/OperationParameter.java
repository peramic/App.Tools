package havis.custom.harting.tools.model;

import havis.device.rf.tag.operation.LockOperation.Privilege;

public class OperationParameter {
	private String epc;
	private Operation operation;
	private Field field;
	private Field bank;
	private String password;
	private String data;
	private short length;
	private short offset;
	private Privilege privilege;
	private boolean passwordEnabled;

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		if(epc != null) {
			this.epc = epc.replace("x", "").replace("X", "");
		}
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if(password != null) {
			this.password = password.replace("x", "").replace("X", "");
		}
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		if(data != null) {
			this.data = data.replace("x", "").replace("X", "");
		}
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public void setLength(String length) {
		short lng;

		try {
			lng = Short.parseShort(length);
		} catch (NumberFormatException nfe) {
			lng = 0;
		}

		setLength(lng);
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public void setOffset(String offset) {
		short off;

		try {
			off = Short.parseShort(offset);
		} catch (NumberFormatException nfe) {
			off = 0;
		}

		setOffset(off);
	}

	public Field getBank() {
		return bank;
	}

	public void setBank(Field bank) {
		this.bank = bank;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public boolean isPasswordEnabled() {
		return passwordEnabled;
	}

	public void setPasswordEnabled(boolean passwordEnabled) {
		this.passwordEnabled = passwordEnabled;
	}
}
