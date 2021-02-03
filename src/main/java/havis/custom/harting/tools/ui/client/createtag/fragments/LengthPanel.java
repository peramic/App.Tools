package havis.custom.harting.tools.ui.client.createtag.fragments;

import havis.custom.harting.tools.ui.client.widgets.CustomListBox;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox.OptionRenderer;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.Widget;

public class LengthPanel extends Composite implements HasConstrainedValue<Integer>, HasFocusHandlers {

	private static LengthPanelUiBinder uiBinder = GWT.create(LengthPanelUiBinder.class);
	
	interface LengthPanelUiBinder extends UiBinder<Widget, LengthPanel> {
	}
	
	@UiField(provided = true)
	CustomListBox<Integer> lengthListBox = new CustomListBox<Integer>(new OptionRenderer<Integer>() {
		@Override
		public String toOptionValue(Integer t) {
			return t.toString();
		}

		@Override
		public String toOptionItem(Integer t) {
			return t > 0 ? t.toString() : "";
		}

		@Override
		public Integer fromOptionValue(String optionValue) {
			return Integer.parseInt(optionValue);
		}
	});

	public LengthPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Integer getValue() {
		return lengthListBox.getValue();
	}

	@Override
	public void setValue(Integer value) {
		lengthListBox.setValue(value);
	}

	@Override
	public void setValue(Integer value, boolean fireEvents) {
		lengthListBox.setValue(value, fireEvents);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> handler) {
		return lengthListBox.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return lengthListBox.addFocusHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Integer> values) {
		lengthListBox.setAcceptableValues(values);
	}
}
