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
 * 该类用来接收服务容器代理软件发送的主机和服务容器的监控信息
 * 
 * @author huangpeng
 * 
 */
public class UDPServer extends Thread {
	// 用于保存当前的主机和容器的监控信息
	public volatile static CurrentInfo curInfo = new CurrentInfo();
	public static CurrentInfo tempInfo = new CurrentInfo();

	byte[] buf = new byte[1024];// 接收到信息的缓存区
	private static int UDP_PORT;// UDP的监听接口
	
	public static void main(String[] args){
		UDPServer us = new UDPServer();
		us.start();	
		us.serverWork();
	}
	// 用于获取curInfo
	public static CurrentInfo getCurInfo() {
		return curInfo;
	}
	//自检线程，每隔sampleTime*3的时间检测一次是否有主机不再给监控软件发消息了，如果有，
	//从curInfo里面去除掉与该主机相关的信息。
	public static Thread t = new Thread(new CheckThread());
	
	// 注册中心的IP地址 以及 监听端口
	private static String serviceRegistryIP ;
	private static int serviceRegistryPort;
	private static Document doc;
	/**
	 * 读取config配置文件
	 */
	public static void readConfigXml(){
		try {
			doc = XmlUtils.getDocument();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();// 得到根节点
		Element ipEle = root.element("center_IP");			
		Element center_PortEle = root.element("center_Port");	
		Element mon_UDPPortEle = root.element("mon_UDPLSN_PORT");
		serviceRegistryIP = ipEle.getText();
		serviceRegistryPort = Integer.parseInt(center_PortEle.getText());
		UDP_PORT = Integer.parseInt(mon_UDPPortEle.getText());
		System.out.println(serviceRegistryIP+":"+serviceRegistryPort);
	}
	/**
	 * UDPServer里要做的一些工作
	 */
	public void serverWork() {		
		//checkHostInfo();
		
		//获取注册中心的IP地址 以及 监听端口
		readConfigXml();						
		try {
			while (true) {
//				SimpleDateFormat df = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				System.out.println(df.format(new Date()));
				
				CurrentInfo.dbHostList = TCPClient.sendHostListRequest();
				//CurrentInfo.serList = TCPClient.sendServiceStateRequest(serviceRegistryIP, serviceRegistryPort);
				// 发送服务调用信息请求给注册中心
				//TCPClient.sendServiceInvokeInfoRequest();
				// 将curInfo打印到控制台
				curInfo.printCurInfo();
				// 计算整个系统的平均cpu和内存占用，用于主页动态图显示,使用QueueTest的方法
				// 存到文件AvgCPUAndRam.txt里面
				/*
				double[] d = new double[2];
				d[0] = curInfo.calulateAverageCpu();
				d[1] = curInfo.calulateAverageRam();
				QueueTest.putInHistoryAvgValue(d);
				*/
				// 保存主机的历史cpu和内存信息，用于总体概览主机负载状况展示
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
				// 将curInfo的所有信息存入到curInfo.txt文件中
				curInfo.writeFile();
				// 每十秒从数据库获取一次数据
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
	 * 比较tempInfo和dbHostList，将挂掉的主机从curInfo里去掉，每隔SampleTime*3的时间清空一次tempInfo
	 */
	public static void checkHostInfo() {
		t.start();
	}

	@Override
	public void run() {		
		readConfigXml();
			
		System.out.println("UDPServer开始监听..." + UDP_PORT);
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(UDP_PORT);
		} catch (BindException e) {
			System.out.println("UDP端口使用中...请重关闭程序启服务器");
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while (ds != null) {
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				ds.receive(dp);
				// 得到把该数据包发来的端口和Ip
				int rport = dp.getPort();
				InetAddress addr = dp.getAddress();
				// 得到输入流
				ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(
						buf);
				ObjectInputStream objectStream = new ObjectInputStream(
						byteArrayStream);
				Message message = (Message) objectStream.readObject();
				// 处理message，发送给前端

				// 1.主机监控信息的处理
				if (message.getType().equals(MsgType.HostInfo1)) {
					ArrayList<Object> list = (ArrayList<Object>) message.getBody();
					/*System.out.println("主机名：" + list.get(0).toString());
					System.out.println("IP地址：" + list.get(1).toString());
					System.out.println("Mac地址：" + list.get(2).toString());
					System.out.println("CPU占用率：" + list.get(3).toString());
					System.out.println("内存占用率：" + list.get(4).toString());
					System.out.println("硬盘占用率：：" + list.get(5).toString());
					System.out.println("节点类型：" + list.get(6).toString());
					System.out.println("操作系统：" + list.get(7).toString());*/

					Host h = new Host(list.get(0).toString(), addr.toString().substring(1),
							list.get(2).toString(), list.get(3).toString(),
							list.get(4).toString(), list.get(5).toString(),
							list.get(6).toString(), list.get(7).toString());
					UDPServer.curInfo.addHost(h);
					Thread.sleep(30);

					// 写文件
//					curInfo.printCurInfo();
					curInfo.writeFile();
					Liminal l = Liminal.getLiminal();
					TCPClient tc = new TCPClient();
					// 判断该主机cpu监控信息是否超出阈值
					String hcpu = h.getCPU().split("%")[0];
					if (Double.parseDouble(hcpu) > l.getHostCPULiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// 发送给注册中心的警报消息，入库。给消息体填充信息，一定要按照顺序填充.LogNumber-->LogTime-->LogType-->LogContent-->ip-->container-->处理状态（默认0表示未处理）
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置日期格式
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("节点类型");
						alarmInfoList.add("警告：主机" + h.getIP() + "的CPU占用率超过阈值"
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
					// 判断该主机内存监控信息是否超出阈值
					String hram = h.getRAM().split("%")[0];
					if (Double.parseDouble(hram) > l.getHostRamLiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// 给消息体填充信息，一定要按照顺序填充.LogNumber-->LogType-->LogContent-->LogTime
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置日期格式
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("节点类型");
						alarmInfoList.add("警告：主机" + h.getIP() + "的内存占用率超过阈值"
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
					// 判断该主机硬盘监控信息是否超出阈值
					String hhdd = h.getHdd().split("%")[0];
					if (Double.parseDouble(hhdd) > l.getHostHddLiminal()) {
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// 给消息体填充信息，一定要按照顺序填充.LogNumber-->LogType-->LogContent-->LogTime
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置日期格式
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("节点类型");
						alarmInfoList.add("警告：主机" + h.getIP() + "的硬盘占用率超过阈值"
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
				}// end of 主机监控信息的处理
					// 2.服务容器监控信息的处理
				/*
				else if (message.getType().equals(MsgType.ContainerInfo)) {
					ArrayList<Object> list = (ArrayList<Object>) message.getBody();
					// System.out.println("容器名："+list.get(0).toString());
					// System.out.println("容器所在IP地址："+list.get(1).toString());
					// System.out.println("CPU占用率："+list.get(2).toString());
					// System.out.println("内存占用率："+list.get(3).toString());
					// System.out.println("服务个数："+list.get(4).toString());
					Container c = new Container(list.get(0).toString(), list
							.get(1).toString(), list.get(2).toString(), list
							.get(3).toString(), list.get(4).toString());
					UDPServer.curInfo.addContainer(c);
					Thread.sleep(30);
					// 判断该容器cpu监控信息是否超出阈值
					String ccpu = c.getCPU().split("%")[0];
					if (Double.parseDouble(ccpu) > Liminal.getConCPULiminal()) {
						// alarmCount++;
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// 给消息体填充信息，一定要按照顺序填充.LogNumber-->LogType-->LogContent-->LogTime
						// alarmInfoList.add(alarmCount+"");
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置日期格式
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("containerAlarm");
						alarmInfoList.add("警告：主机" + c.getIP() + "上的容器"
								+ c.getConName() + "的CPU占用率超过阈值"
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
					// 判断该容器内存监控信息是否超出阈值
					String cram = c.getRam().split("%")[0];
					if (Double.parseDouble(cram) > Liminal.getConRamLiminal()) {
						// alarmCount++;
						ArrayList<Object> alarmInfoList = new ArrayList<Object>();
						// 给消息体填充信息，一定要按照顺序填充.LogNumber-->LogType-->LogContent-->LogTime
						// alarmInfoList.add(alarmCount+"");
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置日期格式
						alarmInfoList.add(df.format(new Date()));
						alarmInfoList.add("containerAlarm");
						alarmInfoList.add("警告：主机" + c.getIP() + "上的容器"
								+ c.getConName() + "的内存占用率超过阈值"
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
				}// end of 服务容器监控信息的处理
				*/
				// 关闭输入流
				objectStream.close();
				byteArrayStream.close();

				// 给客户端回应
				String sendStr = "echo of monitor message！";
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
