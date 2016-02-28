package View;

import Controller.*;
import Model.*;
import Model.Log.ErrorState;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JProgressBar;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Insets;


/** Class for graphical user interface with methods for creating the window.
*
* @author Benjamin L
* @author Lars
* @author Michael S
* @author Daniel W
* @version 1.0
*/

public class GUI_Computer extends JFrame{
	
	static BufferedReader language_reader;
	Log log;
	GPSTrack gpstrack;
	Controller_Computer controller_Computer;
	
	public JTabbedPane tabPane;
	public JTextPane Live_Log;
	public JSlider speed_slider;
	public JSlider angle_slider;
	public JLabel connection_type;
	public JLabel latitude;
	public JLabel longitude;
	public JLabel up;
	public JLabel down;
	public JLabel left;
	public JLabel right;
	public JLabel image;
	public JProgressBar speedForwardProgressBar = new JProgressBar();
	public JProgressBar speedBackwardProgressBar = new JProgressBar();
	public JProgressBar angleLeftProgressBar = new JProgressBar();
	public JProgressBar angleRightProgressBar = new JProgressBar();
	public JLabel carBatteryProcetual;
	public JLabel carBatteryAbsolute;
	public JLabel carVoltage;
	public JLabel carCurrent;
	public JLabel carTemperature;
	public JLabel carConnection;
	public JLabel phoneBattery;
	public JLabel phoneConnection; 
	public JLabel ultrasoundFrontLabel = new JLabel();
	public JLabel ultrasoundBackLabel = new JLabel();
	
	
	
	JPanel panel_video;

	Boolean filled=false;
	
	//initiate JComboBox
	JComboBox resolution_change = new JComboBox();
	JComboBox gamepad_change = new JComboBox();
	JComboBox ip_comboBox = new JComboBox();
	JButton connect_button = new JButton();
	JButton gamepadRefresh = new JButton();
	JToggleButton light_button = new JToggleButton();
	JToggleButton emergencyStop_button = new JToggleButton();
	

	//initiate icons
	ImageIcon up_icon = new ImageIcon("src/View/Icons/Icon_up.gif");
	ImageIcon down_icon = new ImageIcon("src/View/Icons/Icon_down.gif");
	ImageIcon left_icon = new ImageIcon("src/View/Icons/Icon_left.gif");
	ImageIcon right_icon = new ImageIcon("src/View/Icons/Icon_right.gif");
	ImageIcon up_pressed_icon = new ImageIcon("src/View/Icons/Icon_up_pressed.gif");
	ImageIcon down_pressed_icon = new ImageIcon("src/View/Icons/Icon_down_pressed.gif");
	ImageIcon left_pressed_icon = new ImageIcon("src/View/Icons/Icon_left_pressed.gif");
	ImageIcon right_pressed_icon = new ImageIcon("src/View/Icons/Icon_right_pressed.gif");
	
	//initiate borders
	Border unpressed_border = BorderFactory.createRaisedBevelBorder();
	Border pressed_border = BorderFactory.createLoweredBevelBorder();
	
	/** Constructor which creates the graphical user interface.
	 */
	public GUI_Computer(){
		
		// set icon-image for gui
		setIconImage(Toolkit.getDefaultToolkit().getImage(GUI_Computer.class.getResource("/View/Icons/Icon_main.png")));
		
		//create objects of other classes
		log  = new Log(this);
		gpstrack = new GPSTrack();
		controller_Computer = new Controller_Computer(log, this, gpstrack);
		
		//create programwindow
		this.initWindow();
				
		this.setExtendedState(GUI_Computer.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(800,650));
		this.setTitle("Carduinodroid Desktop 1.0");
		
		//window listener for closing
		this.addWindowListener(new WindowAdapter() {
			
			/** Describes what happens when window is closed.
			 * 
			 * @param arg0		argument when window was closed
			 * @return			void
			 */
			public void windowClosed(WindowEvent arg0) {
//				gpstrack.savegpxfile();
//				log.writelogfile("CarDuinoDroid closed.");
//				System.exit(0);
				controller_Computer.network.close();
				quitProcedure();
				
			}
			
			/** Describes what happens when window is closed by pressing the cross in window.
			 * 
			 * @param arg0		argument when pressing the closebutton of window
			 * @return			void
			 */
			public void windowClosing(WindowEvent arg0) {
//				gpstrack.savegpxfile();
//				log.writelogfile("CarDuinoDroid closed.");
//				System.exit(0);
				quitProcedure();
			}
		});
		
		//KeyListener
		this.addKeyListener(new Direction_KeyListener(this, controller_Computer));		
	}
	
