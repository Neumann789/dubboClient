package com.client.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.alibaba.fastjson.JSON;
import com.client.MainUI;
import com.client.comm.FHBException;
import com.client.ui.dubbo.DubboServiceEntity;
import com.dubbo.entity.ServiceClass;
import com.dubbo.entity.ServiceMethod;
import com.dubbo.entity.ServiceParam;
import com.dubbo.util.ClassMatchUtil;
import com.dubbo.util.ConfigUtil;
import com.dubbo.util.DubboUtil;
import com.dubbo.util.FileUtil;
import com.dubbo.util.LoggerUtil;
import com.dubbo.util.PackageUtil;
import com.dubbo.util.StringUtil;
import com.dubbo.util.ZKUtil;

@SuppressWarnings("all")
public class DubboUI extends JPanel{

	
	private  static List<ServiceClass> serviceClassList=new ArrayList<ServiceClass>();
	
	private  static Map methodParamMap=new HashMap();
	
	private static String PATH_DEFAULT="D:/dubboclient/";
	
	private static String PATH_JAR="jars/";
	
	private static String ZKCONFIG_LIST="zkconfig.list";
	
	private static String DUBBO_LIST="dubbo.list";
	
	private static JPanel jpMid=new JPanel(new FlowLayout());  //new GridLayout(5,2)
	
	private static CardLayout cardLayout=new CardLayout(5, 5);
	
	private static JPanel jpParamContentList=new JPanel(cardLayout);
	
	private static JPanel jpParamButtonList=new JPanel();
	
	private static JPanel jpParamButtonAndContentList=new JPanel();
	
	private static JPanel jpDown=new JPanel();
	
	private static JComboBox dubboServiceListComboBox=new JComboBox(); 
	
	private static JComboBox dubboMethodsListComboBox=new JComboBox(); 
	
	//private static Box serviceParamListBox=Box.createVerticalBox();
	
	private static Box boxn=Box.createVerticalBox();
	
	private static JComboBox zkListComboBox=new JComboBox(); 
	
	private static JButton invokJButton=new JButton("执行");
	
	private static TextArea resultTextArea=new TextArea();
	
	private static List<TextArea> textAreaParamList=new ArrayList<TextArea>();
	
	private static Set<String> zkLineSet=new HashSet<String>();
	
	private static  ExecutorService executor = Executors. newCachedThreadPool();
	
	private static TreeMap<Integer, DubboServiceEntity> dubboServiceTreeMap=new TreeMap<Integer, DubboServiceEntity>();
	
	private static String[] titles = {"编号","服务名", "规则","开发人"};
	
	private static String[] classParamTitles = {"类名"};
	
	public DubboUI() {
		this.setLayout(new BorderLayout());
		this.add(jpMid,BorderLayout.NORTH);
		this.add(jpParamButtonAndContentList,BorderLayout.CENTER);
		this.add(jpDown,BorderLayout.SOUTH);
		initCompoment();
		initDataList();
		
		refresh();
	}
	
	
	private static void refresh(){
		
		initJarFiles();
		
		refreshCompoments();
		
	}




	
	private static void refreshCompoments() {
		
		dubboServiceListComboBox.removeAllItems();
		
		dubboMethodsListComboBox.removeAllItems();
		
		dubboServiceListComboBox.addItem("---请选择---");
		
		dubboMethodsListComboBox.addItem("---请选择---");
		
		for(ServiceClass serviceClass:serviceClassList){
			
			dubboServiceListComboBox.addItem(serviceClass.getClassName());
			
		}
		
		refreshZkListComboBox(zkLineSet);
		
		refreshDubboList();
	}


	private static void initDataList() {
		
		refreshZkList();
		
		refreshDubboList();
	}
	
	private static void refreshDubboList(){
		dubboServiceTreeMap=ConfigUtil.readDubboServices(PATH_DEFAULT+DUBBO_LIST);
	}
	
	private static void refreshZkList(){
		
		zkLineSet=ConfigUtil.readFile(PATH_DEFAULT+ZKCONFIG_LIST);
	}
	
