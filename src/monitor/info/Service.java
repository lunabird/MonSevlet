package monitor.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Service {
	String serviceName;
	String serviceKind;//按容器分
	String serviceSystem;//按系统分
	String ip;
	String state;
	
	public Service(String serviceName,String serviceKind,String serviceSystem,String ip,String state){
		this.serviceName = serviceName;
		if(serviceKind.contains("Service")){
			this.serviceKind = "Axis2";
		}else if(serviceKind.contains("Flow")){
			this.serviceKind = "Mule";
		}else if(serviceKind.contains("Restful")){
			this.serviceKind = "Tomcat";
		}else if(serviceKind.contains("Web Site")){
			this.serviceKind = "IIS";
		}else{
			this.serviceKind = serviceKind;//exe直接写exe
		}		
		this.serviceSystem = serviceSystem;
		this.ip = ip;
		this.state = state;
	}
	
	public String getServiceName(){
		return serviceName;
	}
	public String getServiceKind(){
		return serviceKind;
	}
	public String getServiceSystem(){
		return serviceSystem;
	}
	public String getServiceIP(){
		return ip;
	}
	public String getState(){
		return state;
	}
	public boolean equals(Object o) {
		if (!(o instanceof Service))
			return false;
		Service s = (Service) o;
		return s.serviceName.equals(serviceName);
	}
	public static void main(String[] args){
		Service s1 = new Service("HelloService","Web Service","System1","192.168.0.96","start");
		Service s2 = new Service("HelloService","Web Service","System1","192.168.0.44","start");
//		System.out.println(s1.equals(s2));
//		HashSet<Service> set = new HashSet<Service>();
//		set.add(s1);
//		System.out.println(set.contains(s2));
		ArrayList<Service> serList = new ArrayList<Service>();
		serList.add(s1);
		serList.add(s2);
		HashSet<Service> set = getServiceListWithoutDuplicate(serList);
		Iterator<Service> it = set.iterator();
		while(it.hasNext())
		{
			Service s = (Service)it.next();
			System.out.println(s.serviceName+","+s.ip);
		}
	}
	
	public static HashSet<Service> getServiceListWithoutDuplicate(ArrayList<Service> serList){
		HashSet<Service> serSet = new HashSet<Service>();
		for(int i = 0;i<serList.size();i++){
			Service s1 = serList.get(i);
			
//			if(!serSet.contains(s1)){
//				serSet.add(s1);
//			}
			if(serSet.isEmpty()){
				serSet.add(s1);
			}else{
				
				Iterator<Service> it = serSet.iterator();
				ArrayList<Service> addList = new ArrayList<Service>();
				while(it.hasNext()){
					Service service = (Service)it.next();
					if(s1.serviceName.equals(service.serviceName)){
						//不添加
					}else{
						addList.add(s1);						
					}
				}//while
				serSet.addAll(addList);
			}//else			
		}//for
		return serSet;
	}
	
	
	
}
