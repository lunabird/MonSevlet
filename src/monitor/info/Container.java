package monitor.info;
/**
 * 服务容器类
 * @author huangpeng
 *
 */
public class Container {
	String conName;
	String IPadd;
	String cpu;
	String ram;
	String serviceAmount;
	
	public Container(String conName,String IPadd,String cpu,String ram,String serviceAmount){
		this.conName = conName;
		this.IPadd = IPadd;
		this.cpu = cpu;
		this.ram = ram;
		this.serviceAmount = serviceAmount;
	}
	
	public String getConName(){
		return conName;
	}
	public String getIP(){
		return IPadd;
	}
	public String getCPU(){
		return cpu;
	}
	public String getRam(){
		return ram;
	}
	public String getServiceAmountString(){
		return serviceAmount;
	}
	/**
	 * 获取容器内的服务总数
	 * @return
	 */
	public int getServiceAmount(){
		return Integer.parseInt(serviceAmount.split("\\(")[0]);
	}
	//override equals() 
	public boolean equals(Object o) {
		if (!(o instanceof Container))
			return false;
		Container cp = (Container) o;
		return super.equals(o) && cp.IPadd.equals(IPadd) && cp.conName.equals(conName);
	}
	public String toString(){
		return "conName:"+conName+"\tIPadd:"+IPadd+"\tcpu:"+
				cpu+"\tram:"+ram+"\tserviceAmount："+serviceAmount;
	}
}
