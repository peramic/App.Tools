package havis.custom.harting.tools;

import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslationException;
import havis.middleware.tdt.TdtTranslator;

public class EpcRangeIterator {

	private String epc;
	private int from = 0;
	private int to = 1;
	private String pc;
	private String tag;
	private String hex;
	private int counter;
	private String ai;
	private String preFix = "";
	private int companyPrefixLength;
	private TdtTranslator translator;
	private String currentEpc;

	public EpcRangeIterator(String epc, TdtTranslator translator) {
		super();
		this.epc = epc;
		this.translator = translator;

		init();
	}

	public boolean hasNext() throws TdtTranslationException {
		boolean hN = counter <= to;

		if (hN) {

			String epcTag = currentEpc = preFix + postFix();

			try {
				TdtTagInfo info = translator.translate(epcTag);

				String rawHex = info.getUriRawHex();
				String[] splittedRawHex = rawHex.split("\\.");

				if (splittedRawHex.length == 2) {
					hex = splittedRawHex[1].replace("x", "").replace("X", "");

					pc = "3000"; // TODO calculate automatically
				}

				if (info.isEpcGlobal()) {
					tag = info.getUriTag();
					ai = info.getUriLegacyAi();
				} else {
					tag = rawHex;
					ai = rawHex;
				}

				counter++;

			} catch (TdtTranslationException tte) {
				if (tte.getMessage().contains("Could not find encoding scheme")) {
					throw new TdtTranslationException("Please check the EPC format!");
				}

				throw tte;
			}
		}

		return hN;
	}

	public String postFix() {
		String appendix = "";

		if (epc.toLowerCase().contains("sscc-96")) {
			appendix = "0" + (17 - companyPrefixLength);
		}

		return String.format("%" + appendix + "d", counter);
	}

	public String getTag() {
		return tag;
	}

	public String getPc() {
		return pc;
	}

	public String getHex() {
		return hex;
	}

	public String getAi() {
		return ai;
	}

	public String getCurrentEpc() {
		return currentEpc;
	}

	private void init() {
		String[] exploded = epc.split("\\.");

		if ((exploded != null) && (exploded.length > 1)) {

			for (int i = 0; i < (exploded.length - 1); i++) {
				preFix += exploded[i] + ".";
			}

			String lastSeq = exploded[exploded.length - 1];
			companyPrefixLength = exploded[exploded.length - 2].length();

			if (lastSeq.startsWith("[") && lastSeq.endsWith("]")) {
				lastSeq = lastSeq.replaceAll("\\[", "");
				lastSeq = lastSeq.replaceAll("\\]", "");

				String[] ranges = lastSeq.split("\\-");

				if (ranges.length == 2) {
					from = Integer.parseInt(ranges[0]);
					to = Integer.parseInt(ranges[1]);
				} else if (ranges.length == 1) {
					from = Integer.parseInt(ranges[0]);
					to = from;
				}
			} else {
				from = Integer.parseInt(lastSeq);
				to = from;
			}
		}

		counter = from;
	}
}
