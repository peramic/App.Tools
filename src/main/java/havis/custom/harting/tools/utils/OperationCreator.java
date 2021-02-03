package havis.custom.harting.tools.utils;

import havis.custom.harting.tools.exception.FieldNotSupportedException;
import havis.custom.harting.tools.model.Field;
import havis.custom.harting.tools.model.FieldProperties;
import havis.custom.harting.tools.model.Operation;
import havis.custom.harting.tools.model.OperationParameter;
import havis.custom.harting.tools.ui.client.createtag.CreateTagView;
import havis.custom.harting.tools.ui.client.rfid.RFIDView;
import havis.custom.harting.tools.ui.client.widgets.TagIDLabel;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.device.rf.tag.operation.KillOperation;
import havis.device.rf.tag.operation.LockOperation;
import havis.device.rf.tag.operation.LockOperation.Privilege;
import havis.device.rf.tag.operation.ReadOperation;
import havis.device.rf.tag.operation.TagOperation;
import havis.device.rf.tag.operation.WriteOperation;
import havis.middleware.tdt.DataTypeConverter;
import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import com.google.gwt.user.client.ui.Widget;

public class OperationCreator {
	private static final int WORD = 16;
	private static final int UNIT = WORD;
	private static final Map<Field, FieldProperties> fieldProperties = new HashMap<Field, FieldProperties>();

	public static final OperationCreator INSTANCE = new OperationCreator();

	static {
		fieldProperties.put(Field.RESERVED, new FieldProperties((short) 0, (short) 0, (short) 64));
		fieldProperties.put(Field.KILL_PASSWORD, new FieldProperties((short) 0, (short) 0, (short) 32));
		fieldProperties.put(Field.ACCESS_PASSWORD, new FieldProperties((short) 0, (short) 32, (short) 32));
		fieldProperties.put(Field.EPC_MEMORY, new FieldProperties((short) 1, (short) 32, (short) 96));
		fieldProperties.put(Field.TID_MEMORY, new FieldProperties((short) 2, (short) 0, (short) 0));
		fieldProperties.put(Field.USER_MEMORY, new FieldProperties((short) 3, (short) 0, (short) 512));
	}

	private OperationCreator() {

	}
	
	public List<OperationParameter> getLockEpcOperations(CreateTagView view) throws TdtTranslationException {
		List<OperationParameter> rfidParameters = new ArrayList<OperationParameter>();
		
		rfidParameters.add(initLockAccessPassword(view));
		rfidParameters.add(initLockEpc(view));
		
		return rfidParameters;
	}
	
	public OperationParameter getWriteEpcOperation(CreateTagView view) throws TdtTranslationException {
		String[] temp = null;
		String dataToWrite = "";

		final String epcId = view.getEpcPanel().getValue();

		temp = epcId.split(":");

		if (!"raw".equals(temp[2])) {
			TdtTagInfo tagInfo = Utils.INSTANCE.translateTag(epcId);
			dataToWrite = tagInfo.getUriRawHex();
			temp = dataToWrite.split("\\.");
			dataToWrite = temp[1];
		} else {
			String[] data = temp[3].split("\\.");
			dataToWrite = data[1];
		}

		dataToWrite = dataToWrite.replace("x","").replace("X", "");

		Field field = Field.EPC_MEMORY;
		Operation operation = Operation.WRITE;
		String data = dataToWrite;
		Boolean passwordEnabled = false;
		String password = view.getSettingsSection().getValue();
		Privilege privilege = null;
		Field bank = null;
		String epc = dataToWrite;

		Integer length = new Integer(fieldProperties.get(Field.EPC_MEMORY).getLength());
		Integer offset = new Integer(fieldProperties.get(Field.EPC_MEMORY).getOffset());

		OperationParameter rfidParameter = createOperationParameter(field, bank, operation, privilege, data, epc, passwordEnabled, password, length, offset);

		return rfidParameter;
	}
	
