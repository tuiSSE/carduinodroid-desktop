package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import Controller.*;

/** Methods for working with signal button.
*
* @author Benjamin L
* @author Lars
* @version 1.0
*/
public class Signal_ActionListener implements MouseListener{
	
	Controller_Computer controller_Computer;
	/** 
	 * @param controllercomputer	Used instance of Controller_Computer.
	 */
	public Signal_ActionListener (Controller_Computer controllercomputer)
	{
		controller_Computer = controllercomputer;
	}
	
	/** 
	 * With the help of this event you would be able to signal someone
	 * that your car is on the way or maybe pay attention to it. Later 
	 * you could change the number of the sound and get something like 
	 * a bark, a frog or applause.
	 * 
	 * @param e			Event by pressing the signal button.
	 */
	/*public void actionPerformed(ActionEvent e) {
		//signal_button.get
		controller_Computer.sound_output.send_output_soundsignal("1");
	}*/

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		controller_Computer.sound_output.send_output_soundsignal("1");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		controller_Computer.sound_output.send_output_soundsignal("0");
		
	}
}
