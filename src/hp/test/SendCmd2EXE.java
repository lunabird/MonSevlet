package hp.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
//本代码的作用是通过指令控制exe的相关操作
public class SendCmd2EXE {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket reqSocket = new Socket("127.0.0.1",11239);
		
		System.out.println("建立连接");
		DataOutputStream oos = new DataOutputStream(reqSocket.getOutputStream());
		//指令包括info ,start,stop,exit
		String cmd="info";
		byte[] a=cmd.getBytes();
		oos.write(a, 0, a.length);
		oos.flush();
		byte[] b = new byte[1024];
		DataInputStream disDataInputStream = new DataInputStream(reqSocket.getInputStream());
		byte[] mby=new byte[1024];
		int msg =disDataInputStream.read(mby);
		String aString=new String(mby);
		System.out.println(aString);
		disDataInputStream.close();
		oos.close();
		reqSocket.close();
	}
}