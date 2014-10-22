package hp.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import monitor.info.Container;
import monitor.info.CurrentInfo;
import monitor.info.Host;
import monitor.info.MainClass;
import monitor.info.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class Test {
	
	//把json格式的字符串写到文件
	public static void writeFile(String filePath, String sets) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
//	    System.out.println(fw.getEncoding());
//	    PrintWriter out = new PrintWriter(fw);
	    out.write(sets);
	    out.write("\n");
//	    out.println();
//	    fw.close();
	    out.close();
	   }
	public static void writeAvgFile(double[] d1) throws IOException{
			JSONArray a = new JSONArray();
			a.add(d1[0]);a.add(d1[1]);
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt",true),"UTF-8");
		    out.append(a.toString());
		    out.append("\n");
		    out.close();
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
//		writeLineChartFile();
//		double[] d1 = new double[2];
//		d1[0] = 1+0.1;
//		d1[1] = 1+0.2;
//		writeAvgFile(d1);
//		
//		double[] d2 = new double[2];
//		d2[0] = 2+0.1;
//		d2[1] = 2+0.2;
//		writeAvgFile(d2);
//		JSONArray array = new JSONArray();
//		double[] d1 = new double[2];
//		for(int i=0;i<50;i++){
//			d1[0] = i+0.1;
//			d1[1] = i+0.2;
//			JSONArray a = new JSONArray();
//			a.add(d1[0]);a.add(d1[1]);
//			array.add(a);
//		}
//		writeFile(".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt", array.toString());
//		Thread th=new Thread(new UDPServerThread());
//		th.run();
		
//		System.out.println("***********************");
//		UDPServer.curInfo.printCurInfo();	
//		System.out.println(UDPServer.curInfo.);
//		if(UDPServer.curInfo.getCurHostInfo().isEmpty()){
//			System.out.println("UDPServer.curInfo.hoSet为空！！！！！");
//		}else{
//			HashSet<Host> set = (HashSet<Host>) UDPServer.curInfo.getCurHostInfo();
//			Iterator it = set.iterator();
//			while(it.hasNext()){
//				Host h11 = (Host)it.next();
//				System.out.println(h11.getHostName()+","+h11.getIP());
//			}
//		}
		//		String s = "25.36%";
////		String[] s1 = s.split("(");
//		System.out.println(s.split("%")[0]);
//		
//		
		String path = ".\\WebRoot\\DataFiles\\jsonFile.txt";
		String resdPAth = ".\\WebRoot\\DataFiles\\curInfo_ols.txt";
		CurrentInfo curInfo = MainClass.buildCurInfo(resdPAth);
//		CurrentInfo curInfo = new CurrentInfo();//获取curInfo
//		Set<Host> hoSet = new HashSet<Host>();
//		ArrayList<String> hddList = new ArrayList<String>();
//		hddList.add("C:45%");hddList.add("D:45%");hddList.add("E:45%");		
//		Host h1 = new Host("hp-PC","192.168.0.96","DE-89-OI-9I-EI-KL","46.1%","78%",hddList);
//		Host h2 = new Host("dd-PC","192.168.0.93","DE-99-OI-9I-EI-KL","33.1%","11%",hddList);
//		hoSet.add(h1);hoSet.add(h2);
//		Set<Container> conSet = new HashSet<Container>();
//		Container c1 = new Container("Axis2","192.168.0.96","3.1%","0.0%","20(19/1)");
//		Container c2 = new Container("Tomcat","192.168.0.93","3.2%","0.2%","22(20/2)");
//		conSet.add(c1);conSet.add(c2);
//		curInfo.setHoSet(hoSet);curInfo.setConSet(conSet);
//		Host h3 = new Host("hp-PC2","192.168.0.96","DE-89-OI-9I-EI-KL","46.1%","78%",hddList);
//		curInfo.addHost(h3);
////		curInfo.printCurInfo();
//		Service s1 = new Service("addService","Web Service","sys1","192.168.0.96","started");
//		Service s2 = new Service("SubService","Web Service","sys2","192.168.0.93","started");
//		ArrayList<Service> list = new ArrayList<Service>();
//		list.add(s1);
//		list.add(s2);
//		curInfo.refreshServiceList(list);
		
//		String[] alarmRecord = new String[5];		
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式    	
//		alarmRecord[0] = df.format(new Date());
//		alarmRecord[1] = "hostAlarm";
//		alarmRecord[2] = "警告：主机192.168.0.98的CPU占用率超过80%";
//		alarmRecord[3] = "192.168.0.96";
//		alarmRecord[4] = "0";
//		
//		
//		
//		// yxjk_1的测试
		JSONObject json = new JSONObject();
		
		
		
		
		JSONObject json1 = new JSONObject();
		json1.put("hostAmount", curInfo.getHostAmount());
//		json1.put("systemAmount", curInfo.getSystemSet().size());
//		json1.put("serviceAmount", curInfo.getServiceAmount());
		json.put("graph0", json1);
//		writeFile(path, json.toString());
//		ArrayList<HashMap<String,Object>> map1 = curInfo.sysKindOverview();
//		json.put("graph1", map1);
//		writeFile(path, json.toString());
		//graph2(服务调用概览)
//		ArrayList<String[]> res1 = TCPClient.sendServiceInvokeInfoRequest();
//		ArrayList<String[]> invokeInfoList = TCPClient.sendServiceInvokeInfoRequest();
//		ArrayList<String[]> invokeInfoList = new ArrayList<String[]>();
//		String[] ss1 = new String[2];
//		String[] ss2 = new String[2];
//		String[] ss3 = new String[2];
//		ss1[0] = "";ss1[1] = "2014-05-28 15:51:51";
//		ss2[0] = "";ss2[1] = "2014-05-28 15:52:31";
//		ss3[0] = "";ss3[1] = "2014-05-28 15:52:32";
//		invokeInfoList.add(ss1);
//		invokeInfoList.add(ss2);
//		invokeInfoList.add(ss3);
//		HashSet<String> timeKeySet = new HashSet<String>();
//		for(int i=0;i<invokeInfoList.size();i++){
//			String[] serviceInfo = invokeInfoList.get(i);
//			String Time = serviceInfo[1];
//			String timeKey = Time.substring(0, 16);
//			timeKeySet.add(timeKey);
//		}
////		System.out.println(timeKeySet.size());
//		ArrayList<HashMap> resultMap = new ArrayList<HashMap>();		
//		Iterator<String> it = timeKeySet.iterator();
//		while(it.hasNext()){
//			HashMap<String,Object> map = new HashMap<String,Object>();
//			String key = (String)it.next();
//			int num = 0;
//			for(int i=0;i<invokeInfoList.size();i++){
//				String[] serviceInfo = invokeInfoList.get(i);
//				String Time = serviceInfo[1];
//				if(Time.contains(key)){
//					num += 1;
//				}	
//			}
//			map.put("time", key);
//			map.put("amount", num);
//			resultMap.add(map);
//		}
//		json.put("graph2", resultMap);
//		writeFile(path, json.toString());
		
//		for(int j =0;j<((50<invokeInfoList.size())?50:invokeInfoList.size());j++){			
//			String timeKey;
//			int num = 0;
//			for(int i=0;i<invokeInfoList.size();i++){
//				String[] serviceInfo = invokeInfoList.get(i);
//				String Time = serviceInfo[1];
//				timeKey = Time.substring(0, 16);
//				if(Time.contains(timeKey)){
//					num += 1;
//				}	
//				resultMap.put(timeKey, num);
//			}		
//		}
		
//		//graph3(动态图)
//		String s = MainClass.ReadFile(".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt");
//		System.out.println(s);
//		
//		json.put("graph3", ".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt");
//		writeFile(path, json.toString());
//		
//		//图4
		JSONArray oo = new JSONArray();
		HashMap<String,double[]> cpuAndramValueOfHost = curInfo.hostCpuLineChart();
		HashSet<String> ipKeySet = new HashSet<String>();
		ipKeySet.addAll(cpuAndramValueOfHost.keySet()) ;
		
		Iterator it = ipKeySet.iterator();
		while(it.hasNext()){
			JSONObject obj = new JSONObject();
			String ip = (String)it.next();
			JSONArray a3 = new JSONArray();
			//获取50次主机cpu和内存
			for(int i = 0;i<50;i++){
				double[] res = curInfo.hostCpuLineChart1(ip);
				a3.add(res);
			}
			obj.put("IP", ip);
			obj.put("array",a3);
			oo.add(obj);
		}		
		json.put("graph4", oo);
//		writeFile(path, json.toString());
//					
//		//图5
//		ArrayList<ArrayList<HashMap<String,Object>>> map2 = curInfo.hostConPercentChart();
//		json.put("graph5",map2);
//		writeFile(path, json.toString());
//		yxjk_2_zj的测试
//			double[] d1 = new double[4];
//			d1[0] = curInfo.getHostAmount();
//			d1[1] = curInfo.calulateAverageCpu();
//			d1[2] = curInfo.calulateAverageRam();
//			d1[3] = curInfo.calulateAverageHdd();
//			json.put("graph1",d1);
////		第二张图的内容
//		ArrayList<HashMap<String,String>> map1 = curInfo.getHostCon();
//		json.put("graph2",map1);
//		writeFile(path, json.toString());
//			//表格的内容
//			ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
//			Iterator it = curInfo.getHoSet().iterator();
//			while(it.hasNext()){
//				HashMap<String,String> mp1 =new HashMap<String,String>();
//				Host h11 = (Host) it.next();
//				mp1.put("hostName",h11.getHostName());
//				mp1.put("ip",h11.getIP());
//				mp1.put("mac",h11.getMAC());
//				mp1.put("cpu",h11.getCPU());
//				mp1.put("ram",h11.getRAM());
//				mp1.put("hdd",h11.getHddList().toString());
//				al.add(mp1);
//			}
//			json.put("Table",al);	
//			writeFile(path, json.toString());
		//yxjk_3_fw的测试
			//第一张图的内容
//			int[] array = new int[2];
//			array[0] = curInfo.getServiceAmount();//包含副本的总服务数
//			array[1] = curInfo.getSystemSet().size();//系统个数
//			json.put("graph1",array);			
//			//第二张图的内容(同主页饼图)//不带副本的
//			ArrayList<HashMap<String,Object>> map1 = curInfo.sysKindOverview();
//			json.put("graph2", map1);
//			//第三张图的内容（）不带副本的
//			HashMap<String,int[]> map2 = curInfo.getServiceStateOverview();
//			json.put("graph3",map2);
			//合并的图2和图3的内容
//			ArrayList<HashMap<String,Object>> map1 = curInfo.sysServiceCombined();
//			json.put("graph23", map1);
//			//表格的内容(带有副本的服务表)
//			JSONArray ar = new JSONArray();
//			ArrayList<Service> serList = curInfo.getSerList();
//			for(int i=0;i<serList.size();i++){
//				JSONArray ar1 = new JSONArray();
//				ar1.add(serList.get(i).getServiceName());
//				ar1.add(serList.get(i).getServiceKind());
//				ar1.add(serList.get(i).getServiceSystem());
//				ar1.add(serList.get(i).getServiceIP());
//				ar1.add(serList.get(i).getState());
//				ar.add(ar1);
//			}
//			json.put("Table",ar);	
//			writeFile(path, json.toString());
			//yxjk_4_xt的测试
		//三张图合并起来的测试

//		JSONArray map1 = curInfo.sysCombined();		
//		writeFile(path, map1.toString());
		//第一张图的内容
//		ArrayList<HashMap<String,Object>> map1 = curInfo.sysOverview();	
//		json.put("graph1",map1);		
//		//全部系统服务表的内容
//		JSONArray ar = new JSONArray();
//		ArrayList<Service> serList = curInfo.getSerList();
//		for(int i=0;i<serList.size();i++){
//			JSONArray ar1 = new JSONArray();
//			ar1.add(serList.get(i).getServiceName());
//			ar1.add(serList.get(i).getServiceKind());
//			ar1.add(serList.get(i).getServiceSystem());
//			ar1.add(serList.get(i).getServiceIP());
//			ar1.add(serList.get(i).getState());
//			ar.add(ar1);
//		}
//		json.put("serviceTable", ar);
//		//全部系统主机表的内容
//		JSONArray ar2 = new JSONArray();
//		Set<Host> set1 = curInfo.getCurHostInfo();
//		Iterator it0 = set1.iterator();
//		while(it0.hasNext()){
//			Host h11 = (Host)it0.next();
//			JSONArray ar1 = new JSONArray();
//			ar1.add(h11.getHostName());
//			ar1.add(h11.getIP());
//			ar1.add(h11.getMAC());
//			ar1.add(h11.getCPU());
//			ar1.add(h11.getRAM());
//			ar1.add(h11.getHddList());
//			ar2.add(ar1);
//		}		
//		json.put("hostTable", ar2);
//		writeFile(path, json.toString());
			//yxjk_5_rq的测试
//		//第一张图的内容
//			HashMap<String,Integer> map = curInfo.deployContainerHostAmount();
//			json.put("graph1",map);
////		//第二张图的内容(同yxjk_1图1)
//			HashMap<String,Double> map1 = curInfo.conKindOverview();
//			json.put("graph2",map1);
//////			//表格的内容
//			JSONArray mapa = new JSONArray();
//			Iterator it = curInfo.getConSet().iterator();
//			while(it.hasNext()){
//				HashMap<String,String> lista =new HashMap<String,String>();
//				Container c11 = (Container)it.next();
//				lista.put("containerName",c11.getConName());
//				lista.put("ip",c11.getIP());
//				lista.put("cpu",c11.getCPU());
//				lista.put("ram",c11.getRam());
//				lista.put("amount",c11.getServiceAmountString());
//				
//				mapa.add(lista);
//			}
//			json.put("Table",mapa);	
//			writeFile(path, json.toString());
		//yxjk_4的测试old
		//第一张图的内容
//			HashMap<String,Integer> map1 = curInfo.eachKindServiceAmount();
//			json.put("graph1",map1);
		//第二张图的内容
//			HashMap<String,Float> map2 = curInfo.serviceKindOverview();
//			json.put("graph2",map2);
//			//表格的内容
//			ArrayList<Object> list = TCPClient.sendServiceStateRequest(serviceRegistryIP, serviceRegistryPort);
//			ArrayList<Object> list = new ArrayList<Object>();
//			list.add(serviceInfo1);
//			json.put("Table",list);		
		//yxjk_7_cs的测试
//			json.put("sampleTime", curInfo.getSampleTime());
//			json.put("defaultHostCPU",Liminal.getHostCPULiminal());
//			json.put("defaultHostRam",Liminal.getHostCPULiminal());
//			json.put("defaultContainerCPU",Liminal.getHostCPULiminal());
//			json.put("defaultContainerRam",Liminal.getHostCPULiminal());
//			writeFile(path, json.toString());
		//yxjk_8_jb的测试
//			ArrayList<String[]> list = TCPClient.receiveAlarmInfoList(serviceRegistryIP, serviceRegistryPort);
//			ArrayList<String[]> listb = new ArrayList<String[]>();
//			listb.add(alarmRecord);
//			JSONArray jarray = new JSONArray();
//			for(int i=0;i<listb.size();i++){	
//				JSONArray jarray1 = new JSONArray();
//				jarray1.put(listb.get(i)[0]);
//				jarray1.put(listb.get(i)[1]);
//				jarray1.put(listb.get(i)[2]);
//				jarray1.put(listb.get(i)[3]);
//				jarray1.put(listb.get(i)[4]);
//				jarray.put(jarray1);
//			}
//			json.put("Table",jarray);			
//			writeFile(path, json.toString());
		
		
	}
}
