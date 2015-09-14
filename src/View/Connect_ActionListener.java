package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Model.Log;
import Model.Log.ErrorState;

/** Methods for working with connect menu item.
*
* @author Benjamin L
* @author Michael S
* @version 1.0
*/
public class Connect_ActionListener implements ActionListener{
	
	GUI_Computer gui_computer;
//	String Question;
	JComboBox ip_comboBox;
	String Ip_address;
	Log log;
	JLabel ip;
	
	String number_between_0_and_255 = "(([0-9]{1,2})|([01][0-9]{2})|(2((5[0-5])|([0-4][0-9]))))";
	String valid_ipv4 = "(" + number_between_0_and_255 + "\\.){3}" + number_between_0_and_255;
	
	/** 
	 * @param QUESTION		Question text by opening the connect window. 
	 * @param LOG			Log which is used.
	 * @param IP			Label which should show the actual connected IP.
	 * @param gui_Computer	The GUI which calls this constructor. 
	 */
	public Connect_ActionListener(JComboBox IP_comboBox, Log LOG, JLabel IP, GUI_Computer gui_Computer){
//		Question = QUESTION;
		ip_comboBox = IP_comboBox;
		log = LOG;
		ip = IP;
		gui_computer = gui_Computer;
	}

	/** Opens a new window to enter the ip of the phone when pressing the connect menu item.
	 * 
	 * @param e			Event by pressing the connect menu item.
	 */
	public void actionPerformed(ActionEvent e) {
		//Auslesen des Textfeldes und prüfen ob leer, danach schauen ob Mindestlaenge
		if (ip_comboBox.getItemCount() > 0 ){
			if((Ip_address =  ip_comboBox.getSelectedItem().toString() ) != null){
				if (Ip_address.matches(valid_ipv4)){
					ip.setText(Ip_address);
					//System.out.println(Ip_address);
					log.writelogfile("Connect to: " + Ip_address, ErrorState.INFO);	
					gui_computer.controller_Computer.network.connect(Ip_address);			
				}
			}
		}	
	}
}
