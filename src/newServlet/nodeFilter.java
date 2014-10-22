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

import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Message.XmlUtils;
import Message.toJson;



public class nodeFilter extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");

		String startnumber = request.getParameter("startnumber");
		String itemnumber = request.getParameter("itemnumber");

		TCPClient sn = new TCPClient();
		String type = request.getParameter("type");
		String value = request.getParameter("value");
		ArrayList<String> al = new ArrayList<String>();
		al.add(type);
		al.add(value);
		ArrayList<String[]> filterNodeList = new ArrayList<String[]>();
		ArrayList<String[]> node = new ArrayList<String[]>();
		JSONArray jsonArray = new JSONArray();
		try {
			filterNodeList = sn.nodeFilter(al);
			//Page up start ~
			int start = Integer.valueOf(startnumber);
			int servicenum = Integer.valueOf(itemnumber);
			int length = filterNodeList.size() - start;
			if (servicenum > length)
				servicenum = length;

			int end = start + servicenum - 1;

			for (int i = start; i <= end; i++) {
				// service.add(al.get(i + servicenum * (pagenum - 1)));
				node.add(filterNodeList.get(i));
			}
			//Page up end ~
			for (int i = 0; i < node.size(); i++) {
				JSONObject json1 = new JSONObject();
				json1.put("hostName", node.get(i)[0]);
				json1.put("description", node.get(i)[1]);
				json1.put("hostip", node.get(i)[2]);
				json1.put("mac", node.get(i)[3]);
				json1.put("os", node.get(i)[4]);
				json1.put("location", node.get(i)[5]);
				json1.put("type", node.get(i)[6]);
				json1.put("status", node.get(i)[7]);
				ArrayList<String[]> al1 = new ArrayList<String[]>();
				al1 = sn.nodeCpuMem(node.get(i)[2]);
				String cpuStr = "";
				String memoryStr = "";
				for (int j = 0; j < al1.size(); j++) {
					cpuStr += al1.get(j)[0].split("%")[0] + ",";
					memoryStr += al1.get(j)[1].split("%")[0] + ",";
				}
				if (al1.isEmpty()) {
					json1.put("cpu", "");
					json1.put("memory", "");
				} else {
					json1.put("cpu", cpuStr.substring(0, cpuStr.length() - 1));
					json1.put("memory",
							memoryStr.substring(0, memoryStr.length() - 1));
				}
				ArrayList<String[]> hddal = sn.nodeHarddisk( node.get(i)[2]);

				String hardStr = "";
				for (int j = 0; j < hddal.size(); j++) {
					hardStr += hddal.get(j)[0].split("%")[0] + ",";
				}
				if (hddal.isEmpty()) {
					json1.put("hardDisk", "");
				} else {
					json1.put("hardDisk",hardStr.substring(0, hardStr.length() - 1));
				}
				jsonArray.add(json1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String[] Keys = { "hostname", "description", "hostip", "mac", "os",
				"location", "type", "status" };
		String name = "nodelist";
		toJson j = new toJson();
		JSONObject nodeliststring = new JSONObject();
		try {
			nodeliststring = j.toJsonObject(name, Keys, node);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		System.out.println("zf_nodeFilter_json:" + jsonArray);

		out.print(jsonArray);
		out.flush();

		out.close();
	}

}
