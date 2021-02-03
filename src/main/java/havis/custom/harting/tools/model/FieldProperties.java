package havis.custom.harting.tools.model;

public class FieldProperties {
	private short bank;
	private short offset;
	private short length;
	
	public FieldProperties(short bank, short offset, short length) {
		this.offset = offset;
		this.length = length;
		this.bank = bank;
	}
	
	public short getBank() {
		return bank;
	}

	public short getOffset() {
		return offset;
	}

	public short getLength() {
		return length;
	}
}
