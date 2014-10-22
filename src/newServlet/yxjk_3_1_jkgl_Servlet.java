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
 * 3.1监控概览界面
 * 内容：
 * 		3.1.2服务访问量统计；
 * 		3.1.3服务调用时间统计；
 * 		3.1.4--3.1.5节点实时的cpu和内存；
 * 		3.1.6节点服务数；
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
		//获得request 相关信息  
        String Param = request.getParameter("param1");  
        
        TCPClient c =new TCPClient();
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式
		Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
		String nowstr = String.valueOf(now.getTime());			
		JSONObject json = new JSONObject();
		
		try {
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			// 判断param是哪种类型
			if (Param.equals("serviceCallQuantity")) {
				// 3.1.2服务访问量统计。横坐标是当前时间倒推十分钟。展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.
				int[] visitList;
				visitList = c.sendServiceCallInfoRequest(nowstr);
				json.put("serviceCallQuantity", visitList);
				// 时间点坐标 10分钟
				String[] time = new String[10];
				long timeLong = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {
					timeLong -= 60000;
					Date dt = new Date(timeLong);
					time[i] = df.format(dt);
				}
				json.put("serviceCallQuantity_ordTime", time);

			} else if (Param.equals("serviceRunTime")) {
				// 3.1.3服务调用时间统计。横坐标是从数据库统计的服务运行时间区间。（1小时内）.展示该类别所有服务在一段时间调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
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
				//3.1.4--3.1.5节点实时的cpu和内存	
//				CurrentInfo curInfo = MainClass.buildCurInfo("D:\\curInfo.txt");//获取curInfo
				Document doc = null;
				try {
					doc = XmlUtils.getDocument();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Element root = doc.getRootElement();// 得到根节点
				Element pathEle = root.element("filePath");
				String curInfoPath = pathEle.getText();
				CurrentInfo curInfo = MainClass.buildCurInfo(curInfoPath);
				if(curInfo.equals(null)){
					System.out.println("curInfo 为空值！！");
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
							//如果该主机不在ipSet中（ipSet表示所有被用到的节点，上面有服务的节点），从hoSet里面删除该台主机
							deleteHostList.add((Host) hostArray[i]);
						}
					}
					hoSet.removeAll(deleteHostList);
					hostArray = hoSet.toArray();
					//构造的cpu 和 内存 的 json串
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
				//3.1.6节点服务数
				HashSet<String> ipSet = new HashSet<String>();
				for(int i=0;i<serviceWithCopy.size();i++){
					String ip = serviceWithCopy.get(i)[1];
					ipSet.add(ip);
				}
				Object[] ipArray = ipSet.toArray();//所有节点的集合
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
		Element root = doc.getRootElement();// 得到根节点
		Element pathEle = root.element("filePath");
		String curInfoPath = pathEle.getText();
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式
			Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
			String nowstr = String.valueOf(now.getTime());			
			JSONObject json = new JSONObject();
			//3.1.1系统结构概览.表示请求的是所有服务（包含其副本）的详细信息.拓扑图
			//String[]存的值的顺序依次是 服务名，所在节点IP，服务类型，业务类型，所属系统类型，服务ID
			ArrayList<String[]> serviceWithCopy = c.sendServiceDetailInfoWithCopyRequest();
			//For test 	
//			ArrayList<String[]> serviceWithCopy = new ArrayList<String[]>();
//			String[] s1 = {"service1","192.168.0.201","WebService","情报系统","aba"};
//			String[] s2 = {"service1","192.168.0.202","WebService","情报系统","aba"};
//			String[] s3 = {"service2","192.168.0.201","WebService","雷达系统","aba"};
//			String[] s4 = {"service3","192.168.0.201","WebService","航空系统","aba"};
//			String[] s5 = {"service3","192.168.0.202","WebService","航空系统","aba"};
//			String[] s6 = {"service4","192.168.0.201","WebService","情报系统","aba"};
//			String[] s7 = {"service5","192.168.0.201","WebService","情报系统","aba"};
//			String[] s8 = {"service6","192.168.0.201","WebService","情报系统","aba"};			
//			serviceWithCopy.add(s1);serviceWithCopy.add(s2);serviceWithCopy.add(s3);serviceWithCopy.add(s4);serviceWithCopy.add(s5);
//			serviceWithCopy.add(s6);serviceWithCopy.add(s7);serviceWithCopy.add(s8);
			//For test 				
//			JSONObject jj = ToPolgyJsonTest.buildTopologyForOverview(serviceWithCopy);
//			json.put("ToPo", jj);
			
			//3.1.2服务访问量统计。横坐标是当前时间倒推十分钟。展示该类别所有服务在一段时间（10分钟等）内的调用量。横坐标表示时间，纵坐标表示调用数量.	
			int[] visitList = c.sendServiceCallInfoRequest(nowstr);
			//For test 	
//			int[] visitList ={12,21,2,15,6,8,11,5,4,18};//For test 	
			json.put("serviceCallQuantity", visitList);
			// 时间点坐标 10分钟
			String[] time = new String[10];
			long timeLong = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {
				timeLong -= 60000;
				Date dt = new Date(timeLong);  
				time[i] = df.format(dt);
			}
			json.put("serviceCallQuantity_ordTime", time);
			//3.1.3服务调用时间统计。横坐标是从数据库统计的服务运行时间区间。（1小时内）.展示该类别所有服务在一段时间调用响应时间的统计，横坐标表示不同响应时间区间（1～10ms、11～20ms等），纵坐标表示响应时间在该区间内的服务数量
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
			
			//3.1.4--3.1.5节点实时的cpu和内存	
//			CurrentInfo curInfo = MainClass.buildCurInfo("D:\\curInfo.txt");//获取curInfo
			CurrentInfo curInfo = MainClass.buildCurInfo(curInfoPath);
			if(curInfo.equals(null)){
				System.out.println("curInfo 为空值！！");
				json.put("cpu_ram", null);
			}else{
				curInfo.printCurInfo();
				Set<Host> hoSet =  curInfo.getHoSet();
				Object[] hostArray = hoSet.toArray();
				//For test 	
//				Host[] hostArray = new Host[3];
//				ArrayList<String> hddList = new ArrayList<String>();//硬盘状况
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
						//如果该主机不在ipSet中（ipSet表示所有被用到的节点，上面有服务的节点），从hoSet里面删除该台主机
						deleteHostList.add((Host) hostArray[i]);
					}
				}
				hoSet.removeAll(deleteHostList);
				hostArray = hoSet.toArray();
				//构造的cpu 和 内存 的 json串
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
			
			//3.1.6节点服务数
			HashSet<String> ipSet = new HashSet<String>();
			for(int i=0;i<serviceWithCopy.size();i++){
				String ip = serviceWithCopy.get(i)[1];
				ipSet.add(ip);
			}
			Object[] ipArray = ipSet.toArray();//所有节点的集合
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
//			Object[] ipArray = {"192.168.0.201","192.168.0.202,192","192.168.0.205"};//所有节点的集合			
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
