package Controller;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import Model.Log.ErrorState;
import View.GUI_Computer;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;

/**
* methods for control signals
* @author Lars Vogel
* @author Michael S
* @author Daniel W
* @version 1.0
*/

public class Car_Controller {
	Controller_Computer controller_computer;
	
	//initiate all used variables
	int delay=0;
	boolean up = false, down = false, right = false, left = false, front=true;
	float Vibration = 0;
	String[][] ListGamePad = null;
	int numberGamepads = 0;
	int Gamepadnum = 0;
	boolean drive = false;
	float x_value=0;
	boolean rumblerout = true;
	
	boolean emergencyStop = true; // the car stops/slows down automatically when too near to object
	
	//constructor of the timer
	Timer controlsignal = new Timer();
	Controller[] controller = null;
	EventQueue queue = null;
	Event event = new Event();
	Component comp = null;
	private Rumbler[] rumblers;
	int rumblerIdx;
	
	//constructor of the timer task
	TimerTask ControlTask = new TimerTask(){
		public void run() {
			//Auslesen Controller
			if(numberGamepads!=0){
				controller[Gamepadnum].poll();
				while(queue.getNextEvent(event)) {
					   FindTyp(event.getComponent(),event.getValue());
		        }
		        try {Thread.sleep(100);
	             } catch (InterruptedException e) {
	                e.printStackTrace();
	             }
			}
			
			
			//Vibration einbinden, falls möglich
			//controller_computer.log.writelogfile("Vib: "+Vibration+" Boolean: "+rumblerout);
			if(rumblerIdx != -1 && rumblerout!=true){
				rumblers[rumblerIdx].rumble(Vibration);
				if(Vibration==0)
					rumblerout=true;}
			
			//If someone pushes the up or down key, the Controlsignal will be send
			if((!up&&down)||(up&&!down)||(!up&&!down&&(left||right))){
				int speed = 0;
				int angle = 0;
				if(up){speed = controller_computer.gui_computer.speedForwardProgressBar.getValue();}
				if(down){speed = 100-controller_computer.gui_computer.speedBackwardProgressBar.getValue();}
				if(left){angle = 100-controller_computer.gui_computer.angleLeftProgressBar.getValue();}
				if(right){angle = controller_computer.gui_computer.angleRightProgressBar.getValue();}
				
				float speedValue=controller_computer.gui_computer.speedForwardProgressBar.getValue()-(100-controller_computer.gui_computer.speedBackwardProgressBar.getValue());
				float steerValue=controller_computer.gui_computer.angleRightProgressBar.getValue()-(100-controller_computer.gui_computer.angleLeftProgressBar.getValue());
				//if(down) speedValue = speed * (-1); else
				//if(left) steerValue = angle * (-1);
				
				sendIpControlData((int)(1.27*speedValue),(int)(1.27*steerValue));
				/*if(!right&&!left){
					send_controlsignal(SpeedCalculation(speed),0);
					
				}else{
					send_controlsignal(SpeedCalculation(speed),DirectionCalculation(angle));
				}*/
			}
			if(!up&&!down&&!left&&!right)
				controller_computer.camera_picture.UpdateDirection(false, false, false, false);
			//System.out.println("Front: "+controller_computer.gui_computer.speedForwardProgressBar.getValue()+" - Back: "+ (100-controller_computer.gui_computer.speedBackwardProgressBar.getValue())+ " - Right: "+controller_computer.gui_computer.angleRightProgressBar.getValue()+" - Left: "+(100-controller_computer.gui_computer.angleLeftProgressBar.getValue()));
			//With a 200ms period the Buttons will be released, if they are not any longer pushed.
			delay++;
			if(delay==2){
				if(!up){controller_computer.gui_computer.UnpressedBorderUp();}
				if(!down){controller_computer.gui_computer.UnpressedBorderDown();}
				if(!right){controller_computer.gui_computer.UnpressedBorderRight();}
				if(!left){controller_computer.gui_computer.UnpressedBorderLeft();}
				delay=0;
			}
			
			if(controller_computer.network.isClosed()){
				controller_computer.gui_computer.phoneConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/red_point.png")));
				controller_computer.gui_computer.carConnection.setIcon(new ImageIcon(GUI_Computer.class.getResource("/View/Icons/red_point.png")));
			}
		}
	};
	
	// ***** Car_Controller Constructor ***************************************
	/** 
	 * Needs the Controller_Computer to get access to the log and starts the 
	 * timer without any delay and a 100ms period.
	 */
	public Car_Controller(Controller_Computer ControllerComputer){
		controller_computer = ControllerComputer;
		controller = ControllerEnvironment.getDefaultEnvironment().getControllers();
		ListGamePad = new String[controller.length][2];
		FindGamepad();
		findRumblers(controller[Gamepadnum]);
		controlsignal.scheduleAtFixedRate(ControlTask, 0, 100);
	}
	
