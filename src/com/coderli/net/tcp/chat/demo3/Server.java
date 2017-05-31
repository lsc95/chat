package com.coderli.net.tcp.chat.demo3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	// 创建一个集合，里面每一个元素都是一个用户连接
	private List<MyChannel> all = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

	public void start() throws IOException {
		// 创建服务器端
		ServerSocket server = new ServerSocket(9999);
		// 不断循环，接收新的用户
		while (true) {
			// 接收客户端 阻塞
			Socket socket = server.accept();
			System.out.println("有一个客户端成功接入");
			// 创建一个线程
			MyChannel channel = new MyChannel(socket);
			// 将新加入的用户加入到用户的列表中
			all.add(channel);
			// 每一个用户代表一个线程
			new Thread(channel).start();
		}
	}

	// 内部类，表示用户
	private class MyChannel implements Runnable {
		// 用户的输入流
		private DataInputStream dis;
		// 用户的输出流
		private DataOutputStream dos;
		// 线程是否运行的标识
		private boolean isRunning = true;

		// 构造方法，主要用于初始化
		public MyChannel(Socket socket) {
			try {
				dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			} catch (IOException e) {
				// 当发生异常关闭所有的流并且停止线程
				isRunning = false;
				IOUtil.closeAll(dis, dos);
			}
		}

		/**
		 * 读取数据
		 */
		public String receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				// 如果当前线程发现异常，直接关于与某个用户的socket
				isRunning = false;
				IOUtil.closeAll(dis, dos);
				// 将用户删除
				all.remove(this);
			}
			// 测试输出
			System.out.println("成功收到一条信息:" + msg);
			return msg;
		}

		/**
		 * 输出数据
		 */
		public void send(String msg) {
			if (null == msg || msg.equals("")) {
				return;
			}
			try {
				// 写入数据
				dos.writeUTF(msg);
				// 强制输出内容
				dos.flush();
			} catch (IOException e) {
				// 如果当前线程发现异常，直接关于与某个用户的socket
				isRunning = false;
				IOUtil.closeAll(dis, dos);
				// 将用户删除
				all.remove(this);
			}
		}

		/**
		 * 发送给其他人
		 */
		public void sendOther() {
			// 获取用户发送的内容
			String msg = receive();
			// 遍历用户列表
			for (MyChannel channel : all) {
				// 跳过发送人
				if (channel == this) {
					continue;
				}
				// 给其他的人依次发送信息
				channel.send(msg);
			}
		}

		@Override
		public void run() {
			// 线程体
			while (isRunning) {
				sendOther();
			}

		}

	}
}
