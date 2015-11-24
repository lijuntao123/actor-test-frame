package com.eastsoft.testframe.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 使用者自己的实现类
 * 
 * @author ljt
 * @date 2015-10-20 08:25:44
 *
 */
public class MyBusiness extends AbstractBusiness {

	public static volatile int count=0;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean doWork() {
		boolean flag = false;
		OutputStream os = null;
		InputStream is = null;
		Socket socket = null;
		try {
			socket = new Socket("129.1.77.15", 6060);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			byte[] b = { (byte) 0x01, (byte) 0x00, (byte) 0x13, (byte) 0x00,
					(byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB,
					(byte) 0xBB, (byte) 0xBB, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x0C, (byte) 0x01, (byte) 0x00,
					(byte) 0x16, (byte) 0x09, (byte) 0x15, (byte) 0x12,
					(byte) 0x34, (byte) 0x56, (byte) 0x78 };
			os.write(b);

			byte[] temp = new byte[1024];
			int len = 0;
//			len = is.read(temp);
//			if (len == 22) {
//				flag = true;
//			}
			byte[] buf = new byte[1024 * 1024];
			len = is.read(buf);
			if(len!=0){
				flag=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {				
				is.close();
				os.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return flag;
	}

	@Override
	protected void clean() {
		// TODO Auto-generated method stub

	}

}
