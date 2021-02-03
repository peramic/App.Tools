package havis.custom.harting.tools.ui.client.createtag.fragments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ValuePanel extends Composite implements HasValue<String>, HasFocusHandlers, HasKeyDownHandlers {

	private static ValuePanelUiBinder uiBinder = GWT.create(ValuePanelUiBinder.class);

	interface ValuePanelUiBinder extends UiBinder<Widget, ValuePanel> {
	}
	
	@UiField
	TextBox valueTextBox;
	
	@UiField
	Label valueLabel;

	public ValuePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return valueTextBox.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return valueTextBox.addFocusHandler(handler);
	}

	@Override
	public String getValue() {
		return valueTextBox.getValue();
	}

	@Override
	public void setValue(String value) {
		valueTextBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		valueTextBox.setValue(value, fireEvents);
	}
	
	public void setLabel(String label) {
		valueLabel.setText(label);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return valueTextBox.addKeyDownHandler(handler);
	}
}