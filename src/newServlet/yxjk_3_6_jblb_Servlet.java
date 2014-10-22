package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.6�����б�
 * @author huangpeng
 *
 */
public class yxjk_3_6_jblb_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_6_jblb_Servlet() {
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

		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");
		
		TCPClient c = new TCPClient();

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			//3.6.1�����б�
			ArrayList<String[]> alarmList = c.receiveAlarmInfoList();
			ArrayList<String[]> alarmListPageUp = new ArrayList<String[]>();
			//Page up start ~
			int start = Integer.valueOf(startnumber);
			int servicenum = Integer.valueOf(itemnumber);
			// int length = al.size() - ((pagenum - 1) * servicenum);
			int length = alarmList.size() - start;
			if (servicenum > length)
				servicenum = length;
			int end = start + servicenum - 1;
			for (int i = start; i <= end; i++) {
				// service.add(al.get(i + servicenum * (pagenum - 1)));
				alarmListPageUp.add(alarmList.get(i));
			}
			//Page up end ~
			if(alarmListPageUp.isEmpty()){
				json.put("alarmTable", null);
				System.out.println("3_6_jblb_Servlet:�����ݿ��õ��ľ����б�Ϊ�գ�����");
			}else{
				json.put("alarmTable", alarmListPageUp);
				System.out.println("3_6_jblb_json:"+json);
			}			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(json);
		out.flush();
		out.close();
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
		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");
		
		TCPClient c = new TCPClient();

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			//3.6.1�����б�
			ArrayList<String[]> alarmList = c.receiveAlarmInfoList();
			ArrayList<String[]> alarmListPageUp = new ArrayList<String[]>();
			//Page up start ~
			int start = Integer.valueOf(startnumber);
			int servicenum = Integer.valueOf(itemnumber);
			// int length = al.size() - ((pagenum - 1) * servicenum);
			int length = alarmList.size() - start;
			if (servicenum > length)
				servicenum = length;
			int end = start + servicenum - 1;
			for (int i = start; i <= end; i++) {
				// service.add(al.get(i + servicenum * (pagenum - 1)));
				alarmListPageUp.add(alarmList.get(i));
			}
			//Page up end ~
			if(alarmListPageUp.isEmpty()){
				json.put("alarmTable", null);
				System.out.println("3_6_jblb_Servlet:�����ݿ��õ��ľ����б�Ϊ�գ�����");
			}else{
				json.put("alarmTable", alarmListPageUp);
				System.out.println("3_6_jblb_json:"+json);
			}			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(json);
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
