package havis.custom.harting.tools.model;

import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;

public enum Scheme {
	SGTIN, SSCC, GRAI, GIAI, RAW;

	private final static ConstantsResource resource = ConstantsResource.INSTANCE;

	public String label() {
		String lbl;

		switch (this) {
		case SGTIN:
			lbl = resource.sgtinLabel();
			break;
		case SSCC:
			lbl = resource.ssccLabel();
			break;
		case GRAI:
			lbl = resource.graiLabel();
			break;
		case GIAI:
			lbl = resource.giaiLabel();
			break;
		default:
			lbl = resource.rawLabel();
			break;
		}

		return lbl;
	}
	
	public final static boolean contains(String epc) {
		return epc != null && (epc.contains("tag:giai-96") || epc.contains("tag:grai-96") || 
			   epc.contains("tag:sgtin-96") || epc.contains("tag:sscc-96") || epc.contains("tag:raw:96"));
	}
	
	@Override
	public String toString() {
		return name();
	}
}
