package havis.custom.harting.tools.ui.client.rfid.fragments;

import havis.custom.harting.tools.model.Field;
import havis.custom.harting.tools.model.Operation;
import havis.custom.harting.tools.ui.client.rfid.RFIDPanel;
import havis.custom.harting.tools.ui.client.rfid.RFIDPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.ListBox;

public class RFIDTest extends GWTTestCase {
	RFIDPanel view;
	RFIDPresenter presenter;
	
	@Override
	public String getModuleName() {
		return "havis.net.ui.core.WebUI";
	}
	
	protected void gwtSetUp() throws Exception {
		view = new RFIDPanel("RFID");
		presenter = new RFIDPresenter(view);
		
		presenter.resetService(new TestRfDeviceService());
	}

	public void testOnOperationTypeSelectedInitial() {
		
		view.onOperationTypeChanged(null);
		
		assertEquals(false , view.getDataPanel().isEnabled());
		assertEquals(false, view.getDataPanel().privilegeList.isVisible());
		assertEquals(true, view.getDataPanel().dataTextBox.isVisible());
		
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		assertEquals(Field.EPC_MEMORY, view.getFieldPanel().getValue());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
	}
	
	public void testOnOperationTypeSelectedWrite() {
		view.getOperationPanel().setValue(Operation.WRITE);
		
		view.onOperationTypeChanged(null);
		
		assertEquals(true, view.getDataPanel().isEnabled());
		assertEquals(false, view.getDataPanel().privilegeList.isVisible());
		assertEquals(true, view.getDataPanel().dataTextBox.isVisible());
		
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		assertEquals(Field.EPC_MEMORY, view.getFieldPanel().getValue());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
	}
	
