package com.example.kickfor;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketSingleton {
	private static Socket socket;
	private static SocketSingleton instance=null;
	private boolean isReConnect=true;
	
	private SocketSingleton(){
		while(isReConnect){
			try{
				isReConnect=false;
				System.out.println("ÖØÁ¬ÖÐ");
				socket=new Socket();
				SocketAddress socAddress = new InetSocketAddress("182.92.243.112", 8480);
				socket.connect(socAddress, 5000);
			}catch(Exception e){
				e.printStackTrace();
				isReConnect=true;
			}
		}
	}
	
	public synchronized static SocketSingleton getInstance(){
		if(instance==null){
			instance=new SocketSingleton();
			System.out.println("instance======null");
		}
		return instance;
	}
	
	public synchronized static void resetInstance(){
		instance=null;
	}
	
	public synchronized static void close(){
		if(instance!=null){
			try{
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public synchronized Socket getSocket(){
		return socket;
	}
	

}
