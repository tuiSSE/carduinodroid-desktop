package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GPSTrack;
import Model.Log;

/** Methods for working with the quit menuitem.
* @author Benjamin L
* @author Lars
* @version 1.0
*/
public class Quit_ActionListener implements ActionListener {
	
	GUI_Computer gui_Computer;
	
	public Quit_ActionListener (GUI_Computer guiComputer)
	{
		 gui_Computer = guiComputer;
	}
	
	/** 
	 * Before the whole programm is closed by "system.exit" it is 
	 * important to save the log and *.gpx-file by their own method.
	 * After this you can use the track file on certain platforms.
	 * 
	 * @param e			Event by pressing the quit menuitem.
	 */
	public void actionPerformed(ActionEvent e) {
		gui_Computer.quitProcedure();
	}
}
