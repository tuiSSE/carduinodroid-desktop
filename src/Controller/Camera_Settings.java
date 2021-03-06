package Controller;

import Model.Log.ErrorState;

/**
* methods for sending camera changes
* @author Lars
* @author Benjamin L
* @author Michael S
* @version 1.0
*/

public class Camera_Settings {
	Controller_Computer controller_computer;
	
	public Camera_Settings(Controller_Computer ControllerComputer) {
		controller_computer = ControllerComputer;
	}
	
	// ***** Change Camera Type ***************************************
	/** With this method you will be able to change the camera type
	 * between front and back. It is sending a typical signal to the
	 * Android device which will start the "Change command".
	 * 
	 * @param cameratype Knows if it is the front or back camera
	 * 	 */
	public void send_change_camera(String cameratype) {
		if (controller_computer.network.send_camera_settings("1;" + cameratype)) {
			controller_computer.log.writelogfile("Camera was switched" , ErrorState.INFO);
		} else {
			controller_computer.log.writelogfile("Camera wasn't switched" , ErrorState.WARNING);
		}
	}
	
	// ***** Change Camera Resolution ***************************************
		/** The second important part is to change the resolution of 
		 * your camera. This is important for lowering your traffic and
		 * maybe your built-in camera is not able to send in the chosen
		 * resolution.
		 * 
		 * @param setting Save the chosen Resolution by itemindex
		 */
	public void send_change_resolution(String setting) {
		if (controller_computer.network.send_camera_settings("2;" + setting)) {
			controller_computer.log.writelogfile("Resolution settings were sent" , ErrorState.INFO);
		} else {
			controller_computer.log.writelogfile("Resolution settings weren't sent" , ErrorState.WARNING);
		}
	}
	
	// ***** Change Camera Light ***************************************
			/** At a dark night or twilight u need to switch on a light to 
			 * see what is in front of your car. Sometimes it can be helpful
			 * to have an enlightened place.
			 * 
			 * @param cameralight Variable for turning light on or off
			 */
	public void send_switch_light(String cameralight) { // cameralight 1 = licht an 0 = licht aus ? Lars: Lesen bevor fragen - Schau in Light_ActionListener!
		if (controller_computer.network.send_camera_settings("3;" + cameralight)) {
			controller_computer.log.writelogfile("Camera light was switched" , ErrorState.INFO);
		} else {
			controller_computer.log.writelogfile("Camera light wasn't switched" , ErrorState.WARNING);
		}
	}
	
	/**
	 * 
	 * @param Percent
	 */
	public void send_change_quality(String Percent){
		if  (controller_computer.network.send_camera_settings("4;" + Percent)) {
			controller_computer.log.writelogfile("Quality has been changed" , ErrorState.INFO);
		} else {
			controller_computer.log.writelogfile("Quality hasn't been changed" , ErrorState.WARNING);
		}
	}

}
