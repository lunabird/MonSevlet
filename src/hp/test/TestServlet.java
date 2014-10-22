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
		// 3.2.1服务类别（例如情报服务，电抗服务）
//		ArrayList<String> sysList = TCPClient.sendServiceSystemRequest(serviceRegistryIP, serviceRegistryPort);
		String[] sysList = {"情报系统","雷达系统","海洋系统","航空系统"};
		json.put("sysList", sysList);
		// 从前端页面获取用户选择的系统名称
//		String sysName = request.getParameter("sysName");
//		if (sysName.isEmpty()) {
//			System.out.println("3.2界面获取到的系统名称为空！");
//		} else {
			// 3.2.2异常服务,待定

			// 3.2.3服务访问量统计
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// 定义格式
			Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
			String nowstr = df.format(now);
			String[] str = new String[2];
			str[0] = "情报系统";
			str[1] = nowstr;
//			ArrayList<Integer> serviceCalTimesList = TCPClient.sendServiceCalInfoReq(serviceRegistryIP,serviceRegistryPort, str);
			int[] serviceCalTimesList ={12,21,2,15,6,8,11,5,4,18};
			json.put("serviceCallQuantity", serviceCalTimesList);
			// 3.2.4服务调用时间统计
//			ArrayList<Integer> serviceRunTimeList = TCPClient.sendServiceRunTimeRequest(serviceRegistryIP,serviceRegistryPort, str);
			int[] serviceRunTimeList = {2,1,2,5,6,8,1,0,0,0};
			json.put("serviceRunTime", serviceRunTimeList);
			// 3.2.5当前最活跃的服务访问
//			ArrayList<String[]> activeServiceList = TCPClient.sendActiveServiceRequest(serviceRegistryIP,serviceRegistryPort, str);
			ArrayList<String[]> activeServiceList = new ArrayList<String[]>();
			for(int i=0;i<10;i++){
				String[] strArray = new String[2];
				strArray[0] = "service"+i;
				strArray[1] = 15-i+"";
				activeServiceList.add(strArray);
			}
			json.put("activeServiceList", activeServiceList);
			// 3.2.6获取到该系统下的所有的服务详细信息列表 :服务状态，服务名，服务接口信息，服务类型，业务类型，所属系统类型，服务描述，服务详情
//			ArrayList<String[]> serviceDetailList = TCPClient.sendServiceDetailInfoListRequest(serviceRegistryIP,serviceRegistryPort, sysName);
			ArrayList<String[]> serviceDetailList = new ArrayList<String[]>();
			String[] ser1 = {"start","TheAddService","接口add方法","Web Service","情报服务","情报系统","这是一个加法服务的描述","这里是服务详情"};
			String[] ser2 = {"start","TheShellService","接口shell方法","Web Service","情报服务","情报系统","这是一个shell服务的描述","这里是服务详情"};
			serviceDetailList.add(ser1);
			serviceDetailList.add(ser2);
			json.put("Table", serviceDetailList);
			
			
		JSONObject json = new JSONObject();
		ArrayList<String[]> list = new ArrayList<String[]>();
//		System.out.println("LogNumber:" + LogNumber + "\nLogTime:"+ LogTime + "\nLogType:" + LogType+ "\nLogContent:" + LogContent + "\nip:" + ip+ "\ncontainer:" + container);
		String[] s1 = {"1","2014-08-01 12-02-12","host","192.168.0.201的CPU超出阈值","192.168.0.201","null"};
		list.add(s1);
		json.put("Table", list);
		
		JSONObject json = new JSONObject();
//		HashSet<String> ipSet = new HashSet<String>();
//		for(int i=0;i<serviceWithCopy.size();i++){
//			String ip = serviceWithCopy.get(i)[1];
//			ipSet.add(ip);
//		}
		Object[] ipArray = {"192.168.0.201","192.168.0.202,192","192.168.0.205"};//所有节点的集合
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] s1 = {"service1","192.168.0.201","WebService","情报系统","aba"};
		String[] s2 = {"service1","192.168.0.202","WebService","情报系统","aba"};
		String[] s3 = {"service2","192.168.0.201","WebService","雷达系统","aba"};
		String[] s4 = {"service3","192.168.0.201","WebService","航空系统","aba"};
		String[] s5 = {"service3","192.168.0.202","WebService","航空系统","aba"};
		String[] s6 = {"service4","192.168.0.201","WebService","情报系统","aba"};
		String[] s7 = {"service5","192.168.0.205","WebService","情报系统","aba"};
		String[] s8 = {"service6","192.168.0.201","WebService","情报系统","aba"};
		
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
		//节点实时cpu和内存
		JSONObject json = new JSONObject();
		Host[] hostArray = new Host[3];
		ArrayList<String> hddList = new ArrayList<String>();//硬盘状况
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
	
	
	
	
	// 把json格式的字符串写到文件
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
