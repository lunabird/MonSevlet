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
 * 3.2����������棨ѡ���Ϸ���ҵ�������Ժ���ʾ��
 * ���ݣ�
 * 		3.2.2�쳣����
 * 		3.2.3���������ͳ�ƣ�
 * 		3.2.4�������ʱ��ͳ�ƣ�
 * 		3.2.5��ǰ���Ծ�ķ�����ʣ�
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
		//���request �����Ϣ  ����Request��������������һ����Ҫ�����ͼ������ơ�param1������һ����Ҫ��������ĸ�ҵ�����͵�ͼ��sysName����
        String Param = request.getParameter("param1");  
//        String sysNameParam = request.getParameter("param2");//��Ҫ�������Դ��ҵ����������
		TCPClient c = new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			JSONObject json = new JSONObject();
			// ��ǰ��ҳ���ȡ�û�ѡ���ϵͳ����
			String sysName = request.getParameter("sysName");
			// ��չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �����ʽ
			Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
			String nowstr = String.valueOf(now.getTime());
			String[] str = new String[2];
			str[0] = sysName;
			str[1] = nowstr;//String.valueOf(1407058048193L);
			int[] serviceCalTimesList = c.sendServiceCalInfoReq(str);
			

			// 3.2.2�쳣����,����
			
			if(Param.equals("serviceCallQuantity")){
				// 3.2.3���������ͳ�ơ��������ǵ�ǰʱ�䵹��ʮ����
				
				// int[] serviceCalTimesList = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18
				// };//For test
				json.put("serviceCallQuantity", serviceCalTimesList);
				// ʱ������� 10����
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {// ÿ�μ�ȥ1����
					timeLong -= 60000;
					Date dt = new Date(timeLong);
					time[i] = df1.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);
			}else if(Param.equals("serviceRunTime")){
				// 3.2.4�������ʱ��ͳ�ơ��������Ǵ����ݿ�ͳ�Ƶķ�������ʱ�����䡣��1Сʱ�ڣ�.չʾ��������з�����һ��ʱ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
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
				// 3.2.5��ǰ���Ծ�ķ�����ʡ�
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
			// ��ǰ��ҳ���ȡ�û�ѡ���ϵͳ����
			String sysName = request.getParameter("sysName");
			if (sysName.isEmpty()) {
				System.out.println("3.2�����ȡ����ϵͳ����Ϊ�գ�");
			} else {
				// 3.2.2�쳣����,����

				// 3.2.3���������ͳ�ơ��������ǵ�ǰʱ�䵹��ʮ����	��չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// �����ʽ
				Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
				String nowstr = String.valueOf(now.getTime());
				String[] str = new String[2];
				str[0] = sysName;
				str[1] = nowstr;//String.valueOf(1407058048193L);
				int[] serviceCalTimesList = c.sendServiceCalInfoReq(str);
//				int[] serviceCalTimesList = { 12, 21, 2, 15, 6, 8, 11, 5, 4, 18 };//For test
				json.put("serviceCallQuantity", serviceCalTimesList);
				// ʱ������� 10����
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] time = new String[10];
				// int curMin = now.getMinutes();
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {//ÿ�μ�ȥ1����
					timeLong -= 60000;
					Date dt = new Date(timeLong);  
					time[i] = df1.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);
				// 3.2.4�������ʱ��ͳ�ơ��������Ǵ����ݿ�ͳ�Ƶķ�������ʱ�����䡣��1Сʱ�ڣ�.չʾ��������з�����һ��ʱ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
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
				// 3.2.5��ǰ���Ծ�ķ�����ʡ�
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
				// 3.2.6��ȡ����ϵͳ�µ����еķ�����ϸ��Ϣ�б�
//				 ArrayList<String[]> serviceDetailList = TCPClient
//				 .sendServiceDetailInfoListRequest(serviceRegistryIP,
//				 serviceRegistryPort, sysName);
//				ArrayList<String[]> serviceDetailList = new ArrayList<String[]>();
//				String[] ser1 = { "start", "TheAddService", "�ӿ�add����",
//						"Web Service", "�鱨����", "�鱨ϵͳ", "����һ���ӷ����������", "�����Ƿ�������" };
//				String[] ser2 = { "start", "TheShellService", "�ӿ�shell����",
//						"Web Service", "�鱨����", "�鱨ϵͳ", "����һ��shell���������",
//						"�����Ƿ�������" };
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
