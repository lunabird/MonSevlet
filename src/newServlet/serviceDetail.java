package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

public class serviceDetail extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		TCPClient c = new TCPClient();
		String nodeip = request.getParameter("nodeip");
//		System.out.println(nodeip);
		ArrayList<String> al = new ArrayList<String>();
		try {
			al = c.serviceDetail();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String servicestr = new String();
		for (String str : al) {
			servicestr += str;
			servicestr += "@";
		}
		servicestr = servicestr.substring(0, servicestr.length() - 1);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(servicestr);
		out.flush();
		out.close();
	}

}