	public void testOnOperationTypeSelectedKill() {
		view.getOperationPanel().setValue(Operation.KILL);
		
		view.onOperationTypeChanged(null);
		
		assertEquals(true, view.getDataPanel().isEnabled());
		assertEquals(false, view.getDataPanel().privilegeList.isVisible());
		assertEquals(true, view.getDataPanel().dataTextBox.isVisible());
		
		assertEquals(false, view.getFieldPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
		assertEquals(Field.EPC_MEMORY, view.getFieldPanel().getValue());
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
	}
	
	public void testOnOperationTypeSelectedLock() {
		view.getOperationPanel().setValue(Operation.LOCK);
		
		view.onOperationTypeChanged(null);
		
		assertEquals(true, view.getDataPanel().isEnabled());
		assertEquals(true, view.getDataPanel().privilegeList.isVisible());
		assertEquals(false, view.getDataPanel().dataTextBox.isVisible());
		
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
		assertEquals(Field.EPC_MEMORY, view.getFieldPanel().getValue());
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
	}
	
	public void testOnFieldSelectedAccess() {
		checkOnFieldSelected(Operation.READ, Field.ACCESS_PASSWORD, false);
		checkOnFieldSelected(Operation.WRITE, Field.ACCESS_PASSWORD, false);
		checkOnFieldSelected(Operation.KILL, Field.ACCESS_PASSWORD, false);
		checkOnFieldSelected(Operation.LOCK, Field.ACCESS_PASSWORD, false);
	}
	
	public void testOnFieldSelectedKill() {
		checkOnFieldSelected(Operation.READ, Field.KILL_PASSWORD, false);
		checkOnFieldSelected(Operation.WRITE, Field.KILL_PASSWORD, false);
		checkOnFieldSelected(Operation.KILL, Field.KILL_PASSWORD, false);
		checkOnFieldSelected(Operation.LOCK, Field.KILL_PASSWORD, false);
	}
	
	public void testOnFieldSelectedEpc() {
		checkOnFieldSelected(Operation.READ, Field.EPC_MEMORY, false);
		checkOnFieldSelected(Operation.WRITE, Field.EPC_MEMORY, false);
		checkOnFieldSelected(Operation.KILL, Field.EPC_MEMORY, false);
		checkOnFieldSelected(Operation.LOCK, Field.EPC_MEMORY, false);
	}
	
	public void testOnFieldSelectedTid() {
		checkOnFieldSelected(Operation.READ, Field.TID_MEMORY, false);
		checkOnFieldSelected(Operation.WRITE, Field.TID_MEMORY, false);
		checkOnFieldSelected(Operation.KILL, Field.TID_MEMORY, false);
		checkOnFieldSelected(Operation.LOCK, Field.TID_MEMORY, false);
	}
	
	public void testOnFieldSelectedUser() {
		checkOnFieldSelected(Operation.READ, Field.USER_MEMORY, false);
		checkOnFieldSelected(Operation.WRITE, Field.USER_MEMORY, false);
		checkOnFieldSelected(Operation.KILL, Field.USER_MEMORY, false);
		checkOnFieldSelected(Operation.LOCK, Field.USER_MEMORY, false);
	}
	
	public void testOnFieldSelectedCustom() {
		checkOnFieldSelected(Operation.READ, Field.CUSTOM, true);
		checkOnFieldSelected(Operation.WRITE, Field.CUSTOM, true);
	}
	
	public void testToggle() {
		view.getPasswordPanel().toggleButton.setValue(true);
		view.getPasswordPanel().onPasswordToggledButtonClicked(null);
		assertEquals(true, view.getPasswordPanel().password.isEnabled());
		assertEquals(false, view.getPasswordPanel().password.isReadOnly());
		
		view.getPasswordPanel().toggleButton.setValue(false);
		view.getPasswordPanel().onPasswordToggledButtonClicked(null);
		assertEquals(false, view.getPasswordPanel().password.isEnabled());
		assertEquals(true, view.getPasswordPanel().password.isReadOnly());
	}
	
	public void testSwitchingReadToKill() {
		view.getOperationPanel().setValue(Operation.READ);
		view.onOperationTypeChanged(null);
		view.getFieldPanel().setValue(Field.CUSTOM);
		view.onFieldChanged(null);
		
		assertEquals(true, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(false, view.getDataPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		
		view.getOperationPanel().setValue(Operation.KILL);
		view.onOperationTypeChanged(null);
		
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(false, view.getFieldPanel().isEnabled());
		assertEquals(true, view.getDataPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
	}
	
	public void testSwitchingReadToLock() {
		view.getOperationPanel().setValue(Operation.READ);
		view.onOperationTypeChanged(null);
		view.getFieldPanel().setValue(Field.CUSTOM);
		view.onFieldChanged(null);
		
		assertEquals(true, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		
		view.getOperationPanel().setValue(Operation.LOCK);
		view.onOperationTypeChanged(null);
		
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
	}
	
	public void testSwitchingWriteToKill() {
		view.getOperationPanel().setValue(Operation.WRITE);
		view.onOperationTypeChanged(null);
		view.getFieldPanel().setValue(Field.CUSTOM);
		view.onFieldChanged(null);
		
		assertEquals(true, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(true, view.getDataPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		
		view.getOperationPanel().setValue(Operation.KILL);
		view.onOperationTypeChanged(null);
		
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(false, view.getFieldPanel().isEnabled());
		assertEquals(true, view.getDataPanel().isEnabled());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
	}
	
	public void testSwitchingWriteToLock() {
		view.getOperationPanel().setValue(Operation.WRITE);
		view.onOperationTypeChanged(null);
		view.getFieldPanel().setValue(Field.CUSTOM);
		view.onFieldChanged(null);
		
		assertEquals(true, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(true, view.getDataPanel().isEnabled());
		assertEquals(false, view.getDataPanel().privilegeList.isVisible());
		assertEquals(true, view.getDataPanel().dataTextBox.isVisible());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY, Field.CUSTOM);
		
		view.getOperationPanel().setValue(Operation.LOCK);
		view.onOperationTypeChanged(null);
		
		assertEquals(false, view.getFieldPanel().getCustomPanel().isVisible());
		assertEquals(true, view.getFieldPanel().isEnabled());
		assertEquals(true, view.getDataPanel().isVisible());
		assertEquals(true, view.getDataPanel().privilegeList.isVisible());
		assertEquals(false, view.getDataPanel().dataTextBox.isVisible());
		assertFieldArray(view.getFieldPanel().fieldList, Field.ACCESS_PASSWORD, Field.KILL_PASSWORD, Field.EPC_MEMORY, Field.TID_MEMORY, Field.USER_MEMORY);
	}
	
	public void testInventory() {
		view.onInventory(null);
	}
	
	private void checkOnFieldSelected(Operation operation, Field field, boolean expected) {
		view.getOperationPanel().setValue(operation);
		view.onOperationTypeChanged(null);
		view.getFieldPanel().setValue(field);
		view.onFieldChanged(null);
		
		assertEquals(expected, view.getFieldPanel().getCustomPanel().isVisible());
	}
	
	private void assertFieldArray(ListBox listBox, Field... exp) {
		List<Field> fields = new ArrayList<Field>();
		Object[] expect = Arrays.asList(exp).toArray();
		
		for(int i = 0; i < listBox.getItemCount(); i++) {
			fields.add(Field.valueOf(listBox.getValue(i)));
		}
		
		Object[] current = fields.toArray();
		
		assertTrue(Arrays.equals(expect, current));
	}
}