	private static void refreshZkListComboBox(Set<String> lineSet){
		
		zkListComboBox.removeAllItems();
		
		for(String line:lineSet){
			
			zkListComboBox.addItem(line);
			
		}
	}



	private static void initJarFiles() {
		File defaultJarFileDirFile=new File(PATH_DEFAULT+PATH_JAR);
		if(!defaultJarFileDirFile.exists()||!defaultJarFileDirFile.isDirectory()){
			defaultJarFileDirFile.mkdirs();
		}
		if(defaultJarFileDirFile.isDirectory()){
			File[] jarFiles=defaultJarFileDirFile.listFiles(new FileFilter() {
				
				public boolean accept(File pathname) {
					
					if(pathname.getName().endsWith("jar")){
						return true;
					}
					
					return false;
				}
			});
			
			if(jarFiles==null||jarFiles.length==0){
				LoggerUtil.error(PATH_DEFAULT+PATH_JAR+"目录下没有jar!");
				return ;
			}
			String[] jarFilePathArr=new String[jarFiles.length];
			for(int i=0;i<jarFiles.length;i++){
				jarFilePathArr[i]=jarFiles[i].getAbsolutePath();
			}
			try {
				//PackageUtil.loadFacadeClassFromJars(serviceClassList, jarFilePathArr);
				serviceClassList.clear();
				PackageUtil.loadDubboServiceFromJars(serviceClassList, jarFilePathArr, dubboServiceTreeMap);
				LoggerUtil.info(jarFilePathArr+"加载成功!");
			} catch (Exception e) {
				LoggerUtil.error(jarFilePathArr+"加载失败:"+e.getMessage());
			}
			
		}
	}
	

