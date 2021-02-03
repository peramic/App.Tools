package havis.custom.harting.tools.ui.client;

import havis.custom.harting.tools.ui.client.createtag.CreateTag;
import havis.custom.harting.tools.ui.client.createtag.CreateTagPresenter;
import havis.custom.harting.tools.ui.client.rfid.RFIDPanel;
import havis.custom.harting.tools.ui.client.rfid.RFIDPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ToolsPanel extends Composite implements ToolsPanelView {

	private ToolsPanelPresenter presenter = new ToolsPanelPresenter(this);
	private static ToolsUiBinder uiBinder = GWT.create(ToolsUiBinder.class);

	interface ToolsUiBinder extends UiBinder<Widget, ToolsPanel> {
	}
	
	@UiField
	RFIDPanel rfidSection;
	
	@UiField
	CreateTag createTagSection;

	public ToolsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		new RFIDPresenter(rfidSection);
		new CreateTagPresenter(createTagSection);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.loadRfidAntennaConfiguration();
	}

	@Override
	public void setPresenter(ToolsPanelPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public RFIDPanel getRfidSection() {
		return rfidSection;
	}

	@Override
	public CreateTag getCreateTagSection() {
		return createTagSection;
	}
}