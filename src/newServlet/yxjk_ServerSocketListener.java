package newServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import monitor.info.TCPServer;
import monitor.info.UDPServer;
/**
 * ����Tomcat����ʱ�����й���
 * @author huangpeng
 *
 */
public class yxjk_ServerSocketListener implements ServletContextListener {
	
	UDPServer us;
	
	//tomcat�ر�ʱ���ر��̣߳��ͷŶ˿�
	@SuppressWarnings("deprecation")
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		us.stop();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		us = new UDPServer();
		us.start();		
	}

}