	// ***** Send Control signal ***************************************
	/** 
	 * Control Signal is the method for all direction commands. It has 2
	 * variables which are already calculated and provides a feedback if 
	 * the sending was successful.
	 */
	/*private void send_controlsignal(int speed,int angle){		
		//System.out.println("Speed: "+speed+" - Steer: "+angle);
		if (controller_computer.network.send_controllsignal(speed+";"+up+";"+angle+";"+right+";"+emergencyStop)){
		 feedback_output();
		 controller_computer.camera_picture.UpdateDirection(up, down, left, right);
		}else{
			controller_computer.camera_picture.UpdateDirection(false,false,false,false);
		}
		//System.out.println("Speed: "+speed+" - Up: "+up+" - Angle: "+angle+" - right: "+right);
	}*/
	
	private void sendIpControlData(int speed, int steer){
		
		if(controller_computer.network.send_controllsignal(speed,steer)){
			controller_computer.camera_picture.UpdateDirection((speed>0), (speed<0), (steer<0), (steer>0));
		}else{
			controller_computer.camera_picture.UpdateDirection(false,false,false,false);
		}
		
	}
	
	// ***** Feedback Output ***************************************
				/** 
				 * It is the method to show which button is pressed.
				 * The feedback works with the timer to check all 200ms
				 * if the button is still pressed.
				 */
	private void feedback_output(){
		if(up){controller_computer.gui_computer.PressedBorderUp();}
		if(down){controller_computer.gui_computer.PressedBorderDown();}
		if(right){controller_computer.gui_computer.PressedBorderRight();}
		if(left){controller_computer.gui_computer.PressedBorderLeft();}
	}	
	
	// ***** Update Variables ***************************************
			/** 
			 * The timer needs the current settings of the keys you are 
			 * pressing. Without variables you won't be able to see what
			 * is still pressed or not.
			 */
	public void UpdateVariables(boolean Up, boolean Down, boolean Right, boolean Left){
		up = Up; down = Down; 
		if(!left)right = Right; 
		if(!right)left = Left;
		CalculateSpeed((float)(controller_computer.gui_computer.speed_slider.getValue()) / 100);
		CalculateDir((float)(controller_computer.gui_computer.angle_slider.getValue()) / 100);
	}
	
	public void UpdateVibration(float vibration)
	{
		Vibration = vibration;
		if(Vibration!=0)rumblerout=false;
	}
	
	public void setEmergencyStop (boolean emergencyStopModus){
		emergencyStop = emergencyStopModus;
	}
	
	// ***** Speed Calculation ***************************************
		/** 
		 * It is a method to calculate the speed for a radio controlled
		 * car and its motor. Different types often need a different range
		 * of voltage.
		 */
	private int SpeedCalculation(int speed)
	{	
		if(up||down)
		return (int) ((speed*1.26)+1); //Speed divided by (100/(parts-1)) = 1-9
		else return (int) (0); 
	}
	
	// ***** Direction Calculation ***************************************
			/** 
			 * It is a method to calculate the direction for a radio controlled
			 * car and its motor. Different types often need a different range
			 * of voltage.
			 */
	private int DirectionCalculation(int angle)
	{	
		return (int) ((angle*1.26)+1); 
	}
	
	public void UpdateCamera(boolean Front)
	{
		
		if(front!=Front)
		{
			if(Front)
			{
				controller_computer.camera_settings.send_change_camera("0");
			}else
			{
				controller_computer.camera_settings.send_change_camera("1");
			}
		}
		front = Front;
	}
	
	public void FindGamepad(){ 	
		for (int i = 0; i < controller.length; i++){
			if((controller[i].getType() == Controller.Type.GAMEPAD) || (controller[i].getType() == Controller.Type.STICK)){
				ListGamePad[numberGamepads][1]= controller[i].getName() + ", " + controller[i].getType();
				ListGamePad[numberGamepads][0]= String.valueOf(i);
				//controller_computer.log.writelogfile(controller[i].getName() + ", " + controller[i].getType() ); 
				numberGamepads ++;	
			}
		} 
		if(numberGamepads!=0){
			controller_computer.gui_computer.FillGamepadBox(ListGamePad);
			Gamepadnum = Integer.parseInt(ListGamePad[0][0]);
			queue= controller[Gamepadnum].getEventQueue();
		}
	}
	
	public void ChoiceGamepad(int itemindex){
		Gamepadnum = itemindex;
		queue = controller[Gamepadnum].getEventQueue();
	}
	
	private void FindTyp(Component comp,float value){
		
//		System.out.println(comp.getIdentifier().getName());
		
		if(comp.getIdentifier().getName().equals("pov")){
			ispov(value);
//			System.out.println(value);
		}else
		{
			if(comp.isAnalog()){
				isAnalog(comp,value);
			}else
			{
				isDigital(comp,value);
			}
		}
	}
	
