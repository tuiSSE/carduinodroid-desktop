package Controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class is used to send Controllsignals to the Android-Application
 * @author Robin
 * @version 18.06.2012
 */

public class Socket_Controller {
	
	BufferedWriter controll;
	Socket socket_controll;
	String mobilephone_ip;



	Socket_Controller(){
	}
	
	/**
	 * Connect the Socket to the Android-Application
	 * @param port_controll The Address from the Android-Application
	 */
	public boolean connect(InetSocketAddress port_controll)
	{
		try {
			socket_controll = new Socket();
			socket_controll.connect(port_controll,5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			/*
			 * noch �berlegen
			 */
			System.out.println("Socket_Controller - Fehler beim Connecten");
		}
		
		try {
			controll = new BufferedWriter(new OutputStreamWriter(socket_controll.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Socket_Controller - Fehler beim Outputstream");
		}
		if(socket_controll.isConnected()){
			System.out.println("Socket_Controller - Connected");
			return true;
		}else return false;
	}
	
	/**
	 * Sends the Carcontrollsignal to the Android-Application
	 * @param direction The Signal
	 * @return True if successful
	 */
	public boolean send_controllsignal(String direction)
	{
		if(socket_controll!=null){
			if(!socket_controll.isClosed())
			{
				String bufferdirection = "1;" + direction;
				try {
					controll.write(bufferdirection);
					controll.newLine();
					controll.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("fail");
				}
				return true;
			}
			else{
				return false;
			}
		}else return false;
	}
	
	/**
	 * Sends the Camerasettings to the Android-Application
	 * @param settings The Camerasettings
	 * @return True if successful
	 */
	public boolean send_camera_settings(String settings)
	{
		if(socket_controll!=null){
			if(!socket_controll.isClosed())
			{
				String buffersettings = "2;" + settings;
				try {
					controll.write(buffersettings);
					controll.newLine();
					controll.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("fail");
				}
				return true;
			}
			else{
				return false;
			}
		}else return false;
	}
	
	/**
	 * Sends soundrecording or soundoutput signals to the Android-Application
	 * @param sound_id
	 * @return True if successful
	 */
	public boolean send_sound(String sound_id)
	{
		if(socket_controll!=null){
			if(!socket_controll.isClosed())
			{
				String buffersound_id = "3;" + sound_id;
				try {
					controll.write(buffersound_id);
					controll.newLine();
					controll.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("fail");
				}
				return true;
			}
			else{
				return false;
			}
		} else return false;
	}

	public void close() {
		try {
			socket_controll.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
