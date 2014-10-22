package newServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitor.info.CurrentInfo;
import monitor.info.Host;
import monitor.info.MainClass;
import monitor.info.TCPClient;
import monitor.info.ToPolgyJsonTest;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import Message.XmlUtils;
/**
 * 3.1��ظ�������
 * ���ݣ�
 * 		3.1.2���������ͳ�ƣ�
 * 		3.1.3�������ʱ��ͳ�ƣ�
 * 		3.1.4--3.1.5�ڵ�ʵʱ��cpu���ڴ棻
 * 		3.1.6�ڵ��������
 * @author huangpeng
 *
 */
public class yxjk_3_1_jkgl_Servlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public yxjk_3_1_jkgl_Servlet() {
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
        
        TCPClient c =new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �����ʽ
		Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
		String nowstr = String.valueOf(now.getTime());			
		JSONObject json = new JSONObject();
		
		try {
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			// �ж�param����������
			if (Param.equals("serviceCallQuantity")) {
				// 3.1.2���������ͳ�ơ��������ǵ�ǰʱ�䵹��ʮ���ӡ�չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.
				int[] visitList;
				visitList = c.sendServiceCallInfoRequest(nowstr);
				json.put("serviceCallQuantity", visitList);
				// ʱ������� 10����
				String[] time = new String[10];
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {
					timeLong -= 60000;
					Date dt = new Date(timeLong);
					time[i] = df.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);

			} else if (Param.equals("serviceRunTime")) {
				// 3.1.3�������ʱ��ͳ�ơ��������Ǵ����ݿ�ͳ�Ƶķ�������ʱ�����䡣��1Сʱ�ڣ�.չʾ��������з�����һ��ʱ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
				ArrayList<Object> serviceRunTimeList = c.sendServiceRunTimeRequest(String.valueOf(1407058048193L));
				if (serviceRunTimeList != null) {
					int[] theNum = (int[]) serviceRunTimeList.get(0);
					String theStartTime = (String) serviceRunTimeList.get(1);
					String theEndTime = (String) serviceRunTimeList.get(2);
					json.put("serviceRunTime", theNum);
					double timeInterval = (Double.parseDouble(theEndTime) - Double
							.parseDouble(theStartTime)) / 10;
					double[] serviceRunTime_x = new double[11];
					java.text.DecimalFormat ddf = new java.text.DecimalFormat(
							"#.00");
					for (int i = 0; i <= 10; i++) {
						serviceRunTime_x[i] = Double.parseDouble(ddf
								.format(Double.parseDouble(theStartTime) + i
										* timeInterval));
					}
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				} else {
					int[] nums = new int[10];
					json.put("serviceRunTime", nums);
					double[] serviceRunTime_x = new double[11];
					json.put("serviceRunTime_ordTime", serviceRunTime_x);
				}
			}else if(Param.equals("cpu_ram")){
				//3.1.4--3.1.5�ڵ�ʵʱ��cpu���ڴ�	
//				CurrentInfo curInfo = MainClass.buildCurInfo("D:\\curInfo.txt");//��ȡcurInfo
				Document doc = null;
				try {
					doc = XmlUtils.getDocument();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Element root = doc.getRootElement();// �õ����ڵ�
				Element pathEle = root.element("filePath");
				String curInfoPath = pathEle.getText();
				CurrentInfo curInfo = MainClass.buildCurInfo(curInfoPath);
				if(curInfo.equals(null)){
					System.out.println("curInfo Ϊ��ֵ����");
					json.put("cpu_ram", null);
				}else{
					curInfo.printCurInfo();
					Set<Host> hoSet =  curInfo.getHoSet();
					Object[] hostArray = hoSet.toArray();
					HashSet<String> ipSet = new HashSet<String>();
					for(int i=0;i<serviceWithCopy.size();i++){
						String ip = serviceWithCopy.get(i)[1];
						ipSet.add(ip);
					}
					ArrayList<Host> deleteHostList = new ArrayList<Host>();
					for(int i=0;i<hostArray.length;i++){
						Host temp = (Host) hostArray[i];
						if(!ipSet.contains(temp.getIP())){
							//�������������ipSet�У�ipSet��ʾ���б��õ��Ľڵ㣬�����з���Ľڵ㣩����hoSet����ɾ����̨����
							deleteHostList.add((Host) hostArray[i]);
						}
					}
					hoSet.removeAll(deleteHostList);
					hostArray = hoSet.toArray();
					//�����cpu �� �ڴ� �� json��
					JSONArray ja1 = new JSONArray();
					for(int i=0;i<hostArray.length;i++){
						JSONObject hjson = new JSONObject();
						Host h = (Host)hostArray[i];
						hjson.put("hostname", h.getHostName());
						hjson.put("IP",h.getIP());
						hjson.put("cpu",h.getCPU());
						hjson.put("ram",h.getRAM());
						ja1.add(hjson);
					}
					json.put("cpu_ram", ja1);
				}
			}else if(Param.equals("nodeServiceAmount")){
				//3.1.6�ڵ������
				HashSet<String> ipSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String ip = serviceWithCopy.get(i)[1];
					ipSet.add(ip);
				}
				Object[] ipArray = ipSet.toArray();//���нڵ�ļ���
				JSONArray ja = new JSONArray();
				for(int i=0;i<ipArray.length;i++){
					JSONObject j1 = new JSONObject();
					int count = 0;
					for(int j=0;j<serviceWithCopy.size();j++){
						if(serviceWithCopy.get(j)[1].equals(ipArray[i])){
							count+=1;
						}
					}
					j1.put("IP", ipArray[i]);
					j1.put("amount", count);
					ja.add(j1);
				}
				json.put("nodeServiceAmount", ja);
			}else if(Param.isEmpty()==true){
				json.put("message", "The Param is null!");
			}
			
			out.println(json);	
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		TCPClient c =new TCPClient();
		Document doc = null;
		try {
			doc = XmlUtils.getDocument();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();// �õ����ڵ�
		Element pathEle = root.element("filePath");
		String curInfoPath = pathEle.getText();
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �����ʽ
			Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
			String nowstr = String.valueOf(now.getTime());			
			JSONObject json = new JSONObject();
			//3.1.1ϵͳ�ṹ����.��ʾ����������з��񣨰����丱��������ϸ��Ϣ.����ͼ
			//String[]���ֵ��˳�������� �����������ڽڵ�IP���������ͣ�ҵ�����ͣ�����ϵͳ���ͣ�����ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			//For test 	
//			ArrayList<String[]> serviceWithCopy = new ArrayList<String[]>();
//			String[] s1 = {"service1","192.168.0.201","WebService","�鱨ϵͳ","aba"};
//			String[] s2 = {"service1","192.168.0.202","WebService","�鱨ϵͳ","aba"};
//			String[] s3 = {"service2","192.168.0.201","WebService","�״�ϵͳ","aba"};
//			String[] s4 = {"service3","192.168.0.201","WebService","����ϵͳ","aba"};
//			String[] s5 = {"service3","192.168.0.202","WebService","����ϵͳ","aba"};
//			String[] s6 = {"service4","192.168.0.201","WebService","�鱨ϵͳ","aba"};
//			String[] s7 = {"service5","192.168.0.201","WebService","�鱨ϵͳ","aba"};
//			String[] s8 = {"service6","192.168.0.201","WebService","�鱨ϵͳ","aba"};			
//			serviceWithCopy.add(s1);serviceWithCopy.add(s2);serviceWithCopy.add(s3);serviceWithCopy.add(s4);serviceWithCopy.add(s5);
//			serviceWithCopy.add(s6);serviceWithCopy.add(s7);serviceWithCopy.add(s8);
			//For test 				
//			JSONObject jj = ToPolgyJsonTest.buildTopologyForOverview(serviceWithCopy);
//			json.put("ToPo", jj);
			
			//3.1.2���������ͳ�ơ��������ǵ�ǰʱ�䵹��ʮ���ӡ�չʾ��������з�����һ��ʱ�䣨10���ӵȣ��ڵĵ��������������ʾʱ�䣬�������ʾ��������.	
			int[] visitList = c.sendServiceCallInfoRequest(nowstr);
			//For test 	
//			int[] visitList ={12,21,2,15,6,8,11,5,4,18};//For test 	
			json.put("serviceCallQuantity", visitList);
			// ʱ������� 10����
			String[] time = new String[10];
			long timeLong = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {
				timeLong -= 60000;
				Date dt = new Date(timeLong);  
				time[i] = df.format(dt);
			}
			json.put("serviceCallQuantity_ordTime", time);
			//3.1.3�������ʱ��ͳ�ơ��������Ǵ����ݿ�ͳ�Ƶķ�������ʱ�����䡣��1Сʱ�ڣ�.չʾ��������з�����һ��ʱ�������Ӧʱ���ͳ�ƣ��������ʾ��ͬ��Ӧʱ�����䣨1��10ms��11��20ms�ȣ����������ʾ��Ӧʱ���ڸ������ڵķ�������
			ArrayList<Object> serviceRunTimeList = c.sendServiceRunTimeRequest(String.valueOf(1407058048193L));
			//For test 	
//			int[] serviceRunTimeList = {2,1,2,5,6,8,1,0,0,0};//For test 
			if(serviceRunTimeList!=null){
				int[] theNum = (int[]) serviceRunTimeList.get(0);
				String theStartTime = (String)serviceRunTimeList.get(1);
				String theEndTime = (String)serviceRunTimeList.get(2);
				json.put("serviceRunTime", theNum);
				double timeInterval = (Double.parseDouble(theEndTime)-Double.parseDouble(theStartTime))/10;
				double[] serviceRunTime_x = new double[11];
				java.text.DecimalFormat ddf = new java.text.DecimalFormat("#.00");
				for(int i=0;i<=10;i++){
					serviceRunTime_x[i] = Double.parseDouble(ddf.format(Double.parseDouble(theStartTime) +i*timeInterval));
				}
				json.put("serviceRunTime_ordTime", serviceRunTime_x);
			}else{
				int[] nums = new int[10];
				json.put("serviceRunTime", nums);
				double[] serviceRunTime_x = new double[11];
				json.put("serviceRunTime_ordTime", serviceRunTime_x);
			}
			
			//3.1.4--3.1.5�ڵ�ʵʱ��cpu���ڴ�	
//			CurrentInfo curInfo = MainClass.buildCurInfo("D:\\curInfo.txt");//��ȡcurInfo
			CurrentInfo curInfo = MainClass.buildCurInfo(curInfoPath);
			if(curInfo.equals(null)){
				System.out.println("curInfo Ϊ��ֵ����");
				json.put("cpu_ram", null);
			}else{
				curInfo.printCurInfo();
				Set<Host> hoSet =  curInfo.getHoSet();
				Object[] hostArray = hoSet.toArray();
				//For test 	
//				Host[] hostArray = new Host[3];
//				ArrayList<String> hddList = new ArrayList<String>();//Ӳ��״��
//				hostArray[0] = new Host("hp","192.168.0.201","xx","48.5%","88.2%","22.3%","PC","win7");
//				hostArray[1] = new Host("hp","192.168.0.202","xx","12.5%","55.2%","56.8%","PC","win7");
//				hostArray[2] = new Host("hp","192.168.0.203","xx","56.5%","59.7%","85.2%","PC","win7");
				//For test 	
				
				HashSet<String> ipSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String ip = serviceWithCopy.get(i)[1];
					ipSet.add(ip);
				}
				ArrayList<Host> deleteHostList = new ArrayList<Host>();
				for(int i=0;i<hostArray.length;i++){
					Host temp = (Host) hostArray[i];
					if(!ipSet.contains(temp.getIP())){
						//�������������ipSet�У�ipSet��ʾ���б��õ��Ľڵ㣬�����з���Ľڵ㣩����hoSet����ɾ����̨����
						deleteHostList.add((Host) hostArray[i]);
					}
				}
				hoSet.removeAll(deleteHostList);
				hostArray = hoSet.toArray();
				//�����cpu �� �ڴ� �� json��
				JSONArray ja1 = new JSONArray();
				for(int i=0;i<hostArray.length;i++){
					JSONObject hjson = new JSONObject();
					Host h = (Host)hostArray[i];
					hjson.put("hostname", h.getHostName());
					hjson.put("IP",h.getIP());
					hjson.put("cpu",h.getCPU());
					hjson.put("ram",h.getRAM());
					ja1.add(hjson);
				}
				json.put("cpu_ram", ja1);
			}
			
			//3.1.6�ڵ������
			HashSet<String> ipSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String ip = serviceWithCopy.get(i)[1];
				ipSet.add(ip);
			}
			Object[] ipArray = ipSet.toArray();//���нڵ�ļ���
			JSONArray ja = new JSONArray();
			for(int i=0;i<ipArray.length;i++){
				JSONObject j1 = new JSONObject();
				int count = 0;
				for(int j=0;j<serviceWithCopy.size();j++){
					if(serviceWithCopy.get(j)[1].equals(ipArray[i])){
						count+=1;
					}
				}
				j1.put("IP", ipArray[i]);
//				j1.put("IP", ipArray[i]);
				j1.put("amount", count);
				ja.add(j1);
			}
			//For test 
//			Object[] ipArray = {"192.168.0.201","192.168.0.202,192","192.168.0.205"};//���нڵ�ļ���			
//			JSONArray ja = new JSONArray();
//			for(int i=0;i<ipArray.length;i++){
//				JSONObject j1 = new JSONObject();
//				int count = 0;
//				for(int j=0;j<serviceWithCopy.size();j++){
//					if(serviceWithCopy.get(j)[1].equals(ipArray[i])){
//						count+=1;
//					}
//				}
//				j1.put("IP", ipArray[i]);
//				j1.put("amount", count);
//				ja.add(j1);
//			}//For test 	
			json.put("nodeServiceAmount", ja);						
			System.out.println("yxjk_3_1_jkgl_json:"+json);		
			out.println(json);			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (ClassNotFoundException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }

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
