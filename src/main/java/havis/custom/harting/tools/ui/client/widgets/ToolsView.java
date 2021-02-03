package havis.custom.harting.tools.ui.client.widgets;

import havis.net.ui.shared.client.ErrorPanel;

import com.google.gwt.user.client.ui.IsWidget;

public interface ToolsView extends IsWidget {
	ErrorPanel getErrorPanel();
	ResultPanel getResultPanel();
	public void setEnabled(boolean enabled);
	void closeSection();
}
