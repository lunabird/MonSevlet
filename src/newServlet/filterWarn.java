package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import Message.XmlUtils;
import Message.toJson;

import monitor.info.TCPClient;

public class filterWarn extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");
		TCPClient c = new TCPClient();
		String type = request.getParameter("type");
		String value = request.getParameter("value");
//		System.out.println("type:"+type+"__value:"+ value);
		ArrayList<String> al = new ArrayList<String>();
		al.add(type);
		al.add(value);
		ArrayList<String[]> filterWarnList = new ArrayList<String[]>();
		ArrayList<String[]> filterWarnListPageUp = new ArrayList<String[]>();
		JSONObject json = new JSONObject();
		try {

//			System.out.println(serviceRegistryIP + "   " + serviceRegistryPort);
			filterWarnList = c.filterWarn(al);
			//page up start ~
			int start = Integer.valueOf(startnumber);
			int servicenum = Integer.valueOf(itemnumber);

			// int length = al.size() - ((pagenum - 1) * servicenum);
			int length = filterWarnList.size() - start;

			if (servicenum > length)
				servicenum = length;

			int end = start + servicenum - 1;

			for (int i = start; i <= end; i++) {
				// service.add(al.get(i + servicenum * (pagenum - 1)));
				filterWarnListPageUp.add(filterWarnList.get(i));
			}
			//page up end ~
			json.put("alarmTable", filterWarnListPageUp);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String[] Keys = {"warnid", "time", "type",
		// "content","ip","container","status"};
		// String name = "warnlist";
		// toJson j = new toJson();
		// String nodeliststring = new String();
		// try {
		// nodeliststring = j.toJsonObject(name, Keys, filterWarnList);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// System.out.println("nodeinfo:" + nodeliststring);
		System.out.println("zf_filterWarn_nodeliststring_json:" + json);
		out.print(json);
		out.flush();

		out.close();
	}

}
