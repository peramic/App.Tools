package havis.custom.harting.tools.ui.client.createtag.fragments;

import java.math.BigInteger;

import havis.custom.harting.tools.ui.client.widgets.PlusMinusBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class SerialNumberPanel extends Composite implements HasValue<BigInteger>, HasFocusHandlers, HasChangeHandlers {
	
	private boolean valueChangeHandlerInitialized;
	private static SerNumPanelUiBinder uiBinder = GWT.create(SerNumPanelUiBinder.class);

	interface SerNumPanelUiBinder extends UiBinder<Widget, SerialNumberPanel> {
	}
	
	@UiField
	PlusMinusBox serNumberPlusMinusBox;

	public SerialNumberPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BigInteger> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				public void onChange(final ChangeEvent event) {
					ValueChangeEvent.fire(SerialNumberPanel.this, getValue());
				}
			});
		}

		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return serNumberPlusMinusBox.getBox().addFocusHandler(handler);
	}
	
	public String getStringValue() {
		return serNumberPlusMinusBox.getText();
	}

	@Override
	public BigInteger getValue() {
		return serNumberPlusMinusBox.getValue();
	}

	@Override
	public void setValue(BigInteger value) {
		serNumberPlusMinusBox.setValue(value);
	}

	public void setValue(String value) {
		serNumberPlusMinusBox.setValue(value, true);
	}

	@Override
	public void setValue(BigInteger value, boolean fireEvents) {
		setValue(value);
		
		if(fireEvents) {
			 ValueChangeEvent.fire(this, value);
		}
	}
	
	public void setValue(String value, boolean fireEvents) {
		setValue(value);
		
		if(fireEvents) {
			 ValueChangeEvent.fire(this, getValue());
		}
	}
	
	public void setKeepLeadingZeros(boolean value) {
		serNumberPlusMinusBox.setKeepLeadingZeros(value);
	}
	
	public void setMinBound(BigInteger value) {
		serNumberPlusMinusBox.setMinBound(value);
	}

	public void setMinNoOfDigits(int value) {
		serNumberPlusMinusBox.setMinNoOfDigits(value, false);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		serNumberPlusMinusBox.setChangeHandler(handler);
		return addDomHandler(handler, ChangeEvent.getType());
	}
}