	private void ispov(float value)
	{
		if(value==0.0f){/*up=false; down=false; right=false;left=false;*/}
		else if(value==0.125f){/*up=true; down=false; right=false; left=true;*/}
		else if(value==0.25f){/*up=true; down=false; right=false; left=false;*/ controller_computer.gui_computer.speed_slider.setValue(controller_computer.gui_computer.speed_slider.getValue() +20);}
		else if(value==0.375f){/*up=true; down=false; right=true; left=false;*/}
		else if(value==0.5f){/*up=false; down=false; right=true; left=false;*/ controller_computer.gui_computer.angle_slider.setValue(controller_computer.gui_computer.angle_slider.getValue() +10);}
		else if(value==0.625f){/*up=false; down=true; right=true; left=false;*/}
		else if(value==0.75f){/*up=false; down=true; right=false; left=false;*/ controller_computer.gui_computer.speed_slider.setValue(controller_computer.gui_computer.speed_slider.getValue() -20);}
		else if(value==0.875f){/*up=false; down=true; right=false; left=true;*/}
		else if(value==1.0f){/*up=false; down=false; right=false; left=true;*/ controller_computer.gui_computer.angle_slider.setValue(controller_computer.gui_computer.angle_slider.getValue() -10);}
	}
	
	private void isAnalog(Component comp,float value){
		if(comp.getIdentifier().getName().equals("rx")){}
		else if(comp.getIdentifier().getName().equals("ry")){}
		else if(comp.getIdentifier().getName().equals("x")){CalculateDir(value);}
		else if(comp.getIdentifier().getName().equals("y")){}
		else if(comp.getIdentifier().getName().equals("z")){drive=true; CalculateSpeed(value);}
		else if(comp.getIdentifier().getName().equals("rz")){}
	}
	
	private void isDigital(Component comp,float value){
		if(comp.getIdentifier().getName().equals("0")){/*findRumblers(controller[Gamepadnum]);*/}
		else if(comp.getIdentifier().getName().equals("1")){System.out.println("Kreis"); controller_computer.gui_computer.toggleLight();}
		else if(comp.getIdentifier().getName().equals("2")){System.out.println("X");}
		else if(comp.getIdentifier().getName().equals("3")){if(value==1.0f) {controller_computer.sound_output.send_output_soundsignal("1");}}
		else if(comp.getIdentifier().getName().equals("4")){System.out.println("L1");}
		else if(comp.getIdentifier().getName().equals("5")){System.out.println("R1");if(value==1.0f){drive=false;up=false;down=false;}else{drive=true;CalculateSpeed(x_value);}}
		else if(comp.getIdentifier().getName().equals("6")){System.out.println("L2");}
		else if(comp.getIdentifier().getName().equals("7")){System.out.println("R2");}
		else if(comp.getIdentifier().getName().equals("8")){System.out.println("Select");}
		else if(comp.getIdentifier().getName().equals("9")){System.out.println("L3");}
		else if(comp.getIdentifier().getName().equals("10")){System.out.println("R3");}
		else if(comp.getIdentifier().getName().equals("11")){System.out.println("Start");}
	}
	
	private void CalculateSpeed(float value){
//		controller_computer.gui_computer.speed_slider.setValue(Math.abs((int)(value*100)));
		int speed = Math.abs((int)(value*100));
		int speedMax = controller_computer.gui_computer.speed_slider.getValue();
		if (speed > speedMax){
			speed = speedMax;
		}
		if (up){
			controller_computer.gui_computer.speedForwardProgressBar.setValue(speed);
			controller_computer.gui_computer.speedBackwardProgressBar.setValue(100);
		}else{
			controller_computer.gui_computer.speedForwardProgressBar.setValue(0);
		}
		if (down){
			controller_computer.gui_computer.speedForwardProgressBar.setValue(0);
			controller_computer.gui_computer.speedBackwardProgressBar.setValue(100-speed);
		}else{
			controller_computer.gui_computer.speedBackwardProgressBar.setValue(100);
		}
		if((int)(value*100)==0){up=false;down=false;}
		else if(value*100<0){up=(true&&drive);down=false;}
		else{up=false;down=(true&&drive);}
		x_value=value;
	}
	
	private void CalculateDir(float value){
//		controller_computer.gui_computer.angle_slider.setValue(Math.abs((int)(value*100)));
		int angle = Math.abs((int)(value*100));
		int angleMax = controller_computer.gui_computer.angle_slider.getValue();
		if (angle > angleMax){
			angle = angleMax;
		}
		if (left){
			controller_computer.gui_computer.angleLeftProgressBar.setValue(100-angle);
			controller_computer.gui_computer.angleRightProgressBar.setValue(0);
		}else{
			controller_computer.gui_computer.angleLeftProgressBar.setValue(100);
		}
		if (right){
			controller_computer.gui_computer.angleLeftProgressBar.setValue(100);
			controller_computer.gui_computer.angleRightProgressBar.setValue(angle);
		}else{
			controller_computer.gui_computer.angleRightProgressBar.setValue(0);
		}
		if((int)(value*100)==0){left=false;right=false;}
		else if(value>0){right=true;left=false;}
		else{right=false;left=true;}
	}
	
	private void findRumblers(Controller controller) 
	{ 
	 rumblers = controller.getRumblers(); 
	 
	 if (rumblers.length == 0) { 
		 controller_computer.log.writelogfile("No Rumblers found" , ErrorState.WARNING); 
		 // System.out.println("bin hier");
		 rumblerIdx = -1; } 
	 else { 
		 System.out.println("Rumblers found: " + rumblers.length);}
	} 
	
}
