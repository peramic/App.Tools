package havis.custom.harting.tools.model;

import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.device.rf.tag.operation.LockOperation;

public enum Field {
	ACCESS_PASSWORD, KILL_PASSWORD, EPC_MEMORY, TID_MEMORY, USER_MEMORY, CUSTOM, RESERVED;

	private final static ConstantsResource resource = ConstantsResource.INSTANCE;
	
	public LockOperation.Field getLockOperation() {
		return parseLockOperation(this);
	}
	
	public LockOperation.Field parseLockOperation(Field field) {
		LockOperation.Field lockOperationfield;

		switch (field) {
		case ACCESS_PASSWORD:
			lockOperationfield = LockOperation.Field.ACCESS_PASSWORD;
			break;
		case EPC_MEMORY:
			lockOperationfield = LockOperation.Field.EPC_MEMORY;
			break;
		case KILL_PASSWORD:
			lockOperationfield = LockOperation.Field.KILL_PASSWORD;
			break;
		case TID_MEMORY:
			lockOperationfield = LockOperation.Field.TID_MEMORY;
			break;
		case USER_MEMORY:
			lockOperationfield = LockOperation.Field.USER_MEMORY;
			break;
		default:
			lockOperationfield = null;
			break;
		}

		return lockOperationfield;
	}
	
	public String label(boolean custom) {
		String item;

		switch (this) {
		case ACCESS_PASSWORD:
			item = resource.fieldAccessPasswordLabel();
			break;
		case KILL_PASSWORD:
			item = resource.fieldKillPassword();
			break;
		case EPC_MEMORY:
			item = custom ? resource.fieldEpcBankLabel() : resource.fieldEpcLabel();
			break;
		case TID_MEMORY:
			item = resource.fieldTidLabel();
			break;
		case USER_MEMORY:
			item = resource.fieldUserMemoryBankLabel();
			break;
		case CUSTOM:
			item = resource.fieldCustomLabel();
			break;
		case RESERVED:
			item = resource.fieldReservedBankLabel();
			break;
		default:
			item = "";
			break;
		}
		
		return item;
	}
}
