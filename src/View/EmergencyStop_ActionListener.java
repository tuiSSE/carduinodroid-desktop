package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import Controller.*;
import Model.Log;
import Model.Log.ErrorState;

/** Methods for working with the emergencyStop toggle button.
* 
* @author Michael S
* @author Daniel W
* @version 1.0
*/
public class EmergencyStop_ActionListener implements ActionListener{
	Controller_Computer controller_Computer;
	Log log;
	JToggleButton status;
	
	/** 
	 * @param controllercomputer	used instance of Controller_Computer.
	 * @param LOG					Log which is used.
	 * @param STATUS				emergencyStop ToggleButton. 
	 */
	public EmergencyStop_ActionListener (Controller_Computer controllercomputer, Log LOG, JToggleButton STATUS){
		controller_Computer = controllercomputer;
		log = LOG;
		status = STATUS;
	}
	
	/** activates/deactivates the Emergency-Stop-Modus
	 * 
	 * @param e	Event by pressing the emergencyStop ToggleButton.
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (status.isSelected()){
			log.writelogfile("Emergency-Stop-Modus aus!" , ErrorState.INFO);
			controller_Computer.car_controller.setEmergencyStop(false);

		}
		else{
			log.writelogfile("Emergency-Stop-Modus an!" , ErrorState.INFO);
			controller_Computer.car_controller.setEmergencyStop(true);
		}		
	}
}
