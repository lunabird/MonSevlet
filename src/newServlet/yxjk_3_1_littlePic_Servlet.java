package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.TCPClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.1监控概览界面右上方的小图标
 * 内容：
 * 		1.服务总数：计算每种服务类型（Web service等）的服务有多少个。服务类型的顺序依次是Web Service，Restful，exe，Web Site，Flow；
 * 		2.系统总数：计算系统的数量以及计算每个系统的服务个数；
 * 		3.节点总数：计算部署了服务的节点数量以及计算每个节点上有几个服务；
 * @author huangpeng
 *
 */
public class yxjk_3_1_littlePic_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_1_littlePic_Servlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//获得request 相关信息  
        String Param = request.getParameter("param1");  
		
		
		TCPClient c = new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		//监控概览页面上右上角的小图标
		JSONObject json = new JSONObject();
		
		try {	
			//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型，服务ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			if(Param.equals("serviceAmount")){
				//1.服务总数
				json.put("serviceAmount", serviceWithCopy.size());
				//计算每种服务类型（Web service等）的服务有多少个。顺序依次是Web Service，Restful，exe，Web Site，Flow
				int[] kindArr = new int[5];
				for(int i=0;i<serviceWithCopy.size();i++){
					String serviceKind = serviceWithCopy.get(i)[2];
					if(serviceKind.contains("Service")){
						kindArr[0]++;
					}else if(serviceKind.contains("Restful")){
						kindArr[1]++;
					}else if(serviceKind.contains("exe")){
						kindArr[2]++;
					}else if(serviceKind.contains("Web Site")){
						kindArr[3]++;
					}else if(serviceKind.contains("Flow")){
						kindArr[4]++;
					}		
				}
				json.put("serDetail", kindArr);
			}else if(Param.equals("sysAmount")){
				//2.系统总数
				HashSet<String> sysSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String sys = serviceWithCopy.get(i)[4];
					sysSet.add(sys);
				}
				Object[] sysArray = sysSet.toArray();
				json.put("sysAmount", sysSet.size());
				//计算每个系统的服务个数
				int[] jarr = new int[sysArray.length];
				for(int i=0;i<sysArray.length;i++){
					int count = 0;
					for(int j=0;j<serviceWithCopy.size();j++){
						if(sysArray[i].equals(serviceWithCopy.get(j)[4])){
							count++;
						}
					}
					jarr[i] = count;
				}
				json.put("sysDetail", jarr);
			}else if(Param.equals("nodeAmount")){
				//3.节点总数
				HashSet<String> ipSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String ip = serviceWithCopy.get(i)[1];
					ipSet.add(ip);
				}
				json.put("nodeAmount", ipSet.size());
				//计算每个节点上有几个服务
				Object[] ipArray = ipSet.toArray();
				int[] countArray = new int[ipArray.length];
				for(int i=0;i<ipArray.length;i++){
					int count =0;
					for(int j=0;j<serviceWithCopy.size();j++){
						if(ipArray[i].equals(serviceWithCopy.get(j)[1])){
							count++;
						}
					}
					countArray[i] = count;
				}
				json.put("nodeDetail", countArray);
			}		
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(json);
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
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		//监控概览页面上右上角的小图标
		JSONObject json = new JSONObject();
		try {		
			//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型，服务ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			//1.服务总数
			json.put("serviceAmount", serviceWithCopy.size());
			//计算每种服务类型（Web service等）的服务有多少个。顺序依次是Web Service，Restful，exe，Web Site，Flow
			int[] kindArr = new int[5];
			for(int i=0;i<serviceWithCopy.size();i++){
				String serviceKind = serviceWithCopy.get(i)[2];
				if(serviceKind.contains("Service")){
					kindArr[0]++;
				}else if(serviceKind.contains("Restful")){
					kindArr[1]++;
				}else if(serviceKind.contains("exe")){
					kindArr[2]++;
				}else if(serviceKind.contains("Web Site")){
					kindArr[3]++;
				}else if(serviceKind.contains("Flow")){
					kindArr[4]++;
				}		
			}
			json.put("serDetail", kindArr);
			//2.系统总数
			HashSet<String> sysSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String sys = serviceWithCopy.get(i)[4];
				sysSet.add(sys);
			}
			Object[] sysArray = sysSet.toArray();
			json.put("sysAmount", sysSet.size());
			//计算每个系统的服务个数
			int[] jarr = new int[sysArray.length];
			for(int i=0;i<sysArray.length;i++){
				int count = 0;
				for(int j=0;j<serviceWithCopy.size();j++){
					if(sysArray[i].equals(serviceWithCopy.get(j)[4])){
						count++;
					}
				}
				jarr[i] = count;
			}
			json.put("sysDetail", jarr);
			//3.节点总数
			HashSet<String> ipSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String ip = serviceWithCopy.get(i)[1];
				ipSet.add(ip);
			}
			json.put("nodeAmount", ipSet.size());
			//计算每个节点上有几个服务
			Object[] ipArray = ipSet.toArray();
			int[] countArray = new int[ipArray.length];
			for(int i=0;i<ipArray.length;i++){
				int count =0;
				for(int j=0;j<serviceWithCopy.size();j++){
					if(ipArray[i].equals(serviceWithCopy.get(j)[1])){
						count++;
					}
				}
				countArray[i] = count;
			}
			json.put("nodeDetail", countArray);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("yxjk_3_1_littlePic_json:"+json);
		out.println(json);
		out.flush();
		out.close();
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
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
