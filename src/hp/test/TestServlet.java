package hp.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import monitor.info.Host;
import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestServlet {
	public static void main(String[] args){
		
		
		/*
		JSONObject json = new JSONObject();
		// 3.2.1������������鱨���񣬵翹����
//		ArrayList<String> sysList = TCPClient.sendServiceSystemRequest(serviceRegistryIP, serviceRegistryPort);
		String[] sysList = {"�鱨ϵͳ","�״�ϵͳ","����ϵͳ","����ϵͳ"};
		json.put("sysList", sysList);
		// ��ǰ��ҳ���ȡ�û�ѡ���ϵͳ����
//		String sysName = request.getParameter("sysName");
//		if (sysName.isEmpty()) {
//			System.out.println("3.2�����ȡ����ϵͳ����Ϊ�գ�");
//		} else {
			// 3.2.2�쳣����,����

			// 3.2.3���������ͳ��
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// �����ʽ
			Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
			String nowstr = df.format(now);
			String[] str = new String[2];
			str[0] = "�鱨ϵͳ";
			str[1] = nowstr;
//			ArrayList<Integer> serviceCalTimesList = TCPClient.sendServiceCalInfoReq(serviceRegistryIP,serviceRegistryPort, str);
			int[] serviceCalTimesList ={12,21,2,15,6,8,11,5,4,18};
			json.put("serviceCallQuantity", serviceCalTimesList);
			// 3.2.4�������ʱ��ͳ��
//			ArrayList<Integer> serviceRunTimeList = TCPClient.sendServiceRunTimeRequest(serviceRegistryIP,serviceRegistryPort, str);
			int[] serviceRunTimeList = {2,1,2,5,6,8,1,0,0,0};
			json.put("serviceRunTime", serviceRunTimeList);
			// 3.2.5��ǰ���Ծ�ķ������
//			ArrayList<String[]> activeServiceList = TCPClient.sendActiveServiceRequest(serviceRegistryIP,serviceRegistryPort, str);
			ArrayList<String[]> activeServiceList = new ArrayList<String[]>();
			for(int i=0;i<10;i++){
				String[] strArray = new String[2];
				strArray[0] = "service"+i;
				strArray[1] = 15-i+"";
				activeServiceList.add(strArray);
			}
			json.put("activeServiceList", activeServiceList);
			// 3.2.6��ȡ����ϵͳ�µ����еķ�����ϸ��Ϣ�б� :����״̬��������������ӿ���Ϣ���������ͣ�ҵ�����ͣ�����ϵͳ���ͣ�������������������
//			ArrayList<String[]> serviceDetailList = TCPClient.sendServiceDetailInfoListRequest(serviceRegistryIP,serviceRegistryPort, sysName);
			ArrayList<String[]> serviceDetailList = new ArrayList<String[]>();
			String[] ser1 = {"start","TheAddService","�ӿ�add����","Web Service","�鱨����","�鱨ϵͳ","����һ���ӷ����������","�����Ƿ�������"};
			String[] ser2 = {"start","TheShellService","�ӿ�shell����","Web Service","�鱨����","�鱨ϵͳ","����һ��shell���������","�����Ƿ�������"};
			serviceDetailList.add(ser1);
			serviceDetailList.add(ser2);
			json.put("Table", serviceDetailList);
			
			
		JSONObject json = new JSONObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
//		System.out.println("LogNumber:" + LogNumber + "\nLogTime:"+ LogTime + "\nLogType:" + LogType+ "\nLogContent:" + LogContent + "\nip:" + ip+ "\ncontainer:" + container);
		String[] s1 = {"1","2014-08-01 12-02-12","host","192.168.0.201��CPU������ֵ","192.168.0.201","null"};
		list.add(s1);
		json.put("Table", list);
		
		JSONObject json = new JSONObject();
//		HashSet<String> ipSet = new HashSet<String>();
//		for(int i=0;i<serviceWithCopy.size();i++){
//			String ip = serviceWithCopy.get(i)[1];
//			ipSet.add(ip);
//		}
		Object[] ipArray = {"192.168.0.201","192.168.0.202,192","192.168.0.205"};//���нڵ�ļ���
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] s1 = {"service1","192.168.0.201","WebService","�鱨ϵͳ","aba"};
		String[] s2 = {"service1","192.168.0.202","WebService","�鱨ϵͳ","aba"};
		String[] s3 = {"service2","192.168.0.201","WebService","�״�ϵͳ","aba"};
		String[] s4 = {"service3","192.168.0.201","WebService","����ϵͳ","aba"};
		String[] s5 = {"service3","192.168.0.202","WebService","����ϵͳ","aba"};
		String[] s6 = {"service4","192.168.0.201","WebService","�鱨ϵͳ","aba"};
		String[] s7 = {"service5","192.168.0.205","WebService","�鱨ϵͳ","aba"};
		String[] s8 = {"service6","192.168.0.201","WebService","�鱨ϵͳ","aba"};
		
		list.add(s1);list.add(s2);list.add(s3);list.add(s4);list.add(s5);
		list.add(s6);list.add(s7);list.add(s8);
		JSONArray ja = new JSONArray();
		for(int i=0;i<ipArray.length;i++){
			JSONObject j1 = new JSONObject();
			int count = 0;
			for(int j=0;j<list.size();j++){
				if(list.get(j)[1].equals(ipArray[i])){
					count+=1;
				}
			}
			j1.put("IP", ipArray[i]);
			j1.put("amount", count);
			ja.add(j1);
		}
		json.put("nodeServiceAmount", ja);
		*/
		//�ڵ�ʵʱcpu���ڴ�
		JSONObject json = new JSONObject();
		Host[] hostArray = new Host[3];
		ArrayList<String> hddList = new ArrayList<String>();//Ӳ��״��
		hostArray[0] = new Host("hp","192.168.0.201","xx","48.5%","88.2%","48.4%","PC","win7");
		hostArray[1] = new Host("hp","192.168.0.202","xx","12.5%","55.2%","22.1%","PC","win7");
		hostArray[2] = new Host("hp","192.168.0.203","xx","56.5%","59.7%","33.9%","PC","win7");
		JSONArray ja1 = new JSONArray();
		for(int i=0;i<hostArray.length;i++){
			JSONObject hjson = new JSONObject();
			Host h = (Host)hostArray[i];
			hjson.put("IP",h.getIP());
			hjson.put("cpu",h.getCPU());
			hjson.put("ram",h.getRAM());
			ja1.add(hjson);
		}
		json.put("cpu&ram", ja1);
		
		try {
			writeFile("F:\\servletJson.txt", json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	// ��json��ʽ���ַ���д���ļ�
	public static void writeFile(String filePath, String sets)
			throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				filePath), "UTF-8");
		// System.out.println(fw.getEncoding());
		// PrintWriter out = new PrintWriter(fw);
		out.write(sets);
		out.write("\n");
		// out.println();
		// fw.close();
		out.close();
	}
}
