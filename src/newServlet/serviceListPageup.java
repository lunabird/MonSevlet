package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Message.XmlUtils;
import Message.toJson;

public class serviceListPageup extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");
		
//		String jsondata = request.getParameter("aoData");
////		System.out.println("aoData get!!");
//
//		JSONArray jsonarray = JSONArray.fromObject(jsondata);
//		String sEcho = "", iDisplayStart = "", iDisplayLength = "";
//		for (int i = 0; i < jsonarray.size(); i++) {
//			JSONObject obj = (JSONObject) jsonarray.get(i);
//
//			if (obj.get("name").equals("iDisplayStart")) {
//				iDisplayStart = obj.get("value").toString();
////				System.out.println("iDisplayStart: " + iDisplayStart);
//			}
//			if (obj.get("name").equals("iDisplayLength")) {
//				iDisplayLength = obj.get("value").toString();
////				System.out.println("iDisplayLength: " + iDisplayLength);
//			}
//		}

		// ServerNode sn = new ServerNode();
		// int pagenum=Integer.parseInt(request.getParameter("pagenum"));
		// int servicenum=Integer.parseInt(request.getParameter("servicenum"));

		ArrayList<String[]> servicelist = new ArrayList<String[]>();
		ArrayList<String[]> messagelist = new ArrayList<String[]>();
		ArrayList<String[]> service = new ArrayList<String[]>();
		ArrayList<String[]> al = new ArrayList<String[]>();

		ArrayList<String[]> serviceCopyList = new ArrayList<String[]>();
		JSONObject servicelistjso = new JSONObject();
		JSONObject statuslistjso = new JSONObject();
		JSONObject messagelistjso = new JSONObject();

		TCPClient c = new TCPClient();
		
		
		//获取服务类别（例如情报服务，电抗服务）String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型
		/*Object[] sysArray = null;
		try {
			ArrayList<String[]> serviceWithCopy = TCPClient.sendServiceDetailInfoWithCopyRequest(serviceRegistryIP,serviceRegistryPort);
			HashSet<String> sysSet = new HashSet<String>();
			for (int i = 0; i < serviceWithCopy.size(); i++) {
				String sys = serviceWithCopy.get(i)[3];
				sysSet.add(sys);
			}
			sysArray = sysSet.toArray();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String value = sysArray[0].toString();
		ArrayList<String> filterCondition = new ArrayList<String>();
		filterCondition.add("businesstype");
		filterCondition.add(value);
		ArrayList<String[]> filterServiceList = new ArrayList<String[]>();*/
		try {

//			filterServiceList = c.filterService(serviceRegistryIP,
//					serviceRegistryPort, filterCondition);
			// 获取不带副本的服务列表
			servicelist = c.serviceList();
			serviceCopyList = c.serviceCopyList(); // 获取带副本的服务列表
			messagelist = c.interfaceInfo();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		al.addAll(servicelist);
		//page up start ~
		int start = Integer.valueOf(startnumber);
		int servicenum = Integer.valueOf(itemnumber);

		// int length = al.size() - ((pagenum - 1) * servicenum);
		int length = servicelist.size() - start;

		if (servicenum > length)
			servicenum = length;

		int end = start + servicenum - 1;

		for (int i = start; i <= end; i++) {
			// service.add(al.get(i + servicenum * (pagenum - 1)));
			service.add(servicelist.get(i));
		}
		//page up end ~
		
		
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
//			serviceliststring = j.toJsonObject(name, Keys, filterServiceList);
			servicelistjso = j.toJsonObject(name, Keys, service);
			statuslistjso = j.toJsonObject(statusname, statustKeys,
					serviceCopyList);
			messagelistjso = j.toJsonObject(messagename, messageKeys,
					messagelist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(messageliststring);
		ArrayList<JSONObject> info = new ArrayList<JSONObject>();
		info.add(servicelistjso);
		info.add(statuslistjso);
		info.add(messagelistjso);

		JSONArray jsonarray = j.toJsonArray(info);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String jsonString = new String();
		System.out.println("zf_serviceListPageup_json:" + jsonarray);
		out.print(jsonarray);
		out.flush();
		out.close();
	}

}
