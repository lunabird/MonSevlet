package monitor.info;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Message.Message;
import Message.MsgType;
/**
 * 该类主要来监听注册中心发送的2种数据请求，分别是主机CPU，主机内存 和 系统的主机列表
 * @author huangpeng
 *
 */
public class TCPServer extends Thread{
	// MON监听端口
	private static final int monListenPort = 7001;

	private Socket socket;//接入的客户端Socket
	private ServerSocket serverSocket;
	
//	public static void main(String[] args) throws IOException {
//		TCPServer ts = new TCPServer();
//		ts.start();
//	}
	
	@Override
	public void run() {			
		try {
			System.out.println("monListenPort:"+monListenPort);
			serverSocket = new ServerSocket(monListenPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("mon TCP 开始监听端口"+monListenPort+"...");
		while (true) {
			try {
				socket = serverSocket.accept();
				System.out.println("有客户端/其他节点接入: "
						+ socket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new ResponseThread(socket);
		}
	}
}

class ResponseThread extends Thread {
	private Socket socket;

	public ResponseThread(Socket socket) {
		this.socket = socket;
		start();
	}
	@Override
	public void run() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			Message msg = (Message) ois.readObject();//当注册中心请求时有可能会发生EOF异常
			Message outMes = null;
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			//注册中心向MON请求当前的主机信息
			if (msg.getType().equals(MsgType.CurrentInfo)) {
				System.out.println("接收到注册中心向MON请求当前的主机信息!");
				ArrayList<String[]> hostcpuList = new ArrayList<String[]>();
				
				Thread.sleep(100);
				UDPServer.curInfo.printCurInfo();
				if(UDPServer.curInfo.getCurHostInfo().isEmpty()){
					System.out.println("UDPServer.curInfo.hoSet为空！！！！！");
				}
//				else{
//					Iterator<Host> it1 = UDPServer.curInfo.getCurHostInfo().iterator();
//					Host h = (Host)it1.next();
//					System.out.println(h.IPadd+"//"+h.cpu+"//"+h.ram);
////					System.out.println(UDPServer.curInfo.getCurHostInfo());
//				}
				
				
//				Set<Host> curHostSet = UDPServer.curInfo.getCurHostInfo();
//				ArrayList<String> hddList = new ArrayList<String>();
//				hddList.add("C:45%");hddList.add("D:45%");hddList.add("E:45%");
//				Set<Host> curHostSet = new HashSet<Host>();
//				Host h1 = new Host("h1","192.168.0.1","SS-87-jj-oo-99","34.5","33.9",hddList);
//				Host h2 = new Host("h2","192.168.0.7","SS-87-jj-oo-99","10.5","20.9",hddList);
//				Host h3 = new Host("h3","192.168.0.8","SS-87-jj-oo-99","36.5","39.9",hddList);
//				Host h4 = new Host("h4","192.168.0.52","SS-87-jj-oo-99","88.5","67.9",hddList);
//				Host h5 = new Host("h5","192.168.0.24","SS-87-jj-oo-99","10.5","10.9",hddList);
//				Host h6 = new Host("h5","192.168.0.23","SS-87-jj-oo-99","38.5","16.9",hddList);
//				curHostSet.add(h1);
//				curHostSet.add(h2);
//				curHostSet.add(h3);
//				curHostSet.add(h4);
//				curHostSet.add(h5);
//				curHostSet.add(h6);
				
				Iterator<Host> it = UDPServer.curInfo.getCurHostInfo().iterator();				
				while (it.hasNext()) {
					//发的时候去掉cpu和ram的百分号
					Host h11 = (Host) it.next();
					String[] hInfo = new String[3];
					hInfo[0] = h11.getIP();
					hInfo[1] = h11.getCPU().split("%")[0];
					hInfo[2] = h11.getRAM().split("%")[0];					
					hostcpuList.add(hInfo);
				}
				System.out.println("outMes!hostcpuList:");
				for(int i=0;i<hostcpuList.size();i++){
					System.out.println(hostcpuList.get(i)[0]+","+hostcpuList.get(i)[1]+","+hostcpuList.get(i)[2]);
				}
				outMes = new Message(MsgType.CurrentInfoResult, hostcpuList);
				oos.writeObject(outMes);
				oos.flush();
				System.out.println("outMes OK!");
			}
			// 注册中心向MON发送所有的主机列表ArrayList<String>
			else if (msg.getType().equals(MsgType.AllHost)) {
				System.out.println("接收到注册中心向MON发送所有的主机列表!");
				CurrentInfo.dbHostList = (ArrayList<String>) msg.getBody();
				outMes = new Message(MsgType.AllHostResult, true);
				oos.writeObject(outMes);
				oos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// run()

	
}
