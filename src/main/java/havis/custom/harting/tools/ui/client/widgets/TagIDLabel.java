package havis.custom.harting.tools.ui.client.widgets;

import javax.validation.ValidationException;

import havis.custom.harting.tools.utils.Utils;
import havis.middleware.tdt.TdtTranslationException;
import havis.net.ui.shared.client.list.ComparableWidget;

import com.google.gwt.user.client.ui.Label;

public class TagIDLabel extends Label implements ComparableWidget<TagIDLabel> {

	private byte[] epc;

	public TagIDLabel() {
	}

	public TagIDLabel(byte[] epc) throws ValidationException, TdtTranslationException {
		this.epc = epc;
		String s = "";
		for (byte b : epc) {
			int i = b & 255;
			if (i < 16)
				s += "0";
			s += Integer.toHexString(i).toUpperCase();
		}

		setText(Utils.INSTANCE.translateTag(epc).getUriTag());
		setTitle(s);
	}

	public TagIDLabel(String epc) {
		setText(epc);
	}

	public String getEpcString() {
		return getText();
	}

	public byte[] getEpc() {
		return epc;
	}

	@Override
	public int compareTo(TagIDLabel tagID) {
		return getEpcString().compareTo(tagID.getEpcString());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TagIDLabel) {
			return getEpcString().equals(((TagIDLabel) obj).getEpcString());
		}
		return super.equals(obj);
	}
}
