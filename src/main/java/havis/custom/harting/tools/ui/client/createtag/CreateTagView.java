package havis.custom.harting.tools.ui.client.createtag;

import havis.custom.harting.tools.ui.client.createtag.fragments.CompanyPrefixPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.EpcPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.LengthPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ReferencePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SchemePanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.SerialNumberPanel;
import havis.custom.harting.tools.ui.client.createtag.fragments.ValuePanel;
import havis.custom.harting.tools.ui.client.widgets.ToolsView;

public interface CreateTagView extends ToolsView {
	void setPresenter(CreateTagPresenter presenter);

	LengthPanel getLengthPanel();

	SchemePanel getSchemePanel();

	CompanyPrefixPanel getCompanyPrefixPanel();

	ReferencePanel getReferencePanel();

	ValuePanel getValuePanel();

	SerialNumberPanel getSerialNumberPanel();

	EpcPanel getEpcPanel();
	
	SettingsSection getSettingsSection();
}
