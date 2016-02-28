/*
 * NOCH NICHT FERTIG BZW EPRÜFT MUSS NOCH ÜBERARBEITET WERDEN
 */

/*
 *ip wo hin?
 * 
 */
package Controller;
import java.net.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import Model.Log.ErrorState;
import View.GUI_Computer;


/**
 * This Class is responsible for the communication with the android-application
 * @author Robin
 * @author Michael S
 * @version 1.0
 */

public class Network {
	
	Controller_Computer controller_computer;
	//Socket_Picture socket_picture;
	//Socket_Package socket_package;
	//Socket_Controller socket_controller;
	String mobilephone_ip;
	Camera_Picture camera_picture;
	Packagedata packagedata;
	//Thread t1;
	//Thread t2;
	private boolean isClosed = false;
	
	IP_Connection ipConnection;
	
	
	public Network(Packagedata n_packagedata, Camera_Picture n_camera_picture, Controller_Computer ControllerComputer)
	{
		controller_computer = ControllerComputer;
		//socket_picture = new Socket_Picture(this);
		//socket_package = new Socket_Package(this);
		//socket_controller = new Socket_Controller();
		camera_picture = n_camera_picture;
		packagedata = n_packagedata;
		
		ipConnection = new IP_Connection(this);
	}
	
//	public Network(String ip)
//	{
//		socket_picture = new Socket_Picture(this);
//		socket_package = new Socket_Package(this);
//		socket_controller = new Socket_Controller();
//		mobilephone_ip = ip;
//	}
	/*
	 * ports = ???????
	 */
	
	/**
	 * This method connects with the Android-Application
	 * @param ipstring
	 */
	public void connect(String ipstring)
	{
		//InetAddress ip;
		//mobilephone_ip = ipstring;
		//boolean infosocket_picture = false, infosocket_controller = false, infosocket_package=false;
		
		//try {
			//ip = InetAddress.getByName(ipstring);
			//InetSocketAddress port_controll = new InetSocketAddress(ip, 12345);
			//InetSocketAddress port_package = new InetSocketAddress(ip, 12346);
			//InetSocketAddress port_picture = new InetSocketAddress(ip, 12347);
			//infosocket_picture = socket_picture.connect(port_picture);
			//infosocket_controller = socket_controller.connect(port_controll);
			//infosocket_package = socket_package.connect(port_package);
			//t1 = new Thread(socket_picture);
			//t2 = new Thread(socket_package);
			//t1.start();
			//t2.start();
			
			
		//} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		//if(infosocket_picture!=true){controller_computer.log.writelogfile("Socket_Picture - Fehler beim Verbinden" , ErrorState.ERROR);}
		//if(infosocket_controller!=true){controller_computer.log.writelogfile("Socket_Controller - Fehler beim Verbinden" , ErrorState.ERROR);}
		//if(infosocket_package!=true){controller_computer.log.writelogfile("Socket_Package - Fehler beim Verbinden" , ErrorState.ERROR);}
		
		if(ipConnection.connect(ipstring)){
			controller_computer.gui_computer.resetStatusbar();
			controller_computer.gui_computer.phoneConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/green_point.png")));
			controller_computer.gui_computer.tabPane.setSelectedIndex(0);
			isClosed = false;
		}
	}
	/*
	 * 1 = controllsignal
	 * 2 = settings
	 * 3 = sound
	 */
	
	
	/**
	 * @param direction
	 * @return
	 * @see Socket_Controller#send_controllsignal(String)
	 */
	public boolean send_controllsignal(String direction)
	{

		return ipConnection.sendControlData(direction);
	}
	
	
	/**
	 * @param settings
	 * @return
	 * @see Socket_Controller#send_camera_settings(String)
	 */
	public boolean send_camera_settings(String settings)
	{

		return ipConnection.sendCameraData(settings);
	}
	
	/**
	 * @param sound_id
	 * @return
	 * @see Socket_Controller#send_sound(String)
	 */
	public boolean send_sound(String sound_id)
	{
	
		return ipConnection.sendSoundData(sound_id);
	}

	/**
	 * Transfer the message to the Packagedata
	 * @param message
	 * @see Packagedata#receive_package(String)
	 */
	public void receive_package(String message) {
		// TODO Auto-generated method stub
		packagedata.receive_package(message);
	}
	
	/**
	 * Transfer the image to the Camera_Picture
	 * @param bufferedImage
	 * @see Camera_Picture#receive_picture(ImageIcon)
	 */
	public void receive_picture(BufferedImage bufferedImage) {
		camera_picture.receive_picture(bufferedImage);
	}

	public void close() {
		//socket_picture.close();
		//socket_package.close();
		//socket_controller.close();
		ipConnection.close();
		isClosed = true;
	}
	public boolean isClosed(){
		return isClosed;
	}
	
	
}
