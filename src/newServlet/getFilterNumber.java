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


public class getFilterNumber extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		String filtertype = request.getParameter("filtertype");
		String type = request.getParameter("type");
		String value = request.getParameter("value");
		
		ArrayList<String> al = new ArrayList<String>();
		al.add(filtertype);
		al.add(type);
		al.add(value);
		TCPClient sn = new TCPClient();
		String number = new String();
		try {
			number = sn.getFilterNum(al); // 从服务引擎请求主机列表
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print(number);
		out.flush();
		out.close();
	}

}
