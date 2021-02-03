package havis.custom.harting.tools.ui.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ConstantsResource extends Messages {

	public static final ConstantsResource INSTANCE = GWT.create(ConstantsResource.class);

	String antenna();

	String transponderId();

	String epcColLabel();

	String advancedLabel();

	String schemeTypeLabel();

	String sgtinLabel();

	String ssccLabel();

	String graiLabel();

	String giaiLabel();

	String rawLabel();

	String lengthLabel();

	String length96Label();

	String filterLabel();

	String companyPrefixLabel();

	String itemRefLabel();

	String serNumberLabel();

	String settingsLabel();

	String bcCompPrefLen();

	String length6Label();

	String length7Label();

	String length8Label();

	String length9Label();

	String length10Label();

	String length11Label();

	String length12Label();

	String lockPassword();

	String resultLabel();

	String readOPLabel();

	String writeOPLabel();

	String killOPLabel();

	String lockOPLabel();

	String executeLabel();

	String operationTypeLabel();

	String fieldLabel();

	String fieldReservedBankLabel();

	String fieldAccessPasswordLabel();

	String fieldKillPassword();

	String fieldEpcBankLabel();

	String fieldEpcLabel();

	String fieldTidLabel();

	String fieldUserMemoryBankLabel();

	String fieldCustomLabel();

	String bankLabel();

	String offsetLabel();

	String passwordLabel();

	String lockLockLabel();

	String lockUnlockLabel();

	String lockPermalockLabel();

	String lockPermaunlockLabel();

	String dataLabel();

	String inventoryLabel();

	String unexpectedError();

	String connectionError();

	String parameterError();

	String lockedError();

	String unavailableError();

	String incorrectInputFormatMessage();

	String sixteenTimesMessage();

	String assetTypeLabel();

	String serReferenceLabel();

	String indAssRefLabel();

	String valueLabel();

	String noTagSelected();

	String noAntennaConfigurationAvailable();

	String rfidCaption();

	String createTagCaption();
	
	String toolsCaption();
	
	String rssi();

	String numberOfTagsSeen(int size);

	String noTagsFound();

	String epcTranslationError();

	String invalidEpcData();

	String invalidCompPrefRefLenngth();

	String invalidValueLength(Integer length);

	String operationNotRequested();

	String emptyTagsList();

	String noEpcData();
	
	String schemeNotSupported();

	String lockPwdInvalid();

	String insufficientUrn();

	String unsupportedScheme();
}