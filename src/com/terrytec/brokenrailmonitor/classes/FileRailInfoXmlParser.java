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

	// �Զ�����XML
	public void createXml() {
		// sdcard/test/message.xml
		this.Write(XMLPath, FileRailInfoXmlParser.getInstance().writeXml());
	}

	public boolean Write(String Filepath, String txt) {
		FileOutputStream fos = null;
		// if (Environment.getExternalStorageState() != null) {//
		// �����������̽�ն��Ƿ���sdcard!
		File path = new File("sdcard/test");// ����Ŀ¼
		File f = new File(Filepath);// �����ļ�
		if (!path.exists()) {// Ŀ¼�����ڷ���false
			path.mkdirs();// ����һ��Ŀ¼
		}
		if (!f.exists()) {// �ļ������ڷ���false
			try {
				f.createNewFile();
				fos = new FileOutputStream(f);
				fos.write((txt).getBytes("UTF-8"));
				fos.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // ����һ���ļ�
		}
		// }
		return false;
	}
}
