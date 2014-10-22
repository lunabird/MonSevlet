package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import monitor.info.ToPolgyJsonTest;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

public class yxjk_3_3_fwxq_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_3_fwxq_Servlet() {
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

		TCPClient c = new TCPClient();
		 
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			// 从前端页面获取用户选择的系统名称 和 该服务的服务名称
			String sysName = request.getParameter("sysName");
			String serviceName = request.getParameter("serviceName");
			String serviceID = request.getParameter("serviceID");
			if (sysName.isEmpty()) {
				System.out.println("3.3界面获取到的系统名称为空！");
			} else if(serviceName.isEmpty()) {
				System.out.println("3.3界面获取到的服务名称为空！");
			} else if(serviceID.isEmpty()) {
				System.out.println("3.3界面获取到的服务ID为空！");
			}else{
				//3.3.3服务拓扑结构
				ArrayList<String[]> copyIPList = c.sendServiceCopyRequest(serviceID);

//				ArrayList<String[]> copyIPList = new ArrayList<String[]>();
//				for(int i=0;i<3;i++){
//					String[] str = new String[2];
//					str[0] = "ho-PC"+i;
//					str[1] = "192.168.0.20"+i;					
//					copyIPList.add(str);					
//				}
			    //构造拓扑json结构
				JSONObject jj = ToPolgyJsonTest.buildTopologyForSingleService(sysName,serviceID,serviceName,copyIPList);
				out.println(jj);
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
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
