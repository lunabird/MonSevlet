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

import Message.XmlUtils;

import monitor.info.TCPClient;

public class nodeDetail extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		TCPClient c = new TCPClient();
		ArrayList<String> al = new ArrayList<String>();
		try {
			al = c.nodeDetail();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String nodestr = new String();
		for (String str : al) {
			nodestr += str;
			nodestr += "@";
		}
		nodestr = nodestr.substring(0, nodestr.length() - 1);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(nodestr);
		out.flush();
		out.close();
	}

}
