package monitor.info;

import java.util.ArrayList;

/**
 * 主机信息类
 * @author huangpeng
 *
 */
public class Host {
	String hostName;
	String IPadd;
	String mac;
	String cpu;
	String ram;
//	ArrayList<String> hddList = new ArrayList<String>();//硬盘状况
	String hdd;
	String type;//虚拟机或实体机
	String OS;//操作系统
	
	public Host(String hostName,String IPadd,String mac,String cpu,String ram,String hdd,String type,String OS){//ArrayList<String> hddList
		this.hostName = hostName;
		this.IPadd = IPadd;
		this.mac = mac;
		this.cpu = cpu;
		this.ram = ram;
//		this.hddList = hddList;
		this.hdd = hdd;
		this.type = type;
		this.OS = OS;
	}
	public String getHostName(){
		return hostName;
	}
	public String getIP(){
		return IPadd;
	}
	public String getMAC(){
		return mac;
	}
	public String getCPU(){
		return cpu;
	}
	public String getRAM(){
		return ram;
	}
//	public ArrayList<String> getHddList(){
//		return hddList;
//	}
	public String getHdd(){
		return hdd;
	}
	public Host getHostObject(String ip){
		if(ip.equals(IPadd)){
			return this;
		}else{
			return null;
		}
		
	}
	public String getType(){
		return type;
	}
	public String getOS(){
		return OS;
	}
	//override equals() 
	public boolean equals(Object o) {
		if (!(o instanceof Host))
			return false;
		Host cp = (Host) o;
		return cp.IPadd.equals(IPadd);
	}
	public String toString(){
		return "hostName:"+hostName+"\tIPadd:"+IPadd+"\tmac:"+mac+"\tcpu:"+cpu+"\tram:"+ram+"\t hdd:"+hdd+"\ttype:"+type+"\tOS"+OS;
	}
}
