package com.coderli.net.tcp.chat.demo3;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
	public static void closeAll(Closeable... io) {
		for (Closeable temp : io) {
			try {
				if (null != temp) {
					temp.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
