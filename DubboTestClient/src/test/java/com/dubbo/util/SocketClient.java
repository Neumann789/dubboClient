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
            channel.configureBlocking(false);  
            this.selector = Selector.open();  
            channel.connect(new InetSocketAddress(ip, port));   
            channel.register(this.selector, SelectionKey.OP_CONNECT);    
        } catch(ClosedChannelException e1){  
            System.out.println("");  
            e1.printStackTrace();  
        } catch (IOException e2) {  
            System.out.println("");  
            try {  
                if(channel != null) channel.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            e2.printStackTrace();  
        }  
    }  
      
    public void  pollSelect() throws Exception {    
        while ( this.selector.select() > 0 ) {  
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();    
            while (ite.hasNext()) {    
                SelectionKey selectKey = (SelectionKey) ite.next();    
                ite.remove();    
                process(selectKey);  
            }    
        }    
    }  
      
    /** 
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
     * @param selectKey 
     * @throws Exception 
     */  
    public void connect(SelectionKey selectKey) throws Exception{  
        try {  
            SocketChannel channel = (SocketChannel) selectKey    
                    .channel();    
            if(channel.isConnectionPending()){  
                channel.finishConnect();    
            }  
            channel.configureBlocking(false);    
            channel.write(ByteBuffer.wrap(new String("").getBytes()));    
            channel.register(this.selector, SelectionKey.OP_READ);  
        } catch (ClosedChannelException e) {  
            throw new IOException("");  
        } catch (IOException e) {  
            throw new IOException("");  
        }  
    }  
      
    /** 
     * @param selectKey 
     * @throws Exception 
     */  
    public void read(SelectionKey selectKey) throws Exception{  
        try {  
            SocketChannel channel = (SocketChannel) selectKey.channel();   
            ByteBuffer buffer = ByteBuffer.allocate(100);    
            channel.read(buffer);    
            byte[] data = buffer.array();    
            String msg = new String(data).trim();    
            System.out.println(msg);    
            ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
            //channel.write(outBuffer);  
        } catch (Exception e) {  
            throw new IOException("");  
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