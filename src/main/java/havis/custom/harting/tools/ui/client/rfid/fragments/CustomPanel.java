package havis.custom.harting.tools.ui.client.rfid.fragments;

import havis.custom.harting.tools.model.Field;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox;
import havis.custom.harting.tools.ui.client.widgets.CustomListBox.OptionRenderer;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

public class CustomPanel extends Composite implements HasConstrainedValue<Field>, HasFocusHandlers {

	private static CustomPanelUiBinder uiBinder = GWT.create(CustomPanelUiBinder.class);

	@UiField(provided = true)
	CustomListBox<Field> bankList = new CustomListBox<Field>(new OptionRenderer<Field>() {
		@Override
		public String toOptionValue(Field t) {
			return t.name();
		}

		@Override
		public String toOptionItem(Field t) {
			return t.label(true);
		}

		@Override
		public Field fromOptionValue(String optionValue) {
			return Field.valueOf(optionValue);
		}
	});
	
	@UiField
	IntegerBox lengthIntegerBox;
	
	@UiField
	IntegerBox offsetIntegerBox;
	
	interface CustomPanelUiBinder extends UiBinder<Widget, CustomPanel> {
	}

	public CustomPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public CustomPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasValue<Integer> getLength() {
		return lengthIntegerBox;
	}

	public HasValue<Integer> getOffset() {
		return offsetIntegerBox;
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return bankList.addDomHandler(handler, FocusEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Field> handler) {
		return bankList.addValueChangeHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Field> values) {
		bankList.setAcceptableValues(values);
	}

	@Override
	public Field getValue() {
		return bankList.getValue();
	}

	@Override
	public void setValue(Field value) {
		bankList.setValue(value);
	}

	@Override
	public void setValue(Field value, boolean fireEvents) {
		bankList.setValue(value, fireEvents);
	}
}