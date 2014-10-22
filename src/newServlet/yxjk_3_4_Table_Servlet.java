package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
import Message.toJson;
/**
 * 3.4����ڵ�����ĵ����ı�
 * @author huangpeng
 *
 */
public class yxjk_3_4_Table_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_4_Table_Servlet() {
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

		doPost(request, response);
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
		
//		String startnumber = request.getParameter("startnumber");
//		String itemnumber = request.getParameter("itemnumber");
		// response.setContentType("application/json;charset=UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		TCPClient c = new TCPClient();

		
		String nodeIP = request.getParameter("nodeIP");
		JSONArray jarr = new JSONArray();
		PrintWriter out = response.getWriter();
		// String[]���ֵ��˳�������� �����������ڽڵ�IP���������ͣ�ҵ�����ͣ�����ϵͳ����,����ID
		ArrayList<String[]> serviceWithCopy = new ArrayList<String[]>();
		ArrayList<String[]> serviceWithCopyPageUp = new ArrayList<String[]>();
		try {
			serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			ArrayList<String> theServiceList = new ArrayList<String>();// ѡ�������IP�ϵ����еķ������б�
			//Page up start ~
//			int start0 = Integer.valueOf(startnumber);
//			int servicenum0 = Integer.valueOf(itemnumber);
//			// int length = al.size() - ((pagenum - 1) * servicenum);
//			int length0 = serviceWithCopy.size() - start0;
//			if (servicenum0 > length0)
//				servicenum0 = length0;
//			int end0 = start0 + servicenum0 - 1;
//			for (int i = start0; i <= end0; i++) {
//				// service.add(al.get(i + servicenum * (pagenum - 1)));
//				serviceWithCopyPageUp.add(serviceWithCopy.get(i));
//			}
			//Page up end ~
			for (int i = 0; i < serviceWithCopy.size(); i++) {
				if (serviceWithCopy.get(i)[1].equals(nodeIP)) {
					JSONObject json = new JSONObject();
					theServiceList.add(serviceWithCopy.get(i)[5]);// ID

					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// �����ʽ
					Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
					String nowstr = String.valueOf(now.getTime());
					String[] str = new String[2];
					str[0] = serviceWithCopy.get(i)[5];
					str[1] = nowstr;
					// 3.3.1��������ƽ����������10����
					int[] list1 = c.sendSingleServiceCallTimesRequest(str);
					json.put("visitQuantity", list1);
					// 3.3.2��������ƽ����Ӧʱ�䣬6����
					double[] list2 = c.sendSingleServiceRunTimeRequest(str);
					json.put("SingleServiceRunTime", list2);
					jarr.add(json);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String[]> servicelist = new ArrayList<String[]>();
		ArrayList<String[]> messagelist = new ArrayList<String[]>();
		// ArrayList<String[]> flowlist = new ArrayList<String[]>();
		ArrayList<String[]> service = new ArrayList<String[]>();
		ArrayList<String[]> al = new ArrayList<String[]>();

		ArrayList<String[]> serviceCopyList = new ArrayList<String[]>();
		// ArrayList<String[]> flowCopyList = new ArrayList<String[]>();
		// ArrayList<String[]> al2 = new ArrayList<String[]>();

		JSONObject serviceliststring = new JSONObject();
		JSONObject statusliststring = new JSONObject();
		JSONObject messageliststring = new JSONObject();

		

		try {
			// ��ȡ���������ķ����б�
			servicelist = c.serviceList();
//			System.out.println("servicelist" + servicelist);
			serviceCopyList = c.serviceCopyList(); // ��ȡ�������ķ����б�
			messagelist = c.interfaceInfo();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Page up start ~
//		int start = Integer.valueOf(startnumber);
//		int servicenum = Integer.valueOf(itemnumber);
//		// int length = al.size() - ((pagenum - 1) * servicenum);
//		int length = servicelist.size() - start;
//		if (servicenum > length)
//			servicenum = length;
//		int end = start + servicenum - 1;
//		for (int i = start; i <= end; i++) {
//			// service.add(al.get(i + servicenum * (pagenum - 1)));
//			service.add(servicelist.get(i));
//		}
		//Page up end ~
		String[] Keys = { "serviceid", "servicename", "version", "description",
				"servicetype", "businesstype", "copynum", "concurrency",
				"owersystem" ,"maxcopynum"};
		String name = "servicelist";

		String[] messageKeys = { "serviceid", "servicemessage" };
		String messagename = "messagelist";

		String[] statustKeys = { "serviceid", "ip", "status" };
		String statusname = "statuslist";

		toJson j = new toJson();
		try {
			serviceliststring = j.toJsonObject(name, Keys, servicelist);
			statusliststring = j.toJsonObject(statusname, statustKeys,
					serviceCopyList);
			messageliststring = j.toJsonObject(messagename, messageKeys,
					messagelist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<JSONObject> info = new ArrayList<JSONObject>();
		info.add(serviceliststring);
		info.add(statusliststring);
		info.add(messageliststring);
		

		JSONArray jsonarray = j.toJsonArray(info);
		jsonarray.add(jarr);
		

		System.out.println("yxjk_3_4_Table_json:" + jsonarray);
		out.print(jsonarray);
		out.flush();
		out.close();
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
