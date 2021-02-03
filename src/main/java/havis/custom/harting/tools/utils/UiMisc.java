package havis.custom.harting.tools.utils;

import havis.custom.harting.tools.ui.client.widgets.ToolsView;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.custom.harting.tools.ui.resourcebundle.ResourceBundle;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;

public class UiMisc {

	public static final UiMisc INSTANCE = new UiMisc();

	private UiMisc() {

	}

	public void highlight(final TextBox uiObject) {
		if (Utils.SUCCESS.equals(uiObject.getValue())) {
			uiObject.addStyleName(ResourceBundle.INSTANCE.css().textSuccess());
			uiObject.removeStyleName(ResourceBundle.INSTANCE.css().textError());
		} else {
			uiObject.addStyleName(ResourceBundle.INSTANCE.css().textError());
			uiObject.removeStyleName(ResourceBundle.INSTANCE.css().textSuccess());
		}

		Timer timer = new Timer() {
			@Override
			public void run() {
				uiObject.removeStyleName(ResourceBundle.INSTANCE.css().textSuccess());
				uiObject.removeStyleName(ResourceBundle.INSTANCE.css().textError());
			}
		};

		timer.schedule(2500);
	}
	
	public String getEffectName(boolean enabled) {
		return enabled ? ResourceBundle.INSTANCE.css().effectOff() : ResourceBundle.INSTANCE.css().effectOn(); 
	}

	public void handleError(int httpStatus, ToolsView view) {
		switch (httpStatus) {
		case 400:
			view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.parameterError());
			break;
		case 404:
			view.closeSection();
			break;
		case 423:
			view.closeSection();
			view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.lockedError());
			break;
		case 500:
			view.closeSection();
			view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.unexpectedError());
			break;
		case 502:
			view.closeSection();
			view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.connectionError());
			break;
		case 503:
			view.closeSection();
			view.getErrorPanel().showErrorMessage(ConstantsResource.INSTANCE.unavailableError());
			break;
		default:
			break;
		}
	}
}
