package newServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class inputSession extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		String serviceid = request.getParameter("serviceid");
		String servicename = request.getParameter("servicename");
		String systemname = request.getParameter("systemname");
		System.out.println("serviceid:" + serviceid);
		System.out.println("servicename" + servicename);
		System.out.println("systemname" + systemname);
		HttpSession session = request.getSession();
		session.setAttribute("serviceid", serviceid);
		session.setAttribute("servicename", servicename);
		session.setAttribute("systemname", systemname);
	}

}
