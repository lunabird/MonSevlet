package monitor.info;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import Message.XmlUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

public class QueueTest {
	public static Queue<double[]> avgQueue = new LinkedList<double[]>();
	public static HashMap<String, Queue<String[]>> historyMap = new HashMap<String, Queue<String[]>>();
	
	public static JSONArray avgQueue_json = new JSONArray();
	public static JSONArray historyMap_json = new JSONArray();
	
	/**
	 * ����������ļ�Ⱥ������������Ʒ��cpu���ڴ���Ϣ�洢�����������������ҳ���е�����ƽ������״������ͼ��չʾ
	 * @param d
	 * @return
	 * @throws IOException
	 */
	public static JSONArray putInHistoryAvgValue(double[] d) throws IOException {
		// ���avgQueue�Ĵ�Сδ����50����������������
		if (avgQueue.size() < 50) {
			avgQueue.offer(d);
		} else {// ���avgQueue�Ĵ�С����50������ɾ��һ�����ݣ��ټ����������
			avgQueue.poll();
			avgQueue.offer(d);
		}
		JSONArray json = new JSONArray();
		avgQueue_json.add(avgQueue);
		json.add(avgQueue);
		String filepath = XmlUtils.filePath+"\\AvgCPUAndRam.txt";
		writeFile(filepath, json.toString());
		return json;
	}

	public static JSONArray putInHistoryCPUAndRam(String ip, String[] strArray)
			throws JSONException, IOException {
		// �жϸ�IP�Ƿ���ԭ����HashMap��
		// �����ip�Ѿ���HashMap���ˣ������ip����Ӧ�Ķ���������Ӽ����Ϣ
		if (historyMap.containsKey(ip)) {
			Queue<String[]> queue = historyMap.get(ip);
			if (queue.size() < 50) {
				// ������г���С��50����������
				queue.offer(strArray);
				historyMap.put(ip, queue);
			} else {
				queue.poll();
				queue.offer(strArray);
				historyMap.put(ip, queue);
			}
		} else {// �����ip����HashMap�У������һ��key��valueֵ��
			Queue<String[]> queue = new LinkedList<String[]>();
			queue.offer(strArray);
			historyMap.put(ip, queue);
		}
		JSONArray json = new JSONArray();
		
		HashMap<String,Object> resultMap1 = new HashMap<String,Object>();
		Set<String> keySet = (Set<String>) historyMap.keySet();
		Object[] keyArray = (Object[])keySet.toArray();
		for(int j=0;j<keyArray.length;j++){
			resultMap1.put("IP", keyArray[j]);
			resultMap1.put("array", historyMap.get(keyArray[j]));
			json.add(resultMap1);
			historyMap_json.add(resultMap1);
		}
		
		// ���historyMap
		String filepath = XmlUtils.filePath+"\\cpuAndRamLineChart.txt";
		writeFile(filepath, json.toString());
		// return historyMap;
		return json;
	}

	public static void main(String[] args) throws JSONException, IOException {

		// for (int i = 0; i < 5; i++) {
		// String[] strArr = new String[2];
		// strArr[0] = "21.5"+i+"%";
		// strArr[1] = "34.5"+i+"%";
		// putInHistoryCPUAndRam("192.168.0.96", strArr);
		// strArr[0] = "22.5%";
		// strArr[1] = "44.5%";
		// putInHistoryCPUAndRam("192.168.0.96", strArr);

		// }
		// for(String ip:historyMap.keySet())
		// {
		// Queue q = historyMap.get(ip);
		// // for(int i =0;i<q.size();i++){
		// // System.out.println(q.);
		// // }
		// System.out.println(ip);
		// Object[] o = q.toArray();
		// for(int i=0;i<o.length;i++){
		// String[] s = (String[])o[i];
		// System.out.println(s[0]+","+s[1]);
		// }
		// }
		// for (int j = 0; j < 3; j++) {
		// strArr[0] = "41.5"+j+"%";
		// strArr[1] = "74.5"+j+"%";
		// putInHistoryCPUAndRam("192.168.0.92", strArr);
		// }
		
		/*
		for (int i = 0; i < 50; i++) {
			double[] d = new double[2];
			d[0] = i + 0.44;
			d[1] = i + 0.33;
			putInHistoryAvgValue(d);
		}
		*/
	}

	public static void writeFile(String filePath, String sets)
			throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				filePath), "UTF-8");
		out.write(sets);
		out.write("\n");
		out.close();
	}
}
