package havis.net.ui.core.client;

import havis.custom.harting.tools.ui.client.ToolsPanel;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class WebUI extends Composite implements EntryPoint {

	private ResourceBundle res = ResourceBundle.INSTANCE;
	private havis.custom.harting.tools.ui.resourcebundle.ResourceBundle webres = havis.custom.harting.tools.ui.resourcebundle.ResourceBundle.INSTANCE;

	private static WebUIUiBinder uiBinder = GWT
			.create(WebUIUiBinder.class);
	
	@UiField FlowPanel container;
	@UiField ToolsPanel toolsPanel;

	@UiTemplate("WebUI.ui.xml")
	interface WebUIUiBinder extends UiBinder<Widget, WebUI> {
	}

	public WebUI() {
		initWidget(uiBinder.createAndBindUi(this));
		Defaults.setDateFormat(null);
		ensureInjection();
	}

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(this);
		Defaults.setByteArraysToHexString(true);
	}
		
	private void ensureInjection() {
		res.css().ensureInjected();
		webres.css().ensureInjected();
	}
}