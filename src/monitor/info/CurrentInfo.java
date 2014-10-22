package monitor.info;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
/**
 * ��ǰ�����Ϣ�洢�࣬����������Ϣ��������Ϣ
 * @author huangpeng
 *
 */
public  class CurrentInfo {
	
	Set<Host>  hoSet;
//	Set<Container> conSet;
//	static ArrayList<Service> serList;//���и����ķ����б�
	static ArrayList<String> dbHostList;//����IP�б�
	static int sampleTime = 10;
	
	/**
	 * ���캯��
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public CurrentInfo() {
		hoSet = new HashSet<Host>();
//		conSet = new HashSet<Container>();
//		serList = new ArrayList<Service>();
		dbHostList = new ArrayList<String>();
	}
	
	
	//������
	public  void setHoSet(Set<Host> hoSet){
		this.hoSet = hoSet;
	}
	//������
	
	public   void  setConSet(Set<Container> conSet){
//		this.conSet = conSet;
	}
	
//	public ArrayList<Service> getSerList(){
//		return serList;
//	}
	/**
	 * ����ServiceList������
	 * @param list
	 */
//	public void refreshServiceList(ArrayList<Service> list){
//		serList = list;
//	}
	
	public synchronized void writeFile() throws IOException, JSONException {
		try {
			File path = new File("D:/"); // "D:/"Ŀ¼�������
			File dir = new File(path, "curInfo.txt");
			if (!dir.exists())
				dir.createNewFile();
		} catch (Exception e) {
			System.out.print("D:\\curInfo.txt����ʧ��");
		}		 
		String filePath = "D:\\curInfo.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				filePath), "UTF-8");
		JSONObject json = new JSONObject();
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		HashSet<Host> theHostSet = new HashSet<Host>();
		theHostSet = (HashSet<Host>) hoSet;
		Iterator<Host> it = theHostSet.iterator();
		while (it.hasNext()) {
			HashMap<String, Object> mp1 = new HashMap<String, Object>();
			Host h11 = (Host) it.next();
			mp1.put("hostName", h11.getHostName());
			mp1.put("ip", h11.getIP());
			mp1.put("mac", h11.getMAC());
			mp1.put("cpu", h11.getCPU());
			mp1.put("ram", h11.getRAM());
			mp1.put("hdd", h11.getHdd());//h11.getHddList()
			mp1.put("type", h11.getType());
			mp1.put("OS",h11.getOS());
			al.add(mp1);
		}
		json.put("host-Table", al);
		
		/*
			JSONArray mapa = new JSONArray();
			HashSet<Container> theConSet = new HashSet<Container>();
			theConSet = (HashSet<Container>) conSet;
			Iterator<Container> it1 = theConSet.iterator();
			while (it1.hasNext()) {
				HashMap<String, String> lista = new HashMap<String, String>();
				Container c11 = (Container) it1.next();
				lista.put("containerName", c11.getConName());
				lista.put("ip", c11.getIP());
				lista.put("cpu", c11.getCPU());
				lista.put("ram", c11.getRam());
				lista.put("amount", c11.getServiceAmountString());
				mapa.add(lista);
			}
			json.put("container-Table", mapa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
//		json.put("dbHostList", dbHostList);
		/*
		JSONArray ar = new JSONArray();
		for (int i = 0; i < serList.size(); i++) {
			JSONObject ar1 = new JSONObject();
			ar1.put("ServiceName", serList.get(i).getServiceName());
			ar1.put("ServiceKind", serList.get(i).getServiceKind());
			ar1.put("ServiceSystem", serList.get(i).getServiceSystem());
			ar1.put("ServiceIP", serList.get(i).getServiceIP());
			ar1.put("State", serList.get(i).getState());
			ar.add(ar1);
		}
		json.put("service-Table", ar);
		*/
		json.put("sampleTime", sampleTime);

		out.write(json.toString());
		out.write("\n");
		out.close();
//		System.out.println("write "+filePath+" file success!");
	}
	public synchronized void printCurInfo(){
//		try{
		System.out.println("printed curInfo ��hoSet�����ݣ�");
		HashSet<Host> theHostSet = new HashSet<Host>();
		theHostSet = (HashSet<Host>) hoSet;
		Iterator<Host> it = theHostSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			System.out.println(h11.toString());
		}
		/*
		System.out.println("conSet�����ݣ�");
		HashSet<Container> theConSet = new HashSet<Container>();
		theConSet = (HashSet<Container>) conSet;
		Iterator<Container> it1 = theConSet.iterator();
		while(it1.hasNext()){
			Container c11 = (Container)it1.next();
			System.out.println(c11.toString());
		}
		}catch(Exception e){
			e.printStackTrace();
		}				
		if(dbHostList.isEmpty()){
			System.out.println("��ǰ�����б�Ϊ�գ�");
		}else{
			System.out.println("��ǰ�����б�:");
			for(int i=0;i<dbHostList.size();i++){
				System.out.println(dbHostList.get(i));
			}
		}*/
		/*
		if(serList.isEmpty()){
			System.out.println("��ǰ�����б�Ϊ�գ�");
		}else{
			System.out.println("��ǰ�����б�:");
			for(int i=0;i<serList.size();i++){
				System.out.println(serList.get(i));
				System.out.println("��������" + serList.get(i).serviceName.toString());
				System.out.println("��������IP��ַ��" + serList.get(i).ip.toString());
				System.out.println("��ǰ״̬��" + serList.get(i).state.toString());
				System.out.println("���ͣ�" + serList.get(i).serviceKind.toString());
				System.out.println("����ϵͳ��" + serList.get(i).serviceSystem.toString());
			}
		}
		*/
		System.out.println("sampleTime:"+sampleTime);
	}
	public Set<Host> getHoSet(){
		return hoSet;
	}
