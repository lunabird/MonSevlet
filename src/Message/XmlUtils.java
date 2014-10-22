package Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
//Xml工具类，读写XML文档
//在该类的50行和63行处有路径符“/”
public class XmlUtils {
		public static String filePath;
		public static String configFilePath;
		static{
			try {
				//filePath = new URI(XmlUtils.class.getClassLoader().getResource("curInfo.txt").toString()).getPath();
				configFilePath = new URI(XmlUtils.class.getClassLoader().getResource("config.xml").toString()).getPath();
			} catch (MalformedURIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public static Document getDocument() throws DocumentException{
			
			SAXReader reader = new SAXReader();
		    Document document = reader.read(new File(configFilePath));
		    return document;
		}
		
		public static void write2Xml(Document document) throws IOException{
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        XMLWriter writer = new XMLWriter( new FileOutputStream(filePath), format );
	        writer.write( document );
	        writer.close();
		}
		public static void modifyXml(String type,String value) throws DocumentException{
			SAXReader reader = new SAXReader();
		    Document doc = reader.read(new File("src/config.xml"));
		    List list = doc.selectNodes("Body/liminal");
		    Iterator iter = list.iterator();
		    while(iter.hasNext()){
				Element modEle = (Element)iter.next();
				if(type.equals("cpu")){
					modEle.element("cpu").setText(value);
				}else if(type.equals("ram")){
					modEle.element("ram").setText(value);
				}else if(type.equals("hdd")){
					modEle.element("hdd").setText(value);
				}
		    }
		    writeXmlLocal(doc,"src/config.xml");//写myeclipse工程里的文件
		    Document doc1 = getDocument();
		    List list1 = doc.selectNodes("Body/liminal");
		    Iterator iter1 = list.iterator();
		    while(iter1.hasNext()){
				Element modEle = (Element)iter1.next();
				if(type.equals("cpu")){
					modEle.element("cpu").setText(value);
				}else if(type.equals("ram")){
					modEle.element("ram").setText(value);
				}else if(type.equals("hdd")){
					modEle.element("hdd").setText(value);
				}
		    }
		    writeXmlLocal(doc1,configFilePath);//写tomcat部署的工程里的文件
		}
		public static void writeXmlLocal(Document doc,String Path)
		{	
			FileOutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(Path);
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				format.setIndent(true);
		        format.setIndent(" ");
				XMLWriter writer = new XMLWriter(fileOut,format);			
				writer.write(doc);	
				writer.flush();
				writer.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static void main(String[] args) throws DocumentException{
			/*System.out.println(filePath);
			System.out.println(configFilePath);
			Document doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
			Element ipEle = root.element("center_IP");
			System.out.println(ipEle.getText());
			Element center_PortEle = root.element("center_Port");
			System.out.println(center_PortEle.getText());*/
//			modifyXml("cpu","100");
			Document doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
			Element pathEle = root.element("filePath");
			System.out.println(pathEle.getText());
		}
}
