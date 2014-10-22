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
 * 3.1��ظ����������Ϸ���Сͼ��
 * ���ݣ�
 * 		1.��������������ÿ�ַ������ͣ�Web service�ȣ��ķ����ж��ٸ����������͵�˳��������Web Service��Restful��exe��Web Site��Flow��
 * 		2.ϵͳ����������ϵͳ�������Լ�����ÿ��ϵͳ�ķ��������
 * 		3.�ڵ����������㲿���˷���Ľڵ������Լ�����ÿ���ڵ����м�������
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
		
		//���request �����Ϣ  
        String Param = request.getParameter("param1");  
		
		
		TCPClient c = new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		//��ظ���ҳ�������Ͻǵ�Сͼ��
		JSONObject json = new JSONObject();
		
		try {	
			//String[]���ֵ��˳�������� �����������ڽڵ�IP���������ͣ�ҵ�����ͣ�����ϵͳ���ͣ�����ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			if(Param.equals("serviceAmount")){
				//1.��������
				json.put("serviceAmount", serviceWithCopy.size());
				//����ÿ�ַ������ͣ�Web service�ȣ��ķ����ж��ٸ���˳��������Web Service��Restful��exe��Web Site��Flow
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
				//2.ϵͳ����
				HashSet<String> sysSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String sys = serviceWithCopy.get(i)[4];
					sysSet.add(sys);
				}
				Object[] sysArray = sysSet.toArray();
				json.put("sysAmount", sysSet.size());
				//����ÿ��ϵͳ�ķ������
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
				//3.�ڵ�����
				HashSet<String> ipSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String ip = serviceWithCopy.get(i)[1];
					ipSet.add(ip);
				}
				json.put("nodeAmount", ipSet.size());
				//����ÿ���ڵ����м�������
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
		
		//��ظ���ҳ�������Ͻǵ�Сͼ��
		JSONObject json = new JSONObject();
		try {		
			//String[]���ֵ��˳�������� �����������ڽڵ�IP���������ͣ�ҵ�����ͣ�����ϵͳ���ͣ�����ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			//1.��������
			json.put("serviceAmount", serviceWithCopy.size());
			//����ÿ�ַ������ͣ�Web service�ȣ��ķ����ж��ٸ���˳��������Web Service��Restful��exe��Web Site��Flow
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
			//2.ϵͳ����
			HashSet<String> sysSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String sys = serviceWithCopy.get(i)[4];
				sysSet.add(sys);
			}
			Object[] sysArray = sysSet.toArray();
			json.put("sysAmount", sysSet.size());
			//����ÿ��ϵͳ�ķ������
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
			//3.�ڵ�����
			HashSet<String> ipSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String ip = serviceWithCopy.get(i)[1];
				ipSet.add(ip);
			}
			json.put("nodeAmount", ipSet.size());
			//����ÿ���ڵ����м�������
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
