package com.example.kickfor;

import java.net.Socket;

public class SocketSingleton {
	private static Socket socket;
	private static SocketSingleton instance=null;
	
	private SocketSingleton(){
		try{
			socket=new Socket("182.92.243.112",8480);//182.92.243.112
		}catch(Exception e){e.printStackTrace();}
	}
	
	public synchronized static SocketSingleton getInstance(){
		if(instance==null){
			instance=new SocketSingleton();
		}
		return instance;
	}
	
	public synchronized static void resetInstance(){
		instance=null;
	}
	
	public synchronized Socket getSocket(){
		return socket;
	}
	
	

}
