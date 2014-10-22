package monitor.info;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

/**
 * 报警阈值类，存储用户设定的阈值信息
 * @author huangpeng
 *
 */
public class Liminal {
	//默认阈值
	double HostCPU;
	double HostRam;
	double HostHdd;
	
	private Liminal() {
		// 注册中心的IP地址 以及 监听端口
		double cpuL = 0;
		double ramL = 0;
		double hddL = 0;
		Document doc;
		try {
			doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
			Element cpuEle1 = root.element("liminal");
			Element cpuEle = cpuEle1.element("cpu");
			Element ramEle = cpuEle1.element("ram");
			Element hddEle = cpuEle1.element("hdd");
			cpuL = Double.parseDouble(cpuEle.getText());
			ramL = Double.parseDouble(ramEle.getText());
			hddL = Double.parseDouble(hddEle.getText());
//			System.out.println(cpuL+","+ramL);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.HostCPU = cpuL;
		this.HostRam = ramL;
		this.HostHdd = hddL;
	}
	/**
	 *  类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
	 * @author huangpeng
	 *
	 */
	private static class LiminalHolder{
		//静态初始化器，由JVM来保证线程安全
		private static Liminal lm = new Liminal();
	}
	public static Liminal getLiminal() {
		return LiminalHolder.lm;
	}
	public  double getHostCPULiminal(){
		return HostCPU;
	}
	public void setHostCPULiminal(double HostCPU){
		this.HostCPU = HostCPU;
	}
	
	public  double getHostRamLiminal(){
		return HostRam;
	}
	public  void setHostRamLiminal(double HostRam){
		this.HostRam = HostRam;
	}
	public  double getHostHddLiminal(){
		return HostHdd;
	}
	public  void setHostHddLiminal(double HostHdd){
		this.HostHdd = HostHdd;
	}
	public void printInfo(){
		System.out.println(this.HostCPU+","+this.HostRam+","+this.HostHdd);
	}
	/*public static double getConCPULiminal(){
		return ContainerCPU;
	}
	public static void setConCPULiminal(double ConCPU){
		Liminal.ContainerCPU = ConCPU;
	}
	
	public static double getConRamLiminal(){
		return ContainerRam;
	}
	public static void setConRamLiminal(double ConRam){
		Liminal.ContainerRam = ConRam;
	}*/
	public static void main(String[] args){
		Liminal l1 = Liminal.getLiminal();
		Liminal l2 = Liminal.getLiminal();
		l1.printInfo();
		l2.printInfo();
		if (l1 == l2) {
			System.out.println("创建的是同一个实例");
		} else {
			System.out.println("创建的不是同一个实例");
		}
		l1.setHostCPULiminal(200);
		l1.printInfo();
		l2.printInfo();
	}
}
