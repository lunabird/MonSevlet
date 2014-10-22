package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Message.toJson;

public class nodeHarddisk extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		TCPClient c = new TCPClient();
		toJson tojson = new toJson();
		String nodeip = request.getParameter("nodeip");
//		System.out.println("zf_nodeHarddisk_nodeIP:"+nodeip);
		ArrayList<String[]> al = new ArrayList<String[]>();
		try {
			al = c.nodeHarddisk(nodeip);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String key = "nodeharddisk";
		String[] names = { "harddisk", "time" };

		JSONObject jsonString = new JSONObject();
		try {
			jsonString = tojson.toJsonObject(key, names, al);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("zf_nodeHarddisk_json:"+jsonString);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(jsonString);
		out.flush();
		out.close();
	}

}
