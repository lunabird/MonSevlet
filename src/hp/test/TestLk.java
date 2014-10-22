package hp.test;

import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import monitor.info.CurrentInfo;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class TestLk implements Runnable{
	
	public volatile static CurrentInfo info=new CurrentInfo();
	public volatile static int testInt=4;
	private static ReadWriteLock lock = new ReentrantReadWriteLock(); 
	
	public void run() {
		// TODO Auto-generated method stub
		
		
		
			HashSet set1=new HashSet();
			for(int i=0;i<1000;i++){
				set1.add(i+"fdsa");
			}
			
			HashSet set2=new HashSet();
			for(int j=0;j<1000;j++){
				set2.add(j+"fdfffff");
			}
			
			
			info.setConSet(set1);
			info.setHoSet(set2);
			testInt=99;	
			
			
		
	}
}
