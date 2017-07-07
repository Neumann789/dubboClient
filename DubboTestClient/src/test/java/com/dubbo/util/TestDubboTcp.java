package com.dubbo.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestDubboTcp {
	
		public static void main(String[] args) throws Exception, Throwable {
			String ip="127.0.0.1";
			//	ip="192.168.224.196";
			ip="192.168.224.195";
			Socket socket = new Socket(ip, 20896);
			
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			
			String msg="\r\n";
			pw.write(msg);
			pw.flush();
			
			InputStream ins=socket.getInputStream();
			Scanner sc=new Scanner(System.in);
			  byte[] tt=new byte[1024];
			  int z;
/*			  while((z=ins.read(tt, 0, tt.length))!=-1){
			   System.out.println(new String(tt,"utf-8"));
			   String s=sc.nextLine();
			   pw.write(s+"\r\n");
			   pw.flush();
			  }*/
			  
			  ins.read(tt, 0, tt.length);
			  System.out.println(new String(tt,"utf-8"));
			  
			  //pw.write("invoke com.zb.payment.yw.mgw.facade.service.YWTransReceiverService.syncSendAndReceive({\"bidEndDate\":\"20170801000000\"})\r\n");
			  pw.write("invoke com.zb.payment.yw.boss.facade.service.YWBossService.addBidInfo({\"bidEndDate\":\"20170801000000\"})\r\n");
			  pw.flush();
			  ins.read(tt, 0, tt.length);
			  System.out.println(new String(tt,"gbk"));

			
			
		}
		
		

}
