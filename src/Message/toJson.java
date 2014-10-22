package Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * 
 * @author zhaofen
 *
 */
public class toJson {
	// public String serviceList2Json(ArrayList<String[]> serviceInfo){
	// String json = new String();
	// json += "{\"serivicelist\": {";
	// for(int i = 0;i < serviceInfo.size();i++ ){
	// String s[] = serviceInfo.get(i);
	// for(int j = 0; j < s.length; j++){
	// if(j == 0){
	// json+="{\"serviceName\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 1){
	// json+="\"description\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 2){
	// json+="\"publisher\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 3){
	// json+="\"parameterNum\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 4){
	// json+="\"parameter\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 5){
	// json+="\"serviceType\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 6){
	// json+="\"copyNum\": \"";
	// json += s[j];
	// json += "\"}";
	// }
	//
	// }
	// }
	// json += "}}";
	// return json;
	// }
	// public String serviceCopyList2Json(ArrayList<String[]> serviceCopyList){
	// String json = new String();
	// json += "{\"serivicecopylist\": {";
	// for(int i = 0;i < serviceCopyList.size();i++ ){
	// String s[] = serviceCopyList.get(i);
	// for(int j = 0; j < s.length; j++){
	// if(j == 0){
	// json+="{\"serviceName\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 1){
	// json+="\"hostip\": \"";
	// json += s[j];
	// json += "\",";
	// }
	// if(j == 2){
	// json+="\"status\": \"";
	// json += s[j];
	// json += "\"}";
	// }
	//
	// }
	// }
	// json += "}}";
	// return json;
	// }

	public JSONObject toJsonObject(String jsonName, String[] keys,
			ArrayList<String[]> serviceList) throws JSONException {
		String jsonString = new String();
		JSONArray jsonArray = new JSONArray();
		// String[] keys = {"servicename", "description", "publisher",
		// "paranum", "parameter","serviceType","copynum"};
		for (int i = 0; i < serviceList.size(); i++) {
			String s[] = serviceList.get(i);
			Map<String, String> m = new HashMap<String, String>();
			for (int j = 0; j < s.length; j++) {
				m.put(keys[j], s[j]);

			}
			jsonArray.add(m);

		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(jsonName, jsonArray);
		// jsonString = jsonObj.toString();

		return jsonObj;
	}

	public JSONArray toJsonArray(ArrayList<JSONObject> al) {

		JSONArray jsonarray = new JSONArray();
		// JSONObject jso = new JSONObject();
		for (JSONObject jso : al) {
			jsonarray.add(jso);
		}

		return jsonarray;

	}

	/**
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		toJson j = new toJson();
		JSONObject ss1 = new JSONObject();
		JSONObject ss2 = new JSONObject();
		JSONObject ss3 = new JSONObject();

		// ArrayList<String[]> serviceInfo = new ArrayList<String[]>();
		// String[] s1 = {"AddService", "add service", "admin", "2",
		// "double,double", "Web Service", "3"};
		// String[] s2 = {"SubService", "sub service", "admin", "2",
		// "int,double", "Web Service", "2"};
		// String[] s3 = {"减法", "减法服务", "赵芬", "2", "double,String",
		// "Web Service", "1"};
		// serviceInfo.add(s1);
		// serviceInfo.add(s2);
		// serviceInfo.add(s3);
		// String[] keys = {"servicename", "description", "publisher",
		// "paranum", "parameter","serviceType","copynum"};
		// s = j.toJsonObject("servicelist",keys,serviceInfo);

		// ArrayList<String[]> strategyInfo = new ArrayList<String[]>();
		// String[] keys = {"strategyname",
		// "description","cpuvalue","memoryvalue","inuse"};
		// String[] s1 = {"A 策略", "A是服务路由策略", "10", "20", "0"};
		// String[] s2 = {"B 策略", "B是服务路由策略", "75", "80", "1"};
		// strategyInfo.add(s1);
		// strategyInfo.add(s2);
		// s = j.toJsonObject("strategylist",keys, strategyInfo);

		// ArrayList<String[]> hostlist = new ArrayList<String[]>();
		// String[] s1 = {"192.168.0.1", "gateway", "Axis2,IIS"};
		// String[] s2 = {"192.168.0.10", "ZXQ-PC", "Axis2,Tomcat"};
		// String[] s3 = {"192.168.0.23", "park_wh-PC", "Axis2"};
		// hostlist.add(s1);
		// hostlist.add(s2);
		// hostlist.add(s3);
		// String[] keys = {"ip", "hostName", "container"};
		// s = j.toJsonObject("hostlist",keys,hostlist);

		ArrayList<String[]> serviceCopyList = new ArrayList<String[]>();
		String[] s1 = { "AddService", "192.168.0.23", "start" };
		String[] s2 = { "SubService", "192.168.0.23", "start" };
		String[] s3 = { "SubService", "192.168.0.23", "stop" };
		serviceCopyList.add(s1);
		serviceCopyList.add(s2);
		serviceCopyList.add(s3);
		String[] keys = { "servicename", "ip", "status" };
		ss1 = j.toJsonObject("servicecopylist", keys, serviceCopyList);
		ss2 = j.toJsonObject("servicecopylist", keys, serviceCopyList);
		ss3 = j.toJsonObject("servicecopylist", keys, serviceCopyList);

		ArrayList<JSONObject> al = new ArrayList<JSONObject>();
		al.add(ss1);
		al.add(ss2);
		al.add(ss3);

		JSONArray jsa = new JSONArray();
		jsa = j.toJsonArray(al);
		System.out.println(jsa.toString());
		// System.out.println(s);

	}

}
