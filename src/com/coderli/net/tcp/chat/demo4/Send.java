package com.coderli.net.tcp.chat.demo4;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//发送数据的线程
public class Send implements Runnable {
	// 控制台输入流
	private BufferedReader console;
	// 管道输出流
	private DataOutputStream dos;
	// 线程标识
	private boolean isRunner = true;
	// 用户名称
	private String name = null;

	public Send() {
		console = new BufferedReader(new InputStreamReader(System.in));
	}

	public Send(Socket client, String name) {
		this();
		try {
			dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
			this.name = name;
			send(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isRunner = false;
			IOUtil.closeAll(dos, console);
		}
	}

	// 从控制台接收数据
	private String getMsgFromConsole() {
		try {
			return console.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void send(String msg) {
		if (null != msg && !msg.equals("")) {
			try {
				// 写入数据
				dos.writeUTF(msg);
				// 强制刷新
				dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				isRunner = false;
				IOUtil.closeAll(dos, console);
			}
		}
	}

	@Override
	public void run() {
		while (isRunner) {
			send(getMsgFromConsole());
		}

	}

}