	public OperationParameter getWriteAccessPasswordOperation(CreateTagView view) {
		Field field = Field.ACCESS_PASSWORD;
		Operation operation = Operation.WRITE;
		String data = view.getSettingsSection().getValue();
		Boolean passwordEnabled = false;
		String password = view.getSettingsSection().getValue();
		String epc = null;
		Field bank = null;
		Privilege privilege = null;

		Integer length = new Integer(fieldProperties.get(Field.ACCESS_PASSWORD).getLength());
		Integer offset = new Integer(fieldProperties.get(Field.ACCESS_PASSWORD).getOffset());

		OperationParameter rfidParameter = createOperationParameter(field, bank, operation, privilege, data, epc, passwordEnabled, password, length, offset);

		return rfidParameter;
	}

	public OperationParameter getRfidOperation(RFIDView view) {

		Widget[] selectedItem = view.getTagsList().getSelectedItem();

		if (selectedItem != null) {
			TagIDLabel tag = (TagIDLabel) selectedItem[0];

			Field field = view.getFieldPanel().getValue();
			Field bank = view.getFieldPanel().getCustomPanel().getValue();
			Operation operation = view.getOperationPanel().getValue();
			Privilege privilege = view.getDataPanel().getValue();
			String data = view.getDataPanel().getData();
			String epc = tag.getTitle();
			Boolean passwordEnabled = view.getPasswordPanel().isActivated();
			String password = view.getPasswordPanel().getPassword().trim();

			Integer length = view.getFieldPanel().getCustomPanel().getLength().getValue();
			Integer offset = view.getFieldPanel().getCustomPanel().getOffset().getValue();

			OperationParameter rfidParameter = createOperationParameter(field, bank, operation, privilege, data, epc, passwordEnabled, password, length, offset);

			return rfidParameter;
		} else {
			throw new ValidationException(ConstantsResource.INSTANCE.noTagSelected());
		}
	}
	
	public TagOperation getTagOperation(OperationParameter param) throws FieldNotSupportedException {
		FieldProperties fieldProperties = convertField(param);

		String data = param.getData();
		Privilege privilege = param.getPrivilege();
		Field field = param.getField();
		short bank = fieldProperties.getBank();
		short offset = (short) (fieldProperties.getOffset() / UNIT);
		short length = (short) (fieldProperties.getLength() / UNIT);
		boolean passwordEnabled = param.isPasswordEnabled();

		TagOperation operation;

		switch (param.getOperation()) {
		case READ:
			ReadOperation readOp = new ReadOperation();
			readOp.setBank(bank);
			readOp.setLength(length);
			readOp.setOffset(offset);

			if (passwordEnabled) {
				readOp.setPassword(Utils.INSTANCE.strToInt(param.getPassword()));
			}

			operation = readOp;
			break;
		case WRITE:
			WriteOperation writeOp = new WriteOperation();
			writeOp.setBank(bank);
			writeOp.setOffset(offset);

			data = prepare(data, length);

			writeOp.setData(DataTypeConverter.hexStringToByteArray(data));

			if (passwordEnabled) {
				writeOp.setPassword(Utils.INSTANCE.strToInt(param.getPassword()));
			}

			operation = writeOp;
			break;
		case LOCK:
			LockOperation lockOp = new LockOperation();
			lockOp.setField(field.getLockOperation());
			lockOp.setPrivilege(privilege);

			if (passwordEnabled) {
				lockOp.setPassword(Utils.INSTANCE.strToInt(param.getPassword()));
			}

			operation = lockOp;
			break;
		case KILL:
			KillOperation killOp = new KillOperation();
			killOp.setKillPassword(Utils.INSTANCE.strToInt(param.getPassword()));
			operation = killOp;
			break;
		default:
			operation = null;
			break;
		}

		return operation;
	}


