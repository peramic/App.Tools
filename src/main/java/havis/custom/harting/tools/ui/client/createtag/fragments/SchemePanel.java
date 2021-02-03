package havis.custom.harting.tools.ui.client.createtag.fragments;

import havis.custom.harting.tools.model.Scheme;
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

public class SchemePanel extends Composite implements HasConstrainedValue<Scheme>, HasFocusHandlers {

	private static SchemePanelUiBinder uiBinder = GWT.create(SchemePanelUiBinder.class);

	interface SchemePanelUiBinder extends UiBinder<Widget, SchemePanel> {
	}

	@UiField(provided = true)
	CustomListBox<Scheme> schemeTypeListBox = new CustomListBox<Scheme>(new OptionRenderer<Scheme>() {
		@Override
		public String toOptionValue(Scheme t) {
			return t.name();
		}

		@Override
		public String toOptionItem(Scheme t) {
			return t.label();
		}

		@Override
		public Scheme fromOptionValue(String optionValue) {
			return Scheme.valueOf(optionValue);
		}
	});
	
	public SchemePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Scheme getValue() {
		return schemeTypeListBox.getValue();
	}

	@Override
	public void setValue(Scheme value) {
		schemeTypeListBox.setValue(value);
	}

	@Override
	public void setValue(Scheme value, boolean fireEvents) {
		schemeTypeListBox.setValue(value, fireEvents);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Scheme> handler) {
		return schemeTypeListBox.addValueChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return schemeTypeListBox.addFocusHandler(handler);
	}

	@Override
	public void setAcceptableValues(Collection<Scheme> values) {
		schemeTypeListBox.setAcceptableValues(values);
	}

}
