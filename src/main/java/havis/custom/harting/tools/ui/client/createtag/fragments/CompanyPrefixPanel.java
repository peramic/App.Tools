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

public class CompanyPrefixPanel extends Composite implements HasValue<String>, HasFocusHandlers {

	private static CompanyPrefixPanelUiBinder uiBinder = GWT.create(CompanyPrefixPanelUiBinder.class);

	interface CompanyPrefixPanelUiBinder extends UiBinder<Widget, CompanyPrefixPanel> {
	}
	
	@UiField
	TextBox companyPrefixTextBox;

	public CompanyPrefixPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return companyPrefixTextBox.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		return companyPrefixTextBox.getValue();
	}

	@Override
	public void setValue(String value) {
		companyPrefixTextBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		companyPrefixTextBox.setValue(value, fireEvents);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return companyPrefixTextBox.addFocusHandler(handler);
	}

}
