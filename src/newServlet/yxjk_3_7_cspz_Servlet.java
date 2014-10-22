package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.CurrentInfo;
import monitor.info.Host;
import monitor.info.Liminal;
import monitor.info.MainClass;
import monitor.info.TCPClient;
import monitor.info.UDPClient;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.7��������
 * @author huangpeng
 *
 */
public class yxjk_3_7_cspz_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_7_cspz_Servlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ע�����ĵ�IP��ַ �Լ� �����˿�
		String serviceRegistryIP = "";
		int serviceRegistryPort = 0;
		Document doc;
		try {
			doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// �õ����ڵ�
			Element ipEle = root.element("center_IP");
			Element center_PortEle = root.element("center_Port");
			serviceRegistryIP = ipEle.getText();
			serviceRegistryPort = Integer.parseInt(center_PortEle.getText());
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String sampleTime = request.getParameter("sampleTime");
		String responseTime = request.getParameter("responseTime");
		String concurrentNum  = request.getParameter("concurrentNum");
		String cpuValue = request.getParameter("cpuValue");
		String ramValue = request.getParameter("ramValue");
		String hddValue = request.getParameter("hddValue");
		
		//�������������ֵ
		Liminal l = Liminal.getLiminal();//Liminal��ʹ���˵���ģʽ��Ҫ��������
		l.setHostCPULiminal(Double.parseDouble(cpuValue));
		l.setHostRamLiminal(Double.parseDouble(ramValue));
		l.setHostHddLiminal(Double.parseDouble(hddValue));
		//�޸������ļ�config.xml�������Ļ��´β���ʱĬ��ֵ���Ǹղ��趨��ֵ��
		try {
			XmlUtils.modifyXml("cpu", cpuValue);
			XmlUtils.modifyXml("ram", ramValue);
			XmlUtils.modifyXml("hdd", hddValue);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//������ʱ�����ַ�����������
		Document doca = null;
		try {
			doca = XmlUtils.getDocument();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doca.getRootElement();// �õ����ڵ�
		Element pathEle = root.element("filePath");
		String curInfoPath = pathEle.getText();
		CurrentInfo curInfo = MainClass.buildCurInfo(curInfoPath);//��ȡ��curInfo�ļ�
		
		if(curInfo.equals(null)){
			System.out.println("curInfo Ϊ��ֵ����");
		}else{
//			curInfo.printCurInfo();
			Set<Host> hoSet =  curInfo.getHoSet();
			Object[] hostArray = hoSet.toArray();
			ArrayList<String> hostIPList = new ArrayList<String>();
			for(int i=0;i<hostArray.length;i++){
				Host h = (Host)hostArray[i];
				hostIPList.add(h.getIP());
			}
			UDPClient ec = new UDPClient();
	    	ec.sendSampleTime(10,hostIPList);
		}
		//������������Ӧʱ��
		//�����ݿ����������ñ�,������Ӧʱ�䣬���������ֵ���򱨾�
		
		
		
		System.out.println("yxjk_3_7_cspz_Servlet:"+sampleTime+","+responseTime+","+concurrentNum+","+cpuValue+","+ramValue+","+hddValue);
		out.flush();
		out.close();
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
