package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Model.Log;

/** Methods for working with IP_ComboBox.
*
* @author Michael S
* @version 1.0
*/
public class IP_ComboBox_ActionListener implements ActionListener{
	
	GUI_Computer gui_computer;
	Log log;
	JComboBox ip_comboBox;
	String number_between_0_and_255 = "(([0-9]{1,2})|([01][0-9]{2})|(2((5[0-5])|([0-4][0-9]))))";
	String valid_ipv4 = "(" + number_between_0_and_255 + "\\.){3}" + number_between_0_and_255;
	
	/** 
	 * @param QUESTION		Question text by opening the connect window. 
	 * @param LOG			Log which is used.
	 * @param IP			Label which should show the actual connected IP.
	 * @param gui_Computer	The GUI which calls this constructor. 
	 */
	public IP_ComboBox_ActionListener(JComboBox IP_comboBox){
//		Question = QUESTION;
		ip_comboBox= IP_comboBox;
		
	}

	/** Opens a new window to enter the ip of the phone when pressing the connect menu item.
	 * 
	 * @param e			Event by pressing the connect menu item.
	 */
	public void actionPerformed(ActionEvent e) {
		
		
		String Ip_address = ip_comboBox.getEditor().getItem().toString();
		
		if (Ip_address.matches(valid_ipv4)){
			
			 boolean exists = false;
			 
			 for (int index = 0; index < ip_comboBox.getItemCount() && !exists; index++) {
			   if (ip_comboBox.getEditor().getItem().equals(ip_comboBox.getItemAt(index))) {
			     exists = true;
			   }
			 }
			 
			if (!exists){ 
				ip_comboBox.addItem(ip_comboBox.getEditor().getItem());
			}
		}
		
		//set last selected item to first item
		Object selectedItem = ip_comboBox.getSelectedItem();
		ip_comboBox.removeItemAt(ip_comboBox.getSelectedIndex());
		ip_comboBox.insertItemAt(selectedItem, 0);
		ip_comboBox.setSelectedIndex(0);
	}
}
