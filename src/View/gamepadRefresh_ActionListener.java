package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Model.Log;
import Model.Log.ErrorState;

/** Methods for refreshing the Gamepad List //does not work yet
*
* @author Michael S
* @version 1.0
*/
public class gamepadRefresh_ActionListener implements ActionListener{
	
	GUI_Computer gui_computer;

	Log log;	
	
	public gamepadRefresh_ActionListener(GUI_Computer gui_Computer, Log LOG){

		log = LOG;
		gui_computer = gui_Computer;
	}

	/** starts to find connected Gamepads
	 * 
	 * @param e		Event when gamePadRefreshButton is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		gui_computer.controller_Computer.car_controller.FindGamepad();
	}
}
