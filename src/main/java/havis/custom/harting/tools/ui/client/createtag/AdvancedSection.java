package havis.custom.harting.tools.ui.client.createtag;

import havis.custom.harting.tools.model.Scheme;
import havis.custom.harting.tools.ui.client.createtag.fragments.CompanyPrefixPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.LengthPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ReferencePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SchemePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SerialNumberPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ValuePanel;
import havis.net.ui.shared.client.ConfigurationSection;

import java.math.BigInteger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedSection extends ConfigurationSection {
	
	private CreateTagPresenter presenter;

	@UiField
	LengthPanel lengthPanel;
	
	@UiField
	SchemePanel schemePanel;

	@UiField
	CompanyPrefixPanel companyPrefixPanel; /*SGTIN, SSCC, GRAI, GIAI*/
	
	@UiField
	ReferencePanel referencePanel; /*SGTIN[Label=Item Reference], SSCC[Label=Serial Reference], GIAI[Label=Individual Asset Reference], GRAI[Label=Asset Type]*/
	
	@UiField
	ValuePanel valuePanel; /*Raw-Hex[Label=Value], SGTIN[Label=Filter], SSCC[Label=Filter], GRAI[Label=Filter], GIAI[Label=Filter]*/
	
	@UiField
	SerialNumberPanel serialNumberPanel; /*SGTIN, GRAI*/
	
	private static AdvancedSectionUiBinder uiBinder = GWT
			.create(AdvancedSectionUiBinder.class);

	interface AdvancedSectionUiBinder extends UiBinder<Widget, AdvancedSection> {
	}
	
	@UiConstructor
	public AdvancedSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("schemePanel")
	public void onSchemeChange(ValueChangeEvent<Scheme> vce) {
		presenter.onSchemeChanged();
	}
	
	@UiHandler("lengthPanel")
	public void onLengthChange(ValueChangeEvent<Integer> vce){
		presenter.onAdvancedDataChanged();
	}
	
	@UiHandler("valuePanel")
	public void onFilterChange(ValueChangeEvent<String> vce){
		presenter.onAdvancedDataChanged();
	}
	
	@UiHandler("valuePanel")
	public void onKeyDownEvent(KeyDownEvent kde) {
		presenter.onValueBoxKeyDownEvent(kde);
	}
	
	@UiHandler({ "referencePanel", "serialNumberPanel" })
	public void onPlusMinusDataChange(ValueChangeEvent<BigInteger> vce) {
		presenter.onAdvancedDataChanged();
	}
	
	@UiHandler("companyPrefixPanel")
	public void onDataChange(ValueChangeEvent<String> vce) {
		presenter.onAdvancedDataChanged();
	}
	
	@UiHandler({"referencePanel", "serialNumberPanel"})
	public void onItemrefChange(ChangeEvent ce){
		presenter.onAdvancedDataChanged();
	}
	
	@UiHandler("exportButton")
	void onExport(ClickEvent event) {
		presenter.export();
	}

	void setPresenter(CreateTagPresenter presenter) {
		this.presenter = presenter;
	}	
}