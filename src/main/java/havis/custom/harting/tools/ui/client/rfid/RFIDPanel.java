package havis.custom.harting.tools.ui.client.rfid;

import havis.custom.harting.tools.model.Field;
import havis.custom.harting.tools.model.Operation;
import havis.custom.harting.tools.ui.client.rfid.fragments.DataPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.FieldPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.OperationPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.PasswordPanel;
import havis.custom.harting.tools.ui.client.widgets.ResultPanel;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.custom.harting.tools.utils.UiMisc;
import havis.net.ui.shared.client.ConfigurationSection;
import havis.net.ui.shared.client.list.WidgetList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class RFIDPanel extends ConfigurationSection implements RFIDView {

	private RFIDPresenter presenter;
	private boolean available;

	private static RFIDUiBinder uiBinder = GWT.create(RFIDUiBinder.class);

	@UiField
	HTMLPanel effect;
	
	@UiField
	OperationPanel operationPanel;
	
	@UiField
	FieldPanel fieldPanel;
	
	@UiField
	PasswordPanel passwordPanel;
	
	@UiField
	DataPanel dataPanel;
	
	@UiField
	ResultPanel resultPanel;
	
	@UiField
	WidgetList tagsList;
	
	@UiField
	Button inventoryButton;
	
	@UiField
	Button executeButton;
	
	interface RFIDUiBinder extends UiBinder<Widget, RFIDPanel> {
	}

	@UiConstructor
	public RFIDPanel(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("inventoryButton")
	public void onInventory(ClickEvent ce) {
		presenter.onInventory();
	}

	@UiHandler("executeButton")
	public void onExecute(ClickEvent ce) {
		presenter.onExecute();
	}

	@UiHandler("fieldPanel")
	public void onFieldChanged(ValueChangeEvent<Field> vce) {
		presenter.onFieldSelected();
	}

	@UiHandler("operationPanel")
	public void onOperationTypeChanged(ValueChangeEvent<Operation> vce) {
		presenter.onOperationTypeSelected();
	}

	@Override
	public void setPresenter(RFIDPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public RFIDPresenter getPresenter() {
		return presenter;
	}

	@Override
	public OperationPanel getOperationPanel() {
		return operationPanel;
	}

	@Override
	public FieldPanel getFieldPanel() {
		return fieldPanel;
	}

	@Override
	public PasswordPanel getPasswordPanel() {
		return passwordPanel;
	}

	@Override
	public DataPanel getDataPanel() {
		return dataPanel;
	}

	@Override
	public ResultPanel getResultPanel() {
		return resultPanel;
	}

	@Override
	public WidgetList getTagsList() {
		return tagsList;
	}
	
	@Override
	public void closeSection() {
		setOpen(false);
		setEnabled(true);
	}
	
	@Override
	protected void onOpenSection() {
		if(!available) {
			this.setOpen(false);
			presenter.showErrorMessage(ConstantsResource.INSTANCE.unavailableError());
		}
		else {
			super.onOpenSection();
			presenter.loadAntennaConfiguration();
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		effect.setStyleName(UiMisc.INSTANCE.getEffectName(enabled));
		inventoryButton.setEnabled(enabled);
		executeButton.setEnabled(enabled);
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean isAvailable() {
		return this.available;
	}
}