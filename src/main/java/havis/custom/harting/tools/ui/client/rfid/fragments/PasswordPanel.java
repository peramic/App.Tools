package havis.custom.harting.tools.ui.client.rfid.fragments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PasswordPanel extends Composite implements HasValueChangeHandlers<Boolean> {

	private static PasswordPanelUiBinder uiBinder = GWT.create(PasswordPanelUiBinder.class);

	interface PasswordPanelUiBinder extends UiBinder<Widget, PasswordPanel> {
	}

	@UiField
	ToggleButton toggleButton;

	@UiField
	TextBox password;

	public PasswordPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		//toggleButton.setEnabled(false);
	}

	public PasswordPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public String getPassword() {
		return password.getText();
	}

	public void resetPassword() {
		password.setText("");
	}
	
	@UiHandler("toggleButton")
	public void onPasswordToggledButtonClicked(ClickEvent ce) {
		password.setVisible(toggleButton.getValue());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return toggleButton.addValueChangeHandler(handler);
	}

	public boolean isActivated() {
		return toggleButton.isDown();
	}
}
