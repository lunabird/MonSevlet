package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import monitor.info.TCPClient;
import monitor.info.ToPolgyJsonTest;
import net.sf.json.JSONObject;

import Message.XmlUtils;

public class ToPolgyServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		TCPClient c = new TCPClient();
		String serviceid=(String)session.getAttribute("serviceid");
		String servicename=(String)session.getAttribute("servicename");
		String systemname=(String)session.getAttribute("systemname");
		
		/*System.out.println("serviceid:" +serviceid);
		System.out.println("servicename:" +servicename);
		System.out.println("systemname:" +systemname);*/
		ArrayList<String[]> copyIPList = new ArrayList<String[]>();
		
		try {
			copyIPList = c.sendServiceCopyRequest(serviceid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jo = ToPolgyJsonTest.buildTopologyForSingleService(systemname,serviceid,servicename,copyIPList);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		System.out.println("zf_ToPolgyServlet_json:" + jo.toString());
		out.print(jo.toString());
		
		out.flush();
		out.close();
		
		
	}

}
