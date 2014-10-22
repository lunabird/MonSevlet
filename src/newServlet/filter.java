package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONArray;


public class filter extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TCPClient c = new TCPClient();
		String type = request.getParameter("type");
		HashSet<String> hs = new HashSet<String>();
		try {
			hs = c.filter(type);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray filterarray = new JSONArray();
		for (String str : hs) {
			filterarray.add(str);
		}
//		String filterstring = new String();
//		for (String str : hs) {
//			filterstring += str;
//			filterstring += "@";
//		}
//		filterstring = filterstring.substring(0, filterstring.length() - 1);

		System.out.println("zf_filter_json:" + filterarray);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(filterarray);
		out.flush();
		out.close();

	}

}
