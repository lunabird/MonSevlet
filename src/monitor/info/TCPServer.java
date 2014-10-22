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
 * ������Ҫ������ע�����ķ��͵�2���������󣬷ֱ�������CPU�������ڴ� �� ϵͳ�������б�
 * @author huangpeng
 *
 */
public class TCPServer extends Thread{
	// MON�����˿�
	private static final int monListenPort = 7001;

	private Socket socket;//����Ŀͻ���Socket
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
		System.out.println("mon TCP ��ʼ�����˿�"+monListenPort+"...");
		while (true) {
			try {
				socket = serverSocket.accept();
				System.out.println("�пͻ���/�����ڵ����: "
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
			Message msg = (Message) ois.readObject();//��ע����������ʱ�п��ܻᷢ��EOF�쳣
			Message outMes = null;
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			//ע��������MON����ǰ��������Ϣ
			if (msg.getType().equals(MsgType.CurrentInfo)) {
				System.out.println("���յ�ע��������MON����ǰ��������Ϣ!");
				ArrayList<String[]> hostcpuList = new ArrayList<String[]>();
				
				Thread.sleep(100);
				UDPServer.curInfo.printCurInfo();
				if(UDPServer.curInfo.getCurHostInfo().isEmpty()){
					System.out.println("UDPServer.curInfo.hoSetΪ�գ���������");
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
					//����ʱ��ȥ��cpu��ram�İٷֺ�
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
			// ע��������MON�������е������б�ArrayList<String>
			else if (msg.getType().equals(MsgType.AllHost)) {
				System.out.println("���յ�ע��������MON�������е������б�!");
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
