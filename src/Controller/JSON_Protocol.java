package Controller;

import org.json.*;

import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;

public class JSON_Protocol {
	
	Network network;
	
	private JSONObject JsonObjectData;
	private boolean isMaskTypeServer;
	
	private boolean isCar;
    private boolean isMobility;
    private boolean isNetwork;
    private boolean isHardware;
    private boolean isVideo;
    private boolean isControl;
    private boolean isCamera;
    private boolean isSound;
    private boolean isSerial;

    private boolean isForClient;
    private boolean isForServer;
    
    private int speed;
    private int steering;
    private int frontLight;
    private int statusLed;
    
    
	
	public JSON_Protocol(Network _network){
		
		network = _network;
	}
	
	public synchronized String parseJSON(String jsonObjectRxData){
		
		JSONObject jsonObject = new JSONObject(jsonObjectRxData);
        
        JSONObject JsonObjectHeader = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_HEADER);
        //First Check if both sides (Remote & Transceiver) using the same JSON Version
        if(JsonObjectHeader.getInt(Constants.JSON_OBJECT.TAG_HEADER_VERSION) != Constants.JSON_OBJECT.MY_VERSION) {
            //Log.e(TAG, "Wrong JSON Version between Transmitter and Receiver");
            return "true";
        }else{
            
        	String mask = JsonObjectHeader.getString(Constants.JSON_OBJECT.TAG_HEADER_INFORMATION_TYPE);
            
            if(isMaskLogic(mask)){
                //Define the Type for the right Parsing
                //Log.i(TAG, String.valueOf(JsonObjectHeader.getBoolean(Constants.JSON_OBJECT.TAG_HEADER_DATA_SERVER_STATUS)));
                if(!isForServer&&!isForClient){
                    //Ctrl Server Socket Data: Information about Data Server Socket Status as feedback
                    //test if a boolean value will be shown as String
                	if(JsonObjectHeader.getBoolean(Constants.JSON_OBJECT.TAG_HEADER_DATA_SERVER_STATUS)) return "true";
                	else return "false";
                    
                }else if(isForClient){
                    //Parsing all the information given from a Server to the Client
                    if(isCar){
                        JSONObject JsonObjectCarInfo = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_CAR);

                        String information = "4;";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_CURRENT) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_BATTERY_ABSOLUTE) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_BATTERY_PERCENTAGE) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_VOLTAGE) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_TEMPERATURE) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_ULTRASONIC_FRONT) + ";";
                        information += JsonObjectCarInfo.getInt(Constants.JSON_OBJECT.TAG_CAR_ULTRASONIC_BACK);
                        
                        network.receive_package(information);
                    }
                    if(isMobility){
                        JSONObject JsonObjectMobility = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_MOBILITY);
                        
                        String information = "3;";
                        /*carduinoDroidData.setGpsData(JsonObjectMobility.getInt(Constants.JSON_OBJECT.TAG_MOBILITY_GPS));
                        carduinoDroidData.setVibration(JsonObjectMobility.getInt(Constants.JSON_OBJECT.TAG_MOBILITY_VIBRATION));*/
                    }
                    if(isHardware){
                        JSONObject JsonObjectHardware = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_HARDWARE);
                        
                        String information = "2";
                        
                        String values = getSupportedSizedValues(JsonObjectHardware.getJSONObject(Constants.JSON_OBJECT.TAG_HARDWARE_CAMERA_RESOLUTION),
                                JsonObjectHardware.getInt(Constants.JSON_OBJECT.TAG_HARDWARE_CAMERA_RESOLUTION_NUM));
                        
                        if(values != null) network.receive_package(information+values);
                        //TODO give all the Resolutions to show whats possible and the ID of the actual chosen one
                        /*carduinoDroidData.setCameraSupportedSizes(getSupportedSizedValues(JsonObjectHardware.getJSONObject(Constants.JSON_OBJECT.TAG_HARDWARE_CAMERA_RESOLUTION),
                                JsonObjectHardware.getInt(Constants.JSON_OBJECT.TAG_HARDWARE_CAMERA_RESOLUTION_NUM)));*/
                    }
                    if(isHardware){
                        //TO-DO ? Maybe we can leave it out in this app because i see no value out of it
                    }
                    if(isVideo){
                        JSONObject JsonObjectVideo = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_VIDEO);

                        String PictureFrameBase64Encoded = JsonObjectVideo.getString(Constants.JSON_OBJECT.TAG_VIDEO_SOURCE);
                        String replacedPictureString = PictureFrameBase64Encoded.replace("\n", "");
                        byte[] PictureFrameBase64Decoded = Base64.getDecoder().decode(replacedPictureString);
                        InputStream in = new ByteArrayInputStream(PictureFrameBase64Decoded);
                        try {
							BufferedImage image = ImageIO.read(in);
							network.receive_picture(image);
						} catch (IOException e) {
							e.printStackTrace();
						}                      
                    }
                    if(isSerial){
                        JSONObject JsonObjectSerial = jsonObject.getJSONObject(Constants.JSON_OBJECT.TAG_SERIAL);

                        /*carduinoData.setSerialState(new ConnectionState(
                                ConnectionEnum.fromInteger(JsonObjectSerial.getInt(Constants.JSON_OBJECT.TAG_SERIAL_STATUS)),
                                JsonObjectSerial.getString(Constants.JSON_OBJECT.TAG_SERIAL_ERROR)));
                        carduinoData.setSerialName(
                                JsonObjectSerial.getString(Constants.JSON_OBJECT.TAG_SERIAL_NAME));
                        carduinoData.setSerialType(
                                SerialType.fromInteger(JsonObjectSerial.getInt(Constants.JSON_OBJECT.TAG_SERIAL_TYPE)));*/
                    }
                    return mask;
                }                   
            }else{
                //Log.e(TAG, "Error on JSON Parsing by mixed up Data inclusion (Both Client and Server Data)");
                return "true";
            }
        }

		//Ja KA bisher wieso er das will
		return jsonObjectRxData;
	}
	
	private boolean isMaskLogic(String mask){

        isCar = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_CAR);
        isMobility = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_MOBILITY);
        isNetwork = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_NETWORK);
        isHardware = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_HARDWARE);
        isVideo = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_VIDEO);
        isControl = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_CONTROL);
        isCamera = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_CAMERA);
        isSound = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_SOUND);
        isSerial = checkDataTypeMask(mask,Constants.JSON_OBJECT.NUM_SERIAL);

        isForClient = (isCar || isMobility || isNetwork || isHardware || isVideo || isSerial);
        isForServer = (isControl || isCamera ||isSound);

        return !(isForClient&&isForServer);
    }
	
	public synchronized JSONObject getTransmitData(String dataTypeMask, String dataPacket){
		
		if(createJSON(dataTypeMask, dataPacket))
			return JsonObjectData;
		
		return null;
	}
	
	private synchronized boolean createJSON(String dataTypeMask, String dataPacket){
		
		isMaskTypeServer = false;
		String[] information = getDataParameters(dataPacket);
        
		JsonObjectData = new JSONObject();
        // Header is used for all the JSON Objects to define Version and Type
        // It should be in Client and Server Packet to have a certain control setup
        JSONObject JsonObjectHeader = new JSONObject();
        
        // Create the Header to define version, data type to secure the transmission and Ctrl Socket
       
        JsonObjectHeader.put(Constants.JSON_OBJECT.TAG_HEADER_VERSION, Constants.JSON_OBJECT.MY_VERSION);
        JsonObjectHeader.put(Constants.JSON_OBJECT.TAG_HEADER_DATA_SERVER_STATUS, "");
        JsonObjectHeader.put(Constants.JSON_OBJECT.TAG_HEADER_INFORMATION_TYPE, dataTypeMask);
        JsonObjectData.put(Constants.JSON_OBJECT.TAG_HEADER, JsonObjectHeader);

        
        /**** Client Paket Part ****/
        /**** Including Car Control ****/
        if (checkDataTypeMask(dataTypeMask,Constants.JSON_OBJECT.NUM_CONTROL)) {

            if(isMaskTypeServer) {
                //Log.i(TAG,"Error on Creating JSON Object - Mixed up Types of Server and Client");
                return false;
            }
            else{
                JSONObject JsonObjectCarControl = new JSONObject();

                int speed = Integer.parseInt(information[0]);
                if(information[1].equals(true)) JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_SPEED, speed);
                else JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_SPEED, (-1 * speed));
                
                int steering = Integer.parseInt(information[2]);
                if(information[3].equals(true)) JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_SPEED, steering);
                else JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_SPEED, (-1 * steering));
                
                //JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_FRONT_LIGHT, carduinoData.getFrontLight());
                //JsonObjectCarControl.put(Constants.JSON_OBJECT.TAG_CONTROL_STATUS_LED, carduinoData.getStatusLed());

                JsonObjectData.put(Constants.JSON_OBJECT.TAG_CONTROL, JsonObjectCarControl);
            }
        }
        /**** Including Camera Information ****/
        if (checkDataTypeMask(dataTypeMask,Constants.JSON_OBJECT.NUM_CAMERA)) {
            if(isMaskTypeServer) {
                //Log.i(TAG,"Error on Creating JSON Object - Mixed up Types of Server and Client");
                return false;
            }
            else {
                JSONObject JsonObjectCameraInformation = new JSONObject();
                
                if(information[0].equals("1")){
                	if(information[1].equals("0"))
                		JsonObjectCameraInformation.put(Constants.JSON_OBJECT.TAG_CAMERA_TYPE, 1);
                	else JsonObjectCameraInformation.put(Constants.JSON_OBJECT.TAG_CAMERA_TYPE, 0);
                }
                if(information[0].equals("2"))
                	JsonObjectCameraInformation.put(Constants.JSON_OBJECT.TAG_CAMERA_RESOLUTION, Integer.parseInt(information[1]));
                if(information[0].equals("3"))
                	JsonObjectCameraInformation.put(Constants.JSON_OBJECT.TAG_CAMERA_LIGHT, Integer.parseInt(information[1]));
                if(information[0].equals("4"))
                	JsonObjectCameraInformation.put(Constants.JSON_OBJECT.TAG_CAMERA_QUALITY, Integer.parseInt(information[1]));              

                JsonObjectData.put(Constants.JSON_OBJECT.TAG_CAMERA, JsonObjectCameraInformation);
            }
        }
        /**** Including Sound Options ****/
        if (checkDataTypeMask(dataTypeMask,Constants.JSON_OBJECT.NUM_SOUND)) {
            if(isMaskTypeServer) {
                //Log.i(TAG,"Error on Creating JSON Object - Mixed up Types of Server and Client");
                return false;
            }
            else {
                JSONObject JsonObjectSoundOptions = new JSONObject();
                if(information[0].equals("1"))
                	JsonObjectSoundOptions.put(Constants.JSON_OBJECT.TAG_SOUND_PLAY, Integer.parseInt(information[1]));
               	JsonObjectSoundOptions.put(Constants.JSON_OBJECT.TAG_SOUND_RECORD, 0);

                JsonObjectData.put(Constants.JSON_OBJECT.TAG_SOUND, JsonObjectSoundOptions);
            }
        }

		return true;
	}
	
	private boolean checkDataTypeMask(String dataTypeMask, String Type){
		
		return dataTypeMask.contains(Type);
	}
		
	private String[] getDataParameters(String Settings){
		
		return Settings.split(";");
	}
	
	private synchronized String getSupportedSizedValues(JSONObject JsonObjectCameraSizes, int count){

        if(count > 0){
            String values = "";
            try {
                for (int i = count-1; i >= 0; i--) {
                    values += ";" + JsonObjectCameraSizes.getString(Constants.JSON_OBJECT.TAG_HARDWARE_CAMERA_RESOLUTION_NUM+i);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            //carduinoDroidData.setCameraResolutionID(count-1);
            return values;
        }

        return null;
    }
	
}