	private OperationParameter createOperationParameter(Field field, Field bank, Operation operation, Privilege privilege, String data, String epc,
			Boolean passwordEnabled, String password, Integer len, Integer off) {

		OperationParameter rfidParameter = new OperationParameter();

		password = passwordEnabled ? password : null;

		Short length = len != null ? len.shortValue() : 0;
		Short offset = off != null ? off.shortValue() : 0;

		if ((passwordEnabled && (password.trim().length() > 0))) {
			if (!password.matches("^(x|X)?[0-9a-fA-F]+$")) {
				throw new ValidationException(ConstantsResource.INSTANCE.incorrectInputFormatMessage());
			}
		}

		if ((Operation.WRITE == operation) || (Operation.KILL == operation)) {
			if (!data.matches("^(x|X)?[0-9a-fA-F]+$")) {
				throw new ValidationException(ConstantsResource.INSTANCE.incorrectInputFormatMessage());
			}
		}

		if (field == Field.CUSTOM) {
			int l = length;
			int o = offset;

			if ((l % UNIT) != 0) {
				throw new ValidationException(ConstantsResource.INSTANCE.sixteenTimesMessage());
			}

			if ((o % UNIT) != 0) {
				throw new ValidationException(ConstantsResource.INSTANCE.sixteenTimesMessage());
			}
		}

		rfidParameter.setPassword(password);
		rfidParameter.setField(field);
		rfidParameter.setPrivilege(privilege);
		rfidParameter.setData(data);
		rfidParameter.setEpc(epc);
		rfidParameter.setOperation(operation);
		rfidParameter.setBank(bank);
		rfidParameter.setLength(length);
		rfidParameter.setOffset(offset);
		rfidParameter.setPasswordEnabled(passwordEnabled);

		return rfidParameter;
	}

	private FieldProperties convertField(OperationParameter parameter) throws FieldNotSupportedException {
		Field field = parameter.getField();
		FieldProperties fieldProps;

		if (field == Field.CUSTOM) {
			FieldProperties bankProperties = fieldProperties.get(parameter.getBank());
			fieldProps = new FieldProperties(bankProperties.getBank(), parameter.getOffset(), parameter.getLength());
		} else {
			fieldProps = fieldProperties.get(field);

			if (fieldProps == null) {
				throw new FieldNotSupportedException("Bank: " + field);
			}
		}

		return fieldProps;
	}
	
	private OperationParameter initLockAccessPassword(CreateTagView view) {
		Operation operation = Operation.LOCK;
		Field field = Field.ACCESS_PASSWORD;
		Privilege privilege = Privilege.LOCK;
		Boolean passwordEnabled = true;
		String password = view.getSettingsSection().getValue();
		String data = null;
		String epc = null;
		Field bank = null;

		Integer length = 0;
		Integer offset = 0;

		OperationParameter rfidParameter = createOperationParameter(field, bank, operation, privilege, data, epc, passwordEnabled, password, length, offset);

		return rfidParameter;
	}
	
	private OperationParameter initLockEpc(CreateTagView view) {
		Operation operation = Operation.LOCK;
		Field field = Field.EPC_MEMORY;
		Privilege privilege = Privilege.LOCK;
		Boolean passwordEnabled = true;
		String password = view.getSettingsSection().getValue();
		String data = null;
		String epc = null;
		Field bank = null;

		Integer length = 0;
		Integer offset = 0;

		OperationParameter rfidParameter = createOperationParameter(field, bank, operation, privilege, data, epc, passwordEnabled, password, length, offset);

		return rfidParameter;
	}

	/**
	 * Prepares data parameter depending on entered length
	 * 
	 * @param data
	 *            that should be written to the tag
	 * @param lengthInWords
	 *            entered field length
	 * @return when "data" length is greater than entered length, then "data"
	 *         String will be cut. When entered length is greater then "data"
	 *         length, then "data" will be filled with "0".
	 */
	private String prepare(String data, int lengthInWords) {
		int dataLengthInNibble = data.length();
		/* convert to nibble */
		// 1 Character --> 1 Nibble
		// 2 Characters --> 2 Niddles --> 1 Byte
		// 4 Characters --> 4 Niddles --> 2 Byte --> 1Word
		// WORDs * 2 (to bytes) * 2 (to nibbles)
		int enteredLengthInNibble = lengthInWords * 2 * 2;

		String preparedData = data;

		if (enteredLengthInNibble < dataLengthInNibble)
			preparedData = preparedData.substring(0, enteredLengthInNibble);

		for (int i = dataLengthInNibble; i < enteredLengthInNibble; i++) {
			preparedData = "0" + preparedData;
		}

		return preparedData;
	}
}
