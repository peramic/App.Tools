package havis.custom.harting.tools.ui.client.rfid;

import havis.custom.harting.tools.model.Field;
import havis.custom.harting.tools.model.Operation;
import havis.custom.harting.tools.model.OperationParameter;
import havis.custom.harting.tools.ui.client.widgets.TagRow;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.custom.harting.tools.utils.OperationCreator;
import havis.custom.harting.tools.utils.UiMisc;
import havis.custom.harting.tools.utils.Utils;
import havis.device.rf.configuration.AntennaConfiguration;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.LockOperation.Privilege;
import havis.device.rf.tag.operation.TagOperation;
import havis.device.rf.tag.result.KillResult;
import havis.device.rf.tag.result.LockResult;
import havis.device.rf.tag.result.OperationResult;
import havis.device.rf.tag.result.ReadResult;
import havis.device.rf.tag.result.WriteResult;
import havis.middleware.tdt.DataTypeConverter;
import havis.middleware.tdt.TdtTranslationException;
import havis.net.rest.rf.async.RFDeviceServiceAsync;
import havis.net.ui.shared.client.list.WidgetList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.validation.ValidationException;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.TextArea;

public class RFIDPresenter {

	private RFIDView view;
	private HashMap<String, TagRow> tagsMap = new HashMap<String, TagRow>();
	private ConstantsResource resource = ConstantsResource.INSTANCE;
	private List<AntennaConfiguration> antennaConfigurations;

	public RFIDPresenter(final RFIDView view) {
		this.view = view;
		this.view.setPresenter(this);
		init();
	}

	public void onFieldSelected() {
		Field field = view.getFieldPanel().getValue();

		view.getFieldPanel().setCustomPanelVisible(field == Field.CUSTOM);
	}

