package com.coderli.net.tcp.chat.demo4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = reader.readLine();
		// 创建客户端
		Socket client = new Socket("localhost", 9999);
		new Thread(new Send(client, name)).start();
		new Thread(new Receive(client)).start();
	}
}
