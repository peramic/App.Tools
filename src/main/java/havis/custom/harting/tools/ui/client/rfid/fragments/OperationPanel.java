package havis.custom.harting.tools.ui.client.rfid.fragments;

import havis.custom.harting.tools.model.Operation;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox.OptionRenderer;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.Widget;

public class OperationPanel extends Composite implements HasConstrainedValue<Operation>, HasFocusHandlers {

	private static OperationPanelUiBinder uiBinder = GWT.create(OperationPanelUiBinder.class);

	@UiField(provided = true)
	CustomListBox<Operation> operationTypeList = new CustomListBox<Operation>(new OptionRenderer<Operation>() {
		@Override
		public String toOptionValue(Operation t) {
			return t.name();
		}

		@Override
		public String toOptionItem(Operation t) {
			return t.label();
		}

		@Override
		public Operation fromOptionValue(String optionValue) {
			return Operation.valueOf(optionValue);
		}
	});

	interface OperationPanelUiBinder extends UiBinder<Widget, OperationPanel> {
	}

	public OperationPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public OperationPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Operation getValue() {
		return operationTypeList.getValue();
	}

	@Override
	public void setValue(Operation value) {
		operationTypeList.setValue(value, true);
	}

	@Override
	public void setValue(Operation value, boolean fireEvents) {
		operationTypeList.setValue(value, fireEvents);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Operation> handler) {
		return operationTypeList.addValueChangeHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Operation> values) {
		operationTypeList.setAcceptableValues(values);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return operationTypeList.addDomHandler(handler, FocusEvent.getType());
	}
}