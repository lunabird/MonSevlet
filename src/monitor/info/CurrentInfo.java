package monitor.info;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
/**
 * 当前监控信息存储类，包括主机信息和容器信息
 * @author huangpeng
 *
 */
public  class CurrentInfo {
	
	Set<Host>  hoSet;
//	Set<Container> conSet;
//	static ArrayList<Service> serList;//带有副本的服务列表
	static ArrayList<String> dbHostList;//主机IP列表
	static int sampleTime = 10;
	
	/**
	 * 构造函数
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public CurrentInfo() {
		hoSet = new HashSet<Host>();
//		conSet = new HashSet<Container>();
//		serList = new ArrayList<Service>();
		dbHostList = new ArrayList<String>();
	}
	
	
	//测试用
	public  void setHoSet(Set<Host> hoSet){
		this.hoSet = hoSet;
	}
	//测试用
	
	public   void  setConSet(Set<Container> conSet){
//		this.conSet = conSet;
	}
	
//	public ArrayList<Service> getSerList(){
//		return serList;
//	}
	/**
	 * 更新ServiceList的内容
	 * @param list
	 */
//	public void refreshServiceList(ArrayList<Service> list){
//		serList = list;
//	}
	
	public synchronized void writeFile() throws IOException, JSONException {
		try {
			File path = new File("D:/"); // "D:/"目录必须存在
			File dir = new File(path, "curInfo.txt");
			if (!dir.exists())
				dir.createNewFile();
		} catch (Exception e) {
			System.out.print("D:\\curInfo.txt创建失败");
		}		 
		String filePath = "D:\\curInfo.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				filePath), "UTF-8");
		JSONObject json = new JSONObject();
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		HashSet<Host> theHostSet = new HashSet<Host>();
		theHostSet = (HashSet<Host>) hoSet;
		Iterator<Host> it = theHostSet.iterator();
		while (it.hasNext()) {
			HashMap<String, Object> mp1 = new HashMap<String, Object>();
			Host h11 = (Host) it.next();
			mp1.put("hostName", h11.getHostName());
			mp1.put("ip", h11.getIP());
			mp1.put("mac", h11.getMAC());
			mp1.put("cpu", h11.getCPU());
			mp1.put("ram", h11.getRAM());
			mp1.put("hdd", h11.getHdd());//h11.getHddList()
			mp1.put("type", h11.getType());
			mp1.put("OS",h11.getOS());
			al.add(mp1);
		}
		json.put("host-Table", al);
		
		/*
			JSONArray mapa = new JSONArray();
			HashSet<Container> theConSet = new HashSet<Container>();
			theConSet = (HashSet<Container>) conSet;
			Iterator<Container> it1 = theConSet.iterator();
			while (it1.hasNext()) {
				HashMap<String, String> lista = new HashMap<String, String>();
				Container c11 = (Container) it1.next();
				lista.put("containerName", c11.getConName());
				lista.put("ip", c11.getIP());
				lista.put("cpu", c11.getCPU());
				lista.put("ram", c11.getRam());
				lista.put("amount", c11.getServiceAmountString());
				mapa.add(lista);
			}
			json.put("container-Table", mapa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
//		json.put("dbHostList", dbHostList);
		/*
		JSONArray ar = new JSONArray();
		for (int i = 0; i < serList.size(); i++) {
			JSONObject ar1 = new JSONObject();
			ar1.put("ServiceName", serList.get(i).getServiceName());
			ar1.put("ServiceKind", serList.get(i).getServiceKind());
			ar1.put("ServiceSystem", serList.get(i).getServiceSystem());
			ar1.put("ServiceIP", serList.get(i).getServiceIP());
			ar1.put("State", serList.get(i).getState());
			ar.add(ar1);
		}
		json.put("service-Table", ar);
		*/
		json.put("sampleTime", sampleTime);

		out.write(json.toString());
		out.write("\n");
		out.close();
//		System.out.println("write "+filePath+" file success!");
	}
	public synchronized void printCurInfo(){
//		try{
		System.out.println("printed curInfo ，hoSet的内容：");
		HashSet<Host> theHostSet = new HashSet<Host>();
		theHostSet = (HashSet<Host>) hoSet;
		Iterator<Host> it = theHostSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			System.out.println(h11.toString());
		}
		/*
		System.out.println("conSet的内容：");
		HashSet<Container> theConSet = new HashSet<Container>();
		theConSet = (HashSet<Container>) conSet;
		Iterator<Container> it1 = theConSet.iterator();
		while(it1.hasNext()){
			Container c11 = (Container)it1.next();
			System.out.println(c11.toString());
		}
		}catch(Exception e){
			e.printStackTrace();
		}				
		if(dbHostList.isEmpty()){
			System.out.println("当前主机列表为空！");
		}else{
			System.out.println("当前主机列表:");
			for(int i=0;i<dbHostList.size();i++){
				System.out.println(dbHostList.get(i));
			}
		}*/
		/*
		if(serList.isEmpty()){
			System.out.println("当前服务列表为空！");
		}else{
			System.out.println("当前服务列表:");
			for(int i=0;i<serList.size();i++){
				System.out.println(serList.get(i));
				System.out.println("服务名：" + serList.get(i).serviceName.toString());
				System.out.println("所在主机IP地址：" + serList.get(i).ip.toString());
				System.out.println("当前状态：" + serList.get(i).state.toString());
				System.out.println("类型：" + serList.get(i).serviceKind.toString());
				System.out.println("所在系统：" + serList.get(i).serviceSystem.toString());
			}
		}
		*/
		System.out.println("sampleTime:"+sampleTime);
	}
	public Set<Host> getHoSet(){
		return hoSet;
	}
