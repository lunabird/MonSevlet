package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
/**
 * 3.3服务详情
 * 内容：
 * 		3.3.1单个服务平均访问量
 * 		3.3.2单个服务平均响应时间
 * @author huangpeng
 *
 */
public class yxjk_3_3_12_fwxq_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_3_12_fwxq_Servlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//获得request 相关信息  ，该Request包含两个参数，一个是要请求的图表的名称“param1”，另一个是要请求的是哪个服务的图表“serviceID”。
        String Param = request.getParameter("param1");  
		TCPClient c = new TCPClient();

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			// 从前端页面获取 服务的服务名称
			String serviceID = request.getParameter("serviceID");
			// System.out.println("3.3.1和3.3.2 accept serviceID: " + serviceID);

			JSONObject json = new JSONObject();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式
			Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
			String nowstr = String.valueOf(now.getTime());
			String[] str = new String[2];
			str[0] = serviceID;
			str[1] = nowstr;
			long timeLong = System.currentTimeMillis();
			if(Param.equals("SingleServiceCallQuantity")){
				// 3.3.1单个服务平均访问量
				int[] list1 = c.sendSingleServiceCallTimesRequest(str);
				// int[] list1 = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18 };
				json.put("SingleServiceCallQuantity", list1);
				// 时间点坐标
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
				for (int i = 0; i < 10; i++) {//每次减去1分钟
					timeLong -= 60000;
					Date dt = new Date(timeLong);  
					time[i] = df.format(dt);
				}
				json.put("ordTime", time);
			}else if(Param.equals("SingleServiceRunTime")){
				// 3.3.2单个服务平均响应时间
				double[] list2 = c.sendSingleServiceRunTimeRequest( str);
				// double[] list2 = { 33.3, 25.1, 14.0, 12.5, 2.1, 3.0 };
				json.put("SingleServiceRunTime", list2);
				String[] time1 = new String[6];
				// int curMin = now.getMinutes();
				timeLong = System.currentTimeMillis();
				for (int i = 0; i < 6; i++) {//每次减去10分钟
					timeLong -= 600000;
					Date dt = new Date(timeLong);  
					time1[i] = df.format(dt);
				}
				json.put("ordTime1", time1);
			}
			out.println(json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		TCPClient c = new TCPClient();

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			// 从前端页面获取 服务的服务名称
			String serviceID = request.getParameter("serviceID");
//			System.out.println("3.3.1和3.3.2 accept serviceID: " + serviceID);
			JSONObject json = new JSONObject();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式
			Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
			String nowstr = String.valueOf(now.getTime());
			String[] str = new String[2];
			str[0] = serviceID;
			str[1] = nowstr;
			// 3.3.1单个服务平均访问量
			int[] list1 = c.sendSingleServiceCallTimesRequest(str);
			// int[] list1 = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18 };
			json.put("SingleServiceCallQuantity", list1);

			// 时间点坐标
			String[] time = new String[10];
			// int curMin = now.getMinutes();
			SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
			long timeLong = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {//每次减去1分钟
				timeLong -= 60000;
				Date dt = new Date(timeLong);  
				time[i] = df.format(dt);
			}
			json.put("ordTime", time);
			// 3.3.2单个服务平均响应时间
			double[] list2 = c.sendSingleServiceRunTimeRequest( str);
			// double[] list2 = { 33.3, 25.1, 14.0, 12.5, 2.1, 3.0 };
			json.put("SingleServiceRunTime", list2);
			String[] time1 = new String[6];
			// int curMin = now.getMinutes();
			timeLong = System.currentTimeMillis();
			for (int i = 0; i < 6; i++) {//每次减去10分钟
				timeLong -= 600000;
				Date dt = new Date(timeLong);  
				time1[i] = df.format(dt);
			}
			json.put("ordTime1", time1);
			System.out.println("yxjk_3_3_12_fwxq_json:" + json);
			out.println(json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("");
		out.flush();
		out.close();
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
