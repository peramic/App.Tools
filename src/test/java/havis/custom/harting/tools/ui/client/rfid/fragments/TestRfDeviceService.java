package havis.custom.harting.tools.ui.client.rfid.fragments;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.TextCallback;

import havis.device.rf.capabilities.DeviceCapabilities;
import havis.device.rf.capabilities.RegulatoryCapabilities;
import havis.device.rf.configuration.AntennaConfiguration;
import havis.device.rf.configuration.AntennaProperties;
import havis.device.rf.configuration.InventorySettings;
import havis.device.rf.configuration.KeepAliveConfiguration;
import havis.device.rf.configuration.RssiFilter;
import havis.device.rf.configuration.SelectionMask;
import havis.device.rf.configuration.SingulationControl;
import havis.device.rf.configuration.TagSmoothingSettings;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.TagOperation;
import havis.middleware.tdt.DataTypeConverter;
import havis.net.rest.rf.DeviceConfiguration;
import havis.net.rest.rf.async.RFDeviceServiceAsync;

public class TestRfDeviceService implements RFDeviceServiceAsync {

	@Override
	public void isServiceAvailable(TextCallback arg0) {
	}

	@Override
	public void optionsInstallFirmware(MethodCallback<Void> callback) {
	}

	@Override
	public void installFirmware(MethodCallback<Void> callback) {
	}

	@Override
	public void getCapabilities(MethodCallback<List<String>> callback) {
	}

	@Override
	public void getDeviceCapabilities(MethodCallback<DeviceCapabilities> callback) {
	}

	@Override
	public void getRegulatoryCapabilities(MethodCallback<RegulatoryCapabilities> callback) {
	}

	@Override
	public void optionsConfiguration(MethodCallback<Void> callback) {
	}


	public void resetConfiguration(MethodCallback<Void> callback) {
	}

	@Override
	public void optionsKeepAliveConfiguration(MethodCallback<Void> callback) {
	}

	@Override
	public void getKeepAliveConfiguration(MethodCallback<KeepAliveConfiguration> callback) {
	}

	@Override
	public void setKeepAliveConfiguration(KeepAliveConfiguration keepAlive, MethodCallback<Void> callback) {
	}

	@Override
	public void getAntennaProperties(MethodCallback<List<AntennaProperties>> callback) {
	}

	@Override
	public void optionsAntennaConfiguration(MethodCallback<Void> callback) {
	}

	@Override
	public void optionsRegion(MethodCallback<Void> callback) {
	}

	@Override
	public void getSupportedRegions(MethodCallback<List<String>> callback) {

	}

	@Override
	public void getTags(MethodCallback<List<TagData>> callback) {
	}

	@Override
	public void getTags(String id, List<TagOperation> tagOperations, MethodCallback<List<TagData>> callback) {
		if (tagOperations == null) {
			callback.onFailure(new Method(new Resource("http://localhost"), "POST"), new Exception("Test Exception"));
		} else {
			List<TagData> response = new ArrayList<TagData>();

			TagData tagData1 = new TagData();
			tagData1.setEpc(DataTypeConverter.hexStringToByteArray("000ADB7E39E8000000000000"));
			tagData1.setAntennaID((short) 1);
			tagData1.setRssi(-67);

			callback.onSuccess(new Method(new Resource("http://localhost"), "POST"), response);
		}
	}

	@Override
	public void getInventorySettings(MethodCallback<InventorySettings> arg0) {
	}

	@Override
	public void optionsInventorySettings(MethodCallback<Void> arg0) {
	}

	@Override
	public void getAntennaConfiguration(short arg0, MethodCallback<AntennaConfiguration> arg1) {
	}

	@Override
	public void getAntennaConfigurations(MethodCallback<List<AntennaConfiguration>> arg0) {
	}

	@Override
	public void getAntennaProperties(short arg0, MethodCallback<AntennaProperties> arg1) {
	}

	@Override
	public void getConfiguration(MethodCallback<DeviceConfiguration> arg0) {
	}

	@Override
	public void getRegion(TextCallback arg0) {
	}

	@Override
	public void getRssiFilter(MethodCallback<RssiFilter> arg0) {
	}

	@Override
	public void getSelectionMasks(MethodCallback<List<SelectionMask>> arg0) {
	}

	@Override
	public void getSingulationControl(MethodCallback<SingulationControl> arg0) {
	}

	@Override
	public void getTagSmoothingSettings(MethodCallback<TagSmoothingSettings> arg0) {
	}

	@Override
	public void optionsAntennaConfigurations(MethodCallback<Void> arg0) {
	}

	@Override
	public void optionsRssiFilter(MethodCallback<Void> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void optionsSelectionMasks(MethodCallback<Void> arg0) {
	}

	@Override
	public void optionsSingulationControl(MethodCallback<Void> arg0) {
	}

	@Override
	public void optionsTagSmoothingSettings(MethodCallback<Void> arg0) {
	}

	@Override
	public void setAntennaConfiguration(short arg0, AntennaConfiguration arg1, MethodCallback<Void> arg2) {
	}

	@Override
	public void setConfiguration(DeviceConfiguration arg0, MethodCallback<Void> arg1) {
	}

	@Override
	public void setRegion(String arg0, MethodCallback<Void> arg1) {
	}

	@Override
	public void setRssiFilter(RssiFilter arg0, MethodCallback<Void> arg1) {
	}

	@Override
	public void setSelectionMasks(List<SelectionMask> arg0, MethodCallback<Void> arg1) {
	}

	@Override
	public void setSingulationControl(SingulationControl arg0, MethodCallback<Void> arg1) {
	}

	@Override
	public void setTagSmoothingSettings(TagSmoothingSettings arg0, MethodCallback<Void> arg1) {
	}
}
