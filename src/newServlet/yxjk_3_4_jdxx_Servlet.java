package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.CurrentInfo;
import monitor.info.Host;
import monitor.info.MainClass;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.4节点信息
 * 
 * @author huangpeng
 *
 */
public class yxjk_3_4_jdxx_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_4_jdxx_Servlet() {
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

		// 注册中心的IP地址 以及 监听端口
		String serviceRegistryIP = "";
		int serviceRegistryPort = 0;
		Document doc;
		try {
			doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
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
		/*
		JSONObject json = new JSONObject();
		//读取文件，获得currentInfo
		String Path = XmlUtils.filePath;
		CurrentInfo curInfo = MainClass.buildCurInfo(Path+"\\curInfo.txt");//获取curInfo
		curInfo.printCurInfo();
		Set<Host> hoSet =  curInfo.getHoSet();
		Object[] hostArray = hoSet.toArray();
		//3.4.1节点数量
		json.put("NodeAmount", hostArray.length);
		//3.4.2节点详细列表
		JSONArray ja1 = new JSONArray();
		for(int i=0;i<hostArray.length;i++){
			JSONObject hjson = new JSONObject();
			Host h = (Host)hostArray[i];
			hjson.put("hostName",h.getHostName());
			hjson.put("type",h.getType());
			hjson.put("IP",h.getIP());
			hjson.put("mac",h.getMAC());
			hjson.put("OS",h.getOS());
			hjson.put("cpu",h.getCPU());
			hjson.put("ram",h.getRAM());
			hjson.put("hdd",h.getHdd());
			ja1.add(hjson);
		}
		json.put("Table", ja1);
		out.println(json);
		*/
		out.flush();
		out.close();
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
