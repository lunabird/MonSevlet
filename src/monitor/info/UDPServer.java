package monitor.info;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.Message;
import Message.MsgType;
import Message.XmlUtils;

/**
 * �����������շ�����������������͵������ͷ��������ļ����Ϣ
 * 
 * @author huangpeng
 * 
 */
public class UDPServer extends Thread {
	// ���ڱ��浱ǰ�������������ļ����Ϣ
	public volatile static CurrentInfo curInfo = new CurrentInfo();
	public static CurrentInfo tempInfo = new CurrentInfo();

	byte[] buf = new byte[1024];// ���յ���Ϣ�Ļ�����
	private static int UDP_PORT;// UDP�ļ����ӿ�
	
	public static void main(String[] args){
		UDPServer us = new UDPServer();
		us.start();	
		us.serverWork();
	}
	// ���ڻ�ȡcurInfo
	public static CurrentInfo getCurInfo() {
		return curInfo;
	}
	//�Լ��̣߳�ÿ��sampleTime*3��ʱ����һ���Ƿ����������ٸ�����������Ϣ�ˣ�����У�
	//��curInfo����ȥ�������������ص���Ϣ��
	public static Thread t = new Thread(new CheckThread());
	
	// ע�����ĵ�IP��ַ �Լ� �����˿�
	private static String serviceRegistryIP ;
	private static int serviceRegistryPort;
	private static Document doc;
	/**
	 * ��ȡconfig�����ļ�
	 */
	public static void readConfigXml(){
		try {
			doc = XmlUtils.getDocument();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();// �õ����ڵ�
		Element ipEle = root.element("center_IP");			
		Element center_PortEle = root.element("center_Port");	
		Element mon_UDPPortEle = root.element("mon_UDPLSN_PORT");
		serviceRegistryIP = ipEle.getText();
		serviceRegistryPort = Integer.parseInt(center_PortEle.getText());
		UDP_PORT = Integer.parseInt(mon_UDPPortEle.getText());
		System.out.println(serviceRegistryIP+":"+serviceRegistryPort);
	}
	/**
	 * UDPServer��Ҫ����һЩ����
	 */
	public void serverWork() {		
		//checkHostInfo();
		
		//��ȡע�����ĵ�IP��ַ �Լ� �����˿�
		readConfigXml();						
		try {
			while (true) {
//				SimpleDateFormat df = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				System.out.println(df.format(new Date()));
				
				CurrentInfo.dbHostList = TCPClient.sendHostListRequest();
				//CurrentInfo.serList = TCPClient.sendServiceStateRequest(serviceRegistryIP, serviceRegistryPort);
				// ���ͷ��������Ϣ�����ע������
				//TCPClient.sendServiceInvokeInfoRequest();
				// ��curInfo��ӡ������̨
				curInfo.printCurInfo();
				// ��������ϵͳ��ƽ��cpu���ڴ�ռ�ã�������ҳ��̬ͼ��ʾ,ʹ��QueueTest�ķ���
				// �浽�ļ�AvgCPUAndRam.txt����
				/*
				double[] d = new double[2];
				d[0] = curInfo.calulateAverageCpu();
				d[1] = curInfo.calulateAverageRam();
				QueueTest.putInHistoryAvgValue(d);
				*/
				// ������������ʷcpu���ڴ���Ϣ���������������������״��չʾ
				/*
				HashSet<Host> hoSet = new HashSet<Host>();
				hoSet.addAll(curInfo.hoSet);
				Iterator<Host> it1 = hoSet.iterator();
				while (it1.hasNext()) {
					Host h11 = (Host) it1.next();
					String[] strArray = new String[2];
					strArray[0] = h11.getCPU().split("%")[0];
					strArray[1] = h11.getRAM().split("%")[0];
					QueueTest.putInHistoryCPUAndRam(h11.getIP(), strArray);
				}
				*/
				// ��curInfo��������Ϣ���뵽curInfo.txt�ļ���
				curInfo.writeFile();
				// ÿʮ������ݿ��ȡһ������
				Thread.sleep(10000);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}

	/**
	 * �Ƚ�tempInfo��dbHostList�����ҵ���������curInfo��ȥ����ÿ��SampleTime*3��ʱ�����һ��tempInfo
	 */
	public static void checkHostInfo() {
		t.start();
	}

	@Override
	public void run() {		
		readConfigXml();
			
		System.out.println("UDPServer��ʼ����..." + UDP_PORT);
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(UDP_PORT);
		} catch (BindException e) {
			System.out.println("UDP�˿�ʹ����...���عرճ�����������");
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while (ds != null) {
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				ds.receive(dp);
				// �õ��Ѹ����ݰ������Ķ˿ں�Ip
				int rport = dp.getPort();
				InetAddress addr = dp.getAddress();
				// �õ�������
				ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(
						buf);
				ObjectInputStream objectStream = new ObjectInputStream(
						byteArrayStream);
				Message message = (Message) objectStream.readObject();
				// ����message�����͸�ǰ��

				// 1.���������Ϣ�Ĵ���
				if (message.getType().equals(MsgType.HostInfo1)) {
					ArrayList<Object> list = (ArrayList<Object>) message.getBody();
					/*System.out.println("��������" + list.get(0).toString());
					System.out.println("IP��ַ��" + list.get(1).toString());
					System.out.println("Mac��ַ��" + list.get(2).toString());
					System.out.println("CPUռ���ʣ�" + list.get(3).toString());
					System.out.println("�ڴ�ռ���ʣ�" + list.get(4).toString());
					System.out.println("Ӳ��ռ���ʣ���" + list.get(5).toString());
					System.out.println("�ڵ����ͣ�" + list.get(6).toString());
					System.out.println("����ϵͳ��" + list.get(7).toString());*/

					Host h = new Host(list.get(0).toString(), addr.toString().substring(1),
							list.get(2).toString(), list.get(3).toString(),
							list.get(4).toString(), list.get(5).toString(),
							list.get(6).toString(), list.get(7).toString());
					UDPServer.curInfo.addHost(h);
					Thread.sleep(30);

					// д�ļ�
//					curInfo.printCurInfo();
					curInfo.writeFile();
					Liminal l = Liminal.getLiminal();
					TCPClient tc = new TCPClient();
					// �жϸ�����cpu�����Ϣ�Ƿ񳬳���ֵ
					String hcpu = h.getCPU().split("%")[0];
					if (Double.parseDouble(hcpu) > l.getHostCPULiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// ���͸�ע�����ĵľ�����Ϣ����⡣����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogTime-->LogType-->LogContent-->ip-->container-->����״̬��Ĭ��0��ʾδ����
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("�ڵ�����");
						alarmInfoList.add("���棺����" + h.getIP() + "��CPUռ���ʳ�����ֵ"
								+ l.getHostCPULiminal() + "%");
						alarmInfoList.add(h.getIP());
						alarmInfoList.add("no container");
						alarmInfoList.add("0");
						Message aMsg = new Message(MsgType.AlarmStorageRequest,
								alarmInfoList);
						boolean result = tc.sendAlarmInfo(aMsg);
						while (!result) {
							result = tc.sendAlarmInfo(aMsg);
						}
					}
					// �жϸ������ڴ�����Ϣ�Ƿ񳬳���ֵ
					String hram = h.getRAM().split("%")[0];
					if (Double.parseDouble(hram) > l.getHostRamLiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// ����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogType-->LogContent-->LogTime
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("�ڵ�����");
						alarmInfoList.add("���棺����" + h.getIP() + "���ڴ�ռ���ʳ�����ֵ"
								+ l.getHostRamLiminal());
						alarmInfoList.add(h.getIP());
						alarmInfoList.add("no container");
						alarmInfoList.add("0");
						Message aMsg = new Message(MsgType.AlarmStorageRequest,
								alarmInfoList);
						boolean result = tc.sendAlarmInfo( aMsg);
						while (!result) {
							result = tc.sendAlarmInfo( aMsg);
						}
					}
					// �жϸ�����Ӳ�̼����Ϣ�Ƿ񳬳���ֵ
					String hhdd = h.getHdd().split("%")[0];
					if (Double.parseDouble(hhdd) > l.getHostHddLiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// ����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogType-->LogContent-->LogTime
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("�ڵ�����");
						alarmInfoList.add("���棺����" + h.getIP() + "��Ӳ��ռ���ʳ�����ֵ"
								+ l.getHostHddLiminal());
						alarmInfoList.add(h.getIP());
						alarmInfoList.add("no container");
						alarmInfoList.add("0");
						Message aMsg = new Message(MsgType.AlarmStorageRequest,
								alarmInfoList);
						boolean result = tc.sendAlarmInfo(aMsg);
						while (!result) {
							result = tc.sendAlarmInfo(aMsg);
						}
					}
					//
				}// end of ���������Ϣ�Ĵ���
					// 2.�������������Ϣ�Ĵ���
				/*
				else if (message.getType().equals(MsgType.ContainerInfo)) {
					ArrayList<Object> list = (ArrayList<Object>) message.getBody();
					// System.out.println("��������"+list.get(0).toString());
					// System.out.println("��������IP��ַ��"+list.get(1).toString());
					// System.out.println("CPUռ���ʣ�"+list.get(2).toString());
					// System.out.println("�ڴ�ռ���ʣ�"+list.get(3).toString());
					// System.out.println("���������"+list.get(4).toString());
					Container c = new Container(list.get(0).toString(), list
							.get(1).toString(), list.get(2).toString(), list
							.get(3).toString(), list.get(4).toString());
					UDPServer.curInfo.addContainer(c);
					Thread.sleep(30);
					// �жϸ�����cpu�����Ϣ�Ƿ񳬳���ֵ
					String ccpu = c.getCPU().split("%")[0];
					if (Double.parseDouble(ccpu) > Liminal.getConCPULiminal()) {
						// alarmCount++;
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// ����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogType-->LogContent-->LogTime
						// alarmInfoList.add(alarmCount+"");
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("containerAlarm");
						alarmInfoList.add("���棺����" + c.getIP() + "�ϵ�����"
								+ c.getConName() + "��CPUռ���ʳ�����ֵ"
								+ Liminal.getConCPULiminal());
						alarmInfoList.add(c.getIP());
						alarmInfoList.add(c.getConName());
						Message aMsg = new Message(MsgType.AlarmStorageRequest,
								alarmInfoList);
						boolean result = TCPClient.sendAlarmInfo(
								serviceRegistryIP, serviceRegistryPort, aMsg);
						while (!result) {
							result = TCPClient.sendAlarmInfo(serviceRegistryIP,
									serviceRegistryPort, aMsg);
						}
					}
					// �жϸ������ڴ�����Ϣ�Ƿ񳬳���ֵ
					String cram = c.getRam().split("%")[0];
					if (Double.parseDouble(cram) > Liminal.getConRamLiminal()) {
						// alarmCount++;
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// ����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogType-->LogContent-->LogTime
						// alarmInfoList.add(alarmCount+"");
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("containerAlarm");
						alarmInfoList.add("���棺����" + c.getIP() + "�ϵ�����"
								+ c.getConName() + "���ڴ�ռ���ʳ�����ֵ"
								+ Liminal.getConRamLiminal());
						alarmInfoList.add(c.getIP());
						alarmInfoList.add(c.getConName());
						Message aMsg = new Message(MsgType.AlarmStorageRequest,
								alarmInfoList);
						boolean result = TCPClient.sendAlarmInfo(
								serviceRegistryIP, serviceRegistryPort, aMsg);
						while (!result) {
							result = TCPClient.sendAlarmInfo(serviceRegistryIP,
									serviceRegistryPort, aMsg);
						}
					}
					//
				}// end of �������������Ϣ�Ĵ���
				*/
				// �ر�������
				objectStream.close();
				byteArrayStream.close();

				// ���ͻ��˻�Ӧ
				String sendStr = "echo of monitor message��";
				byte[] sendBuf;
				sendBuf = sendStr.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBuf,
						sendBuf.length, addr, rport);
				ds.send(sendPacket);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// while()

	}// run()
}
