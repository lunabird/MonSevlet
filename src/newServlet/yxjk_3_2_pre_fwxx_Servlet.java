package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

import monitor.info.TCPClient;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
/**
 * 3.2服务详情界面（首次载入时显示）
 * 内容：
 * 		3.2.2异常服务；
		3.2.3服务访问量统计；
		3.2.4服务调用时间统计；
		3.2.5当前最活跃的服务访问；
 * @author huangpeng
 *
 */
public class yxjk_3_2_pre_fwxx_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_2_pre_fwxx_Servlet() {
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
			JSONObject json = new JSONObject();
			// 3.2.1服务类别（例如情报服务，电抗服务）String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			HashSet<String> sysSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String sys = serviceWithCopy.get(i)[3];
				sysSet.add(sys);
			}
			Object[] sysArray = sysSet.toArray();

//			String[] sysArray = {"情报系统","雷达系统","海洋系统","航空系统"};
			json.put("sysList", sysArray);
				
			if(!(sysArray.length<1)){
				// 3.2.2异常服务,待定
				// 3.2.3服务访问量统计
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 定义格式
				Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
				String nowstr = String.valueOf(now.getTime());
				String[] str = new String[2];
				str[0] = sysArray[0].toString();
				str[1] = nowstr;//String.valueOf(1407058048193L);
				int[] serviceCalTimesList = c.sendServiceCallInfoRequest(str[1]);
//				int[] serviceCalTimesList = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18 };//For test
				json.put("serviceCallQuantity", serviceCalTimesList);
				// 时间点坐标 10分钟
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {// 每次减去1分钟
					timeLong -= 60000;
					Date dt = new Date(timeLong);
					time[i] = df.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);
				// 3.2.4服务调用时间统计
				ArrayList<Object> serviceRunTimeList = c.sendServiceRunTimeRequest( str[1]);
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
					json.put("serviceRunTime_ordTime", "");
				}
				// 3.2.5当前最活跃的服务访问
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
			}else{
				System.out.println("yxjk_3_2_pre_fwxx_Servlet:从数据库获取的业务类型列表为空！");
			}					
			System.out.println("yxjk_3_2_pre_fwxx_json:"+json);
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
