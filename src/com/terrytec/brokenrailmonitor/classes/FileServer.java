package com.terrytec.brokenrailmonitor.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.terrytec.brokenrailmonitor.HomeFragment;
import com.terrytec.brokenrailmonitor.MainActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FileServer implements Runnable {

	private final int RECEIVEFILE = 3;
	Socket socket = null;

	public FileServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 向android客户端输出
		String line = null;
		String totalLine = "";
		InputStream input;
		try {
			// 向客户端发送信息
			input = socket.getInputStream();
			BufferedReader bff = new BufferedReader(new InputStreamReader(input));
			// 半关闭socket
			socket.shutdownOutput();
			// 获取客户端的信息
			while ((line = bff.readLine()) != null) {
				totalLine += line;
			}
			FileOperate fileOperator = new FileOperate(MainActivity.getMainActivity());
			fileOperator.writeFile(totalLine);

			System.out.print(totalLine);
			Log.e("file", totalLine);
			Log.w("size", String.valueOf(totalLine.length()));
			// 关闭输入输出流
			bff.close();
			input.close();
			socket.close();
			
			Handler myHandler = ((HomeFragment) MainActivity.getMainActivity().homeFragment).getHomeHandler();
			Message msg = new Message();
			msg.what = RECEIVEFILE;
			myHandler.sendMessage(msg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