    private static void initCompoment() {
    	
    	dubboServiceListComboBox.setBackground(Color.LIGHT_GRAY);
    	dubboServiceListComboBox.setEditable(true);
    	dubboServiceListComboBox.setPreferredSize(new Dimension(400, 20));
    	dubboServiceListComboBox.addItem("---请选择---");
    	
    	dubboMethodsListComboBox.setBackground(Color.LIGHT_GRAY);
    	dubboMethodsListComboBox.setPreferredSize(new Dimension(400, 20));
    	dubboMethodsListComboBox.addItem("---请选择---");
    	
    	zkListComboBox.setBackground(Color.ORANGE);
    	zkListComboBox.setPreferredSize(new Dimension(400, 20));
    	zkListComboBox.addItem("---请选择---");
    	
    	
    	
    	
    	Box box0=Box.createHorizontalBox();
    	Box box1=Box.createHorizontalBox();
    	Box box2=Box.createHorizontalBox();
    	Box box3=Box.createHorizontalBox();
    	Box box4=Box.createHorizontalBox();
    	

    	final JButton importJarJButton=new JButton("导入jar");
    	final JButton serviceRuleJButton=new JButton("规则设置");
    	final JButton refreshJarJButton=new JButton("刷新");
    	final JButton genJsonJButton=new JButton("类搜索");
    	box0.add(importJarJButton);
    	box0.add(Box.createHorizontalStrut(20));
    	box0.add(serviceRuleJButton);
    	box0.add(Box.createHorizontalStrut(20));
    	box0.add(refreshJarJButton);
    	box0.add(Box.createHorizontalStrut(20));
    	box0.add(genJsonJButton);

    	
    	JLabel label=new JLabel("服务:");  
    	box1.add(label); 
    	box1.add(dubboServiceListComboBox); 
    	
    	JLabel label2=new JLabel("方法:");  
    	box2.add(label2);  
    	box2.add(dubboMethodsListComboBox);  
    	
    	box3.add(new JLabel("zk地址:"));
    	zkListComboBox.setEditable(true);
    	box3.add(zkListComboBox);
    	
    	box4.add(Box.createHorizontalStrut(20));
    	box4.add(invokJButton);
    	
    	boxn.add(box0);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box1);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box2);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box3);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box4);
    	
    	boxn.add(Box.createVerticalStrut(40));
    	//boxn.add(serviceParamListBox);
    	
    	jpMid.add(boxn);
    	
    	Box paramsBox=Box.createVerticalBox();
    	paramsBox.add(jpParamButtonList);
    	paramsBox.add(Box.createVerticalStrut(10));
    	paramsBox.add(jpParamContentList);
    	jpParamButtonAndContentList.add(paramsBox);
    	
    	Box resultBox=Box.createVerticalBox();
    	Box resultHoriBox=Box.createHorizontalBox();
    	final JButton jsonFormatJButton=new JButton("格式化");
    	resultHoriBox.add(new JLabel("结果:"));
    	resultHoriBox.add(Box.createHorizontalStrut(300));
    	resultHoriBox.add(jsonFormatJButton);
    	resultBox.add(resultHoriBox);
    	resultTextArea.setEditable(false);
    	resultBox.add(resultTextArea);
    	jpDown.add(resultBox);
    	
    	
    	jsonFormatJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String resutText=resultTextArea.getText();
				String[] resultArr=resutText.split("\r\n\r\n");
				Map resultMap=JSON.parseObject(resultArr[1], Map.class);
				resultArr[1]=JSON.toJSONString(resultMap, true);
				resultTextArea.setText(resultArr[0]+"\r\n\r\n"+resultArr[1]);
			}
    		
    	});
    	
    	
    	
    	
    	importJarJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				openFileDialog(importJarJButton);
				
			}

			private void openFileDialog(final JButton jb1) {
				
				JFileChooser filechooser = new JFileChooser();
		        filechooser.setCurrentDirectory(new File(PATH_DEFAULT+PATH_JAR));
		        filechooser.setAcceptAllFileFilterUsed(false);
		        filechooser.setMultiSelectionEnabled(true);
		        filechooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
		            public boolean accept(File f) {
		            	if(f.isDirectory()||f.getName().endsWith("jar")){
		            		return true;
		            	}
		                return false;
		            }

		            public String getDescription() {
		                return "(*.jar)";
		            }

		        });
		        int returnVal = filechooser.showOpenDialog(jb1);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	boolean isImportSuc=true;
		        	String importResult="";
		        	for(File file:filechooser.getSelectedFiles()){
		        		try {
			            	String srcPath=PATH_DEFAULT+PATH_JAR+file.getName();
			            	FileUtil.moveFile(file.getAbsolutePath(),srcPath );
			            	//PackageUtil.loadFacadeClassFromJar(serviceClassList, destPath);
			        		//registDubboConsumer();
			        		//initDataList();
			            	isImportSuc=true;;
						} catch (Exception e) {
							LoggerUtil.error(filechooser.getSelectedFile().getAbsolutePath()+"jar导入失败:"+e.getMessage(),e);
							isImportSuc=false;
						}
		        	}
		            
		            
		            if(isImportSuc){
		            	JOptionPane.showMessageDialog(null, "jar导入成功", "info", JOptionPane.INFORMATION_MESSAGE);
		            }else{
		            	
		            	JOptionPane.showMessageDialog(null, "jar失败", "error", JOptionPane.ERROR_MESSAGE);
		            }

		        }
			}
		});
    	
    	
    	refreshJarJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				refresh();
			}
		});
    	
    	
    	genJsonJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				final JDialog jDialog=new JDialog();
				jDialog.setTitle("JSON");
				JPanel  operateJPanel=new JPanel();
				JScrollPane  listJPanel=new JScrollPane();
				final JPanel  ruleContentJPanel=new JPanel();
				jDialog.add(operateJPanel,BorderLayout.NORTH);
				jDialog.add(listJPanel,BorderLayout.CENTER);
				jDialog.add(ruleContentJPanel ,BorderLayout.SOUTH);
				ruleContentJPanel.setVisible(false);
				jDialog.setVisible(true);
				jDialog.setSize(500, 500);
		        jDialog.setLocationRelativeTo(null);
		        
		        
		        final JTextField classJtf=new JTextField(20);
		        JButton classSearchJButton=new JButton("搜索");
		        final JLabel countJtf=new JLabel();
		        //operateJPanel.add(addJButton);
		        operateJPanel.add(classJtf);
		        operateJPanel.add(classSearchJButton);
		        operateJPanel.add(countJtf);
		        
		        final JTable jTable = new JTable();
		        DefaultTableCellRenderer   r   =   new   DefaultTableCellRenderer();     
		        r.setHorizontalAlignment(JLabel.CENTER);     
		        jTable.setDefaultRenderer(Object.class, r);
		        Object[][] result=getColumnlistForParamClass();
		        jTable.setModel(new TableValues(classParamTitles,result));
			    listJPanel.setViewportView(jTable);
			    countJtf.setText(String.valueOf(result.length));
			    addJsonJTableMouseListener(jTable);
			    
			    classSearchJButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String classKey=classJtf.getText();
						
						freshJsonTable(jTable,countJtf, classKey);
					}
				});	
			    
			    
			    
				
			}
		});
    	
    	serviceRuleJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
				final JDialog jDialog=new JDialog();
				jDialog.setTitle("规则设置");
				JPanel  operateJPanel=new JPanel();
				JScrollPane  listJPanel=new JScrollPane();
				final JPanel  ruleContentJPanel=new JPanel();
				jDialog.add(operateJPanel,BorderLayout.NORTH);
				jDialog.add(listJPanel,BorderLayout.CENTER);
				jDialog.add(ruleContentJPanel ,BorderLayout.SOUTH);
				ruleContentJPanel.setVisible(false);
				jDialog.setVisible(true);
				jDialog.setSize(500, 500);
		        jDialog.setLocationRelativeTo(null);
		        
		        
		        JButton modifyJButton=new JButton("修改");
		        JButton saveJButton=new JButton("保存");
		        //operateJPanel.add(addJButton);
		        operateJPanel.add(modifyJButton);
		        operateJPanel.add(saveJButton);
		        
		        final JTable jTable = new JTable();
		        DefaultTableCellRenderer   r   =   new   DefaultTableCellRenderer();     
		        r.setHorizontalAlignment(JLabel.CENTER);     
		        jTable.setDefaultRenderer(Object.class, r);
		        jTable.setModel(new TableValues(titles,getColumnlistForDubboList()));
			    listJPanel.setViewportView(jTable);
		        	
		        
		        
		        final TextArea ruleContentArea=new TextArea();
		    	Box ruleContentBox=Box.createVerticalBox();
		    	ruleContentBox.add(new JLabel("规则"));
		    	ruleContentBox.add(ruleContentArea);
		    	ruleContentJPanel.add(ruleContentBox);
		        
		        
		        modifyJButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						if(!ruleContentJPanel.isVisible()){
							
							ruleContentArea.setText( ConfigUtil.readFile(PATH_DEFAULT+DUBBO_LIST,"\r\n"));
							
							ruleContentJPanel.setVisible(true);
						}
						
					}
				});
		        
		        saveJButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						if(ruleContentJPanel.isVisible()){
							

							FileUtil.saveFile(ruleContentArea.getText(), PATH_DEFAULT+DUBBO_LIST);
							
							refreshDubboList();
							
							freshRuleTable(jTable, ruleContentJPanel);
							
							ruleContentArea.setText("");
							
							ruleContentJPanel.setVisible(false);
							
						}
						
						
					}
				});
		        
		        
		        
		        
		        
		        
				
				
				
				
			}
		});
    	
    	
    	dubboServiceListComboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
				if(dubboServiceListComboBox.getSelectedItem()==null){
					return ;
				}
				
				methodParamMap.clear();
				
				String selectItem=dubboServiceListComboBox.getSelectedItem().toString();
				for(ServiceClass serviceClass:serviceClassList){
					if(selectItem.endsWith(serviceClass.getClassName())){
						
						
						
						List<ServiceMethod> serviceMethodList=serviceClass.getServiceMethods();
						
						dubboMethodsListComboBox.removeAllItems();
						dubboMethodsListComboBox.addItem("---请选择---");
						for(ServiceMethod sm:serviceMethodList){
							List<ServiceParam> serviceParamList=sm.getServiceParams();
							String serviceParamListStr="";
							for(ServiceParam serviceParam:serviceParamList){
								serviceParamListStr+=serviceParam.getParamName()
										.substring(serviceParam.getParamName().lastIndexOf(".")+1)+",";
							}
							if(serviceParamListStr.endsWith(",")){
								serviceParamListStr=serviceParamListStr.substring(0,serviceParamListStr.length()-1);
							}
							
							String methodItemName=sm.getMethodName()+"("+serviceParamListStr+")";
							
							methodParamMap.put(methodItemName, serviceParamList);
							
							dubboMethodsListComboBox.addItem(methodItemName);
						}
						
					}
				}
				
			}
		});
    	
    	
    	
    	
    	dubboMethodsListComboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				jpParamContentList.removeAll();
				jpParamButtonList.removeAll();
				textAreaParamList.clear();
				
				if(dubboMethodsListComboBox.getSelectedItem()!=null&&methodParamMap.containsKey(dubboMethodsListComboBox.getSelectedItem().toString())){
					List<ServiceParam> serviceParamList=(List<ServiceParam>)methodParamMap.get(dubboMethodsListComboBox.getSelectedItem().toString());
					
					for(final ServiceParam sp:serviceParamList){
						final JButton jb=new JButton(sp.getParamName()
								.substring(sp.getParamName().lastIndexOf(".")+1));
						final JComboBox childrenParamJComboBox=new JComboBox();
						final TextArea paramTextArea=new TextArea(sp.getParamJsonContent());
						if(sp.isAbstract()){
							childrenParamJComboBox.addItem("---请选择---");
							for(String key:sp.getChildrenParamMap().keySet()){
								childrenParamJComboBox.addItem(key);
							}

						}
						
						childrenParamJComboBox.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								String key=childrenParamJComboBox.getSelectedItem().toString();
								ServiceParam serviceParam=sp.getChildrenParamMap().get(key);
								paramTextArea.setText(serviceParam.getParamJsonContent());
								textAreaParamList.add(paramTextArea);
								sp.setRealUsePramName(key);
							}
						});
						
						jb.addActionListener(new ActionListener() {
							
							public void actionPerformed(ActionEvent e) {
								cardLayout.show(jpParamContentList, sp.getParamName());
								jb.setForeground(Color.green);
								for(Component c:jpParamButtonList.getComponents()){
									
									if(c.getClass()==JButton.class){
										
										if(jb!=c){
											c.setForeground(null);
										}
										
									}
									
								}
							}
						});
						jpParamButtonList.add(jb);
						if(sp.isAbstract()){
							jpParamButtonList.add(childrenParamJComboBox);
						}
						
						textAreaParamList.add(paramTextArea);
						
						jpParamContentList.add(paramTextArea, sp.getParamName());
					}
					jpMid.updateUI();
				}
				
			}
		});
    	
    	
    	
    	
    	
    	
    	invokJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String responseMsg="";
				long startTime=0L;
				long endTime=0L;
				startTime=System.currentTimeMillis();
				try {
					
				
				
				Future<String> future=executor.submit(new Callable<String>() {
					
					public String call() throws Exception {
						String result="";
						try {
							result=invokeService();
						} catch (Exception e2) {
							result="调用服务异常："+e2.getMessage();
							LoggerUtil.error("调用服务异常", e2);
						}
						
						return result;
					}
				});
				
				responseMsg=future.get(10,TimeUnit.SECONDS);
				
				}
