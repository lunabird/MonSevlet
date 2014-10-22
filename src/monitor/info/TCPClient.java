package monitor.info;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.Message;
import Message.MsgType;
import Message.XmlUtils;

/**
 * 该类主要用来与注册中心的数据库进行交互
 * 
 * @author huangpeng
 * 
 */
public class TCPClient {
	// 注册中心的IP地址 以及 监听端口
	public static String serviceRegistryIP ;
	public static int serviceRegistryPort;
	static Document doc;
	
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
		serviceRegistryIP = ipEle.getText();
		serviceRegistryPort = Integer.parseInt(center_PortEle.getText());
//		System.out.println(serviceRegistryIP+","+serviceRegistryPort);
	}
	
	public TCPClient(){
		readConfigXml();
	}
	//*************************监控概览界面（3.1）设计*********************************//
	/**
	 * 3.1.1系统结构概览.表示请求的是所有服务（包含其副本）的详细信息
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceDetailInfoWithCopyRequest() throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceDetailInfoWithCopyRequest,null);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceDetailInfoWithCopy)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的所有服务（包含其副本）的详细信息列表为空！");
			} else {
				System.out.println("获取到的数据库中的所有服务（包含其副本）的详细信息列表如下：");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]+","+list.get(i)[3]+","+list.get(i)[4]+","+list.get(i)[5]);
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.1.2服务访问量统计。表示从当前时刻倒推10分钟，每分钟的服务调用数量。比如0~1分钟：1个，1~2分钟：2个，一直到9~10分钟：0个
	 * @param host
	 * @param port
	 * @param str
	 * @return 10个int类型的数字
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendServiceCallInfoRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCallInfoRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.serviceCallInfo)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("获取到的数据库中的服务访问量统计列表为空！");
			} else {
				System.out.println("获取到的服务访问量统计如下：");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return list;
	}
	/**
	 * 3.1.3服务调用时间(1小时内)统计。表示服务的执行时间。例如在0ms~10ms能运行完毕的服务个数：3个，10ms~20ms能运行完毕的个数：5个
	 * @param host
	 * @param port
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<Object> sendServiceRunTimeRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<Object> list = new ArrayList<Object>();
		if (msg.getType().equals(MsgType.serviceRunTime)) {
			list = (ArrayList<Object>) msg.getBody();			
			if (msg.getBody()==null) {
				System.out.println("获取到的数据库中的服务调用时间统计列表为空！");
			} else {
				int[] theNum = (int[]) list.get(0);
				String theStartTime = (String)list.get(1);
				String theEndTime = (String)list.get(2);
				System.out.println("获取到服务调用时间统计如下：");
				for(int i=0;i<theNum.length;i++){
					System.out.print(theNum[i]+",");
				}
				System.out.println();
				System.out.println(theStartTime+"-"+theEndTime);
			}//else
		}//if
		return list;
	}
	//**********************详细信息-服务信息界面（3.2）设计***************************//
	/**
	 * 3.2.1 服务类别（例如情报服务，电抗服务）
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String> sendServiceSystemRequest() throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceBussinessTypeRequest,null);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String> list = new ArrayList<String>();
		if (msg.getType().equals(MsgType.serviceBussinessType)) {
			list = (ArrayList<String>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的服务系统名称列表为空！");
			} else {
				System.out.println("获取到的数据库中的服务系统名称列表如下：");
				for(int i=0;i<list.size();i++){
					System.out.print(list.get(i)+" ");
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.2.3 服务访问量统计.展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.
	 * @param host
	 * @param port
	 * @param str String[] str,第一个String表示服务系统类型（情报/雷达/…服务），第二个表示当前时刻
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendServiceCalInfoReq(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCallInfoRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.serviceCallInfo)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("获取到的数据库中的"+str[0]+"的服务访问量统计列表为空！");
			} else {
				System.out.println("获取到的"+str[0]+"服务访问量统计如下：");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.2.4 服务调用时间统计（1小时内）.展示该类别所有服务在一段时间调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
	 * @param host
	 * @param port
	 * @param str String[] str,第一个String表示服务系统类型（情报/雷达/…服务），第二个表示当前时刻
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<Object> sendServiceRunTimeRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<Object> list = new ArrayList<Object>();
		if (msg.getType().equals(MsgType.serviceRunTime)) {
			list = (ArrayList<Object>) msg.getBody();			
			if (msg.getBody()==null) {
				System.out.println("获取到的数据库中的"+str[0]+"的服务调用时间统计列表为空！");
			} else {
				int[] theNum = (int[]) list.get(0);
				String theStartTime = (String)list.get(1);
				String theEndTime = (String)list.get(2);
				System.out.println("获取到的"+str[0]+"服务调用时间统计如下：");
				for(int i=0;i<theNum.length;i++){
					System.out.print(theNum[i]+",");
				}
				System.out.println();
				System.out.println(list.get(1)+"-"+list.get(2));				
			}//else
		}//if
		return list;		
	}
	/**
	 * 3.2.5 当前最活跃的服务访问.最近1个小时内访问数量最多的10个服务。横坐标为服务名，纵坐标为访问数量
	 * @param host
	 * @param port
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendActiveServiceRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.activeServiceRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.activeService)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的"+str[0]+"的当前最活跃的服务访问信息为空！");
			} else {
				System.out.println("获取到的"+str[0]+"当前最活跃的服务访问信息如下：");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]);
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.2.6 服务详细信息.(没有用到)
	 * @param host
	 * @param port
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceDetailInfoListRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceDetailInfoListRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceDetailInfoList)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的"+str+"的服务详细信息列表为空！");
			} else {
				System.out.println("获取到的"+str+"的服务详细信息列表信息如下：");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]+","+list.get(i)[3]+","+list.get(i)[4]+","+list.get(i)[5]+","+list.get(i)[6]+","+list.get(i)[7]);
				}
			}//else
		}//if
		return list;
	}
	//**********************服务详情信息界面（3.3）设计***************************//
	/**3.3.1 服务平均访问量.展示该服务（包括副本）在一段时间（10分钟等）内的平均调用量。横坐标表示时间，纵坐标表示调用数量
	 * @param host
	 * @param port
	 * @param str String[]服务ID和当前时间
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendSingleServiceCallTimesRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.singleServiceCallTimesRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.singleServiceCallTimes)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("获取到的数据库中的"+str[0]+"服务平均访问量为空！");
			} else {
				System.out.println("获取到的"+str[0]+"服务平均访问量统计如下：");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.3.2 单个服务平均响应时间。表示从当前时间倒推1小时该服务及其副本被调用时的平均响应时间。例如0~10分钟，33.3ms（该服务一共被调用了3次，第一次响应了50ms，第二次和第三次各响应了25ms，平均每次的响应时间为33.3ms）；10~20分钟，0ms（表示这段时间没被调用）；一直到50~60分钟，0ms）
	 * @param host
	 * @param port
	 * @param str String[]服务ID和当前时间
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  double[] sendSingleServiceRunTimeRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.singleServiceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		double[] list = new double[10];
		if (msg.getType().equals(MsgType.singleServiceRunTime)) {
			list = (double[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("获取到的数据库中的"+str[0]+"的服务调用时间统计列表为空！");
			} else {
				System.out.println("获取到的"+str[0]+"服务调用时间统计如下：");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.3.3 服务拓扑结构
	 * @param host
	 * @param port
	 * @param str 表示serviceID(服务ID)
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceCopyRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCopyRequest, str);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceCopy)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的"+str+"的服务副本IP列表为空！");
			} else {
				System.out.println("获取到的"+str+"服务服务副本IP列表如下：");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]);
				}
			}//else
		}//if
		return list;
	}
	//**********************节点详情信息界面（3.5）设计***************************//
	/**
	 * 3.5.1&3.5.2CPU,内存
	 * @param host
	 * @param port
	 * @param ip
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendNodeHistoryCPURAMRequest(String ip) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.nodeHistoryCPURAMRequest, ip);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.nodeHistoryCPURAM)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的"+ip+"该节点的历史CPU和内存信息列表为空！");
			} else {
				System.out.println("获取到的"+ip+"该节点的历史CPU和内存信息列表如下：");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]);
				}
			}//else
		}//if
		return list;
	}
	//**************************  参数配置界面（3.7）设计***************************//
	public  boolean sendserviceRequest(String concurrentNum) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.concurrentNum, concurrentNum);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		boolean result = false;
		if (msg.getType().equals(MsgType.concurrentNum)) {
			result = (Boolean) msg.getBody();
			if (result) {
				System.out.println("修改服务并发数成功！");
			} else {
				System.out.println("修改服务并发数失败！");
			}//else
		}//if
		return result;
	}
	
	//**************************   z   f    codes     ***************************//
	/*
	 * 显示不带副本的服务列表
	 */
	public ArrayList<String[]> serviceList() throws IOException, ClassNotFoundException{
		//显示不带副本的服务列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.SERVICELIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICELIST,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.SERVICELISTRESULT，MsgBody为服务列表
		ArrayList<String[]> serviceList = null;
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.SERVICELISTRESULT){
			serviceList = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return serviceList;
		
	}
	/*
	 * 显示带副本的服务列表	
	 */
	public ArrayList<String[]> serviceCopyList() throws IOException, ClassNotFoundException{
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.SERVICECOPYLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICECOPYLIST,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.SERVICECOPYLISTRESULT，MsgBody为带副本的服务列表
		ArrayList<String[]> serviceCopyList = new ArrayList<String[]>();
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.SERVICECOPYLISTRESULT){
			serviceCopyList = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return serviceCopyList;
		
	}
	/*
	 * 获取接口信息
	 */
	public ArrayList<String[]> interfaceInfo()
			throws UnknownHostException, IOException, ClassNotFoundException {

		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.MESSAGELIST, null);
		outputStream.writeObject(outMes);
		outputStream.flush();

		ArrayList<String[]> Info = new ArrayList<String[]>();
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.MESSAGELISTRESULT) {
			Info = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return Info;

	}	
	/*
	 * 显示节点列表
	 */
	public ArrayList<String[]> nodeList() throws IOException,
			ClassNotFoundException {
		// 显示主机列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 向服务引擎发送Message消息，类型为MsgType.HOSTLIST
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.NODELIST, null);
		outputStream.writeObject(outMes);
		outputStream.flush();

		// 接收服务引擎返回的Message消息，类型为MsgType.HOSTLISTRESULT，MsgBody为主机列表
		ArrayList<String[]> nodeList = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.NODELISTRESULT) {
			nodeList = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return nodeList;

	}
	/*
	 * 节点CPU和内存信息
	 */
	public ArrayList<String[]> nodeCpuMem(String nodeip) throws IOException, ClassNotFoundException{
		//显示主机列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.HOSTINFO,nodeip);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.HOSTLISTRESULT，MsgBody为主机列表
		ArrayList<String[]> nodeList = null;
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.HOSTINFO){
			nodeList = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return nodeList;
		
	}	
	/*
	 * 节点硬盘信息
	 */
	public ArrayList<String[]> nodeHarddisk(String nodeip) throws IOException, ClassNotFoundException{
		//显示主机列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.HOSTHARDDISK,nodeip);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.HOSTLISTRESULT，MsgBody为主机列表
		ArrayList<String[]> nodeList = null;
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.HOSTHARDDISK){
			nodeList = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return nodeList;		
	}
	/*
	 * 条件筛选警报
	 */
	public ArrayList<String[]> filterWarn(ArrayList<String> filterWarn) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//System.out.println(host+"   "+port);
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.FILTERWARN,filterWarn);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		ArrayList<String[]> filterResult = new ArrayList<String[]>();
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.FILTERWARN){
			filterResult = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
//		System.out.println("filterResult-----"+filterResult);
		return filterResult;
		
	}
	
	
	//以下为界面右上角的柱状图信息
	/*
	 * 服务详细信息
	 */
	public ArrayList<String> serviceDetail() throws IOException, ClassNotFoundException{
		//显示主机列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICEDETAIL,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.HOSTLISTRESULT，MsgBody为主机列表
		ArrayList<String> serviceDetail = null;
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.SERVICEDETAIL){
			serviceDetail = (ArrayList<String>) inMes.getBody();
		}
		socket.close();
		return serviceDetail;		
	}
	
	/*
	 * 节点详细信息
	 */
	public ArrayList<String> nodeDetail() throws IOException, ClassNotFoundException{
		//显示主机列表
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//向服务引擎发送Message消息，类型为MsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.NODEDETAIL,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//接收服务引擎返回的Message消息，类型为MsgType.HOSTLISTRESULT，MsgBody为主机列表
		ArrayList<String> nodeDetail = null;
		ObjectInputStream inputStream=new ObjectInputStream(socket.getInputStream());
		Message inMes=(Message)inputStream.readObject();
		if(inMes.getType() == MsgType.NODEDETAIL){
			nodeDetail = (ArrayList<String>) inMes.getBody();
		}
		socket.close();
		return nodeDetail;		
	}
	/*
	 * 条件筛选
	 */
	public HashSet<String> filter(String filtertype)
			throws UnknownHostException, IOException, ClassNotFoundException {

		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.FILTER, filtertype);
		outputStream.writeObject(outMes);
		outputStream.flush();

		HashSet<String> filterResult = new HashSet<String>();
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.FILTERRESULT) {
			filterResult = (HashSet<String>) inMes.getBody();
		}
		socket.close();
		return filterResult;

	}
	
	
	
	/*
	 * 条件筛选服务
	 */
	public ArrayList<String[]> filterService(ArrayList<String> filterService)
			throws UnknownHostException, IOException, ClassNotFoundException {

		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.FILTERSERVICE, filterService);
		outputStream.writeObject(outMes);
		outputStream.flush();

		ArrayList<String[]> filterResult = new ArrayList<String[]>();
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.FILTERSERVICERESULT) {
			filterResult = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return filterResult;

	}
	/*
	 * 服务和节点总数量
	 */
	public String getNum(String type) throws IOException,
			ClassNotFoundException {
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.SIZENUM, type);
		outputStream.writeObject(outMes);
		outputStream.flush();

		String number = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.SIZENUM) {
			number = (String) inMes.getBody();
		}
		socket.close();
		return number;
	}
	/*
	 * 警报总数量
	 */
	public String getLogNum() throws IOException,
			ClassNotFoundException {
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.LOGSIZE, null);
		outputStream.writeObject(outMes);
		outputStream.flush();

		String number = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.LOGSIZE) {
			number = (String) inMes.getBody();
		}
		socket.close();
		return number;
	}
	/*
	 * 筛选后服务和节点总数量
	 */
	public String getFilterNum(ArrayList<String> filterInfo)
			throws IOException, ClassNotFoundException {
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.FILTERSIZE, filterInfo);
		outputStream.writeObject(outMes);
		outputStream.flush();

		String number = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.FILTERSIZE) {
			number = (String) inMes.getBody();
		}
		socket.close();
		return number;
	}
	/*
	 * 筛选后警报总数量
	 */
	public String getLogFilterNum(ArrayList<String> filterInfo)
			throws IOException, ClassNotFoundException {
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.FILTERLOGSIZE, filterInfo);
		outputStream.writeObject(outMes);
		outputStream.flush();

		String number = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.FILTERLOGSIZE) {
			number = (String) inMes.getBody();
		}
		socket.close();
		return number;
	}
	/*
	 * 节点类型筛选
	 */
	public HashSet<String> nodeFilterFirst(String filter) throws IOException,
			ClassNotFoundException {

		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.FILTERNODE, filter);
		outputStream.writeObject(outMes);
		outputStream.flush();

		HashSet<String> filterResult = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.FILTERNODERESULT) {
			filterResult = (HashSet<String>) inMes.getBody();
		}
		socket.close();
		return filterResult;

	}
	
	/*
	 * 节点筛选,模糊查询
	 */
	public ArrayList<String[]> nodeFilter(ArrayList<String> nodeinfo)
			throws IOException, ClassNotFoundException {

		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.CFILTERNODE, nodeinfo);
		outputStream.writeObject(outMes);
		outputStream.flush();

		ArrayList<String[]> filterResult = null;
		ObjectInputStream inputStream = new ObjectInputStream(
				socket.getInputStream());
		Message inMes = (Message) inputStream.readObject();
		if (inMes.getType() == MsgType.CFILTERNODERESULT) {
			filterResult = (ArrayList<String[]>) inMes.getBody();
		}
		socket.close();
		return filterResult;

	}
	//**********************###########history##########***************************//
	/**
	 * 发送请求所有服务状态消息给数据库
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<Service> sendServiceStateRequest(final String host,
			final int port) throws UnknownHostException, IOException,
			ClassNotFoundException {
		Socket s = new Socket(host, port);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.AllServiceStateRequest, null);
		oos.writeObject(aMsg);

		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<Service> lista = new ArrayList<Service>();
		if (msg.getType().equals(MsgType.AllServiceState)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的服务状态信息列表为空！");
			} else {
				try {
					for (int i = 0; i < list.size(); i++) {
						String[] serviceInfo = (String[]) list.get(i);
						System.out.println("服务名：" + serviceInfo[0].toString());
						System.out.println("所在主机IP地址："
								+ serviceInfo[1].toString());
						System.out.println("当前状态：" + serviceInfo[2].toString());
						System.out.println("类型：" + serviceInfo[3].toString());
						System.out.println("所在系统：" + serviceInfo[4].toString());
						Service ser = new Service(serviceInfo[0].toString(),
								serviceInfo[3].toString(),
								serviceInfo[4].toString(),
								serviceInfo[1].toString(),
								serviceInfo[2].toString());
						lista.add(ser);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 关闭Socket
		oos.close();
		s.close();
		// 发送给前台页面
		return lista;
	}

	/**
	 * 发送请求所有的主机列表请求给数据库
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DocumentException 
	 */
	public static ArrayList<String> sendHostListRequest()
			throws UnknownHostException, IOException, ClassNotFoundException {
		// System.out.println("sendHostListRequest：");
		readConfigXml();
		System.out.println(serviceRegistryIP+":"+serviceRegistryPort);
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.AllHostRequest, null);
		oos.writeObject(aMsg);

		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String> list = new ArrayList<String>();
		if (msg.getType().equals(MsgType.AllHost)) {
			list = (ArrayList<String>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX主机列表空！！！！！");
			} else {
				// System.out.println("从数据库获得的主机IP列表：");
				// for(int i=0;i<list.size();i++){
				// System.out.print(list.get(i)+",");
				// }
				// System.out.println();
			}

		}
		// 关闭Socket
		oos.close();
		s.close();
		return list;
	}

	/**
	 * 发送警报信息给数据库
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  boolean sendAlarmInfo(Message amsg) throws UnknownHostException, IOException,
			ClassNotFoundException {
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		oos.writeObject(amsg);

		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		boolean result = false;
		if (msg.getType().equals(MsgType.AlarmStorageResult)) {
			result = (Boolean) msg.getBody();
			if (result) {
				System.out.println("警报入库成功！");
			} else {
				System.out.println("警报入库失败！");
			}
		}
		// 关闭Socket
		oos.close();
		s.close();
		return result;
	}

	/**
	 * 3.6从数据库请求所有的警报信息，用于警报列表页面展示
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> receiveAlarmInfoList() throws UnknownHostException, IOException,
			ClassNotFoundException {
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message amsg = new Message(MsgType.AlarmListRequest, null);
		oos.writeObject(amsg);

		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.AlarmListResult)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的警报信息列表为空！");
			} else {
				for (int i = 0; i < list.size(); i++) {
					String LogNumber = list.get(i)[0];
					String LogTime = list.get(i)[1];
					String LogType = list.get(i)[2];
					String LogContent = list.get(i)[3];
					String ip = list.get(i)[4];
					String container = list.get(i)[5];
					String status = list.get(i)[6];
//					System.out.println("LogNumber:" + LogNumber + "\nLogTime:"
//							+ LogTime + "\nLogType:" + LogType
//							+ "\nLogContent:" + LogContent + "\nip:" + ip
//							+ "\ncontainer:" + container+"\nstatus:"+status);
				}
			}
		}
		return list;
	}
	/**
	 * 处理警报信息状态
	 * @param host
	 * @param port
	 * @param list1
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  boolean sendHandleAlarmRequest(ArrayList<String> list1) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.handleAlarmRequest, list1);
		oos.writeObject(aMsg);
		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		Boolean res = false;
		if (msg.getType().equals(MsgType.handleAlarmResult)) {
			res = (Boolean) msg.getBody();
			if (res) {
				System.out.println("警报状态处理成功");
			}
		}//if
		return res;
	}
	/**
	 * 请求服务调用的信息（需要解析时间字段，获取某段时间内调用的服务的集合） ）
	 * 
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceInvokeInfoRequest()
			throws UnknownHostException, IOException, ClassNotFoundException {
//		readConfigXml();
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// 发送Socket消息
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.ServiceInvokeInfoRequest, null);
		oos.writeObject(aMsg);

		// 接收服务器的反馈
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.ServiceInvokeInfoResult)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("获取到的数据库中的服务调用信息列表为空！");
			} else {
				System.out.println("获取到的数据库中的服务调用信息:");
				for (int i = 0; i < list.size(); i++) {
					String[] serviceInfo = (String[]) list.get(i);
					System.out.println("Time：" + serviceInfo[1].toString());
					System.out.println("ServiceName："
							+ serviceInfo[2].toString());
					System.out
							.println("ServiceIP：" + serviceInfo[3].toString());
					System.out.println("ServiceType："
							+ serviceInfo[4].toString());
					System.out.println("BussinessType："
							+ serviceInfo[5].toString());
					System.out.println("Status：" + serviceInfo[6].toString());
				}
			}
		}
		// 关闭Socket
		oos.close();
		s.close();
		// 发送给前台页面
		return list;
	}
	/***
	 * 把数组逆置
	 * @param array
	 * @return
	 */
	public static <T> T invertArray(T array) {
		int len = Array.getLength(array);
		Class<?> classz = array.getClass().getComponentType();
		Object dest = Array.newInstance(classz, len);
		System.arraycopy(array, 0, dest, 0, len);
		Object temp;
		for (int i = 0; i < (len / 2); i++) {
			temp = Array.get(dest, i);
			Array.set(dest, i, Array.get(dest, len - i - 1));
			Array.set(dest, len - i - 1, temp);
		}
		return (T) dest;
	}
	
	

	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException, ParseException {
		
		String ip = "192.168.0.204";//注册中心IP
		int port = 5000;
		// 1.构造一个发送请求所有服务状态消息给数据库的测试
		// TCPClient.sendServiceStateRequest(serviceRegistryIP,
		// serviceRegistryPort);

		// 2.构造一个发送请求所有的主机列表请求给数据库的测试
		// TCPClient.sendHostListRequest();
		//
		// //3.构造一个警报入库的测试
//		 ArrayList<String> alarmInfoList = new ArrayList<String>();
//		 //给消息体填充信息，一定要按照顺序填充.LogNumber-->LogTime-->LogType-->LogContent-->ip-->container(空=“0”)-->status
//		 SimpleDateFormat df = new
//		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		 
//		 alarmInfoList.add(df.format(new Date()));
//		 alarmInfoList.add("hostAlarm");//Type有两类，一类是hostAlarm,一类是containerAlarm
//		 alarmInfoList.add("警告：主机192.168.0.98的CPU占用率超过80%");
//		 alarmInfoList.add("192.168.0.96");
//		 alarmInfoList.add("no container");
//		 alarmInfoList.add("0");
//		 Message aMsg = new Message(MsgType.AlarmStorageRequest,alarmInfoList);
//		 TCPClient.sendAlarmInfo(ip, port,aMsg);
		//
		// //4.构造一个请求警报列表测试
		//TCPClient.receiveAlarmInfoList(serviceRegistryIP, serviceRegistryPort);

		// 5.构造一个请求服务调用的信息测试
		// TCPClient.sendServiceInvokeInfoRequest();
		
		
		
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	    String dateString = "2014/08/03 17:20:32";
//	    Date date = df.parse(dateString);
	    long a = 1407058048193L;//date.getTime();
	    String s = String.valueOf(a);
//	    Timestamp now = new Timestamp(a);//	    Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
	    Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间 
	    String str = df.format(now); 
	    
		//3.1.1系统结构概览.表示请求的是所有服务（包含其副本）的详细信息
//		TCPClient.sendServiceDetailInfoWithCopyRequest(ip,port);
		
		//3.1.2服务访问量统计
//		TCPClient.sendServiceCallInfoRequest(ip,port,s);
		
		//3.1.3服务调用时间统计
//		TCPClient.sendServiceRunTimeRequest(ip,port,s);
		
		//3.2.1 服务类别（例如情报服务，电抗服务）
//		TCPClient.sendServiceSystemRequest(ip,port);
		
		//3.2.3 服务访问量统计.展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.
		String[] s1 = new String[2];s1[0] = "情报服务";s1[1]=s;
//		TCPClient.sendServiceCalInfoReq(ip,port,s1);
		
		//3.2.4 服务调用时间统计.展示该类别所有服务在一段时间（1小时内）调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
//		TCPClient.sendServiceRunTimeRequest(ip,port,s1);
		
		//3.2.5 当前最活跃的服务访问.最近1个小时内访问数量最多的10个服务。横坐标为服务名，纵坐标为访问数量		
//		TCPClient.sendActiveServiceRequest(ip,port,s1);
		
		//3.2.6 服务详细信息.
//		TCPClient.sendServiceDetailInfoListRequest(ip,port,s1[0]);
		
		//3.3.1 服务平均访问量.展示该服务（包括副本）在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量
		String[] s2 = new String[2];s2[0] = "020";s2[1] = s;
//		TCPClient.sendSingleServiceCallTimesRequest(ip,port,s2);
		
		//3.3.2 单个服务平均响应时间
//		TCPClient.sendSingleServiceRunTimeRequest(ip,port,s2);
		
		// 3.3.3 服务拓扑结构
//		TCPClient.sendServiceCopyRequest(ip,port,s2[0]);
		
		//3.5.1&3.5.2CPU,内存
//		TCPClient.sendNodeHistoryCPURAMRequest(ip,port,"192.168.0.6");
		
		//3.6.1警报列表
//		TCPClient.receiveAlarmInfoList(ip,port);
		/*修改警报状态*/
//		ArrayList<String> list1 = new ArrayList<String>();
//		list1.add("1");list1.add("0");
//		TCPClient.sendHandleAlarmRequest(ip, port, list1);
		//获取警报总数
		TCPClient c = new TCPClient();
		System.out.println(c.getLogNum());
		//获取筛选后的数量
		ArrayList<String> filterInfo = new ArrayList<String>();
		filterInfo.add("LogContent");
		filterInfo.add("警告");
		System.out.println(c.getLogFilterNum(filterInfo));
		
	}// main

}// TCPClient
