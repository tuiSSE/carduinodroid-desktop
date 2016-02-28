package Controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * This class is used to update the Camera pictures on the GUI
 * @author Robin
 * @autohor Michael S
 * @version 1.0
 */
public class Camera_Picture {

	Controller_Computer controller;
	BufferedImage up_pressed = null;
	BufferedImage down_pressed = null;
	BufferedImage left_pressed = null;
	BufferedImage right_pressed = null;
	BufferedImage image = null;
	boolean loadpicture=false;
	boolean rotate =false;
	boolean up=false,down=false,left=false,right=false;
	
	public Camera_Picture(Controller_Computer controller) {
		this.controller = controller;
		LoadFeedback();
	}
	
	/**
	 * This method updates the Image
	 * @param bufferedImage
	 */
	public void receive_picture(BufferedImage bufferedImage) {
		image = bufferedImage;
		image = rotate(image, Math.toRadians(90));
		if(image != null){	
			rotate=false;
			LoadPicture();}
	}
	
	public BufferedImage rotate(BufferedImage image, double angle) {
		if(image != null){    
			rotate=true;
			double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
		    int w = image.getWidth(), h = image.getHeight();
		    int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
		    GraphicsConfiguration gc = getDefaultConfiguration();
		    BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
		    Graphics2D g = result.createGraphics();
		    g.translate((neww-w)/2, (newh-h)/2);
		    g.rotate(angle, w/2, h/2);
		    g.drawRenderedImage(image, null);
		    g.dispose();
		    return result;
		}
	    return null;
	}
	
	public static GraphicsConfiguration getDefaultConfiguration() {
	   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	   GraphicsDevice gd = ge.getDefaultScreenDevice();
	   return gd.getDefaultConfiguration();
	}
	
	private void LoadFeedback()
	{
		try
        {
			up_pressed = ImageIO.read(new File("src/View/Icons/Icon_up_pressed.gif"));
        	down_pressed = ImageIO.read(new File("src/View/Icons/Icon_down_pressed.gif"));
        	left_pressed = ImageIO.read(new File("src/View/Icons/Icon_left_pressed.gif"));
        	right_pressed = ImageIO.read(new File("src/View/Icons/Icon_right_pressed.gif"));
        }
        catch(IOException e){System.out.println("Icons missing");}
	}
	
	public void UpdateDirection(Boolean up, Boolean down, Boolean left, Boolean right)
	{
		if(this.up!=up||this.down!=down||this.left!=left||this.right!=right){
			this.up=up;this.down=down;this.left=left;this.right=right;
			if(image!=null){
				if(!loadpicture&&!rotate){
					loadpicture=true;
					LoadPicture();
				}
			}
		}
	}
	
	private int ChooseWidth()
	{
		int imagewidth = controller.gui_computer.image.getWidth();
		int panelwidth = controller.gui_computer.getPanelWidth();
		if(imagewidth<panelwidth){return imagewidth;}
		else return panelwidth;
	}
	
	private int ChooseHeight()
	{
		int imageheight = controller.gui_computer.image.getHeight();
		int panelheight = controller.gui_computer.getPanelHeight();
		if(imageheight<panelheight){return imageheight;}
		else return panelheight;
	}
	
	public void LoadPicture()
	{
		BufferedImage work = image;
		//Fit the Picture in the panel_video to see all details
        loadpicture = true;
		int newH = (int)(controller.gui_computer.getPanelHeight());
		//need to get a variable to use a stretched picture. I like to use 1.5 to 2 for a better view
        int newW = (int)(work.getWidth()*controller.gui_computer.getPanelHeight()/work.getHeight()*1.5);
        int type = (work.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        //Create a new
        BufferedImage tmp = new BufferedImage(newW, newH, type);
        Graphics2D g2 = tmp.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(work, 0, 0, newW, newH, null);
		
		//Integrate the Battery Level to the 'picture stream'
//		String BatteryLevel = String.valueOf(controller.packagedata.GetBatteryLevel())+" %";
//		Font f = new Font("Verdana", Font.BOLD, 20);
//		g2.setFont(f);
		
//		//Color for BatteryLevel Green>60%, 60%>=Yellow>20, Red<=20%
//		if(controller.packagedata.GetBatteryLevel()>60)
//		{
//			g2.setColor(Color.GREEN);
//		}else{
//			if(controller.packagedata.GetBatteryLevel()>20)
//			{
//				g2.setColor(Color.YELLOW);
//			}else{g2.setColor(Color.RED);}
//		}
//        g2.drawString(BatteryLevel,(tmp.getWidth()/2)-25,20);
        
        //Feedback is shown in the picture, before there were buttons but sometimes there were persons they wanted to click the old feedback
        if(up) g2.drawImage(up_pressed,ChooseWidth()-100,ChooseHeight()-100,40,40,null); //x,y,width,height
        if(down) g2.drawImage(down_pressed,ChooseWidth()-110,ChooseHeight()-50,40,40,null);
        if(left) g2.drawImage(left_pressed,ChooseWidth()-160,ChooseHeight()-50,40,40,null);
        if(right) g2.drawImage(right_pressed,ChooseWidth()-50,ChooseHeight()-50,40,40,null);
        g2.dispose();  
        
		//Set the picture as Icon of a label on the panel_video
        controller.gui_computer.image.setIcon(new ImageIcon(tmp));
        loadpicture=false;
	}
	
}
