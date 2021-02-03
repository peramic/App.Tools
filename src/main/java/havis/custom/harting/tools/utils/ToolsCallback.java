package havis.custom.harting.tools.utils;

import havis.custom.harting.tools.ui.client.widgets.ToolsView;
import havis.device.rf.tag.TagData;

import com.google.gwt.core.client.Callback;

public abstract class ToolsCallback implements Callback<TagData, Exception> {

	private ToolsView view;

	public ToolsCallback(ToolsView view) {
		this.view = view;
	}
	
	@Override
	public void onSuccess(TagData tagData) {
		view.setEnabled(true);
	}
	
	@Override
	public void onFailure(Exception reason) {
		view.setEnabled(true);
		view.getErrorPanel().showErrorMessage(reason.getMessage());
		view.getResultPanel().setResult(Utils.FAILURE);
	}
}
