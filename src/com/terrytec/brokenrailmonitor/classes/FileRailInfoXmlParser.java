package com.terrytec.brokenrailmonitor.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

import org.xmlpull.v1.XmlSerializer;

import com.terrytec.brokenrailmonitor.MainActivity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class FileRailInfoXmlParser {

	public String XMLPath = null;
	private Calendar date;
	private int terminalNo;
	private String directoryName;
	private String rootDir;

	public FileRailInfoXmlParser(int terminalNum) {
		terminalNo = terminalNum;
	}

	@SuppressLint("DefaultLocale")
	public void setDateAndPath(Calendar cal) {
		date = cal;
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		String strYear = String.valueOf(year);
		String strMonth = String.format("%2d", month).replace(" ", "0");
		String strDay = String.format("%2d", day).replace(" ", "0");
		String forderYear = "/" + strYear;
		String forderMonth = forderYear + "-" + strMonth;
		String forderDay = forderMonth + "-" + strDay;
		directoryName = forderYear + forderMonth + forderDay;
		rootDir = MainActivity.getMainActivity().getFilesDir() + "/DataRecord";
		XMLPath = rootDir + directoryName + "/DataTerminal" + String.format("%3d", terminalNo).replace(" ", "0")
				+ ".xml";
	}

	// �Զ�����XML
	public void createXml() {
		// sdcard/test/message.xml
		if (rootDir == null || directoryName == null) {
			Toast.makeText(MainActivity.getMainActivity(), "δ�������ں�·����", Toast.LENGTH_LONG).show();
			return;
		}
		this.CreateWrite(XMLPath, this.writeXmlCreate());
		Log.i("info","����·��" + XMLPath);

	}

	public String writeXmlCreate() {
		XmlSerializer xml = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			xml.setOutput(writer);
			xml.startDocument("UTF-8", true);
			xml.startTag(null, "Datas");
			xml.endTag(null, "Datas");
			xml.endDocument();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

	public boolean CreateWrite(String Filepath, String txt) {
		FileOutputStream fos = null;
		// if (Environment.getExternalStorageState() != null) {//
		// �����������̽�ն��Ƿ���sdcard!
		File path = new File(rootDir + directoryName);// ����Ŀ¼
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
				e.printStackTrace();
			} // ����һ���ļ�
		}
		// }
		return true;
	}
}