	public void onOperationTypeSelected() {
		Operation operation = view.getOperationPanel().getValue();

		view.getDataPanel().setEnabled(operation != Operation.READ);
		view.getDataPanel().setPrivilegeActive(operation == Operation.LOCK);

		// remember selected field
		Field field = view.getFieldPanel().getValue();
		// initialize default field list
		List<Field> fieldList = Arrays.asList(Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
		List<Field> workaround = new ArrayList<Field>(fieldList);

		// add also custom entry when operation is set to READ or WRITE
		if ((operation == Operation.READ) || (operation == Operation.WRITE)) {
			workaround.add(Field.CUSTOM);
		}

		view.getFieldPanel().setAcceptableValues(workaround);
		view.getFieldPanel().setValue(field == null ? Field.EPC_MEMORY : field);
		view.getFieldPanel().setEnabled(Operation.KILL != operation);
	}

	public void onInventory() {
		//view.setEnabled(false);
		
		try {	
			if ((antennaConfigurations != null) && (antennaConfigurations.size() > 0)) {
				clearInventory();
				inventory();
			} else {
				view.closeSection();
				view.getErrorPanel().showErrorMessage(resource.noAntennaConfigurationAvailable());
				view.getResultPanel().setResult("");
			}
		} catch (Exception exc) {
			view.getErrorPanel().showErrorMessage(exc.getMessage());
			view.getResultPanel().setResult("");
			view.setEnabled(true);
		}
	}

	public void onExecute() {
		//view.setEnabled(false);
		
		try {
			if (antennaConfigurations != null) {
				OperationParameter rfidParameter = OperationCreator.INSTANCE.getRfidOperation(view);
				String epc = rfidParameter.getEpc();
				TagOperation operation = OperationCreator.INSTANCE.getTagOperation(rfidParameter);
				List<TagOperation> tagOperations = new ArrayList<TagOperation>();
				tagOperations.add(operation);
				
				execute(epc, tagOperations);
			} else {
				view.closeSection();
				view.getErrorPanel().showErrorMessage(resource.noAntennaConfigurationAvailable());				
			}
		} catch (Exception exc) {
			view.getErrorPanel().showErrorMessage(exc.getMessage());
			view.setEnabled(true);			
		}
	}

	public void resetService(RFDeviceServiceAsync service) {
		Utils.INSTANCE.setService(service);
	}
	
	public void setAntennaConfigurations(List<AntennaConfiguration> antennaConfigurations) {
		this.antennaConfigurations = antennaConfigurations;
	}
	
	private void clearInventory() {
		view.getTagsList().clear();
		tagsMap.clear();
	}

	private void init() {
		// set selectable operation types
		view.getOperationPanel().setAcceptableValues(Arrays.asList(Operation.values()));
		// Set operation type to READ after initializing the operation list and
		// fire VaĺueChangedEvent to reinitialize the field list
		view.getOperationPanel().setValue(Operation.READ);

		// initialize the custom field 'bank'
		view.getFieldPanel().getCustomPanel()
				.setAcceptableValues(Arrays.asList(Field.RESERVED, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY));
		// set selected memory field to EPC
		view.getFieldPanel().getCustomPanel().setValue(Field.EPC_MEMORY);

		view.getDataPanel().setAcceptableValues(Arrays.asList(Privilege.values()));
	}

	private void resetFound() {
		for (Entry<String, TagRow> entry : tagsMap.entrySet()) {
			entry.getValue().reset();
		}
	}

	private void addTags(List<TagData> tags) throws ValidationException, TdtTranslationException {
		HashSet<String> epcs = new HashSet<String>();
		resetFound();
		if (tags != null) {
			for (TagData tagdata : tags) {
				StringBuilder s = new StringBuilder(tagdata.getEpc().length);
				for (byte b : tagdata.getEpc()) {
					s.append(Integer.toHexString(b));
				}
				String epcString = s.toString();
				TagRow row = tagsMap.get(epcString);
				epcs.add(epcString);

				if (row == null) {
					row = new TagRow(tagdata, antennaConfigurations.size());
					view.getTagsList().addItem(row.getWidgets());
					tagsMap.put(epcString, row);
				} else {
					row.setRSSI(tagdata.getAntennaID(), tagdata.getRssi());
				}
			}
			view.getTagsList().setSelected(0);
			view.getResultPanel().setResult(Utils.SUCCESS);
		} else {
			view.getResultPanel().setResult(ConstantsResource.INSTANCE.emptyTagsList());
		}
	}

	private void displayExecuteResult(List<TagData> executionResult) {

		if (executionResult.size() > 0) {
			TagData tagData = executionResult.get(0);

			OperationResult operationResult = null;
			List<OperationResult> operationResults = tagData.getResultList();

			if (operationResults.size() > 1) {
				operationResult = operationResults.get(1);
			} else if (tagData.getResultList().size() > 0) {
				operationResult = operationResults.get(0);
			}

			if (operationResult != null) {
				String result = "";
				String data = view.getDataPanel().getData();

				if (operationResult instanceof ReadResult) {
					ReadResult readResult = (ReadResult) operationResult;
					result = readResult.getResult().name();
					data = DataTypeConverter.byteArrayToHexString(readResult.getReadData());
				} else if (operationResult instanceof WriteResult) {
					WriteResult readResult = (WriteResult) operationResult;
					result = readResult.getResult().name();
				} else if (operationResult instanceof LockResult) {
					LockResult readResult = (LockResult) operationResult;
					result = readResult.getResult().name();
				} else if (operationResult instanceof KillResult) {
					KillResult readResult = (KillResult) operationResult;
					result = readResult.getResult().name();
				}

				data = data.replace("x", "").replace("X", "");
				
				view.getResultPanel().setResult(result);
				view.getDataPanel().setData(data);
				resizeDataPanel();
			}
		} else {
			view.getResultPanel().setResult(ConstantsResource.INSTANCE.emptyTagsList());
		}
	}
	
	private void resizeDataPanel() {
		TextArea dataText = view.getDataPanel().getTextArea();
		if(dataText.getElement().getScrollHeight() > 35 ) {
			dataText.setHeight("35px");
			dataText.setHeight(dataText.getElement().getScrollHeight() + "px");
			view.getDataPanel().getTextArea().getElement().getScrollHeight();
		}
	}

	private void inventory() {
		Utils.INSTANCE.getService().getTags(new MethodCallback<List<TagData>>() {
			@Override
			public void onSuccess(Method method, List<TagData> response) {
				view.setEnabled(true);

				try {
					if (response.size() > 0) {
						addTags(response);
					} else {
						view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.noTagsFound());
					}
				} catch (Exception exc) {
					view.getErrorPanel().showErrorMessage(exc.getMessage());
					view.getResultPanel().setResult("");
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.setEnabled(true);
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
				view.getResultPanel().setResult("");
			}
		});
	}

	private void execute(String epc, List<TagOperation> operations) {
		Utils.INSTANCE.getService().getTags(epc, operations, new MethodCallback<List<TagData>>() {
			@Override
			public void onSuccess(Method method, List<TagData> response) {
				view.setEnabled(true);
				displayExecuteResult(response);
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.setEnabled(true);
				UiMisc.INSTANCE.handleError(method.getResponse().getStatusCode(), view);
			}
		});
	}

	public void loadAntennaConfiguration() {
		WidgetList l = view.getTagsList();
		
		l.removeHeader();
		l.addHeaderCell(resource.transponderId());
		
		for(int i = 1; i <= antennaConfigurations.size(); i++) {
			l.addHeaderCell(resource.rssi() + " " + resource.antenna() + " " + i);
		}
	}
	
	public void showErrorMessage(String errorMessage) {
		view.getErrorPanel().showErrorMessage(errorMessage);
	}
}