package monitor.info;

import java.util.ArrayList;
import java.util.Iterator;
//�Լ��̣߳�ÿ��sampleTime*3��ʱ����һ���Ƿ����������ٸ�����������Ϣ�ˣ�
//����У���curInfo����ȥ�������������ص���Ϣ��

public class CheckThread implements Runnable {

	public void run() {
		try {
			while (true) {
				Thread.sleep(CurrentInfo.sampleTime * 3000);
				System.out.println("tempInfo���������...");
				// System.out.println("curInfo��ǰֵ��");
				// curInfo.printCurInfo();
				if (CurrentInfo.dbHostList.size() > UDPServer.tempInfo
						.getHostAmount()) {
					if (UDPServer.tempInfo.getHostAmount() < 1) {
						System.out.println("tempInfo��⵽tempInfo��Host��Ϊ��...");
					} else {
						// ��ʾdbHostList�����������ܳ�һ��ʱ������û�и�MON���ͼ����Ϣ�ˣ�
						// ��Ҫ��curInfo����ȥ���⼸̨����,��Ҫȥ���⼸̨���������е�����
						ArrayList<String> tempList = new ArrayList<String>();
						Iterator<Host> it = UDPServer.tempInfo.hoSet.iterator();
						while (it.hasNext()) {
							Host h11 = (Host) it.next();
							tempList.add(h11.getIP());
						}
						// tempList���Ԫ���dbHostList���Ԫ����
						ArrayList<String> tempDBHostList = new ArrayList<String>();
						tempDBHostList = CurrentInfo.dbHostList;
						tempDBHostList.removeAll(tempList);
						// ����tempDBHostList�����ǹҵ���������IP
						Iterator<Host> it1 = UDPServer.curInfo.hoSet.iterator();
						ArrayList<Host> delList1 = new ArrayList<Host>();
						while (it1.hasNext()) {
							Host h11 = (Host) it1.next();
							if (tempDBHostList.contains(h11.getIP())) {
								delList1.add(h11);
							}
						}
						UDPServer.curInfo.hoSet.remove(delList1);
						// ��ʱcurInfo�������ǿ���չʾ��������Ϣ�ˣ���Ҫȥ��һЩ��������Ϣ
//						Iterator<Container> it2 = UDPServer.curInfo.conSet
//								.iterator();
//						ArrayList<Container> delList = new ArrayList<Container>();
//						while (it2.hasNext()) {
//							Container c11 = (Container) it2.next();
//							if (tempDBHostList.contains(c11.getIP())) {
//								delList.add(c11);
//							}
//						}
//						UDPServer.curInfo.conSet.remove(delList);
						// ��ʱcurInfo���conSet�洢��Ҳ�ǿ���չʾ����Ϣ��
					}
					// ���tempInfo
					UDPServer.tempInfo.clearAll();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
