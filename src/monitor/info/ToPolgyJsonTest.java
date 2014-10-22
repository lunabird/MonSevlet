package monitor.info;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 构造系统拓扑结构图
 * @author huangpeng
 *
 */
//
public class ToPolgyJsonTest {
	public static void writeFile(String sets) throws IOException {
		String filePath = "F:\\TopologyJson.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
	    out.append(sets);
	    out.append("\n");
	    out.close();
	   }
	/*
	public static JSONObject buildTopology(CurrentInfo curInfo) throws IOException{
		//group1的节点
		Node.index = -1;
				JSONArray json = new JSONArray();
				JSONArray json1 = new JSONArray();
				Node n = new Node("一体化栅格服务集成平台",1,10);
				HashMap<String,String> map = n.toHashMap();
				HashMap<String,Integer> LinkMap = new HashMap<String,Integer>();
				ArrayList<Node> nodeList = new ArrayList<Node>();
				nodeList.add(n);
				json.add(map);
				//group2的节点，所有系统
				HashSet<String> systemSet = curInfo.getSystemSet();
				Iterator<String> it1 = systemSet.iterator();
				while(it1.hasNext()){
					String sysName = (String)it1.next();
					n = new Node(sysName,2,8);
					nodeList.add(n);
					map = n.toHashMap();
					int target = n.getSpecificIndex("一体化栅格服务集成平台",nodeList);
					LinkMap = new Link(Node.index,target,10).toHashMap();
					json.add(map);
					json1.add(LinkMap);
				}
				//group3的节点，所有主机
				it1 = systemSet.iterator();
				while(it1.hasNext()){
					String sysName = (String)it1.next();
//					System.out.println(sysName);
					HashSet<String> sysHostList = curInfo.sysHostIPList(sysName);
					Iterator<String> it2 = sysHostList.iterator();
					while(it2.hasNext()){
						String ip = (String) it2.next();
						n = new Node(sysName+"_"+ip,3,6);
						nodeList.add(n);
						map = n.toHashMap();
						//计算该主机所在系统的index
						int target = n.getSpecificIndex(sysName,nodeList);
						LinkMap = new Link(Node.index,target,8).toHashMap();
						json.add(map);
						json1.add(LinkMap);
					}
				}
				//group4的节点，所有容器
				Iterator<Container> it2 = curInfo.getConSet().iterator();
				while(it2.hasNext()){
					Container c11 = (Container)it2.next();
					n = new Node(c11.getIP()+"_"+c11.getConName(),4,4);
					nodeList.add(n);
					map = n.toHashMap();
					//计算该容器所在主机的Index
					int target = n.getSpecificIndex(c11.getIP(), nodeList);
					LinkMap = new Link(Node.index,target,6).toHashMap();
					json.add(map);
					json1.add(LinkMap);
				}
				//group5的节点，所有服务
				for(int i=0;i<curInfo.getSerList().size();i++){
					n = new Node(curInfo.getSerList().get(i).getServiceIP()+"_"+
							curInfo.getSerList().get(i).getServiceKind()+"_"+
							curInfo.getSerList().get(i).getServiceName(),5,2);
					nodeList.add(n);
					map = n.toHashMap();
					json.add(map);
					//计算该服务所在容器的Index
					//如果服务类型是exe，直接连接到主机
					int target;
					if(curInfo.getSerList().get(i).getServiceKind().equals("exe")){
						target = n.getSpecificIndex(curInfo.getSerList().get(i).getServiceIP(),nodeList);
					}else{
						target = n.getSpecificIndex(curInfo.getSerList().get(i).getServiceIP()+"_"+curInfo.getSerList().get(i).getServiceKind(), nodeList);
					}			
					LinkMap = new Link(Node.index,target,4).toHashMap();			
					json1.add(LinkMap);
				}
				
//				HashMap<String,Object> mapa = new HashMap<String,Object>();
//				mapa.put("nodes", json.toString());
//				mapa.put("links", json1.toString());
				JSONObject j = new JSONObject();
				j.put("nodes", json.toString());
				j.put("links", json1.toString());
//				writeFile(j.toString());
				
				return j;
	}
	*/
	/**
	 * 单个服务的拓扑结构图
	 * @param sysName
	 * @param serviceID
	 * @param serviceName
	 * @param hostList
	 * @return
	 * @throws IOException
	 */
	public static JSONObject buildTopologyForSingleService(String sysName,String serviceID,String serviceName,ArrayList<String[]> hostList) throws IOException{
		Node.index = -1;
		JSONArray json = new JSONArray();
		JSONArray json1 = new JSONArray();
		//group1的节点。
		Node n = new Node("",1,10);
		HashMap<String,String> map = n.toHashMap();
		HashMap<String,Integer> LinkMap = new HashMap<String,Integer>();
		ArrayList<Node> nodeList = new ArrayList<Node>();
		nodeList.add(n);
		json.add(map);
		//group2的节点。业务类型节点
		n = new Node(sysName,2,8);
		nodeList.add(n);
		map = n.toHashMap();
		int target = n.getSpecificIndex("",nodeList);
		LinkMap = new Link(Node.index,target,10).toHashMap();
		json.add(map);
		json1.add(LinkMap);
		//group3的节点。节点IP地址节点
		for(int i=0;i<hostList.size();i++){
			n = new Node(sysName+"_"+hostList.get(i)[0]+"_"+hostList.get(i)[1],3,6);
			nodeList.add(n);
			map = n.toHashMap();
			target = n.getSpecificIndex(sysName,nodeList);
			LinkMap = new Link(Node.index,target,8).toHashMap();
			json.add(map);
			json1.add(LinkMap);
		}
		//group4节点。服务节点。
		for(int i=0;i<hostList.size();i++){
			n = new Node(sysName+"_"+hostList.get(i)[1]+"_"+serviceName+"_"+serviceID,4,4);
			nodeList.add(n);
			map = n.toHashMap();
			target = n.getSpecificIndex(hostList.get(i)[1],nodeList);
			LinkMap = new Link(Node.index,target,6).toHashMap();
			json.add(map);
			json1.add(LinkMap);
		}
			
		JSONObject j = new JSONObject();
		j.put("nodes", json.toString());
		j.put("links", json1.toString());
		writeFile(j.toString());  //For Test
		
		return j;
	}
	/**
	 * 总体拓扑结构图，监控主页显示
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public static JSONObject buildTopologyForOverview(ArrayList<String[]> list) throws IOException{
		//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型,服务ID
		HashSet<String> ipSet = new HashSet<String>();
		for(int i=0;i<list.size();i++){
			String ip = list.get(i)[1];
			ipSet.add(ip);
		}
		Object[] ipArray = ipSet.toArray();
		HashSet<String> sysSet = new HashSet<String>();
		for(int i=0;i<list.size();i++){
			String sys = list.get(i)[3];
			sysSet.add(sys);
		}
		Object[] sysArray = sysSet.toArray();
		ArrayList<ArrayList<String>> allSysIPList = new ArrayList<ArrayList<String>>();
		for(int j=0;j<sysArray.length;j++){
			String sysName = sysArray[j].toString();
			ArrayList<String> sysIPList = new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				if(list.get(i)[3].equals(sysName)){
					sysIPList.add(list.get(i)[1]);
				}
			}
			allSysIPList.add(sysIPList);
		}
		
		ArrayList<ServiceToPoInfo> sList = new ArrayList<ServiceToPoInfo>();
		for(int i=0;i<list.size();i++){
			String serviceName = list.get(i)[0];
			String IP = list.get(i)[1];
			String sys = list.get(i)[3];
			String id = list.get(i)[5];
			ServiceToPoInfo stpi = new ServiceToPoInfo(serviceName,IP,sys,id);
			sList.add(stpi);
		}
		
		Node.index = -1;
		JSONArray json = new JSONArray();
		JSONArray json1 = new JSONArray();
		//group1的节点。
		Node n = new Node("",1,10);
		HashMap<String,String> map = n.toHashMap();
		HashMap<String,Integer> LinkMap = new HashMap<String,Integer>();
		ArrayList<Node> nodeList = new ArrayList<Node>();
		ArrayList<Link> linkList = new ArrayList<Link>();
		nodeList.add(n);
		json.add(map);
		//group2的节点。业务类型节点
		int target =0;
		for(int i=0;i<sysArray.length;i++){
			n = new Node(sysArray[i].toString(),2,8);
			nodeList.add(n);
			map = n.toHashMap();
			target = n.getSpecificIndex("",nodeList);
			Link link = new Link(Node.index,target,10);
			LinkMap = link.toHashMap();
			linkList.add(link);
			json.add(map);
			json1.add(LinkMap);
		}
		//group3的节点。节点IP地址节点
		for(int i=0;i<ipArray.length;i++){
			n = new Node(ipArray[i].toString(),3,6);
			nodeList.add(n);
			map = n.toHashMap();
			json.add(map);
		}
		//group4节点。服务节点。
		for(int i=0;i<sList.size();i++){
			n = new Node(sList.get(i).getSys()+"_"+sList.get(i).getIP()+"_"+sList.get(i).getServiceName()+"_"+sList.get(i).getID(),4,4);
			nodeList.add(n);
			map = n.toHashMap();
			target = n.getSpecificIndex(sList.get(i).getIP(), nodeList);
			Link link = new Link(Node.index,target,6);
			LinkMap = link.toHashMap();	
			linkList.add(link);
			json.add(map);
			json1.add(LinkMap);
			int targetSys = n.getSpecificIndex(sList.get(i).getSys(), nodeList);
			link = new Link(target,targetSys,8);
			//判断该link是否已经存在
			int flag = 0;
			for(int k=0;k<linkList.size();k++){
				if((linkList.get(k).source==link.source)&&(linkList.get(k).target==link.target)){
					flag = 1;//表示已经有这条连接了
				}else{
					flag = 0;//表示还没有这条连接
				}
			}
			if(flag==0){
				LinkMap = link.toHashMap();
				linkList.add(link);
				json1.add(LinkMap);
			}
			
		}		
		JSONObject j = new JSONObject();
		j.put("nodes", json.toString());
		j.put("links", json1.toString());
		writeFile(j.toString());  //For Test
		return j;
	}
	
	
	
	
	public static void main(String[] args) throws IOException{		
		//**测试整体的拓扑结构
//		CurrentInfo curInfo = new CurrentInfo();//获取curInfo
//		Set<Host> hoSet = new HashSet<Host>();
//		ArrayList<String> hddList = new ArrayList<String>();
//		hddList.add("C:45%");hddList.add("D:45%");hddList.add("E:45%");		
//		Host h1 = new Host("hp-PC","192.168.0.96","DE-89-OI-9I-EI-KL","46.1%","78%",hddList);
//		Host h2 = new Host("dd-PC","192.168.0.93","DE-99-OI-9I-EI-KL","33.1%","11%",hddList);
//		Host h3 = new Host("ss-PC","192.168.0.92","DE-99-OI-9I-EI-KL","33.1%","11%",hddList);
//		hoSet.add(h1);hoSet.add(h2);hoSet.add(h3);
//		Set<Container> conSet = new HashSet<Container>();
//		Container c1 = new Container("Axis2","192.168.0.96","3.1%","0.0%","20(19/1)");
//		Container c2 = new Container("Tomcat","192.168.0.93","3.2%","0.2%","22(20/2)");
//		Container c3 = new Container("Tomcat","192.168.0.92","3.2%","0.2%","22(20/2)");
//		conSet.add(c1);conSet.add(c2);conSet.add(c3);
//		curInfo.setHoSet(hoSet);
//		curInfo.setConSet(conSet);
//		Service s1 = new Service("addService","Web Service","sys1","192.168.0.96","started");
//		Service s2 = new Service("SubService","Restful","sys2","192.168.0.93","started");
//		Service s3 = new Service("exeService","exe","sys2","192.168.0.92","started");
//		ArrayList<Service> list = new ArrayList<Service>();
//		list.add(s1);
//		list.add(s2);
//		list.add(s3);
//		curInfo.refreshServiceList(list);
//		ArrayList<String> dbHostList = new ArrayList<String>();
//		dbHostList.add("192.168.0.96");
//		dbHostList.add("192.168.0.93");
//		curInfo.setdbHostList(dbHostList);
//		buildTopology(curInfo);		
		
		//**测试单个服务的拓扑结构
		
		String sysName = "情报系统";
		String serviceName = "AddService";
		String serviceID = "020";
		ArrayList<String[]> hostList = new ArrayList<String[]>();
		for(int i=0;i<3;i++){
			String[] str = new String[2];
			str[0] = "ho-PC"+i;
			str[1] = "192.168.0.20"+i;
			
			hostList.add(str);
		}
		buildTopologyForSingleService(sysName,serviceID,serviceName,hostList);
		
		//**测试总体服务概览拓扑图
		/*
		//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] s1 = {"service1","192.168.0.201","WebService","情报系统","aba"};
		String[] s2 = {"service1","192.168.0.202","WebService","情报系统","aba"};
		String[] s3 = {"service2","192.168.0.201","WebService","雷达系统","aba"};
		String[] s4 = {"service3","192.168.0.201","WebService","航空系统","aba"};
		String[] s5 = {"service3","192.168.0.202","WebService","航空系统","aba"};
		String[] s6 = {"service4","192.168.0.201","WebService","情报系统","aba"};
		String[] s7 = {"service5","192.168.0.201","WebService","情报系统","aba"};
		String[] s8 = {"service6","192.168.0.201","WebService","情报系统","aba"};
		
		list.add(s1);list.add(s2);list.add(s3);list.add(s4);list.add(s5);
		list.add(s6);list.add(s7);list.add(s8);
		buildTopologyForOverview(list);
		*/
	}
	
}