//				catch (InvocationTargetException ite) {
//					logger.error(ite.getTargetException().getMessage());
//					responseMsg=ite.getTargetException().getMessage();
//				} 
				catch (Exception exception) {
					if(exception.getCause() instanceof InvocationTargetException){
						Throwable t=((InvocationTargetException)exception.getCause()).getTargetException();
						LoggerUtil.error(t.getMessage(),t);
						responseMsg=t.getMessage();
					}else{
						LoggerUtil.error(exception.getMessage(),exception);
						responseMsg=exception.toString()+":"+exception.getMessage();
					}
					
				}finally {
					endTime=System.currentTimeMillis();
					resultTextArea.setText("耗时："+(endTime-startTime)+"ms:\r\n\r\n"+responseMsg);
				} 
			}

			private String invokeService() throws InterruptedException, ExecutionException, TimeoutException,
					NoSuchMethodException, IllegalAccessException, InvocationTargetException {
				String responseMsg;
				String address=zkListComboBox.getSelectedItem().toString().trim();
				
				FileUtil.saveFile(address, PATH_DEFAULT+ZKCONFIG_LIST);
				zkLineSet.add(address);
				refreshZkListComboBox(zkLineSet);
				
				
				
				
				String className=dubboServiceListComboBox.getSelectedItem().toString();
				
				String methodItemStr=dubboMethodsListComboBox.getSelectedItem().toString();
				String methodName=methodItemStr.substring(0, methodItemStr.indexOf("("));
				
				List<ServiceParam> serviceParamList=(List<ServiceParam>)methodParamMap.get(methodItemStr);
				
				ZKUtil.connectZK(address);
				List<String> dubboConnectList=ZKUtil.getDubboServiceInfoList(className);
				
				if(dubboConnectList==null||dubboConnectList.size()==0){
					throw new FHBException("dubbo服务在此ZK上不存在!");
				}
				
				String params="";
				for(int i=0;i<serviceParamList.size();i++){
					params=textAreaParamList.get(i).getText().trim()+",";
				}
				params=params.substring(0, params.length()-1);//去除最后一个逗号
				
				Map<String , String> map=new HashMap<>();
				map.put("className", className);
				map.put("methodName", methodName);
				map.put("params", params);
				
				String response=DubboUtil.invokeDubbo(dubboConnectList.get(0), map);
				
				LoggerUtil.info("响应:"+JSON.toJSONString(response));
					
				return response;
			}
		});
		
	}
    
    private static Class getRealClass(String className) throws ClassNotFoundException{
    	
    	if("int".equals(className)){
    		return int.class;
    	}else if("byte".equals(className)){
    		return byte.class;
    	}else if("char".equals(className)){
    		return char.class;
    	}else if("short".equals(className)){
    		return short.class;
    	}else if("long".equals(className)){
    		return long.class;
    	}else if("float".equals(className)){
    		return float.class;
    	}else if("double".equals(className)){
    		return double.class;
    	}else if("boolean".equals(className)){
    		return boolean.class;
    	}else{
    		
    		for(Class clazz:PackageUtil.paramClassSet){
    			if(clazz.getName().equals(className)){
    				return clazz;
    			}
    		}	
    		
    		return null;
    	}
    	
    }
    
    public static void unLoad(){
    	
    	serviceClassList.clear();
    	
    	dubboServiceListComboBox.removeAll();
    	
    	zkListComboBox.removeAll();
    	
    }
    
    private static void restart(){
    	
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    	    public void run() {
    	        try {
//    	            String restartCmd = "nohup java -jar xxx.jar";
    	        	MainUI.main(null);
    	            Thread.sleep(10000);
    	            LoggerUtil.info("重启成功");
    	        } catch (Exception e) {
    	        	LoggerUtil.error("重启失败", e);
    	        }
    	    }
    	});
    	LoggerUtil.info("重启......");
    	System.exit(0);
    	
    }
    
    private static Object[][] getColumnlistForDubboList(){
    	
    	if(dubboServiceTreeMap.isEmpty()){
    		
    		return new Object[][]{
    				{"0","service","*Service*","test"}
    		};
    		
    	}
    	
    	Object[][] columnList=new Object[dubboServiceTreeMap.size()][4];
    	
    	
    	Set<Entry<Integer, DubboServiceEntity>> entrySet=dubboServiceTreeMap.entrySet();
    	
    	Iterator<Entry<Integer, DubboServiceEntity>> iterator=entrySet.iterator();
    	
    	for(int i=0;i<entrySet.size();iterator.hasNext()) {
    		Entry<Integer, DubboServiceEntity> e = iterator.next();
    		columnList[i]=new Object[]{
    				e.getKey(),
    				e.getValue().getServiceName(),
    				e.getValue().getServiceRule(),
    				e.getValue().getServiceDeveloper()
    		};
    		i++;
        }
    	
    	return columnList;
    }
    
