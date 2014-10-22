package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Message.toJson;


public class filterServiceServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");
		
		TCPClient c = new TCPClient();
		String type = request.getParameter("type");
		String value = request.getParameter("value");
//		System.out.println("type: " + type);
//		System.out.println("value: " + value);

		ArrayList<String> al = new ArrayList<String>();
		al.add(type);
		al.add(value);
		ArrayList<String[]> filterServiceList = new ArrayList<String[]>();
		ArrayList<String[]> service = new ArrayList<String[]>();
		ArrayList<String[]> serviceCopyList = new ArrayList<String[]>();
		ArrayList<String[]> messagelist = new ArrayList<String[]>();
		try {
			filterServiceList = c.filterService(al);
			serviceCopyList = c.serviceCopyList(); // 获取带副本的服务列表
			messagelist = c.interfaceInfo();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int start = Integer.valueOf(startnumber);
		int servicenum = Integer.valueOf(itemnumber);
		int length = filterServiceList.size() - start;
		if (servicenum > length)
			servicenum = length;

		int end = start + servicenum - 1;

		for (int i = start; i <= end; i++) {
			// service.add(al.get(i + servicenum * (pagenum - 1)));
			service.add(filterServiceList.get(i));
		}
		String[] Keys = { "serviceid", "servicename", "version", "description",
				"servicetype", "businesstype", "copynum", "concurrency",
				"owersystem" ,"maxcopynum"};
		String name = "servicelist";

		String[] messageKeys = { "serviceid", "servicemessage" };
		String messagename = "messagelist";

		String[] statustKeys = { "serviceid", "ip", "status" };
		String statusname = "statuslist";

		toJson j = new toJson();
		JSONObject servicelistjso = new JSONObject();
		JSONObject statuslistjso = new JSONObject();
		JSONObject messagelistjso = new JSONObject();
		try {
			servicelistjso = j.toJsonObject(name, Keys, service);
			statuslistjso = j.toJsonObject(statusname, statustKeys,
					serviceCopyList);
			messagelistjso = j.toJsonObject(messagename, messageKeys,
					messagelist);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<JSONObject> info = new ArrayList<JSONObject>();
		info.add(servicelistjso);
		info.add(statuslistjso);
		info.add(messagelistjso);

		JSONArray jsonarray = j.toJsonArray(info);
//		String jsonString = new String();
//		jsonString = serviceliststring + "@" + messageliststring + "@"
//				+ statusliststring;
//
//		System.out.println("zf_filterServiceServlet_json: " + jsonString);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(jsonarray);
		out.flush();

		out.close();

	}

}
