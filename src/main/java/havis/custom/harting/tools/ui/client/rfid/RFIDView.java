package havis.custom.harting.tools.ui.client.rfid;

import havis.custom.harting.tools.ui.client.rfid.fragments.DataPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.FieldPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.OperationPanel;
import havis.custom.harting.tools.ui.client.rfid.fragments.PasswordPanel;
import havis.custom.harting.tools.ui.client.widgets.ResultPanel;
import havis.custom.harting.tools.ui.client.widgets.ToolsView;
import havis.net.ui.shared.client.list.WidgetList;

public interface RFIDView extends ToolsView {
	void setPresenter(RFIDPresenter presenter);
	
	RFIDPresenter getPresenter();

	OperationPanel getOperationPanel();
	
	FieldPanel getFieldPanel();
	
	PasswordPanel getPasswordPanel();
	
	DataPanel getDataPanel();
	
	ResultPanel getResultPanel();
	
	WidgetList getTagsList();
}
