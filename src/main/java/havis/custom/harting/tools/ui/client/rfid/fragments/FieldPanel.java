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
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

public class FieldPanel extends Composite implements HasConstrainedValue<Field>, HasFocusHandlers, HasEnabled {

	private static FieldPanelUiBinder uiBinder = GWT.create(FieldPanelUiBinder.class);
	
	@UiField
	CustomPanel customPanel;
	
	@UiField(provided = true)
	CustomListBox<Field> fieldList = new CustomListBox<Field>(new OptionRenderer<Field>() {
		@Override
		public String toOptionValue(Field t) {
			return t.name();
		}

		@Override
		public String toOptionItem(Field t) {
			return t.label(false);
		}

		@Override
		public Field fromOptionValue(String optionValue) {
			return Field.valueOf(optionValue);
		}
	});

	interface FieldPanelUiBinder extends UiBinder<Widget, FieldPanel> {
	}

	public FieldPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public FieldPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return fieldList.addDomHandler(handler, FocusEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Field> handler) {
		return fieldList.addValueChangeHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Field> values) {
		fieldList.setAcceptableValues(values);
	}

	@Override
	public Field getValue() {
		return fieldList.getValue();
	}

	@Override
	public void setValue(Field value) {
		fieldList.setValue(value, true);
	}

	@Override
	public void setValue(Field value, boolean fireEvents) {
		fieldList.setValue(value, fireEvents);
	}
	
	public void setCustomPanelVisible(boolean visible) {
		customPanel.setVisible(visible);
	}
	
	public CustomPanel getCustomPanel() {
		return customPanel;
	}

	@Override
	public boolean isEnabled() {
		return fieldList.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		fieldList.setEnabled(enabled);
	}
}
