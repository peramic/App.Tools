package havis.custom.harting.tools.ui.client.widgets;

import java.util.Collection;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;

public class CustomListBox<T> extends ListBox implements Focusable, HasConstrainedValue<T>, HasEnabled {

	private boolean valueChangeHandlerInitialized;
	private OptionRenderer<T> renderer;
	
	public CustomListBox(OptionRenderer<T> renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public T getValue() {
		if (getSelectedIndex() == -1) {
			return null;
		}

		return renderer.fromOptionValue(getValue(getSelectedIndex()));
	}

	@Override
	public void setValue(T value) {
		for (int i = 0; i < getItemCount(); i++) {
			String listValue = getValue(i);
			String slctValue = value.toString();
			if (listValue.equals(slctValue) || (slctValue == null && "".equals(listValue))) {
				setSelectedIndex(i);
				return;
			}
		}
		if (value == null) {
			setSelectedIndex(-1);
		}
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		setValue(value); 
	    if (fireEvents) { 
	      ValueChangeEvent.fire(this, value); 
	    } 
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		if (!valueChangeHandlerInitialized) { 
		      valueChangeHandlerInitialized = true; 
		      addChangeHandler(new ChangeHandler() { 
		        public void onChange(final ChangeEvent event) { 
		          ValueChangeEvent.fire(CustomListBox.this, getValue()); 
		        } 
		      }); 
		    } 
		    return addHandler(handler, ValueChangeEvent.getType()); 
	}

	@Override
	public void setAcceptableValues(Collection<T> values) {
		clear();

		for(T t : values) {
			addItem(renderer.toOptionItem(t), renderer.toOptionValue(t));
		}
	}
	
	public static interface OptionRenderer<T> {
		String toOptionValue(T t);
		String toOptionItem(T t);
		T fromOptionValue(String optionValue);
	}
}