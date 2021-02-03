package havis.custom.harting.tools.ui.resourcebundle;

import com.google.gwt.resources.client.CssResource;

public interface CSSResource extends CssResource {

	/**
	 * Add this style to all TextBoxes which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-TextBox
	 */
	@ClassName("webui-TextBox")
	String webuiTextBox();
	
	/**
	 * Add this style to all TextAreas which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-TextArea
	 */
	@ClassName("webui-TextArea")
	String webuiTextArea();

	/**
	 * Add this style to all ListBoxes which are not children of a
	 * {@link CustomTable}
	 * 
	 * @return webui-ListBox
	 */
	@ClassName("webui-ListBox")
	String webuiListBox();
	
	String row();
	String col();
	String flexcol();
	String subSection();
	String firstLabel();
	String secLabel();
	String advancedSubPanel();
	String epcLabel();
	String epcSec();
	String settingsLabel();
	String pwdToggle();
	String botborder();
	String toolbar();
	String plusButton();
	String minusButton();
	String plusMinusBox();
	String wideLabel();
	String customPanel();
	String customCol();
	String headline();
	String indented();
	String crTagSection();
	String rfidpanel();
	
	@ClassName("text-error")
	String textError();

	@ClassName("text-success")
	String textSuccess();
	String listSelected();
	
	@ClassName("effect-off")
	String effectOff();

	@ClassName("effect-on")
	String effectOn();
	
	String marginIndented();
	String switchTb();
	
	String exportButton();
}
