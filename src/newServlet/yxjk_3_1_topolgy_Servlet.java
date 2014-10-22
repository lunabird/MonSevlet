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

import Message.XmlUtils;

import net.sf.json.JSONObject;

import monitor.info.TCPClient;
import monitor.info.ToPolgyJsonTest;
/**
 * 3.1º‡øÿ∏≈¿¿ΩÁ√ÊÕÿ∆ÀΩ·ππÕº
 * @author huangpeng
 *
 */
public class yxjk_3_1_topolgy_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_1_topolgy_Servlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TCPClient c = new TCPClient();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		ArrayList<String[]> serviceWithCopy;
		try {
			serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			JSONObject jj = ToPolgyJsonTest.buildTopologyForOverview(serviceWithCopy);
			json.put("ToPo", jj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(json.toString());
		
		out.flush();
		out.close();
	}
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
		
		TCPClient c = new TCPClient();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		ArrayList<String[]> serviceWithCopy;
		try {
			serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			JSONObject jj = ToPolgyJsonTest.buildTopologyForOverview(serviceWithCopy);
			json.put("ToPo", jj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("yxjk_3_1_topolgy_json:" + json);
		out.print(json.toString());
		
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
