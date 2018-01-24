package com.terrytec.brokenrailmonitor.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.terrytec.brokenrailmonitor.MainActivity;

import android.R.string;
import android.os.Environment;

public class FileRailInfoXmlParser {

	public String XMLPath = null;
	private Calendar date;
	private int terminalNo;
    private String directoryName;

	public FileRailInfoXmlParser(int terminalNum) {
		terminalNo = terminalNum;
	}

	public void setDateAndPath(Calendar cal) {
		date = cal;
		directoryName="/"+String.valueOf(date.get(Calendar.YEAR))+"/"+String.valueOf(date.get(Calendar.MONTH)+1)
		XMLPath = MainActivity.getMainActivity().getFilesDir() + "/.xml";
	}

	// 自动创建XML
	public void createXml() {
		// sdcard/test/message.xml
		this.Write(XMLPath, FileRailInfoXmlParser.getInstance().writeXml());
	}

	public boolean Write(String Filepath, String txt) {
		FileOutputStream fos = null;
		// if (Environment.getExternalStorageState() != null) {//
		// 这个方法在试探终端是否有sdcard!
		File path = new File("sdcard/test");// 创建目录
		File f = new File(Filepath);// 创建文件
		if (!path.exists()) {// 目录不存在返回false
			path.mkdirs();// 创建一个目录
		}
		if (!f.exists()) {// 文件不存在返回false
			try {
				f.createNewFile();
				fos = new FileOutputStream(f);
				fos.write((txt).getBytes("UTF-8"));
				fos.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 创建一个文件
		}
		// }
		return false;
	}
}
