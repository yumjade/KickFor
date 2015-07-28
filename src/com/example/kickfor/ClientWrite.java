package com.example.kickfor;

import java.io.PrintWriter;
import java.net.Socket;


public class ClientWrite implements Runnable{
	private Socket socket=null;
	private String message=null;
	private PrintWriter out=null;
	
	
	public ClientWrite(String str){
		this.message=str;
	}

	
	public void run(){
		try{
			this.socket=SocketSingleton.getInstance().getSocket();
			System.out.println(message);
			out=new PrintWriter(socket.getOutputStream(),false);
			out.println(this.message);
			out.flush();
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	

}