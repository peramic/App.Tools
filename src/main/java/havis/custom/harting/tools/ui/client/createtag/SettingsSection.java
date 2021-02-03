package havis.custom.harting.tools.ui.client.createtag;

import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class SettingsSection extends ConfigurationSection {
	private static SettingsSectionUiBinder uiBinder = GWT
			.create(SettingsSectionUiBinder.class);

	interface SettingsSectionUiBinder extends UiBinder<Widget, SettingsSection> {
	}
	
	@UiField ToggleButton pwdToggleButton;
	@UiField TextBox lockTextBox;

	@UiConstructor
	public SettingsSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("pwdToggleButton")
	public void lockPwdEnable(ClickEvent e) {
		lockTextBox.setVisible(pwdToggleButton.isDown());
	}
	
	public boolean isActivated()  {
		return pwdToggleButton.isDown();
	}	
	
	public String getValue() {
		return lockTextBox.getText();
	}
}
