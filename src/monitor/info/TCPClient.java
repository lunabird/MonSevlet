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
 * ������Ҫ������ע�����ĵ����ݿ���н���
 * 
 * @author huangpeng
 * 
 */
public class TCPClient {
	// ע�����ĵ�IP��ַ �Լ� �����˿�
	public static String serviceRegistryIP ;
	public static int serviceRegistryPort;
	static Document doc;
	
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
		serviceRegistryIP = ipEle.getText();
		serviceRegistryPort = Integer.parseInt(center_PortEle.getText());
//		System.out.println(serviceRegistryIP+","+serviceRegistryPort);
	}
	
	public TCPClient(){
		readConfigXml();
	}
	//*************************��ظ������棨3.1�����*********************************//
	/**
	 * 3.1.1ϵͳ�ṹ����.��ʾ����������з��񣨰����丱��������ϸ��Ϣ
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceDetailInfoWithCopyRequest() throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceDetailInfoWithCopyRequest,null);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceDetailInfoWithCopy)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��е����з��񣨰����丱��������ϸ��Ϣ�б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ�������ݿ��е����з��񣨰����丱��������ϸ��Ϣ�б����£�");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]+","+list.get(i)[3]+","+list.get(i)[4]+","+list.get(i)[5]);
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.1.2���������ͳ�ơ���ʾ�ӵ�ǰʱ�̵���10���ӣ�ÿ���ӵķ����������������0~1���ӣ�1����1~2���ӣ�2����һֱ��9~10���ӣ�0��
	 * @param host
	 * @param port
	 * @param str
	 * @return 10��int���͵�����
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendServiceCallInfoRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCallInfoRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.serviceCallInfo)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("��ȡ�������ݿ��еķ��������ͳ���б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ���ķ��������ͳ�����£�");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return list;
	}
	/**
	 * 3.1.3�������ʱ��(1Сʱ��)ͳ�ơ���ʾ�����ִ��ʱ�䡣������0ms~10ms��������ϵķ��������3����10ms~20ms��������ϵĸ�����5��
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<Object> list = new ArrayList<Object>();
		if (msg.getType().equals(MsgType.serviceRunTime)) {
			list = (ArrayList<Object>) msg.getBody();			
			if (msg.getBody()==null) {
				System.out.println("��ȡ�������ݿ��еķ������ʱ��ͳ���б�Ϊ�գ�");
			} else {
				int[] theNum = (int[]) list.get(0);
				String theStartTime = (String)list.get(1);
				String theEndTime = (String)list.get(2);
				System.out.println("��ȡ���������ʱ��ͳ�����£�");
				for(int i=0;i<theNum.length;i++){
					System.out.print(theNum[i]+",");
				}
				System.out.println();
				System.out.println(theStartTime+"-"+theEndTime);
			}//else
		}//if
		return list;
	}
	//**********************��ϸ��Ϣ-������Ϣ���棨3.2�����***************************//
	/**
	 * 3.2.1 ������������鱨���񣬵翹����
	 * @param host
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String> sendServiceSystemRequest() throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceBussinessTypeRequest,null);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String> list = new ArrayList<String>();
		if (msg.getType().equals(MsgType.serviceBussinessType)) {
			list = (ArrayList<String>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��еķ���ϵͳ�����б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ�������ݿ��еķ���ϵͳ�����б����£�");
				for(int i=0;i<list.size();i++){
					System.out.print(list.get(i)+" ");
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.2.3 ���������ͳ��.չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.
	 * @param host
	 * @param port
	 * @param str String[] str,��һ��String��ʾ����ϵͳ���ͣ��鱨/�״�/�����񣩣��ڶ�����ʾ��ǰʱ��
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendServiceCalInfoReq(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCallInfoRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.serviceCallInfo)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str[0]+"�ķ��������ͳ���б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+str[0]+"���������ͳ�����£�");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.2.4 �������ʱ��ͳ�ƣ�1Сʱ�ڣ�.չʾ��������з�����һ��ʱ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
	 * @param host
	 * @param port
	 * @param str String[] str,��һ��String��ʾ����ϵͳ���ͣ��鱨/�״�/�����񣩣��ڶ�����ʾ��ǰʱ��
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<Object> sendServiceRunTimeRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<Object> list = new ArrayList<Object>();
		if (msg.getType().equals(MsgType.serviceRunTime)) {
			list = (ArrayList<Object>) msg.getBody();			
			if (msg.getBody()==null) {
				System.out.println("��ȡ�������ݿ��е�"+str[0]+"�ķ������ʱ��ͳ���б�Ϊ�գ�");
			} else {
				int[] theNum = (int[]) list.get(0);
				String theStartTime = (String)list.get(1);
				String theEndTime = (String)list.get(2);
				System.out.println("��ȡ����"+str[0]+"�������ʱ��ͳ�����£�");
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
	 * 3.2.5 ��ǰ���Ծ�ķ������.���1��Сʱ�ڷ�����������10�����񡣺�����Ϊ��������������Ϊ��������
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.activeServiceRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.activeService)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str[0]+"�ĵ�ǰ���Ծ�ķ��������ϢΪ�գ�");
			} else {
				System.out.println("��ȡ����"+str[0]+"��ǰ���Ծ�ķ��������Ϣ���£�");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]);
				}
			}//else
		}//if
		return list;
	}
	/**
	 * 3.2.6 ������ϸ��Ϣ.(û���õ�)
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceDetailInfoListRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceDetailInfoList)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str+"�ķ�����ϸ��Ϣ�б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+str+"�ķ�����ϸ��Ϣ�б���Ϣ���£�");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]+","+list.get(i)[3]+","+list.get(i)[4]+","+list.get(i)[5]+","+list.get(i)[6]+","+list.get(i)[7]);
				}
			}//else
		}//if
		return list;
	}
	//**********************����������Ϣ���棨3.3�����***************************//
	/**3.3.1 ����ƽ��������.չʾ�÷��񣨰�����������һ��ʱ�䣨10���ӵȣ��ڵ�ƽ�����������������ʾʱ�䣬�������ʾ��������
	 * @param host
	 * @param port
	 * @param str String[]����ID�͵�ǰʱ��
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  int[] sendSingleServiceCallTimesRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.singleServiceCallTimesRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		int[] list = new int[10];
		if (msg.getType().equals(MsgType.singleServiceCallTimes)) {
			list = (int[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str[0]+"����ƽ��������Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+str[0]+"����ƽ��������ͳ�����£�");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.3.2 ��������ƽ����Ӧʱ�䡣��ʾ�ӵ�ǰʱ�䵹��1Сʱ�÷����丱��������ʱ��ƽ����Ӧʱ�䡣����0~10���ӣ�33.3ms���÷���һ����������3�Σ���һ����Ӧ��50ms���ڶ��κ͵����θ���Ӧ��25ms��ƽ��ÿ�ε���Ӧʱ��Ϊ33.3ms����10~20���ӣ�0ms����ʾ���ʱ��û�����ã���һֱ��50~60���ӣ�0ms��
	 * @param host
	 * @param port
	 * @param str String[]����ID�͵�ǰʱ��
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  double[] sendSingleServiceRunTimeRequest(String[] str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.singleServiceRunTimeRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		double[] list = new double[10];
		if (msg.getType().equals(MsgType.singleServiceRunTime)) {
			list = (double[]) msg.getBody();
			if (list.length < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str[0]+"�ķ������ʱ��ͳ���б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+str[0]+"�������ʱ��ͳ�����£�");
				for(int i=0;i<list.length;i++){
					System.out.print(list[i]+" ");
				}
				System.out.println();
			}//else
		}//if
		return invertArray(list);
	}
	/**
	 * 3.3.3 �������˽ṹ
	 * @param host
	 * @param port
	 * @param str ��ʾserviceID(����ID)
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  ArrayList<String[]> sendServiceCopyRequest(String str) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.serviceCopyRequest, str);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.serviceCopy)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��е�"+str+"�ķ��񸱱�IP�б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+str+"������񸱱�IP�б����£�");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]);
				}
			}//else
		}//if
		return list;
	}
	//**********************�ڵ�������Ϣ���棨3.5�����***************************//
	/**
	 * 3.5.1&3.5.2CPU,�ڴ�
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.nodeHistoryCPURAMRequest, ip);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.nodeHistoryCPURAM)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��е�"+ip+"�ýڵ����ʷCPU���ڴ���Ϣ�б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ����"+ip+"�ýڵ����ʷCPU���ڴ���Ϣ�б����£�");
				for(int i=0;i<list.size();i++){
					System.out.println(list.get(i)[0]+","+list.get(i)[1]+","+list.get(i)[2]);
				}
			}//else
		}//if
		return list;
	}
	//**************************  �������ý��棨3.7�����***************************//
	public  boolean sendserviceRequest(String concurrentNum) throws UnknownHostException, IOException, ClassNotFoundException{
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.concurrentNum, concurrentNum);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		boolean result = false;
		if (msg.getType().equals(MsgType.concurrentNum)) {
			result = (Boolean) msg.getBody();
			if (result) {
				System.out.println("�޸ķ��񲢷����ɹ���");
			} else {
				System.out.println("�޸ķ��񲢷���ʧ�ܣ�");
			}//else
		}//if
		return result;
	}
	
	//**************************   z   f    codes     ***************************//
	/*
	 * ��ʾ���������ķ����б�
	 */
	public ArrayList<String[]> serviceList() throws IOException, ClassNotFoundException{
		//��ʾ���������ķ����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.SERVICELIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICELIST,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.SERVICELISTRESULT��MsgBodyΪ�����б�
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
	 * ��ʾ�������ķ����б�	
	 */
	public ArrayList<String[]> serviceCopyList() throws IOException, ClassNotFoundException{
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.SERVICECOPYLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICECOPYLIST,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.SERVICECOPYLISTRESULT��MsgBodyΪ�������ķ����б�
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
	 * ��ȡ�ӿ���Ϣ
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
	 * ��ʾ�ڵ��б�
	 */
	public ArrayList<String[]> nodeList() throws IOException,
			ClassNotFoundException {
		// ��ʾ�����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ��������淢��Message��Ϣ������ΪMsgType.HOSTLIST
		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());
		Message outMes = new Message(MsgType.NODELIST, null);
		outputStream.writeObject(outMes);
		outputStream.flush();

		// ���շ������淵�ص�Message��Ϣ������ΪMsgType.HOSTLISTRESULT��MsgBodyΪ�����б�
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
	 * �ڵ�CPU���ڴ���Ϣ
	 */
	public ArrayList<String[]> nodeCpuMem(String nodeip) throws IOException, ClassNotFoundException{
		//��ʾ�����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.HOSTINFO,nodeip);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.HOSTLISTRESULT��MsgBodyΪ�����б�
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
	 * �ڵ�Ӳ����Ϣ
	 */
	public ArrayList<String[]> nodeHarddisk(String nodeip) throws IOException, ClassNotFoundException{
		//��ʾ�����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.HOSTHARDDISK,nodeip);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.HOSTLISTRESULT��MsgBodyΪ�����б�
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
	 * ����ɸѡ����
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
	
	
	//����Ϊ�������Ͻǵ���״ͼ��Ϣ
	/*
	 * ������ϸ��Ϣ
	 */
	public ArrayList<String> serviceDetail() throws IOException, ClassNotFoundException{
		//��ʾ�����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.SERVICEDETAIL,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.HOSTLISTRESULT��MsgBodyΪ�����б�
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
	 * �ڵ���ϸ��Ϣ
	 */
	public ArrayList<String> nodeDetail() throws IOException, ClassNotFoundException{
		//��ʾ�����б�
		Socket socket = new Socket(serviceRegistryIP, serviceRegistryPort);
		//��������淢��Message��Ϣ������ΪMsgType.HOSTLIST
		ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
		Message outMes=new Message(MsgType.NODEDETAIL,null);
		outputStream.writeObject(outMes);
		outputStream.flush();
		
		//���շ������淵�ص�Message��Ϣ������ΪMsgType.HOSTLISTRESULT��MsgBodyΪ�����б�
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
	 * ����ɸѡ
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
	 * ����ɸѡ����
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
	 * ����ͽڵ�������
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
	 * ����������
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
	 * ɸѡ�����ͽڵ�������
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
	 * ɸѡ�󾯱�������
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
	 * �ڵ�����ɸѡ
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
	 * �ڵ�ɸѡ,ģ����ѯ
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
	 * �����������з���״̬��Ϣ�����ݿ�
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.AllServiceStateRequest, null);
		oos.writeObject(aMsg);

		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<Service> lista = new ArrayList<Service>();
		if (msg.getType().equals(MsgType.AllServiceState)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��еķ���״̬��Ϣ�б�Ϊ�գ�");
			} else {
				try {
					for (int i = 0; i < list.size(); i++) {
						String[] serviceInfo = (String[]) list.get(i);
						System.out.println("��������" + serviceInfo[0].toString());
						System.out.println("��������IP��ַ��"
								+ serviceInfo[1].toString());
						System.out.println("��ǰ״̬��" + serviceInfo[2].toString());
						System.out.println("���ͣ�" + serviceInfo[3].toString());
						System.out.println("����ϵͳ��" + serviceInfo[4].toString());
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
		// �ر�Socket
		oos.close();
		s.close();
		// ���͸�ǰ̨ҳ��
		return lista;
	}

	/**
	 * �����������е������б���������ݿ�
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
		// System.out.println("sendHostListRequest��");
		readConfigXml();
		System.out.println(serviceRegistryIP+":"+serviceRegistryPort);
		Socket s = new Socket(serviceRegistryIP, serviceRegistryPort);
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.AllHostRequest, null);
		oos.writeObject(aMsg);

		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String> list = new ArrayList<String>();
		if (msg.getType().equals(MsgType.AllHost)) {
			list = (ArrayList<String>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX�����б�գ���������");
			} else {
				// System.out.println("�����ݿ��õ�����IP�б�");
				// for(int i=0;i<list.size();i++){
				// System.out.print(list.get(i)+",");
				// }
				// System.out.println();
			}

		}
		// �ر�Socket
		oos.close();
		s.close();
		return list;
	}

	/**
	 * ���;�����Ϣ�����ݿ�
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

		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		boolean result = false;
		if (msg.getType().equals(MsgType.AlarmStorageResult)) {
			result = (Boolean) msg.getBody();
			if (result) {
				System.out.println("�������ɹ���");
			} else {
				System.out.println("�������ʧ�ܣ�");
			}
		}
		// �ر�Socket
		oos.close();
		s.close();
		return result;
	}

	/**
	 * 3.6�����ݿ��������еľ�����Ϣ�����ھ����б�ҳ��չʾ
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

		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.AlarmListResult)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��еľ�����Ϣ�б�Ϊ�գ�");
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
	 * ��������Ϣ״̬
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.handleAlarmRequest, list1);
		oos.writeObject(aMsg);
		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		Boolean res = false;
		if (msg.getType().equals(MsgType.handleAlarmResult)) {
			res = (Boolean) msg.getBody();
			if (res) {
				System.out.println("����״̬����ɹ�");
			}
		}//if
		return res;
	}
	/**
	 * ���������õ���Ϣ����Ҫ����ʱ���ֶΣ���ȡĳ��ʱ���ڵ��õķ���ļ��ϣ� ��
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
		// ����Socket��Ϣ
		OutputStream ops = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ops);
		Message aMsg = new Message(MsgType.ServiceInvokeInfoRequest, null);
		oos.writeObject(aMsg);

		// ���շ������ķ���
		InputStream ips = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(ips);
		Message msg = (Message) ois.readObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
		if (msg.getType().equals(MsgType.ServiceInvokeInfoResult)) {
			list = (ArrayList<String[]>) msg.getBody();
			if (list.size() < 1) {
				System.out.println("��ȡ�������ݿ��еķ��������Ϣ�б�Ϊ�գ�");
			} else {
				System.out.println("��ȡ�������ݿ��еķ��������Ϣ:");
				for (int i = 0; i < list.size(); i++) {
					String[] serviceInfo = (String[]) list.get(i);
					System.out.println("Time��" + serviceInfo[1].toString());
					System.out.println("ServiceName��"
							+ serviceInfo[2].toString());
					System.out
							.println("ServiceIP��" + serviceInfo[3].toString());
					System.out.println("ServiceType��"
							+ serviceInfo[4].toString());
					System.out.println("BussinessType��"
							+ serviceInfo[5].toString());
					System.out.println("Status��" + serviceInfo[6].toString());
				}
			}
		}
		// �ر�Socket
		oos.close();
		s.close();
		// ���͸�ǰ̨ҳ��
		return list;
	}
	/***
	 * ����������
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
		
		String ip = "192.168.0.204";//ע������IP
		int port = 5000;
		// 1.����һ�������������з���״̬��Ϣ�����ݿ�Ĳ���
		// TCPClient.sendServiceStateRequest(serviceRegistryIP,
		// serviceRegistryPort);

		// 2.����һ�������������е������б���������ݿ�Ĳ���
		// TCPClient.sendHostListRequest();
		//
		// //3.����һ���������Ĳ���
//		 ArrayList<String> alarmInfoList = new ArrayList<String>();
//		 //����Ϣ�������Ϣ��һ��Ҫ����˳�����.LogNumber-->LogTime-->LogType-->LogContent-->ip-->container(��=��0��)-->status
//		 SimpleDateFormat df = new
//		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
//		 
//		 alarmInfoList.add(df.format(new Date()));
//		 alarmInfoList.add("hostAlarm");//Type�����࣬һ����hostAlarm,һ����containerAlarm
//		 alarmInfoList.add("���棺����192.168.0.98��CPUռ���ʳ���80%");
//		 alarmInfoList.add("192.168.0.96");
//		 alarmInfoList.add("no container");
//		 alarmInfoList.add("0");
//		 Message aMsg = new Message(MsgType.AlarmStorageRequest,alarmInfoList);
//		 TCPClient.sendAlarmInfo(ip, port,aMsg);
		//
		// //4.����һ�����󾯱��б����
		//TCPClient.receiveAlarmInfoList(serviceRegistryIP, serviceRegistryPort);

		// 5.����һ�����������õ���Ϣ����
		// TCPClient.sendServiceInvokeInfoRequest();
		
		
		
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	    String dateString = "2014/08/03 17:20:32";
//	    Date date = df.parse(dateString);
	    long a = 1407058048193L;//date.getTime();
	    String s = String.valueOf(a);
//	    Timestamp now = new Timestamp(a);//	    Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
	    Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ�� 
	    String str = df.format(now); 
	    
		//3.1.1ϵͳ�ṹ����.��ʾ����������з��񣨰����丱��������ϸ��Ϣ
//		TCPClient.sendServiceDetailInfoWithCopyRequest(ip,port);
		
		//3.1.2���������ͳ��
//		TCPClient.sendServiceCallInfoRequest(ip,port,s);
		
		//3.1.3�������ʱ��ͳ��
//		TCPClient.sendServiceRunTimeRequest(ip,port,s);
		
		//3.2.1 ������������鱨���񣬵翹����
//		TCPClient.sendServiceSystemRequest(ip,port);
		
		//3.2.3 ���������ͳ��.չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.
		String[] s1 = new String[2];s1[0] = "�鱨����";s1[1]=s;
//		TCPClient.sendServiceCalInfoReq(ip,port,s1);
		
		//3.2.4 �������ʱ��ͳ��.չʾ��������з�����һ��ʱ�䣨1Сʱ�ڣ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
//		TCPClient.sendServiceRunTimeRequest(ip,port,s1);
		
		//3.2.5 ��ǰ���Ծ�ķ������.���1��Сʱ�ڷ�����������10�����񡣺�����Ϊ��������������Ϊ��������		
//		TCPClient.sendActiveServiceRequest(ip,port,s1);
		
		//3.2.6 ������ϸ��Ϣ.
//		TCPClient.sendServiceDetailInfoListRequest(ip,port,s1[0]);
		
		//3.3.1 ����ƽ��������.չʾ�÷��񣨰�����������һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������
		String[] s2 = new String[2];s2[0] = "020";s2[1] = s;
//		TCPClient.sendSingleServiceCallTimesRequest(ip,port,s2);
		
		//3.3.2 ��������ƽ����Ӧʱ��
//		TCPClient.sendSingleServiceRunTimeRequest(ip,port,s2);
		
		// 3.3.3 �������˽ṹ
//		TCPClient.sendServiceCopyRequest(ip,port,s2[0]);
		
		//3.5.1&3.5.2CPU,�ڴ�
//		TCPClient.sendNodeHistoryCPURAMRequest(ip,port,"192.168.0.6");
		
		//3.6.1�����б�
//		TCPClient.receiveAlarmInfoList(ip,port);
		/*�޸ľ���״̬*/
//		ArrayList<String> list1 = new ArrayList<String>();
//		list1.add("1");list1.add("0");
//		TCPClient.sendHandleAlarmRequest(ip, port, list1);
		//��ȡ��������
		TCPClient c = new TCPClient();
		System.out.println(c.getLogNum());
		//��ȡɸѡ�������
		ArrayList<String> filterInfo = new ArrayList<String>();
		filterInfo.add("LogContent");
		filterInfo.add("����");
		System.out.println(c.getLogFilterNum(filterInfo));
		
	}// main

}// TCPClient
