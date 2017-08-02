package com.dubbo.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class DubboUtil {


	public static String invokeDubbo(String dubboUrl, Map<String, String> map) {

		String className = map.get("className");
		String methodName = map.get("methodName");
		String params = map.get("params");

		String request = className + "." + methodName + "(" + params + ")";
		
		String ip=dubboUrl.split(":")[0];
		
		int port=Integer.parseInt(dubboUrl.split(":")[1]);
		
		return telnetDubbo(ip, port, request);
	}

	public static String telnetDubbo2(String ip, int port, String request) {

		Socket socket;
		String result="";
		try {
			socket = new Socket(ip, port);
			PrintWriter pw = new PrintWriter(socket.getOutputStream());

			String msg = "\r\n";
			pw.write(msg);
			pw.flush();

			InputStream ins = socket.getInputStream();
			byte[] tt = new byte[1024];
			ins.read(tt, 0, tt.length);

			pw.write("invoke " + request + "\r\n");
			pw.flush();
			ins.read(tt, 0, tt.length);
			result = new String(tt, "gbk");
			LoggerUtil.info(result);
		} catch (Exception e) {
			
		}

		return result;
	}
	
	
	
	public static String telnetDubbo(String ip, int port, String request) {

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	

	/*public static Object getDubboService(String url, Class serviceClass) {

		ApplicationConfig application = new ApplicationConfig();
		application.setName("dubbotest");
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setProtocol("zookeeper");
		registryConfig.setAddress(url);

		ReferenceConfig referenceConfig = new ReferenceConfig<>();

		referenceConfig.setApplication(application);

		referenceConfig.setRegistry(registryConfig);

		referenceConfig.setInterface(serviceClass);

		// referenceConfig.setVersion("1.0");

		return referenceConfig.get();

	}

	public static void doInvoke() throws Exception {
		Invocation invocation = new RpcInvocation();
		Class serviceType = YWTransReceiverService.class;
		URL url = new URL("zookeeper", "192.168.224.199", 2181);
		ExchangeClient[] clients = new ExchangeClient[] {};
		// ReferenceCountExchangeClient ReferenceCountExchangeClient=new
		// ReferenceCountExchangeClient();
		Result result = new DubboInvoker(serviceType, url, clients).invoke(invocation);
		LoggerUtil.info("������ݣ�" + JSONObject.toJSONString(result));

	}

	public static void doInvoke2() throws Exception {

		
		 * {methods=syncSendAndReceive, application=dubbotest, check=false,
		 * pid=10748, interface=com.zb.payment.yw.mgw.facade.service.
		 * YWTransReceiverService, timestamp=1499269021603, dubbo=2.5.3,
		 * revision=1.0-SNAPSHOT, side=consumer, retries=0, anyhost=true,
		 * timeout=10000}
		 

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("methods", "syncSendAndReceive");
		paramMap.put("application", "dubbotest");
		paramMap.put("check", "false");
		paramMap.put("pid", "10748");
		paramMap.put("interface", "com.zb.payment.yw.mgw.facade.service.YWTransReceiverService");
		paramMap.put("timestamp", "1499269021603");
		paramMap.put("dubbo", "2.5.3");
		paramMap.put("revision", "1.0-SNAPSHOT");
		paramMap.put("side", "consumer");
		paramMap.put("retries", "0");
		paramMap.put("anyhost", "true");
		paramMap.put("timeout", "10000");
		paramMap.put("codec", "dubbo");

		URL url = new URL("dubbo", "192.168.224.196", 20881,
				"com.zb.payment.yw.mgw.facade.service.YWTransReceiverService", paramMap);

		DubboProtocol dubboProtocol = new DubboProtocol();
		Field field = DubboProtocol.class.getDeclaredField("requestHandler");
		field.setAccessible(true);
		HeaderExchangeHandler headerExchangeHandler = new HeaderExchangeHandler(
				(ExchangeHandler) field.get(dubboProtocol));
		ChannelHandler handler = new DecodeHandler(headerExchangeHandler);
		NettyClient nettyClient = new NettyClient(url, handler);

		RpcInvocation invocation = getInvocation();
		Request req = new Request();
		req.setVersion("2.0.0");
		req.setTwoWay(true);
		req.setData(invocation);
		DefaultFuture future = new DefaultFuture(nettyClient, req, 20000);

		nettyClient.send(req);

		Object obj = future.get();

		LoggerUtil.info("������ݣ�" + JSONObject.toJSONString(obj));
	}

	public static RpcInvocation getInvocation() {

		RpcInvocation invocation = new RpcInvocation();

		HFTXPayRequest request = new HFTXPayRequest();
		request.setChannelSystemId("CHINAPNRYW0606");// ������š����䡿
		request.setExchangeTypeId("06");// �������͡����䡿
		request.setOrderNo("1498474395585");
		request.setCertId("340826199202172456");// �û����֤�� �����䡿
		request.setReqExt("");// ��չ�� �Ǳ���

		invocation.setArguments(new Object[] { request });
		invocation.setMethodName("syncSendAndReceive");
		invocation.setParameterTypes(new Class[] { AbstractRequest.class });
		
		 * {path=com.zb.payment.yw.mgw.facade.service.YWTransReceiverService,
		 * interface=com.zb.payment.yw.mgw.facade.service.
		 * YWTransReceiverService, timeout=10000, version=0.0.0}
		 
		invocation.setAttachment("path", "com.zb.payment.yw.mgw.facade.service.YWTransReceiverService");
		invocation.setAttachment("interface", "com.zb.payment.yw.mgw.facade.service.YWTransReceiverService");
		invocation.setAttachment("timeout", "10000");
		invocation.setAttachment("version", "0.0.0");
		return invocation;
	}

	public void doInvoke3() throws Exception {

		Socket socket = new Socket("192.168.224.196", 2181);
	}

	public static void main(String[] args) throws Exception {

		testMgwService();
		// doInvoke();
		// doInvoke2();
	}

	private static void testMgwService() {
		YWTransReceiverService transReceiverService = (YWTransReceiverService) DubboUtil
				.getDubboService("192.168.224.199:2181", YWTransReceiverService.class);
		HFTXPayRequest request = new HFTXPayRequest();
		request.setChannelSystemId("CHINAPNRYW0606");// ������š����䡿
		request.setExchangeTypeId("06");// �������͡����䡿
		request.setOrderNo("1498474395585");
		request.setCertId("340826199202172456");// �û����֤�� �����䡿
		request.setReqExt("");// ��չ�� �Ǳ���
		transReceiverService.syncSendAndReceive(request);
		MgwResult<AbstractResponse> resp = transReceiverService.syncSendAndReceive(request);
		LoggerUtil.info("����״̬��" + resp.getRspCode());
		LoggerUtil.info("����˵����" + resp.getRspMsg());
		LoggerUtil.info("������ݣ�" + JSONObject.toJSONString(resp));
	}
*/
}
