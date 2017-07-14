package com.dubbo.util;
import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.channels.ClosedChannelException;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.SocketChannel;  
import java.util.Iterator;  
/** 
* �ͻ��� 
*/  
public class SocketClient {  
  
    public Selector selector;  
      
    public SocketClient(String ip, int port){  
        SocketChannel channel = null;  
        try {  
            //channel = SocketChannel.open(new InetSocketAddress(ip,port));  
            channel = SocketChannel.open();  
            // ����ͨ��Ϊ������    
            channel.configureBlocking(false);  
            // ���һ��ͨ��������    
            this.selector = Selector.open();  
            // �ͻ������ӷ�����,��ʵ����ִ�в�û��ʵ������   
            channel.connect(new InetSocketAddress(ip, port));   
            /**while(!channel.finishConnect()){ 
                System.out.println("��������...."); 
            }*/  
            // ע�������¼���    
            channel.register(this.selector, SelectionKey.OP_CONNECT);    
        } catch(ClosedChannelException e1){  
            System.out.println("�رյ�ͨ��,�޷�ע�ᵽѡ����");  
            e1.printStackTrace();  
        } catch (IOException e2) {  
            System.out.println("�����쳣!");  
            try {  
                if(channel != null) channel.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            e2.printStackTrace();  
        }  
    }  
      
    /** 
     * ��ѯѡ���� 
     * @throws IOException 
     */  
    public void  pollSelect() throws Exception {    
        /* (����)��ѯѡ����,ֱ�����¼� */  
        while ( this.selector.select() > 0 ) {  
            /* ��ȡ�¼�֪ͨ�б� */  
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();    
            while (ite.hasNext()) {    
                SelectionKey selectKey = (SelectionKey) ite.next();    
                // ɾ����ѡ��key,�Է��ظ�����    
                ite.remove();    
                process(selectKey);  
            }    
        }    
    }  
      
    /** 
     * �����¼� 
     * @param selectKey 
     */  
    public void process(SelectionKey selectKey) throws Exception{  
        if (selectKey.isConnectable()) {  
            connect(selectKey);  
        } else if (selectKey.isReadable()) {    
            read(selectKey);  
        }    
    }  
      
    /** 
     * �����¼� 
     * @param selectKey 
     * @throws Exception 
     */  
    public void connect(SelectionKey selectKey) throws Exception{  
        try {  
            SocketChannel channel = (SocketChannel) selectKey    
                    .channel();    
            /* �����������,��������� */    
            if(channel.isConnectionPending()){  
                /** 
                 * connect()������δ������,����finishConnect()����, 
                 * ��ô������NoConnectionPendingException 
                 */  
                channel.finishConnect();    
            }  
            /** 
             * �ڷ�����ģʽ�µ���connect()����֮��,SocketChannel�ֱ��л���������ģʽ;��ô��� 
             * �б�Ҫ�Ļ��������̻߳�����ֱ�����ӽ������,finishConnect()�������žͻ᷵��true 
             * ֵ�� 
             */  
            /* ���óɷ����� */    
            channel.configureBlocking(false);    
            /* ������˷�����Ϣ */  
            channel.write(ByteBuffer.wrap(new String("���001�ͻ������ӳɹ�!").getBytes()));    
            /* ע����¼� */    
            channel.register(this.selector, SelectionKey.OP_READ);  
        } catch (ClosedChannelException e) {  
            throw new IOException("�رյ�ͨ��,�޷�ע�ᵽѡ����");  
        } catch (IOException e) {  
            throw new IOException("���ӷ��������ʧ��!");  
        }  
    }  
      
    /** 
     * ���¼� 
     * @param selectKey 
     * @throws Exception 
     */  
    public void read(SelectionKey selectKey) throws Exception{  
        try {  
            // �������ɶ�ͨ��  
            SocketChannel channel = (SocketChannel) selectKey.channel();   
            // ������ȡ�Ļ�����    
            ByteBuffer buffer = ByteBuffer.allocate(100);    
            channel.read(buffer);    
            byte[] data = buffer.array();    
            String msg = new String(data).trim();    
            System.out.println(msg);    
            ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
            // ����Ϣ���͸������    
            //channel.write(outBuffer);  
        } catch (Exception e) {  
            throw new IOException("����˽�ͨ���ر�,�޷���ͨ�����뻺��򽫻�������д��ͨ��!");  
        }  
    }  
      
    public static void main(String[] args) {  
        SocketClient sc = null;  
        try {  
            sc = new SocketClient("localhost", 2181);  
            sc. pollSelect();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  