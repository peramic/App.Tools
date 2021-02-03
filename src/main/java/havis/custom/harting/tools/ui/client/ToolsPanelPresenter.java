package havis.custom.harting.tools.ui.client;

import havis.custom.harting.tools.ui.client.rfid.RFIDPresenter;
import havis.custom.harting.tools.utils.Utils;
import havis.device.rf.configuration.AntennaConfiguration;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class ToolsPanelPresenter {
	private ToolsPanelView view;
	
	public ToolsPanelPresenter(ToolsPanelView view) {
		this.view = view;
		this.view.setPresenter(this);
	}

	public void loadRfidAntennaConfiguration() {
		Utils.INSTANCE.getService().getAntennaConfigurations(new MethodCallback<List<AntennaConfiguration>>() {
			@Override
			public void onSuccess(Method method, List<AntennaConfiguration> response) {
				RFIDPresenter presenter = view.getRfidSection().getPresenter();
				presenter.setAntennaConfigurations(response);
				
				view.getCreateTagSection().setAvailable(true);
				view.getRfidSection().setAvailable(true);
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				view.getCreateTagSection().setAvailable(false);
				view.getRfidSection().setAvailable(false);
			}
		});
	}
}
