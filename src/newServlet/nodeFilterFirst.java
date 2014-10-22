package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;

import monitor.info.TCPClient;



public class nodeFilterFirst extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		TCPClient sn = new TCPClient();
		String value = request.getParameter("value");

		HashSet<String> filternodelist = new HashSet<String>();
		try {
			filternodelist = sn.nodeFilterFirst(value);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String filterstring = new String();
		for (String str : filternodelist) {
			filterstring += str;
			filterstring += "@";
		}
		filterstring = filterstring.substring(0, filterstring.length() - 1);

		//response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(filterstring);
		out.flush();

		out.close();
	}

}