//	public Set<Container> getConSet(){
//		return conSet;
//	}
	
	/**
	 * ���һ̨������Ϣ�������ǰ��set�����и�������IP���ͻḲ����ʷ��Ϣ�������¼��������ͻ�ֱ�����
	 * @param h
	 */
	public void addHost(Host h){
		ArrayList<Host> delList = new ArrayList<Host>();
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			if(h.IPadd.equals(h11.IPadd)){
				delList.add(h11);				
			}					
		}	
		hoSet.removeAll(delList);
		hoSet.add(h);
	}
	/**
	 * ���һ��������Ϣ�������ǰ��set�����и��������ͻḲ����ʷ��Ϣ�������¼��������ͻ�ֱ�����
	 * @param c
	 */
//	public synchronized void addContainer(Container c){
//		ArrayList<Container> delList = new ArrayList<Container>();
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.conName.equals(c.conName)&&c11.IPadd.equals(c.IPadd)){
//				delList.add(c11);	
//			}
//		}
//		conSet.removeAll(delList);
//		conSet.add(c);
//	}
	/**
	 * ����ڴ����ִ�������������ļ����Ϣ
	 */
	public void clearAll(){
		hoSet.clear();
//		conSet.clear();
	}
	/**
	 * ��ȡ��ǰ���������Ϣ�б�
	 * @return
	 */
	public Set<Host> getCurHostInfo(){
		return hoSet;
	}
	/**
	 * ��ȡ��ǰ�������������Ϣ�б�
	 * @return
	 */
//	public Set<Container> getCurConInfo(){
//		return conSet;
//	}
	/**
	 * ��ȡ��ǰ���ݿ������IP�б�
	 * @return
	 */
	public ArrayList<String> getdbHostList(){
		return dbHostList;
	}
	/**
	 * �������ݿ�����IP�б�
	 * @param dbHostList
	 */
	public void setdbHostList(ArrayList<String> dbHostList){
		CurrentInfo.dbHostList = dbHostList;
	}
	/**
	 * ��ȡ����ʱ����
	 * @return
	 */
	public int getSampleTime(){
		return sampleTime;
	}
	/**
	 * ���ò���ʱ����
	 * @param stime
	 */
	public void setSampleTime(int stime){
		sampleTime = stime;
	}
	/**
	 * yxjk_1_1��ȡ��ǰ�����ĸ���
	 * @return
	 */
	public int getHostAmount(){
		return hoSet.size();
	}
	/**
	 * yxjk_1_2��ȡ��������������
	 * @return
	 */
//	public int getContainerAmount(){
//		return conSet.size();
//	}
	/**
	 * ��ȡ�����ĸ���״������ÿ̨�������ж��ٸ�����������ҳ��չʾ������ 192.168.0.32��15������
	 * @return
	 */
//	public HashMap<String,Integer> getHostServiceLoad(){
//		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
//		Iterator<Host> it = hoSet.iterator();
//		while(it.hasNext()){
//			Host h11 = (Host)it.next();
//			int h11_amount = 0;
//			Iterator<Container> it1 = conSet.iterator();
//			while(it1.hasNext()){
//				Container c11 = (Container)it1.next();
//				if(h11.getIP().equals(c11.getIP())){
//					h11_amount += c11.getServiceAmount();
//				}
//			}
//			hmap.put(h11.getIP(), h11_amount);
//		}
//		return hmap;
//	}
	/**
	 * ��ȡÿ�����͵ķ�����������������Axis2��5����Tomcat��3��
	 * @return
	 */
//	public HashMap<String,Integer> getAllKindsContainerAmount(){
//		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
//		int tt = 0;int a2 = 0;int is = 0;int me = 0;
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.getConName().contains("Tomcat")){
//				tt++;
//			}else if(c11.getConName().contains("Axis2")){
//				a2++;
//			}else if(c11.getConName().contains("IIS")){
//				is++;
//			}else if(c11.getConName().contains("Mule")){
//				me++;
//			}
//		}
//		hmap.put("Tomcat", tt);
//		hmap.put("Axis2", a2);
//		hmap.put("IIS", is);
//		hmap.put("Mule", me);
//		return hmap;
//	}	
	/**
	 * ��ȡ���������ķ���������������Ǽ���serList�Ĵ�С
	 * @return
	 */
//	public int getServiceAmount(){
//		return serList.size();		
//	}
	/**
	 * ����ĳ�ض������ϵķ�������,��������
	 * @param ip
	 * @return
	 */
//	public int getSpecificHostServiceAmount(String ip){
//		int amount = 0;
//		Iterator<Container> it = conSet.iterator();
//		while(it.hasNext()){
//			Container c11 = (Container)it.next();
//			if(c11.IPadd.equals(ip)){
//				amount += c11.getServiceAmount();
//			}			
//		}
//		return amount;
//	}
	/**
	 * ��ȡϵͳ������,hashSet�Ĵ�С����ϵͳ�ĸ�����
	 * @return
	 */
//	public HashSet<String> getSystemSet(){
//		HashSet<String> sysKindSet = new HashSet<String>();
//		for(int i=0;i<serList.size();i++){
//			sysKindSet.add(serList.get(i).getServiceSystem());
////			System.out.println(serList.get(i).getServiceSystem());
//		}		
//		return sysKindSet;
//	}
	/**
	 * ��ȡ���������ķ����б�
	 * @return
	 */
//	public HashSet<Service> getServiceListWithoutDuplicate(){
//		HashSet<Service> serSet = new HashSet<Service>();
//		for(int i = 0;i<serList.size();i++){
//			Service s1 = serList.get(i);
//			
////			if(!serSet.contains(s1)){
////				serSet.add(s1);
////			}
//			if(serSet.isEmpty()){
//				serSet.add(s1);
//			}else{
//				
//				Iterator<Service> it = serSet.iterator();
//				ArrayList<Service> addList = new ArrayList<Service>();
//				while(it.hasNext()){
//					Service service = (Service)it.next();
//					if(s1.serviceName.equals(service.serviceName)){
//						//�����
//					}else{
//						addList.add(s1);						
//					}
//				}//while
//				serSet.addAll(addList);
//			}//else			
//		}//for
//		return serSet;
//	}
	/**
	 * ��ȡĳ���ض�ϵͳ�������б��������˽ṹͼ
	 * @param sysName
	 * @return
	 */
//	public HashSet<String> sysHostIPList(String sysName){
//		HashSet<String> sysHostList = new HashSet<String>();
//		for(int i = 0;i<serList.size();i++){
//			Service s1 = serList.get(i);
//			if(s1.serviceSystem.equals(sysName)){
//				sysHostList.add(s1.ip);
//			}
//		}
//		return sysHostList;
//	}
	/**
	 * yxjk_3_fw,����״̬����ͼ��
	 * @return ���� ָ��ϵͳ��[10��8��2]
	 */
	/*
	public HashMap<String,int[]> getServiceStateOverview(){
		HashMap<String,int[]> map = new HashMap<String,int[]>();
		//��ȡϵͳ���Ƶļ���
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//��ȡĳ���ض�ϵͳ�ķ�����������������
		
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			int[] a = new int[3];
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					if(s11.getState().contains("start")){
						a[1] +=1;
					}else{
						a[2] +=1;
					}
				}
			}
			map.put(nameArray[i], a);
		}
		return map;
	}
	*/
	//yxjk_3_fw ��ͼ2 ��ͼ3 �ϲ���һ��ͼ2��ʾÿ��ϵͳ�ķ��������
	//ͼ3��ʾÿ��ϵͳ�����з����м����������ģ��м�����ֹͣ��
	/*
	public ArrayList<HashMap<String,Object>> sysServiceCombined(){
		ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();
		//����һ�����飬ÿһ������һ��ϵͳ�ķ�������������������
		//��ȡ����ϵͳ�����ֺ�ϵͳ�ĸ���
		HashSet<String> sysNameSet = getSystemSet();
		int sysAmount = sysNameSet.size();
		
		//��ÿ��ϵͳ�ĳ�ʼ��������ֵΪ0
		int[] amountArray = new int[sysAmount];
		String[] nameArray = new String[sysAmount];
		for(int i = 0;i<sysAmount;i++){
			amountArray[i] = 0;
		}
		
		//��ϵͳ���ֵ�����nameArray��ֵ
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
//			System.out.println("nameArray["+ii+"] ="+nameArray[ii]);
			ii++;
		}
		
		
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
//		ArrayList<Service> arrList = new ArrayList<Service>();
//		arrList.addAll(serSet);
		if(serList.size()==0){
			System.out.println("�����б�Ϊ�գ�ϵͳ���޷���");
		}else{
			Iterator<Service> it = serList.iterator();			
			while(it.hasNext()){
				Service s11 = (Service)it.next();				
				for(int j = 0;j<sysAmount;j++){
					if(s11.serviceSystem.equals(nameArray[j])){
						amountArray[j] += 1;
					}
				}//for							
			}//while
		}
		
		//���㲻�������ķ�������
		//int sum = getServiceListWithoutDuplicate().size();
		//��������뵽hmap��
		for(int j=0;j<sysAmount;j++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("sysName", nameArray[j]);
			map.put("amount", amountArray[j]);
			
			//�����������/ֹͣ�ĸ���
			int[] a = new int[3];
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[j])){
					a[0] +=1;
					if(s11.getState().contains("start")){
						a[1] +=1;
					}else{
						a[2] +=1;
					}
				}
			}
			map.put("started", a[1]);			
			hmap.add(map);
		}
		return hmap;
	}
	*/
	
	
	/**
	 * yxjk_4_xtͼ1���Ϸ�����,���� ָ��ϵͳ��[12,3]	12������3̨����
	 * @return
	 */
	/*
	public ArrayList<HashMap<String,Object>> sysOverview(){
		ArrayList<HashMap<String,Object>> map = new ArrayList<HashMap<String,Object>>();
		//��ȡϵͳ���Ƶļ���
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//��ȡĳ���ض�ϵͳ�ķ�����������������
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			HashMap<String,Object> amap = new HashMap<String,Object>();
			int[] a = new int[2];
			//�����ϵͳ�ķ������
			HashSet<String> set0 = new HashSet<String>();
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					//�����ϵͳ����������
					set0.add(s11.ip);
				}
			}
			a[1] = set0.size();	
			amap.put("sysName", nameArray[i]);
			amap.put("amount", a);
			map.add(amap);
		}
		return map;
	}
	*/
	/**
	 * yxjk_4_xtͼ����ϵͳ����Ϊkey��������ͼ�ϵ�һ��
	 * ϵͳ���֣������������������������������
	 * @return
	 */
	/*
	public JSONArray sysCombined(){
		JSONArray map = new JSONArray();
		//��ȡϵͳ���Ƶļ���
		HashSet<String> sysNameSet = getSystemSet();
		String[] nameArray = new String[sysNameSet.size()];
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
			ii++;
		}
		//����nameArray������������ϵͳ������
		//��ȡĳ���ض�ϵͳ�ķ�����������������
		//HashSet<Service> serSet = getServiceListWithoutDuplicate();
		for(int i=0;i<sysNameSet.size();i++){
			//ÿѭ��һ�Σ���ʾ����һ���ض���ϵͳ����Ϣ��amap������������ض�ϵͳ��������Ϣ��
			//����ϵͳ����ϵͳ�ķ���������ϵͳ������������ϵͳ�����������Ϣ�б�ϵͳ�ķ���״̬��Ϣ�б�
			HashMap<String,Object> amap = new HashMap<String,Object>();
			int[] a = new int[2];
			//�����ϵͳ�ķ������
			HashSet<String> set0 = new HashSet<String>();
			Iterator<Service> it = serList.iterator();
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					a[0] +=1;
					//�����ϵͳ����������
					set0.add(s11.ip);
				}
			}
			a[1] = set0.size();	
			amap.put("sysName", nameArray[i]);
			amap.put("amount", a);
			//��ȡ��ϵͳ���������������Ϣ		
			ArrayList<Host> listhost = new ArrayList<Host>();
				String sysName = nameArray[i];
				HashSet<String> sysHostList = sysHostIPList(sysName);
				Iterator<String> it2 = sysHostList.iterator();
				while(it2.hasNext()){
					
					String ip = (String) it2.next();
					//ͨ����ip��ȡ����������
					//����hoSet���ҳ�һ������IP�����ϱ�ip������
					Iterator<Host> iit = hoSet.iterator();
					while(iit.hasNext()){
						Host h11 = (Host)iit.next();
						if(ip.equals(h11.IPadd)){
							listhost.add(h11);
						}
					}
				}
			amap.put("host-Table", listhost);
			//��ȡ��ϵͳ�����з�����Ϣ
			ArrayList<Service> ServiceList = new ArrayList<Service>();
			Iterator<Service> it3 = serList.iterator();
			while(it3.hasNext()){
				ArrayList<String> strList = new ArrayList<String>();
				Service s11 = (Service)it3.next();
				if(s11.serviceSystem.equals(nameArray[i])){
					ServiceList.add(s11);
					strList.add(s11.serviceName);
					strList.add(s11.serviceKind);
					strList.add(s11.ip);
					strList.add(s11.state);
				}
			}
			amap.put("service-Table", ServiceList);
			//�����е���ϢPut��map��
			map.add(amap);
		}
		return map;
	}
	*/
///
	//****************************************************************************//
	//****************************************************************************//
///
	/**
	 * yxjk_1_ztͼ1��ϵͳ���͸�������ʾÿ��ϵͳ�ķ�����������ͼ��
	 * @return ����һ��HashMap<String,int> ���磺�״�ϵͳ��10����ָ��ϵͳ��12��
	 */
	/*
	public ArrayList<HashMap<String,Object>> sysKindOverview(){
		ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();
		//����һ�����飬ÿһ������һ��ϵͳ�ķ�������������������
		//��ȡ����ϵͳ�����ֺ�ϵͳ�ĸ���
		HashSet<String> sysNameSet = getSystemSet();
		int sysAmount = sysNameSet.size();
		
		//��ÿ��ϵͳ�ĳ�ʼ��������ֵΪ0
		int[] amountArray = new int[sysAmount];
		String[] nameArray = new String[sysAmount];
		for(int i = 0;i<sysAmount;i++){
			amountArray[i] = 0;
		}
		
		//��ϵͳ���ֵ�����nameArray��ֵ
		int ii = 0;
		Iterator<String> it0 = sysNameSet.iterator();
		while(it0.hasNext()){
			String name11 = (String)it0.next();
			nameArray[ii] = name11;
//			System.out.println("nameArray["+ii+"] ="+nameArray[ii]);
			ii++;
		}
		
		
		HashSet<Service> serSet = getServiceListWithoutDuplicate();
//		ArrayList<Service> arrList = new ArrayList<Service>();
//		arrList.addAll(serSet);
		if(serSet.size()==0){
			System.out.println("�����б�Ϊ�գ�ϵͳ���޷���");
		}else{
			
//			for(int i = 0;i<arrList.size();i++){
//				Service s11 = arrList.get(i);
//				System.out.println("i="+i+"\tnameArray[i]="+nameArray[i]);
//				System.out.println(s11.serviceSystem);
//				System.out.println(s11.serviceSystem.equals(nameArray[i]));
//				if(s11.serviceSystem.equals(nameArray[i])){
//					amountArray[i] += 1;
//					System.out.println("i="+i+"\tamountArray[i]="+amountArray[i]);
//				}
//			}
			
			
			
			Iterator<Service> it = serList.iterator();
			
			while(it.hasNext()){
				Service s11 = (Service)it.next();
				
				for(int j = 0;j<sysAmount;j++){
//					System.out.println("j="+j+"\tnameArray[j]="+nameArray[j]);
//					System.out.println(s11.serviceSystem);
//					System.out.println(s11.serviceSystem.equals(nameArray[j]));
					if(s11.serviceSystem.equals(nameArray[j])){
						amountArray[j] += 1;
//						System.out.println("i="+j+"\tamountArray[i]="+amountArray[j]);
					}
				}//for							
			}//while
		}
		
		//���㲻�������ķ�������
		//int sum = getServiceListWithoutDuplicate().size();
		//��������뵽hmap��
		for(int j=0;j<sysAmount;j++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("sysName", nameArray[j]);
			map.put("amount", amountArray[j]);
			hmap.add(map);
//			System.out.println(nameArray[j]+","+amountArray[j]);
		}
		
			
		
		return hmap;
	}
	*/
	/**
	 * yxjk_1ͼ2,����ϵͳ��������ƽ��CPUռ���ʡ�����ͼ��
	 * @return double
	 */
	public double calulateAverageCpu(){
		if(hoSet.size()==0){
			System.out.println("hoSetΪ�գ����ܼ���ƽ��ֵ��");
			return 0;
		}else{
			double hcpuResult = 0;
			Iterator<Host> it = hoSet.iterator();
			while(it.hasNext()){
				Host h11 = (Host)it.next();
				hcpuResult += Double.parseDouble(h11.getCPU().split("%")[0]);
			}
			return hcpuResult/hoSet.size();
		}
		
	}
	/**
	 * yxjk_1ͼ2,����ϵͳ��������ƽ���ڴ�ռ���ʡ�����ͼ��
	 * @return double
	 */
	public double calulateAverageRam(){
		if(hoSet.size()==0){
			System.out.println("hoSetΪ�գ����ܼ���ƽ��ֵ��");
			return 0;
		}else{
			double hramResult = 0;
			Iterator<Host> it = hoSet.iterator();
			while(it.hasNext()){
				Host h11 = (Host)it.next();
				hramResult += Double.parseDouble(h11.getRAM().split("%")[0]);
			}
			return hramResult/hoSet.size();
		}		
	}
	
	/**
	 * yxjk_1ͼ4.����cpu���أ��ڴ渺�أ����·�����ͼ��
	 * @param ip ������IP��ַ
	 * @return �������ĵ�ǰcpu��ֵ���ڴ��ֵ
	 */
	public HashMap<String,double[]> hostCpuLineChart(){
		HashMap<String,double[]> map = new HashMap<String,double[]>();		
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			double[] result = new double[2];
			Host h11 = (Host)it.next();
			result[0] = Double.parseDouble(h11.getCPU().split("%")[0]);
			result[1] = Double.parseDouble(h11.getRAM().split("%")[0]);
			map.put(h11.getIP(), result);
		}
		return map;
	}	

	public double[] hostCpuLineChart1(String ip) {
		// HashMap<String,double[]> map = new HashMap<String,double[]>();
		Iterator<Host> it = hoSet.iterator();
		double[] result = new double[2];
		while (it.hasNext()) {
			Host h11 = (Host) it.next();
			if (h11.IPadd.equals(ip)) {
				result[0] = Double.parseDouble(h11.getCPU().split("%")[0]);
				result[1] = Double.parseDouble(h11.getRAM().split("%")[0]);
			}
		}
		return result;
	}

	/**
	 * yxjk_1ͼ5.���������ϵķ������,��ʾĳ̨�ض�������ÿ�����͵ķ���ռ�ܷ��������ðٷֱȣ�����������һ��
	 * ���·�Բͼ��
	 * @param ip
	 * @return ArrayList<HashMap<String,float[]>> ,����Float���������������� Tomcat,Axis2,
	 *         IIS,Mule���������ֵ��һ��HashMap�����key��ʾ������IP��ַ��ArrayList�����������������Ϣ��
	 */
	/*
	public ArrayList<ArrayList<HashMap<String,Object>>> hostConPercentChart(){
		ArrayList<ArrayList<HashMap<String,Object>>> list = new ArrayList<ArrayList<HashMap<String,Object>>>();		
		Iterator<Host> it0 = hoSet.iterator();
		while(it0.hasNext()){
			ArrayList<HashMap<String,Object>> hmap = new ArrayList<HashMap<String,Object>>();		
			Host h11 = (Host)it0.next();
			HashMap<String,Object> hhMap = new HashMap<String,Object>();
			hhMap.put("IP", h11.IPadd);
			
			//����h11�ϵķ�������
//			int amount = getSpecificHostServiceAmount(h11.IPadd);
			//����h11�ϵĸ��ַ���ĸ�����������Tomcat-->Axis2-->IIS-->Mule
			float[] d1 = new float[4];
			Iterator<Container> it = conSet.iterator();
			while(it.hasNext()){
				Container c11 = (Container)it.next();
				if(c11.IPadd.equals(h11.IPadd)){
					//h11�������������
					if(c11.getConName().contains("Tomcat")){
						d1[0] += c11.getServiceAmount();//Tomcat����ķ������
					}else if(c11.getConName().contains("Axis2")){
						d1[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("IIS")){
						d1[2] += c11.getServiceAmount();
					}else if(c11.getConName().contains("Mule")){
						d1[3] += c11.getServiceAmount();
					}
				}				
			}
			hhMap.put("array", d1);
			hmap.add(hhMap);			
			list.add(hmap);
		}		
		return list;
	}
	*/
	/**
	 * yxjk_2ͼ1,����ƽ����Ӳ��ռ����
	 * @return
	 */
	/*
	public double calulateAverageHdd(){
		double v = 0;
		double temp = 0;
		double result = 0;
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			Host h11 = (Host)it.next();
			ArrayList<String> hddList = h11.getHddList();
			for(int i=0;i<hddList.size();i++){
				double value = Double.parseDouble(hddList.get(i).split(":")[1].split("%")[0]);
				v += value;
			}
			temp = v/hddList.size();
			result += temp;
		}
		result /= hoSet.size();
		return result;
	}	
	*/
	/**
	 * yxjk_2_zjͼ2����ȡĳ������ÿ�����͵ķ���������������
	 * @return  HashMap<String,int[]>  key��ʾ��������int���������������Ǹ������ĸ����͸������ڷ���ĸ���
	 */	
	/*
	public ArrayList<HashMap<String,Object>> getHostCon(){
		ArrayList<HashMap<String,Object>> li = new ArrayList<HashMap<String,Object>>();
		Iterator<Host> it0 = hoSet.iterator();
		while(it0.hasNext()){
			Host h11 = (Host) it0.next();
			HashMap<String,Object> hmap = new HashMap<String,Object>();
			int[] t = new int[2];int[] a = new int[2];
			int[] i = new int[2];int[] m = new int[2];
			int tt = 0;int a2 = 0;int is = 0;int me = 0;
			Iterator<Container> it = conSet.iterator();
			while(it.hasNext()){
				Container c11 = (Container)it.next();
				if(c11.getIP().equals(h11.getIP())){
					if(c11.getConName().contains("Axis2")){
						a[0] = ++a2;//����������
						a[1] += c11.getServiceAmount();//�����ڷ��������					
					}else if(c11.getConName().contains("Tomcat")){
						t[0] = ++tt;
						t[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("IIS")){
						i[0] = ++is;
						i[1] += c11.getServiceAmount();
					}else if(c11.getConName().contains("Mule")){
						m[0] = ++me;
						m[1] += c11.getServiceAmount();
					}
				}	
			}
			hmap.put("IP", h11.IPadd);
			hmap.put("Tomcat", t[0]);
			hmap.put("Axis2", a[0]);
			hmap.put("IIS", i[0]);
			hmap.put("Mule", m[0]);
//			map1.put(h11.IPadd, hmap);
			li.add(hmap);
		}
		
		return li;
	}
	*/
	/**
	 * yxjk_2_zjͼ3����ȡĳ������ÿ�����͵ķ�����������
	 * @return  
	 */
	/*
	public ArrayList<HashMap<String,Object>> getHostServiceKind(){
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<dbHostList.size();i++){
			HashMap<String,Object> map1 = new HashMap<String,Object>();
			String ip = dbHostList.get(i);
			int[] array = new int[4];
			for(int j =0;j<serList.size();j++){
				Service s = serList.get(j);
				if(s.ip.equals(ip)){//s����������ϵķ���
					if(s.serviceKind.contains("Axis2")){
						array[0]+=1;
					}else if(s.serviceKind.contains("Mule")){
						array[1]+=1;
					}else if(s.serviceKind.contains("exe")){
						array[2]+=1;
					}else if(s.serviceKind.contains("IIS")){
						array[3]+=1;
					}
				}
			}
			map1.put("IP", ip);
			map1.put("array", array);
			list.add(map1);
		}
		
		return list;
	}
	*/
	/**
	 * yxjk_5_rqͼ1������ĳ����������������
	 * @return HashMap<String,int>
	 */
	/*
	public HashMap<String,Integer> deployContainerHostAmount(){
		//����һ̨�ض������� ���ܲ�������һ��������
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Iterator<Container> it = conSet.iterator();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt ++;
			}else if(c11.getConName().contains("Axis2")){
				a2 ++;
			}else if(c11.getConName().contains("IIS")){
				is ++;
			}else if(c11.getConName().contains("Mule")){
				me ++;
			}
		}
		map.put("Tomcat",tt);map.put("Axis2",a2);
		map.put("IIS", is);map.put("Mule",me);
		return map;
	}	
	*/
	/**
	 * yxjk_3_fwͼ1����������ϵͳ��ÿ�����͵ķ���ĸ��������磺Axis2��������18����
	 * @return
	 */
	/*
	public HashMap<String,Integer> eachKindServiceAmount(){
		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt += c11.getServiceAmount();
			}else if(c11.getConName().contains("Axis2")){
				a2 += c11.getServiceAmount();;
			}else if(c11.getConName().contains("IIS")){
				is += c11.getServiceAmount();
			}else if(c11.getConName().contains("Mule")){
				me += c11.getServiceAmount();
			}
		}
		hmap.put("Tomcat", tt);
		hmap.put("Axis2", a2);
		hmap.put("IIS", is);
		hmap.put("Mule", me);
		return hmap;
	}
	*/
	/**
	 * yxjk_3_fwͼ2,�������ͷֲ�����,�������ַ������ͣ��ֱ���Axis2��Tomcat��IIS��Mule��
	 * ÿ��ɫ���ʾÿ�����͵� ����ռ���������İٷֱ�
	 * @return ����һ��HashMap<String,double>
	 */
	/*
	public HashMap<String,Float> serviceKindOverview(){
		HashMap<String,Float> hmap = new HashMap<String,Float>();
		//��ȡ���������
		int allserAmount = getServiceAmount();
		float tt = 0;float a2 = 0;float is = 0;float me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt += c11.getServiceAmount();
			}else if(c11.getConName().contains("Axis2")){
				a2 += c11.getServiceAmount();;
			}else if(c11.getConName().contains("IIS")){
				is += c11.getServiceAmount();
			}else if(c11.getConName().contains("Mule")){
				me += c11.getServiceAmount();
			}
		}
		hmap.put("Tomcat", tt/allserAmount);
		hmap.put("Axis2", a2/allserAmount);
		hmap.put("IIS", is/allserAmount);
		hmap.put("Mule", me/allserAmount);
		return hmap;
	}
	*/
	/**
	 * yxjk_5_rqͼ2��ÿ������ռ�����������İٷֱ�,��ǰ�����֣���ǰ�˼���ٷֱ�
	 * @return
	 */
	/*
	public HashMap<String,Integer> conKindOverview(){
		HashMap<String,Integer> hmap = new HashMap<String,Integer>();
		int tt = 0;int a2 = 0;int is = 0;int me = 0;
		Iterator<Container> it = conSet.iterator();
		while(it.hasNext()){
			Container c11 = (Container)it.next();
			if(c11.getConName().contains("Tomcat")){
				tt++;
			}else if(c11.getConName().contains("Axis2")){
				a2++;
			}else if(c11.getConName().contains("IIS")){
				is++;
			}else if(c11.getConName().contains("Mule")){
				me++;
			}
		}
		hmap.put("Tomcat", tt);
		hmap.put("Axis2", a2);
		hmap.put("IIS", is);
		hmap.put("Mule", me);
		return hmap;
	}
	*/
	/**
	 * ��������ϵͳ��ƽ��cpu���ڴ�ռ�ã�������ҳ��̬ͼ��ʾ
	 * //�浽�ļ�AvgCPUAndRam.txt����
	 * @throws IOException
	 */	
	/*
	public void writeAVGFile() throws IOException{
		String filePath = ".\\WebRoot\\DataFiles\\AvgCPUAndRam.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath,true),"UTF-8");
		JSONArray json = new JSONArray();
		
		Double AvGcpu = calulateAverageCpu();
		Double AvGram = calulateAverageRam();
		if(AvGcpu.isInfinite()&&AvGram.isInfinite()){
			json.add(0);
			json.add(0);
		}else if (AvGcpu.isInfinite()) {
			json.add(0);
			json.add(AvGram);
		}else if(AvGram.isInfinite()){
			json.add(AvGcpu);
			json.add(0);
		}else{
			json.add(AvGcpu);
			json.add(AvGram);
		}
		
		out.append(json.toString());
	    out.append("\n");
	    out.close();
	}
	*/
	
	//��ĳ̨������cpu��ram���뵽��ʱ�ļ�cpuAndRamLineChart.txt��
	/*
	public void writeLineChartFile() throws IOException{
		String filePath = ".\\WebRoot\\DataFiles\\cpuAndRamLineChart.txt";
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath,true),"UTF-8");
		JSONArray json = new JSONArray();
		
		
		Iterator<Host> it = hoSet.iterator();
		while(it.hasNext()){
			JSONObject mp1 = new JSONObject();
			JSONArray arr1 = new JSONArray();
			Host h11 = (Host) it.next();			
			h11.getIP();			
			arr1.add(h11.getCPU());
			arr1.add(h11.getRAM());					
			mp1.put(h11.getIP(),arr1);
			json.add(mp1);
		}
			
		
		out.append(json.toString());
	    out.append("\n");
	    out.close();
	}
	*/
}//CurrentInfo
