package com.terrytec.brokenrailmonitor.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.terrytec.brokenrailmonitor.MainActivity;
import com.terrytec.brokenrailmonitor.TerminalAnd2Rails;

import android.content.Context;
import android.util.Xml;

public class FileOperate {

	private MainActivity activity;

	public FileOperate(MainActivity ma) {
		this.activity = ma;
	}

	public void writeFile(String writestr) throws IOException {
		try {

			FileOutputStream fout = activity.openFileOutput("config.xml", Context.MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TerminalAnd2Rails> GetTerminalAnd2Rails() {
		try {
			FileInputStream inStream = activity.openFileInput("config.xml");
			return this.GetTerminalAnd2Rails(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void write(List<TerminalAnd2Rails> tAnd2Rs) {

		try {
			// ����һ��FileOutputStream����,MODE_PRIVATE׷��ģʽ
			FileOutputStream fos = activity.openFileOutput("config.xml", Context.MODE_PRIVATE);
			this.save(tAnd2Rs, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TerminalAnd2Rails> GetTerminalAnd2Rails(FileInputStream xml) throws Exception {

		List<TerminalAnd2Rails> tAnd2Rs = null;
		TerminalAnd2Rails tAnd2R = null;
		XmlPullParser pullParser = Xml.newPullParser();// ��������xml�ļ�����һ���ַ������У�Ȼ��һ��һ���ַ�����
		// ΪҪ�ƽ��xml����
		pullParser.setInput(xml, "utf-8");
		int event = pullParser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				tAnd2Rs = new ArrayList<TerminalAnd2Rails>();
				break;
			case XmlPullParser.START_TAG:
				if ("Device".equals(pullParser.getName())) {// pullָ��Deviceʱ����������
					tAnd2R = new TerminalAnd2Rails(activity, null);
				}
				if ("TerminalNo".equals(pullParser.getName())) {// pullָ��TerminalNoʱ����TerminalNo��ǩ���textֵ�浽tAnd2R������
					int terminalNo = Integer.valueOf(pullParser.nextText());
					tAnd2R.setTerminalNo(terminalNo);
				}
				if ("NeighbourSmall".equals(pullParser.getName())) {// ͬ��
					int neighbourSmall = Integer.valueOf(pullParser.nextText());
					tAnd2R.setNeighbourSmall(neighbourSmall);
				}
				if ("NeighbourBig".equals(pullParser.getName())) {// ͬ��
					int neighbourBig = Integer.valueOf(pullParser.nextText());
					tAnd2R.setNeighbourBig(neighbourBig);
				}
				if ("Is4G".equals(pullParser.getName())) {// ͬ��
					boolean is4G = Boolean.valueOf(pullParser.nextText().toString()).booleanValue();
					tAnd2R.setIs4G(is4G);
				}
				if ("IsEnd".equals(pullParser.getName())) {// ͬ��
					boolean isEnd = Boolean.valueOf(pullParser.nextText().toString()).booleanValue();
					tAnd2R.setIsEnd(isEnd);
				}
				break;

			case XmlPullParser.END_TAG:// ��eventִ�е�END_TAGʱ����tAnd2R����ŵ������У���������ֵ�ÿա�
				if ("Device".equals(pullParser.getName())) {
					tAnd2Rs.add(tAnd2R);
					tAnd2R = null;
				}
				break;
			}
			event = pullParser.next();// ��仰������Ҫ��
		}

		System.out.println("775757" + tAnd2Rs);
		return tAnd2Rs;
	}

	public void save(List<TerminalAnd2Rails> tAnd2Rs, FileOutputStream ouStream) throws Exception {
		XmlSerializer serializer = Xml.newSerializer();// �����л���
		serializer.setOutput(ouStream, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "Devices");
		for (TerminalAnd2Rails tAnd2R : tAnd2Rs) {
			serializer.startTag(null, "Device");

			serializer.startTag(null, "TerminalNo");
			serializer.text(String.valueOf(tAnd2R.terminalNo));
			serializer.endTag(null, "TerminalNo");

			serializer.startTag(null, "NeighbourSmall");
			serializer.text(String.valueOf(tAnd2R.neighbourSmall));
			serializer.endTag(null, "NeighbourSmall");

			serializer.startTag(null, "NeighbourBig");
			serializer.text(String.valueOf(tAnd2R.neighbourBig));
			serializer.endTag(null, "NeighbourBig");

			serializer.startTag(null, "Is4G");
			serializer.text(String.valueOf(tAnd2R.is4G));
			serializer.endTag(null, "Is4G");

			serializer.startTag(null, "IsEnd");
			serializer.text(String.valueOf(tAnd2R.isEnd));
			serializer.endTag(null, "IsEnd");

			serializer.endTag(null, "Device");
		}

		serializer.endTag(null, "Devices");
		serializer.endDocument();
		ouStream.flush();
		ouStream.close();
	}
	
	/** 
     * ��ȡָ���ļ���С  
     * @param f  
     * @return  
     * @throws Exception ���� 
     */  
    public static long getFileSize(File file) throws Exception {  
        long size = 0;  
        if (file.exists()) {  
            FileInputStream fis = null; 
        	try {
                fis = new FileInputStream(file);  
                size = fis.available();  
			} finally {
				fis.close();
			}
        } 
        return size;  
    }  
}
