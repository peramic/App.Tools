package havis.custom.harting.tools.ui.client.createtag.fragments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EpcPanel extends Composite implements HasValue<String>, HasFocusHandlers {

	private static EpcPanelUiBinder uiBinder = GWT.create(EpcPanelUiBinder.class);

	interface EpcPanelUiBinder extends UiBinder<Widget, EpcPanel> {
	}
	
	@UiField
	TextBox epcStringTextBox;

	public EpcPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getValue() {
		return epcStringTextBox.getText().trim();
	}

	@Override
	public void setValue(String value) {
		epcStringTextBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		epcStringTextBox.setValue(value, fireEvents);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return epcStringTextBox.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return epcStringTextBox.addFocusHandler(handler);
	}
}
