package com.eastsoft.testframe.business;

public class Util {

	public static String hex2String(byte[] data) {
		if (data == null) {
			return null;
		}
		String str = new String();
		for (int i = 0; i < data.length; i++) {
			str += String.format("%02X", data[i]);
		}

		return str;
	}
	
	 public static byte [] hexStr2ByteArray(String hexStr){
	        String str=hexStr;
	        int len=str.length();
	        if(str.length()%2!=0){
	            str="0"+str;
	        }
	        byte[] b=new byte[str.length()/2];
	        for(int i =0,j=0;i<str.length()/2;i++,j+=2){
	            b[i]=(byte)Short.parseShort(str.substring(j,j+2),16);
	        }
	        return b;
	    }
}
