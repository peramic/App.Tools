package havis.custom.harting.tools.utils;

import havis.middleware.tdt.DataTypeConverter;
import havis.middleware.tdt.EpcTagDataTranslation;
import havis.middleware.tdt.EpcTagDataTranslationCodec;
import havis.middleware.tdt.TdtResources;
import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslationException;
import havis.middleware.tdt.TdtTranslator;
import havis.net.rest.rf.async.RFDeviceServiceAsync;

import com.google.gwt.core.client.GWT;

public class Utils {

	public static final String FAILURE = "FAILURE";
	public static final String SUCCESS = "SUCCESS";

	private TdtTranslator translator;
	private RFDeviceServiceAsync service = GWT.create(RFDeviceServiceAsync.class);

	public static final Utils INSTANCE = new Utils();

	private Utils() {
		EpcTagDataTranslationCodec codec = EpcTagDataTranslationCodec.INSTANCE;

		EpcTagDataTranslation sgtin96 = codec.decode(TdtResources.INSTANCE.sgtin96().getText());
		EpcTagDataTranslation sscc96 = codec.decode(TdtResources.INSTANCE.sscc96().getText());
		EpcTagDataTranslation grai96 = codec.decode(TdtResources.INSTANCE.grai96().getText());
		EpcTagDataTranslation giai96 = codec.decode(TdtResources.INSTANCE.giai96().getText());

		translator = new TdtTranslator();

		translator.getTdtDefinitions().add(sgtin96);
		translator.getTdtDefinitions().add(sscc96);
		translator.getTdtDefinitions().add(grai96);
		translator.getTdtDefinitions().add(giai96);
	}

	/**
	 * Translates a hex string to a TdtTagInfo
	 * 
	 * @param tag
	 * @return info or null if the data.length() does not fit
	 * @throws TdtTranslationException
	 */
	public TdtTagInfo translateTag(byte[] data) throws TdtTranslationException {
		TdtTagInfo info = null;

		try {
			info = translator.translate(data);
		} catch (TdtTranslationException tte) {
			if (tte.getMessage().contains("Could not find encoding scheme")) {
				throw new TdtTranslationException("Please check the EPC format!");
			}

			throw tte;
		}

		return info;
	}

	/**
	 * Translates a hex string to a TdtTagInfo
	 * 
	 * @param tag
	 * @return info or null if the data.length() does not fit
	 * @throws TdtTranslationException
	 */
	public TdtTagInfo translateTag(String data) throws TdtTranslationException {
		TdtTagInfo info = null;

		try {
			info = translator.translate(data);
		} catch (TdtTranslationException tte) {
			if (tte.getMessage().contains("Could not find encoding scheme")) {
				throw new TdtTranslationException("Please check the EPC format!");
			}

			throw tte;
		}

		return info;
	}

	public RFDeviceServiceAsync getService() {
		return service;
	}

	public void setService(RFDeviceServiceAsync service) {
		this.service = service;
	}

	/**
	 * Extended empty check
	 * 
	 * @param s - string to check
	 * @return
	 */
	public boolean isNullOrEmpty(String s) {
		return s == null || "".equals(s) || (s.trim().length() < 1) || "null".equals(s);
	}

	public int strToInt(String hexStr) {
		hexStr = hexStr.replaceAll("\\s|_", "");

		if (hexStr.length() % 2 != 0) {
			throw new IllegalArgumentException("Hex string must have an even number of characters.");
		}

		byte[] b = DataTypeConverter.hexStringToByteArray(hexStr);
		int i = bytesToInt(b);

		return i;
	}

	/**
	 * Converts an array of bytes to an integer. This array must not contain more
	 * that 4 bytes (32 bits), to make sure the result can be stored in an integer.
	 * 
	 * @param maxFourBytes 4 bytes of data
	 * @return the bytes of data as one integer
	 * @throws IllegalArgumentException if the byte array contains more than 4
	 *                                  bytes.
	 */
	public int bytesToInt(byte[] maxFourBytes) throws IllegalArgumentException {
		if (maxFourBytes.length > 4)
			throw new IllegalArgumentException("Byte array must not contain more than 4 bytes.");
		int ret = 0;
		for (int i = 0; i < maxFourBytes.length; i++) {
			ret |= maxFourBytes[i] & 0xff;
			if (i + 1 < maxFourBytes.length)
				ret <<= 8;
		}
		return ret;
	}
}
