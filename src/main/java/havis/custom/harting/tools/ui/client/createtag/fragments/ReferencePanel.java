package havis.custom.harting.tools.ui.client.createtag.fragments;

import java.math.BigInteger;

import havis.custom.harting.tools.ui.client.widgets.PlusMinusBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ReferencePanel extends Composite implements HasValue<BigInteger>, HasChangeHandlers, HasEnabled {

	private boolean valueChangeHandlerInitialized;
	private static ItemRefPanelUiBinder uiBinder = GWT.create(ItemRefPanelUiBinder.class);

	interface ItemRefPanelUiBinder extends UiBinder<Widget, ReferencePanel> {
	}

	@UiField
	PlusMinusBox referencePlusMinusBox;

	@UiField
	Label referenceLabel;

	public ReferencePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BigInteger> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				public void onChange(final ChangeEvent event) {
					ValueChangeEvent.fire(ReferencePanel.this, getValue());
				}
			});
		}

		return addHandler(handler, ValueChangeEvent.getType());
	}

	public String getStringValue() {
		return referencePlusMinusBox.getText();
	}
	
	@Override
	public BigInteger getValue() {
		return referencePlusMinusBox.getValue();
	}

	@Override
	public void setValue(BigInteger value) {
		referencePlusMinusBox.setValue(value);
	}
	
	public void setValue(String value) {
		referencePlusMinusBox.setValue(value, true);
	}

	@Override
	public void setValue(BigInteger value, boolean fireEvents) {
		referencePlusMinusBox.setValue(value);
		
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

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		referencePlusMinusBox.setChangeHandler(handler);
		return addDomHandler(handler, ChangeEvent.getType());
	}

	public void setLabel(String label) {
		referenceLabel.setText(label);
	}

	public void setMinBound(BigInteger value) {
		referencePlusMinusBox.setMinBound(value);
	}

	public void setMinNoOfDigits(int value) {
		referencePlusMinusBox.setMinNoOfDigits(value, false);
	}

	@Override
	public boolean isEnabled() {
		return referencePlusMinusBox.isFrozen();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if(enabled) {
			referencePlusMinusBox.unfreeze();
		} else {
			referencePlusMinusBox.freeze();
		}
	}
	
	
}
