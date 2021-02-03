package havis.custom.harting.tools.ui.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface ResourceBundle extends ClientBundle {

	public static final ResourceBundle INSTANCE =
			GWT.create(ResourceBundle.class);
	
	@Source("resources/OverallStyle.css")
	CSSResource css();

	@Source("resources/LLRP_Region_Dropdown_Arrow.png")
	DataResource dropDownArrow();
	
	@Source("resources/LLRP_Region_Dropdown_Arrow_disabled.png")
	DataResource dropDownArrowDisabled();
	
	@Source("resources/LLRP_Power_Scale_BT_Plus.png")
	DataResource powerScalePlus();
	
	@Source("resources/LLRP_Power_Scale_BT_Minus.png")
	DataResource powerScaleMinus();
	
	@Source("resources/CONTENT_Switch_On.png")
	DataResource contentSwitchOn();
	
	@Source("resources/CONTENT_Switch_Off.png")
	DataResource contentSwitchOff();
	
	@Source("resources/LLRP_List_Loading.png")
	DataResource inProgress();
	
	@Source("resources/TAGS_EXPORT.png")
	DataResource tagsExport();
}
