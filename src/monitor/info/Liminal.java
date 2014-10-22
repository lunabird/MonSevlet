package monitor.info;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

/**
 * ������ֵ�࣬�洢�û��趨����ֵ��Ϣ
 * @author huangpeng
 *
 */
public class Liminal {
	//Ĭ����ֵ
	double HostCPU;
	double HostRam;
	double HostHdd;
	
	private Liminal() {
		// ע�����ĵ�IP��ַ �Լ� �����˿�
		double cpuL = 0;
		double ramL = 0;
		double hddL = 0;
		Document doc;
		try {
			doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// �õ����ڵ�
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
	 *  �༶���ڲ��࣬Ҳ���Ǿ�̬�ĳ�Աʽ�ڲ��࣬���ڲ����ʵ�����ⲿ���ʵ��û�а󶨹�ϵ������ֻ�б����õ�ʱ�Ż�װ�أ��Ӷ�ʵ�����ӳټ��ء�
	 * @author huangpeng
	 *
	 */
	private static class LiminalHolder{
		//��̬��ʼ��������JVM����֤�̰߳�ȫ
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
			System.out.println("��������ͬһ��ʵ��");
		} else {
			System.out.println("�����Ĳ���ͬһ��ʵ��");
		}
		l1.setHostCPULiminal(200);
		l1.printInfo();
		l2.printInfo();
	}
}
