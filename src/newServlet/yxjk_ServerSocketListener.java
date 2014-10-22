package newServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import monitor.info.TCPServer;
import monitor.info.UDPServer;
/**
 * 设置Tomcat启动时便运行工程
 * @author huangpeng
 *
 */
public class yxjk_ServerSocketListener implements ServletContextListener {
	
	UDPServer us;
	
	//tomcat关闭时，关闭线程，释放端口
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