private static Object[][] getColumnlistForParamClass(){
    	
    	
    	Object[][] columnList=new Object[PackageUtil.paramClassSet.size()][1];
    	
    	
    	
    	Iterator<Class> iterator=PackageUtil.paramClassSet.iterator();
    	
    	for(int i=0;i<PackageUtil.paramClassSet.size();iterator.hasNext()) {
    		Class e = iterator.next();
    		columnList[i]=new Object[]{
    				e.getName()
    		};
    		i++;
        }
    	
    	return columnList;
    }
    

	private static Object[][] getColumnlistForParamClass(String clazzRuleKey){
		
		HashSet<Class> resultSet=new HashSet<>();
		
		clazzRuleKey=clazzRuleKey.trim();
		
		clazzRuleKey="*"+clazzRuleKey+"*";
		
		for(Class clazz:PackageUtil.paramClassSet){
			if(ClassMatchUtil.isMatch(clazz.getName(), clazzRuleKey)){
				resultSet.add(clazz);
			}
		}
		
		Object[][] columnList=new Object[resultSet.size()][1];
		
		int i=0;
		for(Class clazz:resultSet){
			if(ClassMatchUtil.isMatch(clazz.getName(), clazzRuleKey)){
				columnList[i]=new Object[]{
						clazz.getName()
				};
				i++;
			}
		}
		
		
		return columnList;
	}
    
    private static  void freshRuleTable(JTable jTable,JPanel  ruleContentJPanel){
    	
    	jTable.setModel(new TableValues(titles,getColumnlistForDubboList()));
    	
    	ruleContentJPanel.validate();
    	
    }
    
    private static  void freshJsonTable(JTable jTable,JLabel countJtf,String classRuleKey){
    	Object[][] result=getColumnlistForParamClass(classRuleKey);
    	jTable.setModel(new TableValues(classParamTitles,result));
    	
    	countJtf.setText(String.valueOf(result.length));
    	
    }
    
    public static void addJsonJTableMouseListener(final JTable jTable){
		
		 jTable.addMouseListener(new MouseAdapter() {
		    	
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
		    		
		    		if(e.getClickCount()==2){
		    			int row=jTable.rowAtPoint(e.getPoint());
		    			String value=(String)jTable.getValueAt(row, 0);
		    			String jsonStr="";
		    			try {
							jsonStr=StringUtil.genJsonStrPrettyFormat(Class.forName(value).newInstance());
							showMsg(jsonStr);
						} catch (Exception e1) {
							showMsg(e1.getMessage());
						}
		    		}
		    	}
			});
		
	}
    
    public static void showMsg(String cntext){
    	final JDialog jDialog=new JDialog();
		jDialog.setTitle("信息");
		final JPanel  contentJPanel=new JPanel();
		JScrollPane  scrollPane=new JScrollPane(contentJPanel);
		jDialog.add(scrollPane ,BorderLayout.CENTER);
		scrollPane.setVisible(true);
		jDialog.setVisible(true);
		jDialog.setSize(500, 500);
        jDialog.setLocationRelativeTo(null);
        
        JTextArea jtaContext=new JTextArea(500, 500);
        jtaContext.setText(cntext);
        contentJPanel.add(jtaContext);
    }

}


class TableValues extends AbstractTableModel{
         private static final long serialVersionUID = -8430352919270533604L;
         public final static int NAME = 0;
         public final static int GENDER = 1;
         public  String[] titles;
         public Object[][] columnlist;
         
        public TableValues(String[] titles,Object[][] columnlist) {
        	 this.titles=titles;
        	 this.columnlist=columnlist;
		}
         
         public int getColumnCount() {
        	 
        	 if(columnlist.length==0){
        		 return 0;
        	 }
             return  columnlist[0].length;
         }
         public int getRowCount() {
                   return columnlist.length;
         }
         public Object getValueAt(int rowIndex, int columnIndex) {
                   return columnlist[rowIndex][columnIndex];
         }


         public String getColumnName(int column){
                   return titles[column];
         }
} 

