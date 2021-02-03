package havis.custom.harting.tools.ui.client.widgets;

import java.math.BigInteger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PlusMinusBox extends Composite {

	private static PlusMinusBoxUiBinder uiBinder = GWT.create(PlusMinusBoxUiBinder.class);

	interface PlusMinusBoxUiBinder extends UiBinder<Widget, PlusMinusBox> {
	}

	@UiField
	protected Button minusButton;
	@UiField
	protected Button plusButton;
	@UiField
	protected TextBox valueBox;
	
	private ChangeHandler changeHandler;

	private BigInteger theValue;
	private int step;
	private BigInteger minBound;
	private BigInteger maxBound;
	private int minNoOfDigits;
	private boolean keepLeadingZeros;
	private boolean blockClicks;

	public PlusMinusBox() {
		initWidget(uiBinder.createAndBindUi(this));

		// default values
		theValue = new BigInteger("0");
		step = 1;
		keepLeadingZeros = true;
		valueBox.setText("" + theValue);
	}

	public void setChangeHandler(ChangeHandler changeHandler) {
		this.changeHandler = changeHandler;
	}

	protected void outputFormattedValue() {
		outputFormattedValue(true);
	}

	protected void outputFormattedValue(boolean fireEvent) {
		String outPut = "";
		// if theValue >= maxbound || theValue < minBound
		if ((maxBound != null && theValue.compareTo(maxBound) > - 1) || (minBound != null && theValue.compareTo(minBound) < 0)) {
			return;
		}

		outPut = "" + theValue;
		int outLen = outPut.length();

		if (keepLeadingZeros && outLen < minNoOfDigits) {

			outPut = "";
			for (int i = 0; i < minNoOfDigits - outLen; i++) {
				outPut += "0";
			}

			outPut += theValue;
		}

		valueBox.setText(outPut);

		if (fireEvent && (changeHandler != null)) {
			changeHandler.onChange(null);
		}
	}

	@UiHandler("minusButton")
	public void minusClick(ClickEvent e) {
		if (blockClicks) {
			return;
		}

		minNoOfDigits = valueBox.getText().length();

		if (minBound == null || theValue.subtract(new BigInteger(step + "")).compareTo(minBound) > 0) {
			theValue = theValue.subtract(new BigInteger(step + ""));
			outputFormattedValue();
		}
	}

	@UiHandler("plusButton")
	public void plusClick(ClickEvent e) {
		if (blockClicks) {
			return;
		}

		minNoOfDigits = valueBox.getText().length();

		if (maxBound == null || theValue.add(new BigInteger(step + "")).compareTo(maxBound) < 0) {
			theValue = theValue.add(new BigInteger(step + ""));
			outputFormattedValue();
		}
	}

	@UiHandler("valueBox")
	public void valueChanged(ChangeEvent e) {
		TextBox tb = (TextBox) e.getSource();
		minNoOfDigits = tb.getText().length();

		try {			
			theValue = new BigInteger(tb.getText());
			outputFormattedValue();
		} catch (NumberFormatException nfe) {
			if(!tb.getText().startsWith("[")) {
				e.stopPropagation();
			}
		}
	}

	public boolean getKeepLeadingZeros() {
		return keepLeadingZeros;
	}

	public void setKeepLeadingZeros(boolean value) {
		keepLeadingZeros = value;
	}

	public BigInteger getMinBound() {
		return minBound;
	}

	public void setMinBound(BigInteger value) {		
		minBound = value;
	}

	public BigInteger getMaxBound() {
		return maxBound;
	}

	public void setMaxBound(BigInteger value) {
		maxBound = value;
	}

	public void setMinNoOfDigits(int value, boolean fireEvent) {		
		minNoOfDigits = value;
		outputFormattedValue(fireEvent);
	}

	public int getMinNoOfDigits() {
		return minNoOfDigits;
	}

	public String getText() {
		return valueBox.getText();
	}

	public BigInteger getValue() {
		return theValue;
	}

	public void setValue(BigInteger value) {
		if ((maxBound != null && value.compareTo(maxBound) > -1) || (minBound != null && value.compareTo(minBound) < 0)) {
			return;
		}
		theValue = value;
		outputFormattedValue();
	}
	
	public void setValue(String value, boolean fireEvent) {
		if ((value == null) || (!value.startsWith("["))) {
			return;
		}

		valueBox.setText(value);

		if (fireEvent && (changeHandler != null)) {
			changeHandler.onChange(null);
		}
	}

	public int getStep() {
		return step;
	}

	public void setStep(int value) {
		if (value < 1) {
			step = 1;
			return;
		}		
		step = value;
	}

	public TextBox getBox() {
		return valueBox;
	}

	public void freeze() {
		valueBox.setText("");
		theValue = new BigInteger("0");
		blockClicks = true;
	}

	public void unfreeze() {
		valueBox.setText("0");
		theValue = new BigInteger("0");
		blockClicks = false;
	}

	public boolean isFrozen() {
		return blockClicks;
	}
}
