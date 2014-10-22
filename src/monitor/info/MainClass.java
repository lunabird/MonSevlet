package monitor.info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class MainClass {
	/**
	 * ����curInfo.txt�ļ������ݣ�json��ʽ������CurrentInfo��������Servlet��������
	 * @param path
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static CurrentInfo buildCurInfo(String path) throws JSONException, IOException{
		
		CurrentInfo curInfo = new CurrentInfo();
		String s = ReadFile(path);
		JSONObject jo = JSONObject.fromObject(s);//��ʽ����json����
		//����Host-Table
		JSONArray array1 = (JSONArray) jo.get("host-Table");
		HashSet<Host> hostSet = new HashSet<Host>(); 
		for(int i=0;i<array1.size();i++){
			JSONObject hst = JSONObject.fromObject(array1.getString(i));
			/*
			JSONArray hddarray = (JSONArray)hst.get("hdd");			
			ArrayList<String> hddList = new ArrayList<String>();
			for(int j=0;j<hddarray.size();j++){
				String hdditem = (String)hddarray.get(j);
				hddList.add(hdditem);
			}
			*/
			Host h = new Host(hst.getString("hostName"),hst.getString("ip"),hst.getString("mac"),hst.getString("cpu"),hst.getString("ram"),hst.getString("hdd"),hst.getString("type"),hst.getString("OS"));
//			System.out.println(h.toString());
			hostSet.add(h);
		}
		curInfo.hoSet = hostSet;
		//����Container-Table
		/*
		JSONArray array2 = (JSONArray) jo.get("container-Table");
		HashSet<Container> containerSet = new HashSet<Container>(); 
		for(int i=0;i<array2.size();i++){
			JSONObject ctnr = JSONObject.fromObject(array2.getString(i));
			Container c = new Container(ctnr.getString("containerName"),ctnr.getString("ip"),ctnr.getString("cpu"),ctnr.getString("ram"),ctnr.getString("amount")); 
			containerSet.add(c);
		}
		curInfo.conSet = containerSet;
		//����serList
		JSONArray array3 = (JSONArray) jo.get("service-Table");
		ArrayList<Service> serviceList = new ArrayList<Service>(); 
		for(int i=0;i<array3.size();i++){
			JSONObject ser = JSONObject.fromObject(array3.getString(i));
			Service service = new Service(ser.getString("ServiceName"),ser.getString("ServiceKind"),ser.getString("ServiceSystem"),ser.getString("ServiceIP"),ser.getString("State"));
			serviceList.add(service);
		}
		CurrentInfo.serList = serviceList;
		*/
		//����dbHostList
		/*JSONArray array4 = (JSONArray) jo.get("dbHostList");
		ArrayList<String> dbL = new ArrayList<String>();
		for(int i=0;i<array4.size();i++){
			String ipL = (String)array4.getString(i);
			dbL.add(ipL);
		}
		CurrentInfo.dbHostList = dbL;*/
		//����SampleTime
		int sTime = (Integer)jo.get("sampleTime");
		CurrentInfo.sampleTime = sTime;
		if(curInfo.toString().isEmpty()){
			return null;
		}else{
			return curInfo;
		}
		
		
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
//		TCPServer ts = new TCPServer();
//		ts.start();
		UDPServer us = new UDPServer();
		us.start();		
//		us.serverWork();

		//�˴�Ӧ����ǰ�˴������޸Ĳ���ʱ����������
//		Thread.sleep(20000);
//		UDPClient ec = new UDPClient();
//    	ec.sendSampleTime(10);
		
/**
		System.out.println(TestLk.info.getConSet()+"   " +TestLk.info.getHoSet());
		System.out.println(TestLk.testInt);
		Thread th=new Thread(new TestLk());
		th.start();
		
		System.out.println("***********************");
		Thread.sleep(30);
		for(int i=0;i<100;i++){
			System.out.println(TestLk.info.getConSet().size()+"   " +TestLk.info.getHoSet().size());
			System.out.println(TestLk.testInt);
		}
		*/
		//UDPServer.curInfo.printCurInfo();	
		
	}
	/**
	 * ���ļ�����ȡ����curInfo.txt�ļ�
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String ReadFile(String path) throws UnsupportedEncodingException, FileNotFoundException {
		File file = new File(path);
		InputStreamReader read = new InputStreamReader (new FileInputStream(file),"UTF-8");
		BufferedReader reader = null;
		String laststr = "";
		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(read);
			String tempString = null;
//			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				// System.out.println("line "+ line +": " +tempString);
				laststr = laststr + tempString;
//				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
	}
}
