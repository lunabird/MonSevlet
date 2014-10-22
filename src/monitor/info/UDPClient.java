package monitor.info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import Message.Message;
import Message.MsgType;

/**
 * 该类主要用来给所有主机上的服务容器代理软件发送 采样时间间隔
 * @author huangpeng
 *
 */
public class UDPClient {	
    /**
     * UDP客户端发送socket消息
     * @param msg
     * @param host
     * @param port
     * @throws IOException
     */
    public void clientJob(Message msg,String host,int port) throws IOException{
    	DatagramSocket client = new DatagramSocket();
        //client发送消息
    	DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName(host), port);
		// 对象->对象流->字节数组流->字节数组
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(
				byteArrayStream);
		objectStream.writeObject(msg);//将Message类型的消息写到输出流
		byte[] arr = byteArrayStream.toByteArray();
		packet.setData(arr);// 填充DatagramPacket
		client.send(packet);// 发送
		objectStream.close();
		byteArrayStream.close();
       
        //client接受从服务端的反馈信息
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
        client.receive(recvPacket);
        String recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
        System.out.println("server echo:" + recvStr);
        client.close();
    }
    
    /**
     * 发送采样时间间隔，给多台机器发.发送的消息的IP地址是node列表，发送的端口为5000。
     * @param sampleTime
     * @throws IOException
     */
    public void sendSampleTime(int sampleTime,ArrayList<String> hostIPList) throws IOException{
    	CurrentInfo.sampleTime = sampleTime;
    	//给消息体填充信息，一定要按照顺序填充.int类型的采样时间
    	Message sMsg = new Message(MsgType.SampleTime,sampleTime);
		
		if (!hostIPList.isEmpty()) {
			for (int i = 0; i < hostIPList.size(); i++) {
				clientJob(sMsg, hostIPList.get(i), 5000);// 服务容器代理软件的IP和端口
			}
		}else{
			System.out.println("代理容器的主机IP列表为空！");
		}
    }
    
    public static void main(String[] args) throws IOException{
    	//构造测试，发送采样时间间隔为10s
    	UDPClient ec = new UDPClient();
    	ArrayList<String> hostIPList = new ArrayList<String>();
    	hostIPList.add("192.168.0.6");
    	ec.sendSampleTime(10,hostIPList);
    }
    
    
}
