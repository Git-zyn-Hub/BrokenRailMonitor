package com.terrytec.brokenrailmonitor.classes;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.terrytec.brokenrailmonitor.MainActivity;

import android.widget.Toast;

public class MacAddress {
	public static String getLocalIpAddress() {
	    try {
	        Enumeration en = NetworkInterface.getNetworkInterfaces();
	        for (; en.hasMoreElements();) {
	            NetworkInterface intf = (NetworkInterface) en.nextElement();
	            for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Toast.makeText(MainActivity.getMainActivity(), ex.getMessage(),Toast.LENGTH_SHORT).show();
	    }

	    return null;
	}
	
	public static String getLocalMacAddressFromIp() {
	    String mac_s = "";
	    try {
	        byte[] mac;
	        NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
	        mac = ne.getHardwareAddress();
	        mac_s = byte2hex(mac);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mac_s;
	}

	public static String byte2hex(byte[] b) {
	    StringBuffer hs = new StringBuffer(b.length);
	    String stmp = "";
	    int len = b.length;
	    for (int n = 0; n < len; n++) {
	        stmp = Integer.toHexString(b[n] & 0xFF);
	        if (stmp.length() == 1) {
	            hs = hs.append("0").append(stmp);
	        } else {
	            hs = hs.append(stmp);
	        }
	    }

	    return String.valueOf(hs);
	}
}
