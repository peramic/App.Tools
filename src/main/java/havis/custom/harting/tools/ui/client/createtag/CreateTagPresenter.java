package havis.custom.harting.tools.ui.client.createtag;

import havis.custom.harting.tools.exception.FieldNotSupportedException;
import havis.custom.harting.tools.exception.ValidationException;
import havis.custom.harting.tools.model.OperationParameter;
import havis.custom.harting.tools.model.Scheme;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.custom.harting.tools.utils.OperationCreator;
import havis.custom.harting.tools.utils.ToolsCallback;
import havis.custom.harting.tools.utils.UiMisc;
import havis.custom.harting.tools.utils.Utils;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.TagOperation;
import havis.device.rf.tag.result.LockResult;
import havis.device.rf.tag.result.OperationResult;
import havis.device.rf.tag.result.WriteResult;
import havis.middleware.tdt.DataTypeConverter;
import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class CreateTagPresenter {

	private CreateTagView view;
	private Scheme currentSelectedScheme;

	public CreateTagPresenter(final CreateTagView view) {
		this.view = view;
		this.view.setPresenter(this);

		init();
	}

	public void onRead() {
		//view.setEnabled(false);

		read(new ToolsCallback(view) {
			@Override
			public void onSuccess(TagData tagdata) {
				super.onSuccess(tagdata);

				try {
					TdtTagInfo uriTag = Utils.INSTANCE.translateTag(tagdata.getEpc());
					view.getEpcPanel().setValue(uriTag.getUriTag(), true);
					selectSchemeAccordingToReadTag(uriTag.getUriTag(), uriTag.isEpcGlobal());

					view.getResultPanel().setResult(Utils.SUCCESS);
				} catch (TdtTranslationException tte) {
					view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.epcTranslationError());
					view.getResultPanel().setResult(Utils.FAILURE);
				}
			}
		});
	}

	public void onWrite() {
		//view.setEnabled(false);

		try {
			validate(true, false);

			read(new ToolsCallback(view) {
				@Override
				public void onSuccess(TagData result) {
					super.onSuccess(result);
					try {
						String epc = DataTypeConverter.byteArrayToHexString(result.getEpc());
						OperationParameter writeEpcOperation = OperationCreator.INSTANCE.getWriteEpcOperation(view);
						writeEpc(epc, writeEpcOperation, true);
					} catch (Exception e) {
						view.getErrorPanel().showErrorMessage(e.getMessage());
						view.getResultPanel().setResult(Utils.FAILURE);
					}
				}
			});
		} catch (Exception exc) {
			view.setEnabled(true);
			view.getErrorPanel().showErrorMessage(exc.getMessage());
			view.getResultPanel().setResult(Utils.FAILURE);
		}
	}

	public void onSchemeChanged() {
		try {
			if (currentSelectedScheme == Scheme.RAW) {
				view.getValuePanel().setValue("0");
			}

			currentSelectedScheme = view.getSchemePanel().getValue();
			organizeWidgetsAccordingToScheme(currentSelectedScheme, true);

			String epc = generateEpcIdentifierFromWidgets();

			view.getEpcPanel().setValue(epc, true);
		} catch (Exception exc) {
			view.getErrorPanel().showErrorMessage(exc.getMessage());
		}
	}

	public void onAdvancedDataChanged() {
		try {
			String epc = generateEpcIdentifierFromWidgets();
			view.getEpcPanel().setValue(epc);
		} catch (Exception exc) {
			view.getErrorPanel().showErrorMessage(exc.getMessage());
		}
	}

	public void onEpcChanged() {
		try {
			validate(false, true);
			selectSchemeAccordingToReadTag(view.getEpcPanel().getValue(), true);
		} catch (Exception exc) {
			view.getErrorPanel().showErrorMessage(exc.getMessage());
		}
	}

	public void onValueBoxKeyDownEvent(KeyDownEvent event) {
		int code = event.getNativeKeyCode();

		// upper case letters are transmitted via native KeyCode like lower case letters, but with a prefixed code 16 for shift
		boolean charAllowed = (code > 47 && code < 58) || (code > 95 && code < 106) || code == KeyCodes.KEY_BACKSPACE || code == KeyCodes.KEY_DELETE
				|| code == KeyCodes.KEY_LEFT || code == KeyCodes.KEY_RIGHT || code == KeyCodes.KEY_SHIFT || code == KeyCodes.KEY_TAB
				|| code == KeyCodes.KEY_LEFT || code == KeyCodes.KEY_RIGHT || code == KeyCodes.KEY_ENTER || code == 189 || // minus
				((currentSelectedScheme == Scheme.RAW) && (code == 88)) || ((currentSelectedScheme == Scheme.RAW) && (code > 64 && code < 71));

		if (!charAllowed) {
			event.preventDefault();
		}
	}

	public void export() {
		if(view.getSchemePanel().getValue() != Scheme.RAW) {
			final String EXPORT_LINK = GWT.getHostPageBaseURL() + "rest/tools/taglist/file/";

			try {
				validate(false, true);
				String epc = view.getEpcPanel().getValue();
				String url = URL.encode(EXPORT_LINK + epc);
				
				Window.Location.assign(url);
			} catch(Exception exc) {
				view.getErrorPanel().showErrorMessage(exc.getMessage());
			}
		} else {
			view.getErrorPanel().showErrorMessage("RAW scheme export not supported!");
		}
	}

	private void read(final Callback<TagData, Exception> callback) {
		Utils.INSTANCE.getService().getTags(new MethodCallback<List<TagData>>() {
			@Override
			public void onSuccess(Method method, List<TagData> response) {
				try {
					handleOneTag(response, callback);
				} catch (Exception exc) {
					view.getErrorPanel().showErrorMessage(exc.getMessage());
					view.getResultPanel().setResult(Utils.FAILURE);
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
				view.getResultPanel().setResult(Utils.FAILURE);
			}
		});
	}

	private void writeEpc(final String epc, final OperationParameter writeEpcOperation, final boolean retry) throws FieldNotSupportedException {
		TagOperation tagOperation = OperationCreator.INSTANCE.getTagOperation(writeEpcOperation);
		List<TagOperation> operations = new ArrayList<TagOperation>();
		operations.add(tagOperation);

		Utils.INSTANCE.getService().getTags(epc, operations, new MethodCallback<List<TagData>>() {
			@Override
			public void onSuccess(Method method, List<TagData> executionResult) {
				view.setEnabled(true);

				try {
					if (executionResult.size() > 0) {
						TagData tagData = executionResult.get(0);

						if (tagData.getResultList().size() == 1) {
							OperationResult operationResult = tagData.getResultList().get(0);

							String result = "";

							if (operationResult instanceof WriteResult) {
								WriteResult writeResult = (WriteResult) operationResult;
								result = writeResult.getResult().name();
							} else {
								result = ConstantsResource.INSTANCE.operationNotRequested();
							}

							if (!Utils.SUCCESS.equals(result)) {
								if (view.getSettingsSection().isActivated() && retry) {
									//view.setEnabled(false);
									writeEpcOperation.setPasswordEnabled(true);
									writeEpcOperation.setPassword(view.getSettingsSection().getValue().trim());
									writeEpc(epc, writeEpcOperation, false);
								} else {
									view.getResultPanel().setResult(result);
								}
							} else {
								if(view.getSettingsSection().isActivated()) {
									OperationParameter writeAccessPasswordOperation = OperationCreator.INSTANCE.getWriteAccessPasswordOperation(view);
									writePassword(writeEpcOperation.getEpc(), writeAccessPasswordOperation, true);
								} else {
									view.getResultPanel().setResult(result);
								}
							}
						} else {
							view.getResultPanel().setResult(ConstantsResource.INSTANCE.unexpectedError());
						}
					} else {
						view.getResultPanel().setResult(ConstantsResource.INSTANCE.emptyTagsList());
					}
				} catch (Exception exc) {
					view.setEnabled(true);
					view.getResultPanel().setResult(Utils.FAILURE);
					view.getErrorPanel().showErrorMessage(exc.getMessage());
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.setEnabled(true);
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
				view.getResultPanel().setResult(Utils.FAILURE);
			}
		});
	}

	private void writePassword(final String epc, final OperationParameter writeAccessPasswordOperation, final boolean retry) throws FieldNotSupportedException {
		TagOperation tagOperation = OperationCreator.INSTANCE.getTagOperation(writeAccessPasswordOperation);
		List<TagOperation> operations = new ArrayList<TagOperation>();
		operations.add(tagOperation);

		Utils.INSTANCE.getService().getTags(epc, operations, new MethodCallback<List<TagData>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.setEnabled(true);
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
				view.getResultPanel().setResult(Utils.FAILURE);
			}

			@Override
			public void onSuccess(Method method, List<TagData> executionResult) {
				view.setEnabled(true);

				try {
					if (executionResult.size() > 0) {
						TagData tagData = executionResult.get(0);

						if (tagData.getResultList().size() == 1) {
							OperationResult operationResult = tagData.getResultList().get(0);

							String result = "";

							if (operationResult instanceof WriteResult) {
								WriteResult writeResult = (WriteResult) operationResult;
								result = writeResult.getResult().name();
							} else {
								result = ConstantsResource.INSTANCE.operationNotRequested();
							}

							if (!Utils.SUCCESS.equals(result)) {
								if (view.getSettingsSection().isActivated() && retry) {
									//view.setEnabled(false);
									writeAccessPasswordOperation.setPasswordEnabled(true);
									writeAccessPasswordOperation.setPassword(view.getSettingsSection().getValue().trim());
									writePassword(epc, writeAccessPasswordOperation, false);
								} else {
									view.getResultPanel().setResult(result);
								}
							} else {
								lock(epc);
							}
						} else {
							view.getResultPanel().setResult(ConstantsResource.INSTANCE.unexpectedError());
						}
					} else {
						view.getResultPanel().setResult(ConstantsResource.INSTANCE.emptyTagsList());
					}
				} catch (Exception exc) {
					view.setEnabled(true);
					view.getResultPanel().setResult(Utils.FAILURE);
					view.getErrorPanel().showErrorMessage(exc.getMessage());
				}
			}

		});
	}

	private void lock(String epc) throws TdtTranslationException, FieldNotSupportedException {
		List<OperationParameter> parameters = OperationCreator.INSTANCE.getLockEpcOperations(view);
		List<TagOperation> operations = new ArrayList<TagOperation>();

		for (OperationParameter parameter : parameters) {
			operations.add(OperationCreator.INSTANCE.getTagOperation(parameter));
		}

		Utils.INSTANCE.getService().getTags(epc, operations, new MethodCallback<List<TagData>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.setEnabled(true);
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
				view.getResultPanel().setResult(Utils.FAILURE);
			}

			@Override
			public void onSuccess(Method method, List<TagData> executionResult) {
				view.setEnabled(true);

				try {
					if (executionResult.size() == 1) {
						TagData tagData = executionResult.get(0);

						if (tagData.getResultList().size() == 2) {
							OperationResult firstOpResult = tagData.getResultList().get(0);
							OperationResult secondOpResult = tagData.getResultList().get(1);

							String result = "";

							if ((firstOpResult instanceof LockResult) && (secondOpResult instanceof LockResult)) {
								LockResult firstLockResult = (LockResult) firstOpResult;
								LockResult secondLockResult = (LockResult) secondOpResult;

								String firstResult = firstLockResult.getResult().name();
								String secondResult = secondLockResult.getResult().name();

								result = (Utils.SUCCESS.equals(firstResult) && firstResult.equals(secondResult)) ? Utils.SUCCESS : "LOCK_" + Utils.FAILURE;
							} else {
								result = ConstantsResource.INSTANCE.operationNotRequested();
							}

							view.getResultPanel().setResult(result);
						} else {
							view.getResultPanel().setResult("LOCK_" + Utils.FAILURE);
						}
					} else {
						view.getResultPanel().setResult("LOCK_" + Utils.FAILURE);
					}
				} catch (Exception exc) {
					view.setEnabled(true);
					view.getResultPanel().setResult(Utils.FAILURE);
					view.getErrorPanel().showErrorMessage(exc.getMessage());
				}
			}

		});
	}

	private void validate(boolean checkPwdInput, boolean checkBrackets) throws ValidationException {
		String epc = view.getEpcPanel().getValue();
		
		if (!Scheme.contains(epc)) {
			throw new ValidationException(ConstantsResource.INSTANCE.schemeNotSupported());
		}
		
		StringBuffer reg = new StringBuffer();
		reg.append("^(");
		reg.append("(urn:epc:tag:giai-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+)");
		reg.append("|(urn:epc:tag:grai-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+\\.[0-9]+)");
		reg.append("|(urn:epc:tag:sgtin-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+\\.[0-9]+)");
		reg.append("|(urn:epc:tag:sscc-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+)");
		reg.append("|(urn:epc:raw:96\\.(x|X)?[0-9a-fA-F]+)");
		
		if(checkBrackets) {
			reg.append("|(urn:epc:tag:giai-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.\\[[0-9]+\\-[0-9]+\\])");
			reg.append("|(urn:epc:tag:grai-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+\\.\\[[0-9]+\\-[0-9]+\\])");
			reg.append("|(urn:epc:tag:sgtin-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.[0-9]+\\.\\[[0-9]+\\-[0-9]+\\])");
			reg.append("|(urn:epc:tag:sscc-96:(x|X|[0-9]){0,1}\\.[0-9]+\\.\\[[0-9]+\\-[0-9]+\\])");
			reg.append("|(urn:epc:raw:96\\.(x|X)?[0-9a-fA-F]+)");
		}
		
		reg.append("){1}$");

		if (!epc.matches(reg.toString())) {
			throw new ValidationException(ConstantsResource.INSTANCE.noEpcData());
		}

		String[] dataParts = epc.split(":");
		
		String trimmedPwd = view.getSettingsSection().getValue().replace("x", "").replace("X", "");

		if (checkPwdInput && view.getSettingsSection().isActivated()
				&& !((trimmedPwd.length() == 8) && trimmedPwd.matches("^[0-9a-fA-F]+$"))) {
			throw new ValidationException(ConstantsResource.INSTANCE.lockPwdInvalid());
		}

		Integer length;
		Scheme insertedScheme;

		if ("tag".equals(dataParts[2])) {
			String[] typeParts = dataParts[3].split("-");
			length = new Integer(typeParts[1]);

			try {
				insertedScheme = Scheme.valueOf(typeParts[0].toUpperCase());
			} catch (Exception e) {
				throw new ValidationException(ConstantsResource.INSTANCE.unsupportedScheme());
			}
		} else if ("raw".equals(dataParts[2])) {
			String[] typeDataParts = dataParts[3].split("\\.");
			length = new Integer(typeDataParts[0]);
			insertedScheme = Scheme.RAW;
		} else {
			throw new ValidationException(ConstantsResource.INSTANCE.invalidEpcData());
		}

		length = length / 4;

		if (Scheme.GRAI == insertedScheme) {
			String[] typeDataParts = dataParts[4].split("\\.");

			if (typeDataParts.length < 4) {
				throw new ValidationException(ConstantsResource.INSTANCE.invalidEpcData());
			}

			int companyPrefLen = typeDataParts[1].length();
			int itemRefLen = typeDataParts[2].length();

			if ((companyPrefLen + itemRefLen) != 12) {
				throw new ValidationException(ConstantsResource.INSTANCE.invalidCompPrefRefLenngth());
			}
		}

		if (Scheme.RAW == insertedScheme) {
			String[] typeParts = dataParts[3].split("\\.");
			String data = typeParts[1];
			int dataLength = data.length();

			if (!(dataLength > length)) {
				throw new ValidationException(ConstantsResource.INSTANCE.invalidValueLength(length));
			}
		}
	}

	private void handleOneTag(List<TagData> tags, Callback<TagData, Exception> callback) {
		try {
			if (tags.size() > 1) {
				callback.onFailure(new Exception(ConstantsResource.INSTANCE.numberOfTagsSeen(tags.size())));
			} else if (tags.size() < 1) {
				callback.onFailure(new Exception(ConstantsResource.INSTANCE.noTagsFound()));
			} else {
				TagData tagdata = tags.get(0);
				callback.onSuccess(tagdata);
			}
		} catch (Exception exc) {
			callback.onFailure(exc);
		}
	}

	private void init() {
		view.getSerialNumberPanel().setKeepLeadingZeros(false);
		view.getSerialNumberPanel().setMinBound(new BigInteger("0"));
		view.getReferencePanel().setMinBound(new BigInteger("0"));
		view.getValuePanel().setValue("0");
		view.getCompanyPrefixPanel().setValue("0");
		view.getLengthPanel().setAcceptableValues(Arrays.asList(96, 0));
		view.getSchemePanel().setAcceptableValues(Arrays.asList(Scheme.values()));
		view.getSchemePanel().setValue(Scheme.SGTIN, true);
	}

	/*
	 * 
	 * Creating the desired display according to the given scheme
	 * 
	 * @param selectedScheme - the give scheme
	 * 
	 * @param preResetContent - reset widgets or not
	 */
	private void organizeWidgetsAccordingToScheme(Scheme selectedScheme, boolean preResetContent) {
		view.getValuePanel().setLabel(ConstantsResource.INSTANCE.filterLabel());
		view.getCompanyPrefixPanel().setVisible(true);
		view.getSerialNumberPanel().setVisible(false);

		if (preResetContent) {
			view.getCompanyPrefixPanel().setValue("0");

			view.getReferencePanel().setValue(new BigInteger("0"));
			view.getSerialNumberPanel().setValue(new BigInteger("0"));

			view.getLengthPanel().setValue(96);
		}

		switch (selectedScheme) {

		case SGTIN:
			view.getValuePanel().setLabel(ConstantsResource.INSTANCE.filterLabel());
			view.getCompanyPrefixPanel().setVisible(true);
			view.getReferencePanel().setLabel(ConstantsResource.INSTANCE.itemRefLabel());
			view.getReferencePanel().setVisible(true);
			view.getReferencePanel().setMinNoOfDigits(1);
			view.getSerialNumberPanel().setVisible(true);
			view.getSerialNumberPanel().setMinNoOfDigits(1);
			break;

		case SSCC:
			view.getValuePanel().setLabel(ConstantsResource.INSTANCE.filterLabel());
			view.getCompanyPrefixPanel().setVisible(true);
			view.getReferencePanel().setLabel(ConstantsResource.INSTANCE.serReferenceLabel());
			view.getReferencePanel().setVisible(true);
			view.getReferencePanel().setMinNoOfDigits(1);
			view.getSerialNumberPanel().setVisible(false);
			break;

		case GRAI:
			view.getReferencePanel().setLabel(ConstantsResource.INSTANCE.assetTypeLabel());
			view.getReferencePanel().setVisible(true);
			view.getReferencePanel().setMinNoOfDigits(1);
			view.getSerialNumberPanel().setVisible(true);
			view.getSerialNumberPanel().setMinNoOfDigits(1);
			break;

		case GIAI:
			view.getReferencePanel().setLabel(ConstantsResource.INSTANCE.indAssRefLabel());
			view.getReferencePanel().setVisible(true);
			view.getReferencePanel().setMinNoOfDigits(1);

			break;

		case RAW:
			view.getValuePanel().setLabel(ConstantsResource.INSTANCE.valueLabel());
			view.getCompanyPrefixPanel().setVisible(false);
			view.getReferencePanel().setVisible(false);

			if (!view.getValuePanel().getValue().toLowerCase().startsWith("x")) {
				view.getValuePanel().setValue("x");
			}

			break;
		}

	}

	private String generateEpcIdentifierFromWidgets() {
		StringBuffer epcStringText = new StringBuffer();

		currentSelectedScheme = view.getSchemePanel().getValue();

		if (currentSelectedScheme != Scheme.RAW) {
			epcStringText.append("urn:epc:tag:");
			epcStringText.append(currentSelectedScheme.name().toLowerCase());

			epcStringText.append("-" + view.getLengthPanel().getValue());
			epcStringText.append(":" + view.getValuePanel().getValue() + ".");
			epcStringText.append(view.getCompanyPrefixPanel().getValue());
			epcStringText.append(".");
		}

		switch (currentSelectedScheme) {

		case SGTIN:
			epcStringText.append(view.getReferencePanel().getStringValue());
			epcStringText.append(".");
			epcStringText.append(view.getSerialNumberPanel().getStringValue());
			break;

		case SSCC:
			epcStringText.append(view.getReferencePanel().getStringValue());
			break;

		case GRAI:
			int digs = 12 - view.getCompanyPrefixPanel().getValue().toString().length();
			view.getReferencePanel().setMinNoOfDigits(digs);
			epcStringText.append(view.getReferencePanel().getStringValue());
			epcStringText.append(".");
			epcStringText.append(view.getSerialNumberPanel().getStringValue());
			break;

		case GIAI:
			epcStringText.append(view.getReferencePanel().getStringValue());
			break;

		case RAW:
			epcStringText.append("urn:epc:raw:");
			epcStringText.append(view.getLengthPanel().getValue());
			epcStringText.append(".");

			if (view.getValuePanel().getValue().startsWith("x")) {
				epcStringText.append(view.getValuePanel().getValue());
			} else {
				view.getValuePanel().setValue("x" + view.getValuePanel().getValue());
				epcStringText.append(view.getValuePanel().getValue());
			}

			break;
		}

		return epcStringText.toString();
	}

	/**
	 * 
	 * @param uriTag
	 * @param isEpcGlobal
	 */
	private void selectSchemeAccordingToReadTag(String uriTag, boolean isEpcGlobal) {
		try {
			String[] dataContent = null;
			String[] dataPart = uriTag.split(":");
			String schemeString = "RAW";
			String part2 = dataPart[2];

			if (part2.toLowerCase().equals("raw")) {
				if (dataPart.length > 2) {
					dataContent = dataPart[3].split("\\.");

					if (!dataContent[0].equals("96")) {
						view.getLengthPanel().setValue(0, true); // 1:empty
					} else {
						view.getLengthPanel().setValue(96, true); // 0:96
					}
				}
			} else if (part2.toLowerCase().equals("tag")) {
				if (isEpcGlobal) {
					if (dataPart.length > 2) {
						dataContent = dataPart[3].split("-");

						if (!dataContent[1].equals("96")) {
							view.getErrorPanel().showErrorMessage("Length is " + dataContent[1] + " and not 96!");
							view.getLengthPanel().setValue(0); // 1:empty
						} else {
							view.getLengthPanel().setValue(96); // 0:96
						}
						schemeString = dataContent[0];
					}
				}
			} else {
				view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.insufficientUrn());
			}

			Scheme foundScheme = Scheme.valueOf(schemeString.toUpperCase());

			view.getSchemePanel().setValue(foundScheme, true);
			String value;
			
			switch (foundScheme) {

			case SGTIN:
				dataContent = dataPart[4].split("\\.");

				view.getValuePanel().setValue(dataContent[0]);

				view.getCompanyPrefixPanel().setValue(dataContent[1]);

				view.getReferencePanel().setMinNoOfDigits(dataContent[2].length());
				view.getReferencePanel().setValue(new BigInteger(dataContent[2]), true);

				value = dataContent[3];
				
				try {
					BigInteger convertedValue = new BigInteger(value);
					view.getSerialNumberPanel().setValue(convertedValue, true);
				} catch(Exception exc) {
					if(value.startsWith("[")) {
						view.getSerialNumberPanel().setValue(value, true);
					} else {
						throw exc;
					}
				}

				break;
			case SSCC:
				dataContent = dataPart[4].split("\\.");

				view.getValuePanel().setValue(dataContent[0]);
				view.getCompanyPrefixPanel().setValue(dataContent[1]);

				view.getReferencePanel().setMinNoOfDigits(dataContent[2].length());
				
				value = dataContent[2];
				
				try {
					BigInteger convertedValue = new BigInteger(value);
					view.getReferencePanel().setValue(convertedValue);
				} catch(Exception exc) {
					if(value.startsWith("[")) {
						view.getReferencePanel().setValue(value, true);
					} else {
						throw exc;
					}
				}

				break;
			case GRAI:
				dataContent = dataPart[4].split("\\.");

				view.getValuePanel().setValue(dataContent[0]);
				view.getCompanyPrefixPanel().setValue(dataContent[1]);

				if (!Utils.INSTANCE.isNullOrEmpty(dataContent[2])) {
					view.getReferencePanel().setEnabled(true);
					view.getReferencePanel().setMinNoOfDigits(dataContent[2].length());
					view.getReferencePanel().setValue(new BigInteger(dataContent[2]));
				} else {
					view.getReferencePanel().setEnabled(false);
				}

				value = dataContent[3];
				
				try {
					BigInteger convertedValue = new BigInteger(value);
					view.getSerialNumberPanel().setValue(convertedValue);
				} catch(Exception exc) {
					if(value.startsWith("[")) {
						view.getSerialNumberPanel().setValue(value, true);
					} else {
						throw exc;
					}
				}
				
				break;

			case GIAI:
				dataContent = dataPart[4].split("\\.");

				view.getValuePanel().setValue(dataContent[0]);
				view.getCompanyPrefixPanel().setValue(dataContent[1]);

				view.getReferencePanel().setMinNoOfDigits(dataContent[2].length());

				value = dataContent[2];
				
				try {
					BigInteger convertedValue = new BigInteger(value);
					view.getReferencePanel().setValue(convertedValue);
				} catch(Exception exc) {
					if(value.startsWith("[")) {
						view.getReferencePanel().setValue(value, true);
					} else {
						throw exc;
					}
				}
				
				break;

			case RAW:
				view.getValuePanel().setValue(dataContent[1], true);
				break;
			}
		} catch (Exception e) {
			view.getErrorPanel().showErrorMessage(e.getMessage());
		}
	}
	
	public void showErrorMessage(String errorMessage) {
		view.getErrorPanel().showErrorMessage(errorMessage);
	}
}
