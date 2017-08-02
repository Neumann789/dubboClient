package com.dubbo.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.client.comm.FHBException;

public class ZKUtil {
	
	private static ZooKeeper zk = null;
	
	private static Map<String, ZooKeeper> zooKeeperMap=new ConcurrentHashMap<>();
	
	private static String DUBBO_SERVICE_PATH_TEMPLATE = "/dubbo/#dubboservice#/providers";
	
	public static void main(String[] args) throws Exception {
//		String str="dubbo%3A%2F%2F192.18.224.195%3A20896%2Fcom.zb.payment.yw.fir.facade.service.YWFirService%3Fanyhost%3Dtrue%26application%3Dpayment_yw_fbm%26dubbo%3D2.5.3%26interface%3Dcom.zb.payment.yw.fir.facade.service.YWFirService%26methods%3DnotifyTxnTxStatus%2CregisterTxnInfo%26pid%3D409%26side%3Dprovider%26timestamp%3D1499939393236";
//		String connect=extractConnectFromUrl(str);
//		System.out.println("dubbo connect :"+connect);
		
		connectZK("192.168.224.199:2181");
		List<String> resultList=getDubboServiceInfoList("com.zb.payment.yw.fir.facade.service.YWFirService");
		System.out.println(resultList);
	}
	
	
	public static void connectZK(String connectUrl){
		
		zk = zooKeeperMap.get(connectUrl);
		
		if(zk!=null){
			
			return ;
			
		}
		
		try {
			LoggerUtil.info("连接zk服务");
			ZooKeeper zk0 = new ZooKeeper(connectUrl,20000,
			        new MyWatcher(), true);
			zk=zk0;
			zooKeeperMap.put(connectUrl, zk0);
		} catch (Exception e) {
			LoggerUtil.error("连接zk服务异常",e);
		}
	}
	/**
	 * ls /dubbo/com.zb.payment.yw.fir.facade.service.YWFirService/providers
	 * 
	 * [dubbo://192.168.224.195:20896/com.zb.payment.yw.fir.facade.service.YWFirService
	 * ?anyhost=true&application=payment_yw_fbm&dubbo=2.5.3&
	 * interface=com.zb.payment.yw.fir.facade.service.YWFirService
	 * &methods=notifyTxnTxStatus,registerTxnInfo&pid=409&side=provider&timestamp=1499939393236]
	 * @return
	 */
	public static List<String> getDubboServiceInfoList(String dubboService){
		String dubboServicePath=DUBBO_SERVICE_PATH_TEMPLATE.replace("#dubboservice#", dubboService);
		List<String> list = new ArrayList<>();
		List<String> returnList = new ArrayList<>();
		try {
			list=zk.getChildren(dubboServicePath, null);
			
			for(String str:list){
				String connect=extractConnectFromUrl(str);
				returnList.add(connect);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FHBException("获取dubbo服务地址列表失败!");
		} 
		return returnList;
	}


	private static String extractConnectFromUrl(String url) throws UnsupportedEncodingException {
		String dubboUrl=URLDecoder.decode(url,"UTF-8");
		int index1=dubboUrl.indexOf("/", 8);
		return dubboUrl.substring(8, index1);
	}
	
	
	
	
	public static String telnetZK(String ip, int port, String request) {

		Socket socket;
		String result="";
		try {
			socket = new Socket(ip, port);
			PrintWriter pw = new PrintWriter(socket.getOutputStream());

			String msg = "\r\n";
			pw.write(msg);
			pw.flush();

			InputStream ins = socket.getInputStream();
			Scanner sc = new Scanner(System.in);
			
			byte[] tt = new byte[1024];
			int len=0;
			
			len=ins.read(tt, 0, tt.length);

			pw.write("invoke " + request + "\r\n");
			pw.flush();
			len=ins.read(tt, 0, tt.length);
			result = new String(tt,0,len,"gbk");
			result = result.split("\r\n")[0];
			LoggerUtil.info("dubbo���ؽ��"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
    private static class MyWatcher implements Watcher {
        public void process(WatchedEvent event) {
            LoggerUtil.info("收到zk通知："+event);
        }
    }

}
