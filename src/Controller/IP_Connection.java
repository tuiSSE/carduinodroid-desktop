package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.*;


public class IP_Connection {

	Network network;
	
	Socket remoteCtrlSocket;
    Socket remoteDataSocket;
    
    BufferedWriter outData;
    BufferedReader inData;
    
    JSON_Protocol jsonProtocol;
    protected boolean dataSocketServerDisconnected;
    
    public IP_Connection(Network _network){
    	
    	network = _network;
    	reset();
    }
    
    private void reset(){
    	
    	remoteCtrlSocket = null;
    	remoteDataSocket = null;
    	
    	outData = null;
    	inData = null;
    	
    	jsonProtocol = new JSON_Protocol(network);
    }
    
    public void close(){
		
    	dataSocketServerDisconnected = true;
    	
		try {
			remoteDataSocket.shutdownOutput();
			remoteDataSocket.close();
			if(inData != null) inData = null;
			if(outData != null) outData = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public Boolean connect(String serverAdress){
    	
    	inData = null;
		outData = null;
		String incomingCtrlMsg = "";
		Boolean isTargetDataSocketInUse = true;
    	
		try {
			InetAddress ip = InetAddress.getByName(serverAdress);
			InetSocketAddress CtrlAdress = new InetSocketAddress(ip, Constants.IP_CONNECTION.CTRLPORT);
			remoteCtrlSocket = new Socket();
			remoteCtrlSocket.connect(CtrlAdress, 3000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(remoteCtrlSocket.getInputStream()));
            
			while ((incomingCtrlMsg = reader.readLine()) != null) {
                receiveData(incomingCtrlMsg);

                if (receiveCtrl(incomingCtrlMsg).equals("false")) isTargetDataSocketInUse = false;
                else isTargetDataSocketInUse = true;
            }
			
			remoteCtrlSocket.close();
			
			if (!isTargetDataSocketInUse) {

                remoteDataSocket = new Socket(serverAdress, Constants.IP_CONNECTION.DATAPORT);

                inData = new BufferedReader(new InputStreamReader(remoteDataSocket.getInputStream()));
                outData = new BufferedWriter(new OutputStreamWriter(remoteDataSocket.getOutputStream()));
                
                new IpDataReceiveThread(inData).start();
            }else return false;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    	
    }
    
    public class ClientConnection extends Thread{
    	
    	protected String serverAdress;
    	protected boolean isTargetDataSocketInUse;
    	protected String incomingCtrlMsg;
    	
    	public ClientConnection(String _serverAdress){
    		
    		inData = null;
    		outData = null;
    		incomingCtrlMsg = "";
    		serverAdress = _serverAdress;
    		isTargetDataSocketInUse = true;
    	}
    	
    	public void run(){
    		
    		try {
				InetAddress ip = InetAddress.getByName(serverAdress);
    			InetSocketAddress CtrlAdress = new InetSocketAddress(ip, Constants.IP_CONNECTION.CTRLPORT);
				remoteCtrlSocket = new Socket();
				remoteCtrlSocket.connect(CtrlAdress, 3000);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(remoteCtrlSocket.getInputStream()));
	            
				while ((incomingCtrlMsg = reader.readLine()) != null) {
	                receiveData(incomingCtrlMsg);

	                if (receiveCtrl(incomingCtrlMsg).equals("false")) isTargetDataSocketInUse = false;
	                else isTargetDataSocketInUse = true;
	            }
				
				remoteCtrlSocket.close();
				
				if (!isTargetDataSocketInUse) {

                    remoteDataSocket = new Socket(serverAdress, Constants.IP_CONNECTION.DATAPORT);

                    inData = new BufferedReader(new InputStreamReader(remoteDataSocket.getInputStream()));
                    outData = new BufferedWriter(new OutputStreamWriter(remoteDataSocket.getOutputStream()));
                    
                    new IpDataReceiveThread(inData).start();
                }
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    private class IpDataReceiveThread extends Thread{
    	
    	protected BufferedReader inStream;
    	protected String incomingDataMsg;
    	
    	public IpDataReceiveThread(BufferedReader _inStream) {
            inStream = _inStream;
        }
    	
    	public void run(){
    		
    		dataSocketServerDisconnected = false;
    		try {
                while(((incomingDataMsg = inData.readLine())!=null)){
                    if(dataSocketServerDisconnected) break;
                    receiveData(incomingDataMsg);
                }
                
                network.close();
    		}catch(IOException e){
    			
    		}
    	}
    }
    
    public synchronized boolean sendControlData(int speed, int steer){
    	
    	return sendData(Constants.JSON_OBJECT.NUM_CONTROL, speed+";"+steer);
    }
    
    public synchronized boolean sendCameraData(String settingData){
    	
    	return sendData(Constants.JSON_OBJECT.NUM_CAMERA, settingData);
    }

    public synchronized boolean sendSoundData(String settingData){
	
    	return sendData(Constants.JSON_OBJECT.NUM_SOUND, settingData);
    }
    
    private synchronized Boolean sendData(String dataTypeMask, String dataPacket){
    	
    	JSONObject transmitData = jsonProtocol.getTransmitData(dataTypeMask, dataPacket);
    	
    	if(transmitData != null && outData != null){
    		try {
    			outData.write(transmitData.toString());
				outData.newLine();
				outData.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
    	}else return false;
    	
    	return true;
    }
    
    private void receiveData(String dataPacket){
    	
    	jsonProtocol.parseJSON(dataPacket);
    }
    
    private String receiveCtrl(String dataPacket){
    
    	return jsonProtocol.parseJSON(dataPacket);
    }
    
    
    
}
