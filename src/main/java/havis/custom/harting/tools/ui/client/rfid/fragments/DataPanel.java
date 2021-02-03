package havis.custom.harting.tools.ui.client.rfid.fragments;

import havis.custom.harting.tools.ui.client.widgets.CustomListBox;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox.OptionRenderer;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.device.rf.tag.operation.LockOperation.Privilege;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class DataPanel extends Composite implements HasConstrainedValue<Privilege>, HasEnabled {

	private static DataPanelUiBinder uiBinder = GWT.create(DataPanelUiBinder.class);
	private ConstantsResource resource = ConstantsResource.INSTANCE;

	interface DataPanelUiBinder extends UiBinder<Widget, DataPanel> {
	}

	@UiField
	TextArea dataTextBox;
	
	@UiField
	Label xLabel;

	@UiField(provided = true)
	CustomListBox<Privilege> privilegeList = new CustomListBox<Privilege>(new OptionRenderer<Privilege>() {
		@Override
		public String toOptionValue(Privilege t) {
			return t.name();
		}

		@Override
		public String toOptionItem(Privilege t) {
			String label;

			switch (t) {
			case LOCK:
				label = resource.lockLockLabel();
				break;
			case UNLOCK:
				label = resource.lockUnlockLabel();
				break;
			case PERMALOCK:
				label = resource.lockPermalockLabel();
				break;
			case PERMAUNLOCK:
				label = resource.lockPermaunlockLabel();
				break;
			default:
				label = "";
				break;
			}

			return label;
		}

		@Override
		public Privilege fromOptionValue(String optionValue) {
			return Privilege.valueOf(optionValue);
		}
	});

	public DataPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DataPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public String getData() {
		return dataTextBox.getText();
	}

	public void setData(String data) {
		dataTextBox.setText(data);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Privilege> handler) {
		return privilegeList.addValueChangeHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Privilege> values) {
		privilegeList.setAcceptableValues(values);
	}

	@Override
	public Privilege getValue() {
		return privilegeList.getValue();
	}

	@Override
	public void setValue(Privilege value) {
		privilegeList.setValue(value);
	}

	@Override
	public void setValue(Privilege value, boolean fireEvents) {
		privilegeList.setValue(value, fireEvents);
	}

	public void setPrivilegeActive(boolean active) {
		privilegeList.setVisible(active);
		dataTextBox.setVisible(!active);
		xLabel.setVisible(dataTextBox.isVisible());
	}

	public TextArea getTextArea() {
		return dataTextBox;
	}
	
	@Override
	public boolean isEnabled() {
		return dataTextBox.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		dataTextBox.setEnabled(enabled);
	}
}
