package com.coderli.net.tcp.chat.demo4;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//接收数据的线程
public class Receive implements Runnable {
	private DataInputStream dis;
	private boolean isRunning = true;

	public Receive() {
		// TODO Auto-generated constructor stub
	}

	public Receive(Socket client) {
		try {
			dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			isRunning = false;
			IOUtil.closeAll(dis);
		}
	}

	// 接收数据
	public String receive() {
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRunning = false;
			IOUtil.closeAll(dis);
		}
		return msg;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			System.out.println(receive());
		}
	}

}
