package View;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

/** Methods for working with about menutitem.
*
* @author Benjamin L
* @version 1.0
*/
public class About_ActionListener implements ActionListener{
	
	URI uri;
	
	/** 
	 * @param uri link to website of CarduinoDroid project
	 */
	public About_ActionListener(String LINK){
		try {
			 uri = new URI("https://github.com/tuiSSE/carduinodroid-wiki/wiki");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Shows the information about this project in popup window when pressing the about menuitem.
	 * 
	 * @param e			Event by pressing the About menuitem.
	 */
	public void actionPerformed(ActionEvent e){
//		JOptionPane.showMessageDialog(null, about_1 + "\n\n" + about_2 + ":\n" + link, title, JOptionPane.INFORMATION_MESSAGE);
		if (Desktop.isDesktopSupported()) {
		      try {
		        Desktop.getDesktop().browse(uri);
		      } catch (IOException ex) { /* TODO: error handling */ }
		    } else { /* TODO: error handling */ }
	}
}
