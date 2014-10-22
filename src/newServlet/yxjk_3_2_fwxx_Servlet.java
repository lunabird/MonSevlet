package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.2服务详情界面（选择上方的业务类型以后显示）
 * 内容：
 * 		3.2.2异常服务；
 * 		3.2.3服务访问量统计；
 * 		3.2.4服务调用时间统计；
 * 		3.2.5当前最活跃的服务访问；
 * @author huangpeng
 *
 */
public class yxjk_3_2_fwxx_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_2_fwxx_Servlet() {
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
		//获得request 相关信息  ，该Request包含两个参数，一个是要请求的图表的名称“param1”，另一个是要请求的是哪个业务类型的图表“sysName”。
        String Param = request.getParameter("param1");  
//        String sysNameParam = request.getParameter("param2");//需要请求的资源的业务类型名称
		TCPClient c = new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			JSONObject json = new JSONObject();
			// 从前端页面获取用户选择的系统名称
			String sysName = request.getParameter("sysName");
			// 。展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式
			Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
			String nowstr = String.valueOf(now.getTime());
			String[] str = new String[2];
			str[0] = sysName;
			str[1] = nowstr;//String.valueOf(1407058048193L);
			int[] serviceCalTimesList = c.sendServiceCalInfoReq(str);
			

			// 3.2.2异常服务,待定
			
			if(Param.equals("serviceCallQuantity")){
				// 3.2.3服务访问量统计。横坐标是当前时间倒推十分钟
				
				// int[] serviceCalTimesList = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18
				// };//For test
				json.put("serviceCallQuantity", serviceCalTimesList);
				// 时间点坐标 10分钟
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {// 每次减去1分钟
					timeLong -= 60000;
					Date dt = new Date(timeLong);
					time[i] = df1.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);
			}else if(Param.equals("serviceRunTime")){
				// 3.2.4服务调用时间统计。横坐标是从数据库统计的服务运行时间区间。（1小时内）.展示该类别所有服务在一段时间调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
				ArrayList<Object> serviceRunTimeList = c
						.sendServiceRunTimeRequest(str);
				if (serviceRunTimeList != null) {
					int[] theNum = (int[]) serviceRunTimeList.get(0);
					String theStartTime = (String) serviceRunTimeList.get(1);
					String theEndTime = (String) serviceRunTimeList.get(2);
					json.put("serviceRunTime", theNum);
					double timeInterval = (Double.parseDouble(theEndTime) - Double
							.parseDouble(theStartTime)) / 10;
					double[] serviceRunTime_x = new double[11];
					java.text.DecimalFormat ddf = new java.text.DecimalFormat(
							"#.00");
					for (int i = 0; i <= 10; i++) {
						serviceRunTime_x[i] = Double.parseDouble(ddf.format(Double
								.parseDouble(theStartTime) + i * timeInterval));
					}
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				} else {
					int[] nums = new int[10];
					json.put("serviceRunTime", nums);
					double[] serviceRunTime_x = new double[11];
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				}
			}else if(Param.equals("activeServiceList")){
				// 3.2.5当前最活跃的服务访问。
				ArrayList<String[]> activeServiceList = c
						.sendActiveServiceRequest(str);
				json.put("activeServiceList", activeServiceList);
			}
			out.println(json);

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
			JSONObject json = new JSONObject();
			// 从前端页面获取用户选择的系统名称
			String sysName = request.getParameter("sysName");
			if (sysName.isEmpty()) {
				System.out.println("3.2界面获取到的系统名称为空！");
			} else {
				// 3.2.2异常服务,待定

				// 3.2.3服务访问量统计。横坐标是当前时间倒推十分钟	。展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 定义格式
				Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
				String nowstr = String.valueOf(now.getTime());
				String[] str = new String[2];
				str[0] = sysName;
				str[1] = nowstr;//String.valueOf(1407058048193L);
				int[] serviceCalTimesList = c.sendServiceCalInfoReq(str);
//				int[] serviceCalTimesList = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18 };//For test
				json.put("serviceCallQuantity", serviceCalTimesList);
				// 时间点坐标 10分钟
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {//每次减去1分钟
					timeLong -= 60000;
					Date dt = new Date(timeLong);  
					time[i] = df1.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);
				// 3.2.4服务调用时间统计。横坐标是从数据库统计的服务运行时间区间。（1小时内）.展示该类别所有服务在一段时间调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
				ArrayList<Object> serviceRunTimeList = c.sendServiceRunTimeRequest( str);
//				int[] serviceRunTimeList = { 2, 1, 2, 5, 6, 8, 1, 0, 0, 0 };//For test
				if(serviceRunTimeList!=null){
					int[] theNum = (int[]) serviceRunTimeList.get(0);
					String theStartTime = (String)serviceRunTimeList.get(1);
					String theEndTime = (String)serviceRunTimeList.get(2);
					json.put("serviceRunTime", theNum);
					double timeInterval = (Double.parseDouble(theEndTime)-Double.parseDouble(theStartTime))/10;
					double[] serviceRunTime_x = new double[11];
					java.text.DecimalFormat ddf = new java.text.DecimalFormat("#.00");
					for(int i=0;i<=10;i++){
						serviceRunTime_x[i] = Double.parseDouble(ddf.format(Double.parseDouble(theStartTime) +i*timeInterval));
					}
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				}else{
					int[] nums = new int[10];
					json.put("serviceRunTime", nums);
					double[] serviceRunTime_x = new double[11];
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				}
				// 3.2.5当前最活跃的服务访问。
				ArrayList<String[]> activeServiceList = c.sendActiveServiceRequest(str);
				//For test
//				ArrayList<String[]> activeServiceList = new ArrayList<String[]>();
//				for (int i = 0; i < 10; i++) {
//					String[] strArray = new String[2];
//					strArray[0] = "service" + i;
//					strArray[1] = 15 - i + "";
//					activeServiceList.add(strArray);
//				}//For test
				json.put("activeServiceList", activeServiceList);
				// 3.2.6获取到该系统下的所有的服务详细信息列表
//				 ArrayList<String[]> serviceDetailList = TCPClient
//				 .sendServiceDetailInfoListRequest(serviceRegistryIP,
//				 serviceRegistryPort, sysName);
//				ArrayList<String[]> serviceDetailList = new ArrayList<String[]>();
//				String[] ser1 = { "start", "TheAddService", "接口add方法",
//						"Web Service", "情报服务", "情报系统", "这是一个加法服务的描述", "这里是服务详情" };
//				String[] ser2 = { "start", "TheShellService", "接口shell方法",
//						"Web Service", "情报服务", "情报系统", "这是一个shell服务的描述",
//						"这里是服务详情" };
//				serviceDetailList.add(ser1);
//				serviceDetailList.add(ser2);
//				json.put("Table", serviceDetailList);
				System.out.println("yxjk_3_2_fwxx_json:"+json);
				out.println(json);
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