//	public Set<Container> getConSet(){
//		return conSet;
//	}
	
	/**
	 * 添加一台主机信息，如果以前的set里面有该主机的IP，就会覆盖历史消息；若是新加主机，就会直接添加
	 * @param h
	 */
	public void addHost(Host h){
		ArrayList<Host> delList = new ArrayList<Host>();
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			if(h.IPadd.equals(h11.IPadd)){
				delList.add(h11);				
			}					
		}	
		hoSet.removeAll(delList);
		hoSet.add(h);
	}
	/**
	 * 添加一个容器信息，如果以前的set里面有该容器，就会覆盖历史消息；若是新加容器，就会直接添加
	 * @param c
	 */
//	public synchronized void addContainer(Container c){
//		ArrayList<Container> delList = new ArrayList<Container>();
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.conName.equals(c.conName)&&c11.IPadd.equals(c.IPadd)){
//				delList.add(c11);	
//			}
//		}
//		conSet.removeAll(delList);
//		conSet.add(c);
//	}
	/**
	 * 清空内存中现存的主机和容器的监控信息
	 */
	public void clearAll(){
		hoSet.clear();
//		conSet.clear();
	}
	/**
	 * 获取当前主机监控信息列表
	 * @return
	 */
	public Set<Host> getCurHostInfo(){
		return hoSet;
	}
	/**
	 * 获取当前服务容器监控信息列表
	 * @return
	 */
//	public Set<Container> getCurConInfo(){
//		return conSet;
//	}
	/**
	 * 获取当前数据库的主机IP列表
	 * @return
	 */
	public ArrayList<String> getdbHostList(){
		return dbHostList;
	}
	/**
	 * 设置数据库主机IP列表
	 * @param dbHostList
	 */
	public void setdbHostList(ArrayList<String> dbHostList){
		CurrentInfo.dbHostList = dbHostList;
	}
	/**
	 * 获取采样时间间隔
	 * @return
	 */
	public int getSampleTime(){
		return sampleTime;
	}
	/**
	 * 设置采样时间间隔
	 * @param stime
	 */
	public void setSampleTime(int stime){
		sampleTime = stime;
	}
	/**
	 * yxjk_1_1获取当前主机的个数
	 * @return
	 */
	public int getHostAmount(){
		return hoSet.size();
	}
	/**
	 * yxjk_1_2获取服务容器的总数
	 * @return
	 */
//	public int getContainerAmount(){
//		return conSet.size();
//	}
	/**
	 * 获取主机的负载状况，即每台主机上有多少个服务。用于首页的展示。例如 192.168.0.32，15个服务
	 * @return
	 */
//	public HashMap<String,Integer> getHostServiceLoad(){
//		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
//		Iterator<Host> it = hoSet.iterator();
//		while(it.hasNext()){
//			Host h11 = (Host)it.next();
//			int h11_amount = 0;
//			Iterator<Container> it1 = conSet.iterator();
//			while(it1.hasNext()){
//				Container c11 = (Container)it1.next();
//				if(h11.getIP().equals(c11.getIP())){
//					h11_amount += c11.getServiceAmount();
//				}
//			}
//			hmap.put(h11.getIP(), h11_amount);
//		}
//		return hmap;
//	}
	/**
	 * 获取每种类型的服务容器个数，例如Axis2：5个，Tomcat：3个
	 * @return
	 */
//	public HashMap<String,Integer> getAllKindsContainerAmount(){
//		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
//		int tt = 0;int a2 = 0;int is = 0;int me = 0;
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.getConName().contains("Tomcat")){
//				tt++;
//			}else if(c11.getConName().contains("Axis2")){
//				a2++;
//			}else if(c11.getConName().contains("IIS")){
//				is++;
//			}else if(c11.getConName().contains("Mule")){
//				me++;
//			}
//		}
//		hmap.put("Tomcat", tt);
//		hmap.put("Axis2", a2);
//		hmap.put("IIS", is);
//		hmap.put("Mule", me);
//		return hmap;
//	}	
	/**
	 * 获取包含副本的服务的总数。方法是计算serList的大小
	 * @return
	 */
//	public int getServiceAmount(){
//		return serList.size();		
//	}
	/**
	 * 计算某特定主机上的服务总数,包含副本
	 * @param ip
	 * @return
	 */
//	public int getSpecificHostServiceAmount(String ip){
//		int amount = 0;
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.IPadd.equals(ip)){
//				amount += c11.getServiceAmount();
//			}			
//		}
//		return amount;
//	}
	/**
	 * 获取系统的名称,hashSet的大小就是系统的个数。
	 * @return
	 */
//	public HashSet<String> getSystemSet(){
//		HashSet<String> sysKindSet = new HashSet<String>();
//		for(int i=0;i<serList.size();i++){
//			sysKindSet.add(serList.get(i).getServiceSystem());
////			System.out.println(serList.get(i).getServiceSystem());
//		}		
//		return sysKindSet;
//	}
	/**
	 * 获取不带副本的服务列表
	 * @return
	 */
//	public HashSet<Service> getServiceListWithoutDuplicate(){
//		HashSet<Service> serSet = new HashSet<Service>();
//		for(int i = 0;i<serList.size();i++){
//			Service s1 = serList.get(i);
//			
////			if(!serSet.contains(s1)){
////				serSet.add(s1);
////			}
//			if(serSet.isEmpty()){
//				serSet.add(s1);
//			}else{
//				
//				Iterator<Service> it = serSet.iterator();
//				ArrayList<Service> addList = new ArrayList<Service>();
//				while(it.hasNext()){
//					Service service = (Service)it.next();
//					if(s1.serviceName.equals(service.serviceName)){
//						//不添加
//					}else{
//						addList.add(s1);						
//					}
//				}//while
//				serSet.addAll(addList);
//			}//else			
//		}//for
//		return serSet;
//	}
	/**
	 * 获取某个特定系统的主机列表，用于拓扑结构图
	 * @param sysName
	 * @return
	 */
//	public HashSet<String> sysHostIPList(String sysName){
//		HashSet<String> sysHostList = new HashSet<String>();
//		for(int i = 0;i<serList.size();i++){
//			Service s1 = serList.get(i);
//			if(s1.serviceSystem.equals(sysName)){
//				sysHostList.add(s1.ip);
//			}
//		}
//		return sysHostList;
//	}
	/**
	 * yxjk_3_fw,服务状态概览图。
	 * @return 例如 指挥系统：[10，8，2]
	 */
	/*
	public HashMap<String,int[]> getServiceStateOverview(){
		HashMap<String,int[]> map = new HashMap<String,int[]>();
		//获取系统名称的集合
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//获取某个特定系统的服务总数（带副本）
		
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			int[] a = new int[3];
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					if(s11.getState().contains("start")){
						a[1] +=1;
					}else{
						a[2] +=1;
					}
				}
			}
			map.put(nameArray[i], a);
		}
		return map;
	}
	*/
	//yxjk_3_fw 的图2 和图3 合并到一起，图2表示每个系统的服务个数；
	//图3表示每个系统内所有服务有几个是启动的，有几个是停止的
	/*
	public ArrayList<HashMap<String,Object>> sysServiceCombined(){
		ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();
		//构造一个数组，每一项存的是一个系统的服务总数（不带副本）
		//获取所有系统的名字和系统的个数
		HashSet<String> sysNameSet = getSystemSet();
		int sysAmount = sysNameSet.size();
		
		//给每个系统的初始化个数赋值为0
		int[] amountArray = new int[sysAmount];
		String[] nameArray = new String[sysAmount];
		for(int i = 0;i<sysAmount;i++){
			amountArray[i] = 0;
		}
		
		//给系统名字的数组nameArray赋值
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
//			System.out.println("nameArray["+ii+"] ="+nameArray[ii]);
			ii++;
		}
		
		
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
//		ArrayList<Service> arrList = new ArrayList<Service>();
//		arrList.addAll(serSet);
		if(serList.size()==0){
			System.out.println("服务列表为空，系统内无服务！");
		}else{
			Iterator<Service> it = serList.iterator();			
			while(it.hasNext()){
				Service s11 = (Service)it.next();				
				for(int j = 0;j<sysAmount;j++){
					if(s11.serviceSystem.equals(nameArray[j])){
						amountArray[j] += 1;
					}
				}//for							
			}//while
		}
		
		//计算不含副本的服务总数
		//int sum = getServiceListWithoutDuplicate().size();
		//将结果存入到hmap中
		for(int j=0;j<sysAmount;j++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("sysName", nameArray[j]);
			map.put("amount", amountArray[j]);
			
			//计算服务启动/停止的个数
			int[] a = new int[3];
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[j])){
					a[0] +=1;
					if(s11.getState().contains("start")){
						a[1] +=1;
					}else{
						a[2] +=1;
					}
				}
			}
			map.put("started", a[1]);			
			hmap.add(map);
		}
		return hmap;
	}
	*/
	
	
	/**
	 * yxjk_4_xt图1，上方磁贴,例如 指挥系统：[12,3]	12个服务，3台主机
	 * @return
	 */
	/*
	public ArrayList<HashMap<String,Object>> sysOverview(){
		ArrayList<HashMap<String,Object>> map = new ArrayList<HashMap<String,Object>>();
		//获取系统名称的集合
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//获取某个特定系统的服务总数（带副本）
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			HashMap<String,Object> amap = new HashMap<String,Object>();
			int[] a = new int[2];
			//计算该系统的服务个数
			HashSet<String> set0 = new HashSet<String>();
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					//计算该系统的主机个数
					set0.add(s11.ip);
				}
			}
			a[1] = set0.size();	
			amap.put("sysName", nameArray[i]);
			amap.put("amount", a);
			map.add(amap);
		}
		return map;
	}
	*/
	/**
	 * yxjk_4_xt图，以系统名字为key，把三个图合到一起
	 * 系统名字，服务数，主机数量，服务表，主机表
	 * @return
	 */
	/*
	public JSONArray sysCombined(){
		JSONArray map = new JSONArray();
		//获取系统名称的集合
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//现在nameArray里面存的是所有系统的名字
		//获取某个特定系统的服务总数（带副本）
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			//每循环一次，表示在找一个特定的系统的信息，amap里面存的是这个特定系统的所有信息。
			//包括系统名，系统的服务数量，系统的主机数量，系统的主机监控信息列表，系统的服务状态信息列表
			HashMap<String,Object> amap = new HashMap<String,Object>();
			int[] a = new int[2];
			//计算该系统的服务个数
			HashSet<String> set0 = new HashSet<String>();
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					//计算该系统的主机个数
					set0.add(s11.ip);
				}
			}
			a[1] = set0.size();	
			amap.put("sysName", nameArray[i]);
			amap.put("amount", a);
			//获取该系统的所有主机监控信息		
			ArrayList<Host> listhost = new ArrayList<Host>();
				String sysName = nameArray[i];
				HashSet<String> sysHostList = sysHostIPList(sysName);
				Iterator<String> it2 = sysHostList.iterator();
				while(it2.hasNext()){
					
					String ip = (String) it2.next();
					//通过该ip获取该主机对象
					//遍历hoSet，找出一个主机IP等于上边ip的主机
					Iterator<Host> iit = hoSet.iterator();
					while(iit.hasNext()){
						Host h11 = (Host)iit.next();
						if(ip.equals(h11.IPadd)){
							listhost.add(h11);
						}
					}
				}
			amap.put("host-Table", listhost);
			//获取该系统的所有服务信息
			ArrayList<Service> ServiceList = new ArrayList<Service>();
			Iterator<Service> it3 = serList.iterator();
			while(it3.hasNext()){
				ArrayList<String> strList = new ArrayList<String>();
				Service s11 = (Service)it3.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					ServiceList.add(s11);
					strList.add(s11.serviceName);
					strList.add(s11.serviceKind);
					strList.add(s11.ip);
					strList.add(s11.state);
				}
			}
			amap.put("service-Table", ServiceList);
			//把所有的信息Put到map里
			map.add(amap);
		}
		return map;
	}
	*/
///
	//****************************************************************************//
	//****************************************************************************//
///
	/**
	 * yxjk_1_zt图1，系统类型概览。表示每个系统的服务数。（饼图）
	 * @return 返回一个HashMap<String,int> 例如：雷达系统，10个；指挥系统：12个
	 */
	/*
	public ArrayList<HashMap<String,Object>> sysKindOverview(){
		ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();
		//构造一个数组，每一项存的是一个系统的服务总数（不带副本）
		//获取所有系统的名字和系统的个数
		HashSet<String> sysNameSet = getSystemSet();
		int sysAmount = sysNameSet.size();
		
		//给每个系统的初始化个数赋值为0
		int[] amountArray = new int[sysAmount];
		String[] nameArray = new String[sysAmount];
		for(int i = 0;i<sysAmount;i++){
			amountArray[i] = 0;
		}
		
		//给系统名字的数组nameArray赋值
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
//			System.out.println("nameArray["+ii+"] ="+nameArray[ii]);
			ii++;
		}
		
		
		HashSet<Service> serSet = getServiceListWithoutDuplicate();
//		ArrayList<Service> arrList = new ArrayList<Service>();
//		arrList.addAll(serSet);
		if(serSet.size()==0){
			System.out.println("服务列表为空，系统内无服务！");
		}else{
			
//			for(int i = 0;i<arrList.size();i++){
//				Service s11 = arrList.get(i);
//				System.out.println("i="+i+"\tnameArray[i]="+nameArray[i]);
//				System.out.println(s11.serviceSystem);
//				System.out.println(s11.serviceSystem.equals(nameArray[i]));
//				if(s11.serviceSystem.equals(nameArray[i])){
//					amountArray[i] += 1;
//					System.out.println("i="+i+"\tamountArray[i]="+amountArray[i]);
//				}
//			}
			
			
			
			Iterator<Service> it = serList.iterator();
			
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				
				for(int j = 0;j<sysAmount;j++){
//					System.out.println("j="+j+"\tnameArray[j]="+nameArray[j]);
//					System.out.println(s11.serviceSystem);
//					System.out.println(s11.serviceSystem.equals(nameArray[j]));
					if(s11.serviceSystem.equals(nameArray[j])){
						amountArray[j] += 1;
//						System.out.println("i="+j+"\tamountArray[i]="+amountArray[j]);
					}
				}//for							
			}//while
		}
		
		//计算不含副本的服务总数
		//int sum = getServiceListWithoutDuplicate().size();
		//将结果存入到hmap中
		for(int j=0;j<sysAmount;j++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("sysName", nameArray[j]);
			map.put("amount", amountArray[j]);
			hmap.add(map);
//			System.out.println(nameArray[j]+","+amountArray[j]);
		}
		
			
		
		return hmap;
	}
	*/
	/**
	 * yxjk_1图2,计算系统的主机的平均CPU占用率。（动图）
	 * @return double
	 */
	public double calulateAverageCpu(){
		if(hoSet.size()==0){
			System.out.println("hoSet为空，不能计算平均值！");
			return 0;
		}else{
			double hcpuResult = 0;
			Iterator<Host> it = hoSet.iterator();
			while(it.hasNext()){
				Host h11 = (Host)it.next();
				hcpuResult += Double.parseDouble(h11.getCPU().split("%")[0]);
			}
			return hcpuResult/hoSet.size();
		}
		
	}
	/**
	 * yxjk_1图2,计算系统的主机的平均内存占用率。（动图）
	 * @return double
	 */
	public double calulateAverageRam(){
		if(hoSet.size()==0){
			System.out.println("hoSet为空，不能计算平均值！");
			return 0;
		}else{
			double hramResult = 0;
			Iterator<Host> it = hoSet.iterator();
			while(it.hasNext()){
				Host h11 = (Host)it.next();
				hramResult += Double.parseDouble(h11.getRAM().split("%")[0]);
			}
			return hramResult/hoSet.size();
		}		
	}
	
	/**
	 * yxjk_1图4.主机cpu负载，内存负载（右下方折线图）
	 * @param ip 主机的IP地址
	 * @return 该主机的当前cpu的值和内存的值
	 */
	public HashMap<String,double[]> hostCpuLineChart(){
		HashMap<String,double[]> map = new HashMap<String,double[]>();		
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			double[] result = new double[2];
			Host h11 = (Host)it.next();
			result[0] = Double.parseDouble(h11.getCPU().split("%")[0]);
			result[1] = Double.parseDouble(h11.getRAM().split("%")[0]);
			map.put(h11.getIP(), result);
		}
		return map;
	}	

	public double[] hostCpuLineChart1(String ip) {
		// HashMap<String,double[]> map = new HashMap<String,double[]>();
		Iterator<Host> it = hoSet.iterator();
		double[] result = new double[2];
		while (it.hasNext()) {
			Host h11 = (Host) it.next();
			if (h11.IPadd.equals(ip)) {
				result[0] = Double.parseDouble(h11.getCPU().split("%")[0]);
				result[1] = Double.parseDouble(h11.getRAM().split("%")[0]);
			}
		}
		return result;
	}

	/**
	 * yxjk_1图5.单个主机上的服务概览,表示某台特定主机上每种类型的服务占总服务数量得百分比，加起来等于一百
	 * （下方圆图）
	 * @param ip
	 * @return ArrayList<HashMap<String,float[]>> ,其中Float数组里面存的依次是 Tomcat,Axis2,
	 *         IIS,Mule服务的数量值。一个HashMap里面的key表示主机的IP地址。ArrayList里存了所有主机的信息。
	 */
	/*
	public ArrayList<ArrayList<HashMap<String,Object>>> hostConPercentChart(){
		ArrayList<ArrayList<HashMap<String,Object>>> list = new ArrayList<ArrayList<HashMap<String,Object>>>();		
		Iterator<Host> it0 = hoSet.iterator();
		while(it0.hasNext()){
			ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();		
			Host h11 = (Host)it0.next();
			HashMap<String,Object> hhMap = new HashMap<String,Object>();
			hhMap.put("IP", h11.IPadd);
			
			//主机h11上的服务总数
//			int amount = getSpecificHostServiceAmount(h11.IPadd);
			//主机h11上的各种服务的个数。次序是Tomcat-->Axis2-->IIS-->Mule
			float[] d1 = new float[4];
			Iterator<Container> it = conSet.iterator();
			while(it.hasNext()){
				Container c11 = (Container)it.next();
				if(c11.IPadd.equals(h11.IPadd)){
					//h11里面的所有容器
					if(c11.getConName().contains("Tomcat")){
						d1[0] += c11.getServiceAmount();//Tomcat里面的服务个数
					}else if(c11.getConName().contains("Axis2")){
						d1[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("IIS")){
						d1[2] += c11.getServiceAmount();
					}else if(c11.getConName().contains("Mule")){
						d1[3] += c11.getServiceAmount();
					}
				}				
			}
			hhMap.put("array", d1);
			hmap.add(hhMap);			
			list.add(hmap);
		}		
		return list;
	}
	*/
	/**
	 * yxjk_2图1,计算平均的硬盘占用率
	 * @return
	 */
	/*
	public double calulateAverageHdd(){
		double v = 0;
		double temp = 0;
		double result = 0;
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			ArrayList<String> hddList = h11.getHddList();
			for(int i=0;i<hddList.size();i++){
				double value = Double.parseDouble(hddList.get(i).split(":")[1].split("%")[0]);
				v += value;
			}
			temp = v/hddList.size();
			result += temp;
		}
		result /= hoSet.size();
		return result;
	}	
	*/
	/**
	 * yxjk_2_zj图2，获取某主机上每种类型的服务容器类别和数量
	 * @return  HashMap<String,int[]>  key表示容器名，int数组里面存的依次是该容器的个数和该容器内服务的个数
	 */	
	/*
	public ArrayList<HashMap<String,Object>> getHostCon(){
		ArrayList<HashMap<String,Object>> li = new ArrayList<HashMap<String,Object>>();
		Iterator<Host> it0 = hoSet.iterator();
		while(it0.hasNext()){
			Host h11 = (Host) it0.next();
			HashMap<String,Object> hmap = new HashMap<String,Object>();
			int[] t = new int[2];int[] a = new int[2];
			int[] i = new int[2];int[] m = new int[2];
			int tt = 0;int a2 = 0;int is = 0;int me = 0;
			Iterator<Container> it = conSet.iterator();
			while(it.hasNext()){
				Container c11 = (Container)it.next();
				if(c11.getIP().equals(h11.getIP())){
					if(c11.getConName().contains("Axis2")){
						a[0] = ++a2;//容器的数量
						a[1] += c11.getServiceAmount();//容器内服务的数量					
					}else if(c11.getConName().contains("Tomcat")){
						t[0] = ++tt;
						t[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("IIS")){
						i[0] = ++is;
						i[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("Mule")){
						m[0] = ++me;
						m[1] += c11.getServiceAmount();
					}
				}	
			}
			hmap.put("IP", h11.IPadd);
			hmap.put("Tomcat", t[0]);
			hmap.put("Axis2", a[0]);
			hmap.put("IIS", i[0]);
			hmap.put("Mule", m[0]);
//			map1.put(h11.IPadd, hmap);
			li.add(hmap);
		}
		
		return li;
	}
	*/
	/**
	 * yxjk_2_zj图3，获取某主机上每种类型的服务类别和数量
	 * @return  
	 */
	/*
	public ArrayList<HashMap<String,Object>> getHostServiceKind(){
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<dbHostList.size();i++){
			HashMap<String,Object> map1 = new HashMap<String,Object>();
			String ip = dbHostList.get(i);
			int[] array = new int[4];
			for(int j =0;j<serList.size();j++){
				Service s = serList.get(j);
				if(s.ip.equals(ip)){//s是这个主机上的服务
					if(s.serviceKind.contains("Axis2")){
						array[0]+=1;
					}else if(s.serviceKind.contains("Mule")){
						array[1]+=1;
					}else if(s.serviceKind.contains("exe")){
						array[2]+=1;
					}else if(s.serviceKind.contains("IIS")){
						array[3]+=1;
					}
				}
			}
			map1.put("IP", ip);
			map1.put("array", array);
			list.add(map1);
		}
		
		return list;
	}
	*/
	/**
	 * yxjk_5_rq图1，部署某个容器的主机数量
	 * @return HashMap<String,int>
	 */
	/*
	public HashMap<String,Integer> deployContainerHostAmount(){
		//假设一台特定主机上 不能部署两个一样的容器
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Iterator<Container> it = conSet.iterator();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt ++;
			}else if(c11.getConName().contains("Axis2")){
				a2 ++;
			}else if(c11.getConName().contains("IIS")){
				is ++;
			}else if(c11.getConName().contains("Mule")){
				me ++;
			}
		}
		map.put("Tomcat",tt);map.put("Axis2",a2);
		map.put("IIS", is);map.put("Mule",me);
		return map;
	}	
	*/
	/**
	 * yxjk_3_fw图1，计算整个系统内每种类型的服务的个数。例如：Axis2服务数：18个，
	 * @return
	 */
	/*
	public HashMap<String,Integer> eachKindServiceAmount(){
		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt += c11.getServiceAmount();
			}else if(c11.getConName().contains("Axis2")){
				a2 += c11.getServiceAmount();;
			}else if(c11.getConName().contains("IIS")){
				is += c11.getServiceAmount();
			}else if(c11.getConName().contains("Mule")){
				me += c11.getServiceAmount();
			}
		}
		hmap.put("Tomcat", tt);
		hmap.put("Axis2", a2);
		hmap.put("IIS", is);
		hmap.put("Mule", me);
		return hmap;
	}
	*/
	/**
	 * yxjk_3_fw图2,服务类型分布概览,共有四种服务类型，分别是Axis2，Tomcat，IIS，Mule，
	 * 每个色块表示每种类型的 服务占服务总数的百分比
	 * @return 返回一个HashMap<String,double>
	 */
	/*
	public HashMap<String,Float> serviceKindOverview(){
		HashMap<String,Float> hmap = new HashMap<String,Float>();
		//获取服务的总数
		int allserAmount = getServiceAmount();
		float tt = 0;float a2 = 0;float is = 0;float me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt += c11.getServiceAmount();
			}else if(c11.getConName().contains("Axis2")){
				a2 += c11.getServiceAmount();;
			}else if(c11.getConName().contains("IIS")){
				is += c11.getServiceAmount();
			}else if(c11.getConName().contains("Mule")){
				me += c11.getServiceAmount();
			}
		}
		hmap.put("Tomcat", tt/allserAmount);
		hmap.put("Axis2", a2/allserAmount);
		hmap.put("IIS", is/allserAmount);
		hmap.put("Mule", me/allserAmount);
		return hmap;
	}
	*/
	/**
	 * yxjk_5_rq图2，每类容器占总容器数量的百分比,给前端数字，由前端计算百分比
	 * @return
	 */
	/*
	public HashMap<String,Integer> conKindOverview(){
		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt++;
			}else if(c11.getConName().contains("Axis2")){
				a2++;
			}else if(c11.getConName().contains("IIS")){
				is++;
			}else if(c11.getConName().contains("Mule")){
				me++;
			}
		}
		hmap.put("Tomcat", tt);
		hmap.put("Axis2", a2);
		hmap.put("IIS", is);
		hmap.put("Mule", me);
		return hmap;
	}
	*/
	/**
	 * 计算整个系统的平均cpu和内存占用，用于主页动态图显示
	 * //存到文件AvgCPUAndRam.txt里面
	 * @throws IOException
	 */	
	/*
	public void writeAVGFile() throws IOException{
		String filePath = ".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath,true),"UTF-8");
		JSONArray json = new JSONArray();
		
		Double AvGcpu = calulateAverageCpu();
		Double AvGram = calulateAverageRam();
		if(AvGcpu.isInfinite()&&AvGram.isInfinite()){
			json.add(0);
			json.add(0);
		}else if (AvGcpu.isInfinite()) {
			json.add(0);
			json.add(AvGram);
		}else if(AvGram.isInfinite()){
			json.add(AvGcpu);
			json.add(0);
		}else{
			json.add(AvGcpu);
			json.add(AvGram);
		}
		
		out.append(json.toString());
	    out.append("\n");
	    out.close();
	}
	*/
	
	//把某台主机的cpu和ram存入到临时文件cpuAndRamLineChart.txt中
	/*
	public void writeLineChartFile() throws IOException{
		String filePath = ".\\WebRoot\\DataFiles\\cpuAndRamLineChart.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath,true),"UTF-8");
		JSONArray json = new JSONArray();
		
		
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			JSONObject mp1 = new JSONObject();
			JSONArray arr1 = new JSONArray();
			Host h11 = (Host) it.next();			
			h11.getIP();			
			arr1.add(h11.getCPU());
			arr1.add(h11.getRAM());					
			mp1.put(h11.getIP(),arr1);
			json.add(mp1);
		}
			
		
		out.append(json.toString());
	    out.append("\n");
	    out.close();
	}
	*/
}//CurrentInfo
