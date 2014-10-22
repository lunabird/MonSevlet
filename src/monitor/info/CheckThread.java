package monitor.info;

import java.util.ArrayList;
import java.util.Iterator;
//自检线程，每隔sampleTime*3的时间检测一次是否有主机不再给监控软件发消息了，
//如果有，从curInfo里面去除掉与该主机相关的信息。

public class CheckThread implements Runnable {

	public void run() {
		try {
			while (true) {
				Thread.sleep(CurrentInfo.sampleTime * 3000);
				System.out.println("tempInfo检测运行中...");
				// System.out.println("curInfo当前值：");
				// curInfo.printCurInfo();
				if (CurrentInfo.dbHostList.size() > UDPServer.tempInfo
						.getHostAmount()) {
					if (UDPServer.tempInfo.getHostAmount() < 1) {
						System.out.println("tempInfo检测到tempInfo的Host表为空...");
					} else {
						// 表示dbHostList里面有主机很长一段时间以来没有给MON发送监控消息了，
						// 需要在curInfo里面去掉这几台主机,还要去掉这几台机子上所有的容器
						ArrayList<String> tempList = new ArrayList<String>();
						Iterator<Host> it = UDPServer.tempInfo.hoSet.iterator();
						while (it.hasNext()) {
							Host h11 = (Host) it.next();
							tempList.add(h11.getIP());
						}
						// tempList里的元算比dbHostList里的元素少
						ArrayList<String> tempDBHostList = new ArrayList<String>();
						tempDBHostList = CurrentInfo.dbHostList;
						tempDBHostList.removeAll(tempList);
						// 现在tempDBHostList里存的是挂掉的主机的IP
						Iterator<Host> it1 = UDPServer.curInfo.hoSet.iterator();
						ArrayList<Host> delList1 = new ArrayList<Host>();
						while (it1.hasNext()) {
							Host h11 = (Host) it1.next();
							if (tempDBHostList.contains(h11.getIP())) {
								delList1.add(h11);
							}
						}
						UDPServer.curInfo.hoSet.remove(delList1);
						// 此时curInfo里面存的是可以展示的主机信息了，还要去掉一些容器的信息
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
						// 此时curInfo里的conSet存储的也是可以展示的信息了
					}
					// 清空tempInfo
					UDPServer.tempInfo.clearAll();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
