package havis.custom.harting.tools.ui.client.createtag;

import havis.custom.harting.tools.ui.client.createtag.fragments.CompanyPrefixPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.EpcPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.LengthPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ReferencePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SchemePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SerialNumberPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ValuePanel;
import havis.custom.harting.tools.ui.client.widgets.ResultPanel;
import havis.custom.harting.tools.ui.resourcebundle.ConstantsResource;
import havis.custom.harting.tools.utils.UiMisc;
import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateTag extends ConfigurationSection implements CreateTagView {
	
	private CreateTagPresenter presenter;
	private boolean available;
	
	private static CreateTagUiBinder uiBinder = GWT.create(CreateTagUiBinder.class);

	interface CreateTagUiBinder extends UiBinder<Widget, CreateTag> {
	}

	@UiField
	HTMLPanel effect;
	
	@UiField
	EpcPanel epcPanel;
	
	@UiField
	AdvancedSection advancedSection;
	
	@UiField
	SettingsSection settingsSection;
	
	@UiField
	ResultPanel resultPanel;
	
	@UiField
	Button readButton;
	
	@UiField
	Button writeButton;

	@UiConstructor
	public CreateTag(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(CreateTagPresenter presenter) {
		this.presenter = presenter;
		this.advancedSection.setPresenter(this.presenter);
	}
	
	@UiHandler("readButton")
	public void onRead(ClickEvent ce) {
		presenter.onRead();
	}
	
	@UiHandler("writeButton")
	public void onWrite(ClickEvent ce) {
		presenter.onWrite();
	}

	@Override
	public LengthPanel getLengthPanel() {
		return advancedSection.lengthPanel;
	}

	@Override
	public SchemePanel getSchemePanel() {
		return advancedSection.schemePanel;
	}

	@Override
	public CompanyPrefixPanel getCompanyPrefixPanel() {
		return advancedSection.companyPrefixPanel;
	}

	@Override
	public ReferencePanel getReferencePanel() {
		return advancedSection.referencePanel;
	}

	@Override
	public ValuePanel getValuePanel() {
		return advancedSection.valuePanel;
	}

	@Override
	public SerialNumberPanel getSerialNumberPanel() {
		return advancedSection.serialNumberPanel;
	}
	
	@Override
	public EpcPanel getEpcPanel() {
		return epcPanel;
	}

	@Override
	public ResultPanel getResultPanel() {
		return resultPanel;
	}
	
	@Override
	public SettingsSection getSettingsSection() {
		return settingsSection;
	}
	
	@UiHandler("epcPanel")
	public void onEpcChange(ValueChangeEvent<String> vce) {
		presenter.onEpcChanged();
	}
	
	@Override
	protected void onOpenSection() {
		if(!available) {
			this.setOpen(false);
			presenter.showErrorMessage(ConstantsResource.INSTANCE.unavailableError());
		}
		else {
			super.onOpenSection();
		}
	}

	@Override
	public void closeSection() {
		setOpen(false);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		effect.setStyleName(UiMisc.INSTANCE.getEffectName(enabled));
		readButton.setEnabled(enabled);
		writeButton.setEnabled(enabled);
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean isAvailable() {
		return this.available;
	}
}