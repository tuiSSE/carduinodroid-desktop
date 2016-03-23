package Controller;

public class Constants {
	
	public interface IP_CONNECTION{
        int DATAPORT = 12020;
        int CTRLPORT = 12021;
    }

	public interface JSON_OBJECT{
		int MY_VERSION = 2;

        String TAG_HEADER_VERSION = "Version";
        String TAG_HEADER_INFORMATION_TYPE = "Information Type";
        String TAG_HEADER_DATA_SERVER_STATUS = "Data Server Status";
        String TAG_HEADER = "#Header";

        String TAG_CAR_CURRENT = "Current";
        String TAG_CAR_BATTERY_ABSOLUTE = "Battery Absolute";
        String TAG_CAR_BATTERY_PERCENTAGE = "Battery Relative";
        String TAG_CAR_VOLTAGE = "Voltage";
        String TAG_CAR_TEMPERATURE = "Temperature";
        String TAG_CAR_ULTRASONIC_FRONT = "Ultra Sonic Front";
        String TAG_CAR_ULTRASONIC_BACK = "Ultra Sonic Back";
        String TAG_CAR = "Car Information";
        String NUM_CAR = "#Car";

        String TAG_MOBILITY_GPS = "GPS Data";
        String TAG_MOBILITY_WLAN_AVAILABLE = "WLAN Available";
        String TAG_MOBILITY_WLAN_ACTIVE = "WLAN Active";
        String TAG_MOBILITY_MOBILE_AVAILABLE = "Mobile Available";
        String TAG_MOBILITY_MOBILE_ACTIVE = "Mobile Active";
        String TAG_MOBILITY = "Mobilty Information";
        String NUM_MOBILITY = "#Mobility";

        String TAG_FEATURES_BATTERY_PHONE = "Battery Level Phone";
        String TAG_FEATURES_VIBRATION = "Vibration Value";
        String TAG_FEATURES = "Features Data";
        String NUM_FEATURES = "#Features";

        String TAG_HARDWARE_CAMERA_RESOLUTION = "Camera Resolution";
        String TAG_HARDWARE_CAMERA_RESOLUTION_NUM = "Camera Resolution Numbers";
        String TAG_HARDWARE = "Hardware Information";
        String NUM_HARDWARE = "#Hardware";

        String TAG_VIDEO_TYPE = "Video Type";
        String TAG_VIDEO_SOURCE = "Video Source Data";
        String TAG_VIDEO = "Video Data";
        String NUM_VIDEO = "#Video";

        String TAG_SERIAL_STATUS = "Serial State";
        String TAG_SERIAL_ERROR = "Serial Error";
        String TAG_SERIAL_NAME = "Serial Name";
        String TAG_SERIAL_TYPE = "Serial Type";
        String TAG_SERIAL = "Serial Status";
        String NUM_SERIAL = "#Serial";

        String TAG_CONTROL_SPEED = "Car Control Speed";
        String TAG_CONTROL_STEER = "Car Control Steer";
        String TAG_CONTROL_FRONT_LIGHT = "Car Control Light";
        String TAG_CONTROL_STATUS_LED = "Car Control Status LED";
        String TAG_CONTROL = "Car Control";
        String NUM_CONTROL = "#Control";

        String TAG_CAMERA_TYPE = "Camera Type";
        String TAG_CAMERA_RESOLUTION = "Camera Resolution";
        String TAG_CAMERA_LIGHT = "Camera Flashlight";
        String TAG_CAMERA_QUALITY = "Camera Quality";
        String TAG_CAMERA_ORIENTATION = "Camera Orientation";
        String TAG_CAMERA = "Camera Setup";
        String NUM_CAMERA = "#Camera";

        String TAG_SOUND_PLAY = "Sound Play";
        String TAG_SOUND_RECORD = "Sound Record";
        String TAG_SOUND = "Sound Setup";
        String NUM_SOUND = "#Sound";
	}
}
