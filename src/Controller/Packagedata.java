package Controller;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;

import Model.GPSTrack;
import View.GUI_Computer;

/**
 * Methods receive the data strings from the Android and send them to the GUI.
 * @author Christian Schulze
 * @author Michael S
 * @author Daniel W
 * @version 1.0
 */


public class Packagedata {
			
	Controller_Computer controller_computer;
	GUI_Computer guiComputer;
	String [] tokens;
	GPSTrack gpstrack;
	public int Vibration=0;
	private int BatteryLevel=100;
	
	public Packagedata(Controller_Computer ControllerComputer,GUI_Computer GuiComputer){
		controller_computer = ControllerComputer;
		guiComputer = GuiComputer;
	}
	
	/**
	 * This method gets the data- String and split it into an array.
	 * Then the informationbox will be updated.
	 * @param data is the received String from the Android.
	 */
	
	public void receive_package(String data) {
		tokens = data.split(";",-1);
		for (int i = 0; i < tokens.length; i++) tokens[i] = tokens[i].trim(); //Leerzeichen weg vorn und hinten
		if (tokens[0].equals("1"))
			updateInformationbox();
		if (tokens[0].equals("2"))
			updateItemIndex();
		if (tokens[0].equals("3"))
			updateVibration();
		if (tokens[0].equals("4"))
			updateCarData();
		if (tokens[0].equals("5")){
			controller_computer.gui_computer.carConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/red_point.png")));
		}
	}
	
	/**
	 * This method send the separate information to the GUI. 
	 */
	
	private void updateInformationbox(){
		double doublelong= ((double)Math.round(Double.parseDouble(tokens[5]) * 1000000)) / 1000000;
		double doublelat= ((double)Math.round(Double.parseDouble(tokens[6]) * 1000000)) / 1000000;
		String Long = String.valueOf(doublelong);
		String Lat = String.valueOf(doublelat);
		
		if(!(Long.equals("0.0")&Lat.equals("0.0"))){	
			controller_computer.gui_computer.longitude.setText(Long);
			controller_computer.gui_computer.latitude.setText(Lat);
			controller_computer.gpstrack.writegpxfile(String.valueOf(tokens[5]), String.valueOf(tokens[6]));
		}else{
			controller_computer.gui_computer.longitude.setText("N/A");
			controller_computer.gui_computer.latitude.setText("N/A");
		}
		
		if (tokens[4].equals("1"))
			controller_computer.gui_computer.connection_type.setText("WLAN");
		if (tokens[3].equals("1"))
			controller_computer.gui_computer.connection_type.setText("Mobile Internet");

		//MobileAvailable tokens[1]; 
		//WLANAvailable tokens[2];
		//Mobile tokens[3];
		//WLAN tokens[4];
	}
	
	private void updateItemIndex(){
		int itemslength = tokens.length;
		String[] resolution = new String[itemslength-1];
		
		for (int i = 0; i<itemslength-1; i++){
			resolution[itemslength-2-i] = tokens[i+1];
		}
		controller_computer.gui_computer.FillResolutionbox(resolution);
	}
	
	private void updateVibration(){
		Vibration = (int)(Float.parseFloat(tokens[1])*10);
		Vibration = (int)Math.sqrt(Vibration*Vibration);
		if(Vibration>100)
			Vibration = 100;
		controller_computer.car_controller.UpdateVibration(((float)Vibration)/100);
		BatteryLevel = (int)(Float.parseFloat(tokens[2])); //Angabe des prozentualen Batteriestandes
		controller_computer.gui_computer.setPhoneBatteryLevel(BatteryLevel);
	}
	
	private void updateCarData(){
		guiComputer.carCurrent.setText(tokens[1] + "mA");
		guiComputer.carBatteryAbsolute.setText(tokens[2] + "mAh");
		guiComputer.carBatteryProcetual.setText(tokens[3] + "%");
		guiComputer.carVoltage.setText( String.valueOf(Float.parseFloat(tokens[4]) / 10) + "V");
		guiComputer.carTemperature.setText(tokens[5] + "°C");
		guiComputer.refreshUltrasoundImage(true, (int) (Float.parseFloat(tokens[6])));
		guiComputer.refreshUltrasoundImage(false, (int) (Float.parseFloat(tokens[7])));
		
		guiComputer.carConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/green_point.png")));
	}
	
	public int GetBatteryLevel()
	{
		return BatteryLevel;
	}
	
}
	