	//method: create window
	/** Creates window with all elements.
	 */
	private void initWindow(){
		
		//read in names of elements (dependently on languagefile)
		ArrayList<String> Names = Language();
		
		//read in language filenames
		File language_file = new File("src/View/languages/");
		
		//initiate Strings
		String languages[] = language_file.list();
		for (int i = 0; i <= languages.length-1; i++) languages[i] = languages[i].substring(0, languages[i].indexOf('.'));

		JButton language_ok_button = new JButton(Names.get(25));
		
		//initiate ComboBoxes
		JComboBox language_ComboBox = new JComboBox(languages);
		
		//initiate ButtonGroups
		ButtonGroup Camerachoice = new ButtonGroup(); 
		
		//initiate TextPanes
		Live_Log = new JTextPane();
		
		//initiate ScrollPanes
		JScrollPane Live_Log_Scrollbar = new JScrollPane(Live_Log, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//set Cursor of TextPane always at the end
		DefaultCaret caret = (DefaultCaret)Live_Log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				
		//initiate Borders
		Border live_log_border = BorderFactory.createTitledBorder("Log:");
		up = new JLabel(up_icon);
		up.setBounds(56, 39, 20, 20);
		down = new JLabel(down_icon);
		down.setBounds(56, 70, 20, 20);
		left = new JLabel(left_icon);
		left.setBounds(26, 70, 20, 20);
		right = new JLabel(right_icon);
		right.setBounds(86, 70, 20, 20);
		image = new JLabel();
		image.setOpaque(false);
		
		//initiate JDialoges
		JDialog language_dialog = new JDialog();

		//edit Layout
		getContentPane().setLayout(new BorderLayout());
	
		//initiate Buttons
		JButton map_button = new JButton(Names.get(9));
		map_button.setBounds(28, 249, 87, 23);
		
		JButton connect_button_statusbar = new JButton();
		connect_button_statusbar.setMaximumSize(new Dimension(25, 25));
		connect_button_statusbar.setMinimumSize(new Dimension(25, 25));
		connect_button_statusbar.setPreferredSize(new Dimension(25, 25));
		Image connectIcon = new ImageIcon(this.getClass().getResource("/View/Icons/connect.png"))
		.getImage().getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		connect_button_statusbar.setIcon(new ImageIcon(connectIcon));
		connect_button_statusbar.setToolTipText(Names.get(20));
//		connect_button_statusbar.setText(Names.get(20));
		connect_button_statusbar.setBounds(150, 320, 112, 23);
		connect_button_statusbar.setFocusable(false);
							
		//initiate JPanels, JTabbedPane
		tabPane = new JTabbedPane();
		tabPane.setPreferredSize(new Dimension(320, 600));
		
		JPanel panel_settings = new JPanel();
		panel_video = new JPanel();
		JPanel panelCar = new JPanel();
		JPanel panelStatus = new JPanel();
		
		panel_video.add(image);
		this.getContentPane().add(panel_video, BorderLayout.CENTER);
		this.getContentPane().add(tabPane,BorderLayout.EAST);
		this.getContentPane().add(panelStatus, BorderLayout.SOUTH);
		tabPane.addTab(Names.get(29), null, panelCar, null);
		tabPane.addTab(Names.get(4), null, panel_settings, null);
		
		panel_settings.setPreferredSize(new Dimension(400, 500));
		panel_settings.setFocusable(false);
		panel_settings.setLayout(null);
		Image refreshIcon = new ImageIcon(this.getClass().getResource("/View/Icons/refresh.png"))
			.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		map_button.setFocusable(false);
		
		JPanel panel_camera = new JPanel();
		panel_camera.setBorder(new TitledBorder(null, Names.get(6), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_camera.setBounds(12, 12, 291, 141);
		panel_settings.add(panel_camera);
		panel_camera.setLayout(null);
		
		//initiate JLabels
		JLabel resolution_list_text = new JLabel(Names.get(10) + ": ");
		resolution_list_text.setBounds(10, 21, 112, 14);
		panel_camera.add(resolution_list_text);
		JLabel quality_label = new JLabel(Names.get(27) + ": ");
		quality_label.setBounds(10, 46, 115, 14);
		panel_camera.add(quality_label);
		resolution_change.setBounds(100, 18, 179, 20);
		panel_camera.add(resolution_change);
		resolution_change.addActionListener(new Resolution_ActionListener(controller_Computer, log, resolution_change));
		resolution_change.setFocusable(false);
		
		//initiate JSliders
		JSlider quality_slider = new JSlider(1, 100, 30);
		quality_slider.setBounds(100, 46, 179, 23);
		panel_camera.add(quality_slider);
		quality_slider.addMouseListener(new Quality_MouseListener(quality_slider, controller_Computer));
		quality_slider.setFocusable(false);
		
		//initiate RadionButtons
		JRadioButton Frontcamera = new JRadioButton(Names.get(7),true);
		Frontcamera.setBounds(10, 85, 109, 23);
		panel_camera.add(Frontcamera);
		//Frontcamera.setFocusable(false);
		
		Camerachoice.add(Frontcamera);
		JRadioButton Backcamera = new JRadioButton(Names.get(8));
		Backcamera.setBounds(10, 111, 109, 23);
		panel_camera.add(Backcamera);
		//Backcamera.setFocusable(false);

	
		Camerachoice.add(Backcamera);
		
		
//		panel_settings.add(map_button);
		map_button.addActionListener(new Map_ActionListener(controller_Computer, latitude, longitude));
		
		JPanel panel_connection = new JPanel();
		panel_connection.setBorder(new TitledBorder(null, Names.get(2), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_connection.setBounds(12, 165, 291, 141);
		panel_settings.add(panel_connection);
		panel_connection.setLayout(null);
		JLabel connection_type_text = new JLabel(Names.get(2) + ": ");
		connection_type_text.setBounds(10, 26, 112, 14);
		panel_connection.add(connection_type_text);
		
		JLabel present_ip_text = new JLabel(Names.get(19) + ": ");
		present_ip_text.setBounds(10, 51, 112, 14);
		panel_connection.add(present_ip_text);
		ip_comboBox.setBounds(10, 76, 112, 20);
		panel_connection.add(ip_comboBox);
		
		// IP-Address-Box
//		ip_comboBox = new JComboBox();
		ip_comboBox.setEditable(true);
		ip_comboBox.setSelectedIndex(-1);
		ip_comboBox.addActionListener(new IP_ComboBox_ActionListener(ip_comboBox));
		connect_button.setBounds(10, 107, 112, 23);
		panel_connection.add(connect_button);
		connect_button.setText(Names.get(20));
		connect_button.setFocusable(false);
		JLabel ip_label = new JLabel();
		ip_label.setBounds(89, 51, 100, 14);
		panel_connection.add(ip_label);
		connect_button_statusbar.addActionListener(new Connect_ActionListener(ip_comboBox, log, ip_label, this));
		connect_button.addActionListener(new Connect_ActionListener(ip_comboBox, log, ip_label, this));
		connection_type = new JLabel();
		connection_type.setBounds(89, 26, 70, 14);
		panel_connection.add(connection_type);
		
		JPanel panel_advancedSettings = new JPanel();
		panel_advancedSettings.setBorder(new TitledBorder(null, Names.get(43), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_advancedSettings.setBounds(12, 318, 291, 90);
		panel_settings.add(panel_advancedSettings);
		panel_advancedSettings.setLayout(null);
		JButton languageButton = new JButton(Names.get(24));
		languageButton.setBounds(10, 55, 100, 23);
		panel_advancedSettings.add(languageButton);
		JLabel gamepadLabel = new JLabel(Names.get(30));
		gamepadLabel.setBounds(10, 24, 60, 14);
		panel_advancedSettings.add(gamepadLabel);
		gamepad_change.setBounds(80, 24, 112, 20);
		panel_advancedSettings.add(gamepad_change);
		gamepad_change.addActionListener(new ChooseGamepad_ActionListener(controller_Computer, log));
		gamepad_change.setFocusable(false);
		JButton aboutButton = new JButton(Names.get(44));
		aboutButton.setHorizontalAlignment(SwingConstants.LEFT);
		aboutButton.setContentAreaFilled(false);
		aboutButton.setFont(new Font("Dialog", Font.ITALIC, 12));
		aboutButton.setBorderPainted(false);
		aboutButton.setBorder(null);
		aboutButton.setBounds(12, 87, 267, 23);
		gamepadRefresh.setBounds(215, 24, 25, 25);
		panel_advancedSettings.add(gamepadRefresh);
		gamepadRefresh.setIcon(new ImageIcon(refreshIcon));
		
		JPanel panel_about = new JPanel();
		panel_about.setBorder(new TitledBorder(null, Names.get(3), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_about.setBounds(12, 420, 291, 122);
		panel_settings.add(panel_about);
		panel_about.setLayout(null);
		panel_about.add(aboutButton);
		
		JLabel aboutLabel1 = new JLabel(Names.get(45));
		aboutLabel1.setBounds(12, 29, 267, 16);
		panel_about.add(aboutLabel1);
		
		JLabel aboutLabel2 = new JLabel(Names.get(22));
		aboutLabel2.setBounds(12, 57, 267, 16);
		panel_about.add(aboutLabel2);
		gamepadRefresh.addActionListener(new gamepadRefresh_ActionListener(this, log));		
		aboutButton.addActionListener(new About_ActionListener(Names.get(23)));
		languageButton.addActionListener(new Language_ActionListener(language_dialog, language_ComboBox, Names.get(24), Names.get(26), language_ok_button));
		Backcamera.addActionListener(new SwitchCameraType_ActionListener(controller_Computer, log, false));
		Frontcamera.addActionListener(new SwitchCameraType_ActionListener(controller_Computer, log, true));

		tabPane.addTab(Names.get(31), null, Live_Log_Scrollbar, null);
		panel_video.setBackground(Color.lightGray);
		panel_video.setPreferredSize(new Dimension(250,250));
		panel_video.setMinimumSize(new Dimension(250,250));
		Live_Log_Scrollbar.setPreferredSize(new Dimension(400,500));
		panel_video.setFocusable(false);
		
		panelStatus.setPreferredSize(new Dimension(500,30));
		panelStatus.setLayout(new GridLayout());
		
		//menu "Preferences" including submenus

		language_ok_button.addActionListener(new language_ok_button_ActionListener(language_dialog, language_ComboBox));
		
		up.setBorder(unpressed_border);
		down.setBorder(unpressed_border);
		left.setBorder(unpressed_border);
		right.setBorder(unpressed_border);
		panelCar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_cockpit = new JPanel();
		panel_cockpit.setPreferredSize(new Dimension(300, 470));
		panel_cockpit.setMinimumSize(new Dimension(300, 470));
		panel_cockpit.setBorder(new TitledBorder(null, "Cockpit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCar.add(panel_cockpit);
		panel_cockpit.setLayout(null);
		
		JPanel panel_coordinates = new JPanel();
		panel_coordinates.setBounds(28, 11, 250, 75);
		panelCar.add(panel_coordinates);
		panel_coordinates.setPreferredSize(new Dimension(300,80));
		panel_coordinates.setLayout(null);
		panel_coordinates.setBorder(new TitledBorder(null, Names.get(11), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JLabel latitude_text = new JLabel(Names.get(17) + ": ");
		latitude_text.setBounds(10, 21, 62, 14);
		panel_coordinates.add(latitude_text);
		JLabel longitude_text = new JLabel(Names.get(18) + ": ");
		longitude_text.setBounds(10, 46, 62, 14);
		panel_coordinates.add(longitude_text);
		latitude = new JLabel();
		latitude.setBounds(137, 21, 70, 14);
		panel_coordinates.add(latitude);
		longitude = new JLabel();
		longitude.setBounds(137, 46, 70, 14);
		panel_coordinates.add(longitude);
		
		//initiate ToggleButtons
//		JToggleButton light_button = new JToggleButton(Names.get(5), false);
		light_button.setBounds(250, 399, 25, 25);
		panel_cockpit.add(light_button);
		Image lightIcon = new ImageIcon(this.getClass().getResource("/View/Icons/scheinwerfer3.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		light_button.setIcon(new ImageIcon(lightIcon));
		light_button.addActionListener(new Light_ActionListener(controller_Computer, log, light_button));
		light_button.setFocusable(false);
		light_button.setToolTipText(Names.get(39));
//		JButton signal_button = new JButton(Names.get(13));
		JButton signal_button = new JButton();
		signal_button.setBounds(250, 363, 25, 25);
		panel_cockpit.add(signal_button);
		Image signalIcon = new ImageIcon(this.getClass().getResource("/View/Icons/lautsprecher.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		signal_button.setIcon(new ImageIcon(signalIcon));
		signal_button.addMouseListener(new Signal_ActionListener(controller_Computer));
		signal_button.setToolTipText(Names.get(40));
		signal_button.setFocusable(false);
		speed_slider = new JSlider(0, 100, 20);
		speed_slider.setBounds(250, 110, 33, 217);
		panel_cockpit.add(speed_slider);
		speed_slider.setSnapToTicks(true);
		speed_slider.setMajorTickSpacing(20);
		speed_slider.setPaintTicks(true);
		speed_slider.setOrientation(SwingConstants.VERTICAL);
		speed_slider.setFocusable(false);
		JLabel speed_label = new JLabel(Names.get(15) + ": ");
		speed_label.setBounds(220, 88, 70, 14);
		panel_cockpit.add(speed_label);
		angle_slider = new JSlider(0, 100, 50);
		angle_slider.setBounds(104, 348, 113, 23);
		panel_cockpit.add(angle_slider);
		angle_slider.setPaintTicks(true);
		angle_slider.setSnapToTicks(true);
		angle_slider.setMajorTickSpacing(50);
		angle_slider.setMinorTickSpacing(10);
		angle_slider.setFocusable(false);
		JLabel angle_label = new JLabel(Names.get(16) + ": ");
		angle_label.setBounds(10, 357, 108, 14);
		panel_cockpit.add(angle_label);
		
		// Car icon
		JLabel Car_Label = new JLabel();
		Car_Label.setBounds(51, 110, 120, 200);
		panel_cockpit.add(Car_Label);
		Image carIcon = new ImageIcon(this.getClass().getResource("/View/Icons/car_colored2.png")).getImage().getScaledInstance(120, 200, Image.SCALE_SMOOTH);
		Car_Label.setIcon(new ImageIcon(carIcon));
		speedForwardProgressBar.setForeground(new Color(50, 205, 50));
		speedForwardProgressBar.setBorder(null);
		speedForwardProgressBar.setBorderPainted(false);
		speedForwardProgressBar.setBounds(220, 113, 20, 204);
		panel_cockpit.add(speedForwardProgressBar);
		
		speedForwardProgressBar.setOrientation(SwingConstants.VERTICAL);
		speedBackwardProgressBar.setBorder(null);
		speedBackwardProgressBar.setBorderPainted(false);
		speedBackwardProgressBar.setBounds(220, 318, 20, 54);
		panel_cockpit.add(speedBackwardProgressBar);
		
		speedBackwardProgressBar.setBackground(new Color(50, 205, 50));
		speedBackwardProgressBar.setForeground(UIManager.getColor("ColorChooser.background"));
		speedBackwardProgressBar.setValue(100);
		speedBackwardProgressBar.setOrientation(SwingConstants.VERTICAL);
		angleLeftProgressBar.setBorderPainted(false);
		angleLeftProgressBar.setBorder(null);
		angleLeftProgressBar.setBounds(10, 317, 100, 20);
		panel_cockpit.add(angleLeftProgressBar);
		angleLeftProgressBar.setValue(100);
		
		
		angleLeftProgressBar.setForeground(UIManager.getColor("Button.background"));
		angleLeftProgressBar.setBackground(new Color(50, 205, 50));
		angleRightProgressBar.setForeground(new Color(50, 205, 50));
		angleRightProgressBar.setBorderPainted(false);
		angleRightProgressBar.setBorder(null);
		angleRightProgressBar.setBounds(111, 317, 100, 20);
		panel_cockpit.add(angleRightProgressBar);
		emergencyStop_button.setForeground(Color.BLACK);
//		JToggleButton soundrecord_button = new JToggleButton(Names.get(14), false);
//		JToggleButton emergencyStop_button = new JToggleButton(Names.get(14), false);
		emergencyStop_button.setBounds(250, 434, 25, 25);
		panel_cockpit.add(emergencyStop_button);
		Image emergencyStopOnIcon = new ImageIcon(this.getClass().getResource("/View/Icons/stop.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		emergencyStop_button.setIcon(new ImageIcon(emergencyStopOnIcon));
		Image emergencyStopOffIcon = new ImageIcon(this.getClass().getResource("/View/Icons/stop_grey.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		emergencyStop_button.setSelectedIcon(new ImageIcon(emergencyStopOffIcon));
		emergencyStop_button.addActionListener(new EmergencyStop_ActionListener(controller_Computer, log, emergencyStop_button));
		emergencyStop_button.setFocusable(false);
		emergencyStop_button.setToolTipText(Names.get(41));
		
		ultrasoundFrontLabel.setBounds(10, 21, 200, 75);
		panel_cockpit.add(ultrasoundFrontLabel);
		
		
		ultrasoundBackLabel.setBounds(10, 384, 200, 75);
		panel_cockpit.add(ultrasoundBackLabel);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(110, 314, 70, 26);
		panel_cockpit.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(217, 317, 26, 10);
		panel_cockpit.add(separator_1);
				
		carBatteryProcetual= new JLabel();
		carBatteryProcetual.setToolTipText(Names.get(35));
		carBatteryAbsolute= new JLabel();
		carBatteryAbsolute.setToolTipText(Names.get(36));
		carVoltage= new JLabel();
		carVoltage.setToolTipText(Names.get(32));
		carCurrent= new JLabel();
		carCurrent.setToolTipText(Names.get(33));
		carTemperature= new JLabel();
		carTemperature.setToolTipText(Names.get(34));
		carConnection= new JLabel();
		carConnection.setToolTipText(Names.get(37));
		phoneBattery= new JLabel();
		phoneBattery.setToolTipText(Names.get(42));
		phoneConnection= new JLabel(); 
		phoneConnection.setToolTipText(Names.get(38));
		Image carSmallIcon = new ImageIcon(this.getClass().getResource("/View/Icons/carSmallIcon.png"))
							.getImage().getScaledInstance(22, 20, java.awt.Image.SCALE_SMOOTH);
		panelStatus.add(new JLabel(new ImageIcon(carSmallIcon)));
		panelStatus.add(carVoltage);
		panelStatus.add(carCurrent);
		panelStatus.add(carBatteryAbsolute); 
		panelStatus.add(carBatteryProcetual); 
		panelStatus.add(carTemperature);
		panelStatus.add(carConnection); 
		panelStatus.add(new JSeparator(SwingConstants.VERTICAL));
		Image phoneIcon = new ImageIcon(this.getClass().getResource("/View/Icons/smartphone.png"))
							.getImage().getScaledInstance(13, 20, java.awt.Image.SCALE_SMOOTH);
		panelStatus.add(new JLabel(new ImageIcon(phoneIcon)));
		panelStatus.add(phoneBattery); 
		panelStatus.add(phoneConnection);	
		panelStatus.add(connect_button_statusbar);
		
		//Live-Log screen
		Live_Log.setEditable(false);
		Live_Log.setFocusable(false);
		Live_Log_Scrollbar.setBorder(live_log_border);
		
		resetStatusbar();
		restoreIPList();
		refreshUltrasoundImage(true, 0);
		refreshUltrasoundImage(false, 0);
	}
	
	/** Method for read language.txt.
	 * 
	 * @return			Names of elements of window.
	 */
	private ArrayList<String> Language(){
		ArrayList<String> Language_Name = new ArrayList<String>();
		String Language = null;
		
		try {
			language_reader = new BufferedReader (new FileReader("src/View/language.txt"));
			Language = language_reader.readLine();
		} catch (FileNotFoundException e){
			log.writelogfile("language.txt doesn't exist!", ErrorState.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Language_Name = Language_name(Language);
		return Language_Name;
	}
	
	/** Method for read in language (names of different elements).
	 * 
	 * @param Language	Language which is written in the language.txt
	 * @return			Names of elements of window.
	 */
	private ArrayList<String> Language_name (String Language){
		ArrayList<String> Name = new ArrayList<String>();
		String Line = null;
		
		try {
			language_reader = new BufferedReader(new FileReader("src/View/languages/" + Language + ".txt"));
			while((Line = language_reader.readLine()) != null) Name.add(Line);
		} catch (FileNotFoundException e) {
			log.writelogfile(Language + ".txt doesn't exist!" , ErrorState.ERROR);
			Name = Language_name("english");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Name;
	}
	
	/** Method for filling the resolution Combo Box.
	 */
	public void FillResolutionbox(String [] resolution_list){
		filled=true;
		int count = resolution_change.getItemCount();
		int selectedItem = resolution_change.getSelectedIndex();
		
		resolution_change.removeAllItems();
		for (int i = 0; i <= resolution_list.length-1; i++) {
			filled=true;
			resolution_change.addItem(resolution_list[i]);
		}
		filled=false;
		if(count == 0) resolution_change.setSelectedIndex(resolution_list.length-1);
		else resolution_change.setSelectedIndex(selectedItem);
	}
	/** Method for filling the Gamepad Combo Box.
	 */
	public void FillGamepadBox(String [][] Gamepad_list){
		for (int i = 0; i <= Gamepad_list.length-1; i++) {
			if(Gamepad_list[i][1]!=null)
			gamepad_change.addItem(Gamepad_list[i][1]);
		}
		gamepad_change.setSelectedIndex(0);
	}
	
	/** Method for feedback, when car drives forward.
	 */
	public void PressedBorderUp(){
		up.setBorder(pressed_border);
		up.setIcon(up_pressed_icon);
	}
	
	/** Method for feedback, when car drives back.
	 */
	public void PressedBorderDown(){
		down.setBorder(pressed_border);
		down.setIcon(down_pressed_icon);
	}
	
	/** Method for feedback, when car drives right.
	 */
	public void PressedBorderRight(){
		right.setBorder(pressed_border);
		right.setIcon(right_pressed_icon);
	}
	
	/** Method for feedback, when car drives left.
	 */
	public void PressedBorderLeft(){
		left.setBorder(pressed_border);
		left.setIcon(left_pressed_icon);
	}
	
	/** Method for feedback, when car stops driving forward.
	 */
	public void UnpressedBorderUp(){
		if (up != null){
			up.setBorder(unpressed_border);
			up.setIcon(up_icon);
		}
	}
	
	/** Method for feedback, when car stops driving back.
	 */
	public void UnpressedBorderDown(){
		if (down != null){
			down.setBorder(unpressed_border);
			down.setIcon(down_icon);
		}
	}
	
	/** Method for feedback, when car stops driving right.
	 */
	public void UnpressedBorderRight(){
		if (right != null){
			right.setBorder(unpressed_border);
			right.setIcon(right_icon);
		}
	}
	
	/** Method for feedback, when car stops driving left.
	 */
	public void UnpressedBorderLeft(){
		if (left != null){
			left.setBorder(unpressed_border);
			left.setIcon(left_icon);
		}
	}
	
	public int getPanelHeight(){
		return panel_video.getHeight();
	}
	
	public boolean getFilled(){
		return filled;
	}
	
	public void setFilled(boolean Filled){
		filled = Filled;
	}
	
	public int getPanelWidth(){
		return panel_video.getWidth();
	}
	
	/** Method for saving the List of Ip´s from the ip Combobox into File IPList.txt.
	 */
	public void saveIPList(){
		File ipFile = new File("src/", "IPList.txt");
		try {
			ipFile.createNewFile();
		} catch (IOException e) { e.printStackTrace(); }
		ipFile.canWrite();
		ipFile.canRead();
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(ipFile));
			for (int i = 0; i < ip_comboBox.getItemCount() ; i++){
				writer.write(ip_comboBox.getItemAt(i).toString() + "\n");
			}
			writer.close();
		}catch (IOException e) {
	        System.err.println("Problem writing to the file ipList.txt");
	    }
	}
	
	/** Method for restoring the IP-List from IPList.txt into die ip Combobox.
	 */
	public void restoreIPList(){
		try {
			BufferedReader reader = new BufferedReader (new FileReader("src/IPList.txt"));
			String line = null;
			try {
				while ((line = reader.readLine()) != null ){
						ip_comboBox.addItem(line);
				}
			} catch (IOException e) {
				System.err.println("Could not read IPList.txt");
			}				
		} catch (FileNotFoundException e) {
			System.err.println("IPList.txt not found");
		}
	}
	/** Method for quiting the program and saving all data.
	 */
	public void quitProcedure(){
//		System.out.println("Quit");
		saveIPList();
		gpstrack.savegpxfile();
		log.writelogfile("CarDuinoDroid closed." , ErrorState.INFO);
		System.exit(0);
	}
	
	/** Method for setting the phone battery level in the GUI.
	 */
	public void setPhoneBatteryLevel(int level){
		phoneBattery.setText(String.valueOf(level) + "%");
		phoneBattery.setForeground(batteryColor(level));
	}
	
	/** Method for generating a color by computing the level.
	 */
	private Color batteryColor(int level){
		if(level>60)
		{
			return Color.BLACK;
		}else{
			if(level>20)
			{
				return Color.ORANGE;
			}else{
				return Color.RED;
			}
		}
	}
	
	/** Method for reseting the Statusbar.
	 */
	public void resetStatusbar(){
		carVoltage.setText("N/A");
		carCurrent.setText("N/A");
		carBatteryAbsolute.setText("N/A");
		carBatteryProcetual.setText("N/A");
		carBatteryProcetual.setForeground(batteryColor(100));
		carTemperature.setText("N/A");
		carConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/red_point.png")));
		phoneBattery.setText("N/A");
		phoneBattery.setForeground(batteryColor(100));
		phoneConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/red_point.png")));		
	}
	
	public void toggleLight(){
		light_button.doClick();
	}
	
	public void refreshUltrasoundImage(boolean front, int distance){
		Image USFrontImage;
		Image USBackImage;
		if (front){
			if(distance < 25){USFrontImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundFront0.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 50){USFrontImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundFront1.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 100){USFrontImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundFront2.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 150){USFrontImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundFront3.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else{USFrontImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundFront4.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			ultrasoundFrontLabel.setIcon(new ImageIcon(USFrontImage));
		}else{
			if(distance < 25){USBackImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundBack0.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 50){USBackImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundBack1.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 100){USBackImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundBack2.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else if(distance < 150){USBackImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundBack3.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			else{USBackImage =  new ImageIcon(this.getClass().getResource("/View/Icons/UltrasoundBack4.png")).getImage().getScaledInstance(200, 75, Image.SCALE_SMOOTH);}
			ultrasoundBackLabel.setIcon(new ImageIcon(USBackImage));
		}
	}
}