class Node{
	String name;
	int group;
	int value;
	public static int index = -1;	
	int historyIndex;
	
	public Node(String name,int group,int value){
		this.name = name;
		this.group = group;
		this.value = value;
		index++;
		historyIndex = index;
	}	
	public HashMap<String,String> toHashMap() throws IOException{ 
		HashMap<String,String> map = new HashMap<String,String>(); 
		String group1 = group+"";
		String name1 = name+"";
		String value1 = value+"";
		String index1 = index+"";
		map.put("name", name1);
		map.put("group", group1);
		map.put("value", value1);
		map.put("index", index1);
		return map;
	}
	
	public int getSpecificIndex(String name,ArrayList<Node> list){
		for(int i=0;i<list.size();i++){
			if(list.get(i).name.contains(name)){
				return list.get(i).historyIndex;
			}
		}
		return 0;
		
	}
}

class Link{
	int source;
	int target;
	int value;
	
	public Link(int source,int target,int value){
		this.source = source;
		this.target = target;
		this.value = value;		
	}
	public HashMap<String,Integer> toHashMap(){
		HashMap<String,Integer> map = new HashMap<String,Integer>(); 
		map.put("source", source);
		map.put("target",target);
		map.put("value", value);
		return map;
	}
	
}

//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型
class ServiceToPoInfo{
	String serviceName;
	String IP;
//	String kind;
	String sys;
//	String owner;
	String serviceID;
	
	public ServiceToPoInfo(String serviceName,String IP,String sys,String serviceID){
		this.serviceName = serviceName;
		this.IP = IP;
		this.sys = sys;
		this.serviceID = serviceID;
	}
	public String getServiceName(){
		return serviceName;
	}
	public String getIP(){
		return IP;
	}
	public String getSys(){
		return sys;
	}
	public String getID(){
		return serviceID;
	}
}
