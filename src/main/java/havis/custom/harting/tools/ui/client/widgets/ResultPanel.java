package havis.custom.harting.tools.ui.client.widgets;

import havis.custom.harting.tools.utils.UiMisc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ResultPanel extends Composite {

	private static ResultPanelUiBinder uiBinder = GWT.create(ResultPanelUiBinder.class);

	@UiField
	TextBox resultTextBox;
	
	interface ResultPanelUiBinder extends UiBinder<Widget, ResultPanel> {
	}

	public ResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public String getResult() {
		return resultTextBox.getText();
	}
	
	public void setResult(String result) {
		resultTextBox.setText(result);
		UiMisc.INSTANCE.highlight(resultTextBox);
	}
}
