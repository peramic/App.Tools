package havis.custom.harting.tools.ui.client;

import havis.custom.harting.tools.ui.client.createtag.CreateTag;
import havis.custom.harting.tools.ui.client.rfid.RFIDPanel;

import com.google.gwt.user.client.ui.IsWidget;

public interface ToolsPanelView extends IsWidget {
	void setPresenter(ToolsPanelPresenter presenter);
	RFIDPanel getRfidSection();
	CreateTag getCreateTagSection();
}
