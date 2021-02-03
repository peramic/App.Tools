package havis.custom.harting.tools.ui.client.widgets;

import havis.device.rf.tag.TagData;
import havis.middleware.tdt.TdtTranslationException;

import java.util.ArrayList;

import javax.validation.ValidationException;

import com.google.gwt.user.client.ui.Widget;

public class TagRow {
	private static final Widget[] WIDGET_TYPE = new Widget[] {};
	
	private TagIDLabel tagIDLabel;
	private ArrayList<RSSIScalePanel> rssi = new ArrayList<RSSIScalePanel>();

	public TagRow(TagData tagData, int antennaCount) throws ValidationException, TdtTranslationException {
		if (tagData != null) {
			this.tagIDLabel = new TagIDLabel(tagData.getEpc());
			for (int i = 1; i <= antennaCount; i++) {
				if (tagData.getAntennaID() == i)
					rssi.add(new RSSIScalePanel(tagData.getRssi(), true));
				else
					rssi.add(new RSSIScalePanel(0, false));
			}
		} else {
			this.tagIDLabel = new TagIDLabel("ERROR");
			for (int i = 1; i <= antennaCount; i++) {
				rssi.add(new RSSIScalePanel(0, false));
			}
		}
	}

	public void setRSSI(short antennaID, int value) {
		int index = antennaID - 1;
		rssi.get(index).setFound(true);
		rssi.get(index).setValue(value);
	}

	public void reset() {
		for (RSSIScalePanel rssiPanel : rssi) {
			rssiPanel.setFound(false);
			rssiPanel.setValue(0);
		}
	}

	public Widget[] getWidgets() {
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		widgets.add(tagIDLabel);
		for (RSSIScalePanel rssi : this.rssi) {
			widgets.add(rssi);
		}
		return widgets.toArray(WIDGET_TYPE);
	}
}
