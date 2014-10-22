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
 * ������Ҫ���������������ϵķ������������������ ����ʱ����
 * @author huangpeng
 *
 */
public class UDPClient {	
    /**
     * UDP�ͻ��˷���socket��Ϣ
     * @param msg
     * @param host
     * @param port
     * @throws IOException
     */
    public void clientJob(Message msg,String host,int port) throws IOException{
    	DatagramSocket client = new DatagramSocket();
        //client������Ϣ
    	DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName(host), port);
		// ����->������->�ֽ�������->�ֽ�����
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(
				byteArrayStream);
		objectStream.writeObject(msg);//��Message���͵���Ϣд�������
		byte[] arr = byteArrayStream.toByteArray();
		packet.setData(arr);// ���DatagramPacket
		client.send(packet);// ����
		objectStream.close();
		byteArrayStream.close();
       
        //client���ܴӷ���˵ķ�����Ϣ
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
        client.receive(recvPacket);
        String recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
        System.out.println("server echo:" + recvStr);
        client.close();
    }
    
    /**
     * ���Ͳ���ʱ����������̨������.���͵���Ϣ��IP��ַ��node�б����͵Ķ˿�Ϊ5000��
     * @param sampleTime
     * @throws IOException
     */
    public void sendSampleTime(int sampleTime,ArrayList<String> hostIPList) throws IOException{
    	CurrentInfo.sampleTime = sampleTime;
    	//����Ϣ�������Ϣ��һ��Ҫ����˳�����.int���͵Ĳ���ʱ��
    	Message sMsg = new Message(MsgType.SampleTime,sampleTime);
		
		if (!hostIPList.isEmpty()) {
			for (int i = 0; i < hostIPList.size(); i++) {
				clientJob(sMsg, hostIPList.get(i), 5000);// �����������������IP�Ͷ˿�
			}
		}else{
			System.out.println("��������������IP�б�Ϊ�գ�");
		}
    }
    
    public static void main(String[] args) throws IOException{
    	//������ԣ����Ͳ���ʱ����Ϊ10s
    	UDPClient ec = new UDPClient();
    	ArrayList<String> hostIPList = new ArrayList<String>();
    	hostIPList.add("192.168.0.6");
    	ec.sendSampleTime(10,hostIPList);
    }
    
    
}
