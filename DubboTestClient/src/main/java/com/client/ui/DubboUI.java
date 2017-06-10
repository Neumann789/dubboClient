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
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.client.MainUI;
import com.client.ui.dubbo.DubboServiceEntity;
import com.dubbo.entity.ServiceClass;
import com.dubbo.entity.ServiceMethod;
import com.dubbo.entity.ServiceParam;
import com.dubbo.util.ConfigUtil;
import com.dubbo.util.FileUtil;
import com.dubbo.util.PackageUtil;
import com.dubbo.util.SpringUtil;
import com.dubbo.util.StringUtil;

public class DubboUI extends JPanel{

	private final static Logger logger = LoggerFactory.getLogger(DubboUI.class); 
	
	private  static List<ServiceClass> serviceClassList=new ArrayList<ServiceClass>();
	
	private  static Map methodParamMap=new HashMap();
	
	private static String PATH_DEFAULT="D:/dubbclient/";
	
	private static String PATH_JAR="jars/";
	
	private static String ZKCONFIG_LIST="zkconfig.list";
	
	private static String DUBBO_LIST="dubbo.list";
	
	private static JPanel jpMid=new JPanel(new FlowLayout());  //new GridLayout(5,2)
	
	private static CardLayout cardLayout=new CardLayout(5, 5);
	
	private static JPanel jpParamContentList=new JPanel(cardLayout);
	
	private static JPanel jpParamButtonList=new JPanel();
	
	private static JPanel jpParamButtonAndContentList=new JPanel();
	
	private static JPanel jpDown=new JPanel();
	
	//dubbo���������б�
	private static JComboBox dubboServiceListComboBox=new JComboBox(); 
	
	//dubbo���񷽷������б�
	private static JComboBox dubboMethodsListComboBox=new JComboBox(); 
	
	//private static Box serviceParamListBox=Box.createVerticalBox();
	
	private static Box boxn=Box.createVerticalBox();
	
	private static JComboBox zkListComboBox=new JComboBox(); 
	
	private static JButton invokJButton=new JButton("���ýӿ�");
	
	private static TextArea resultTextArea=new TextArea();
	
	private static List<TextArea> textAreaParamList=new ArrayList<TextArea>();
	
	private static Set<String> zkLineSet=new HashSet<String>();
	
	private static  ExecutorService executor = Executors. newCachedThreadPool();
	
	private static TreeMap<Integer, DubboServiceEntity> dubboServiceTreeMap=new TreeMap<Integer, DubboServiceEntity>();
	
	private static String[] titles = {"���","������", "������·��","������"};
	
	public DubboUI() {
		this.setLayout(new BorderLayout());
		this.add(jpMid,BorderLayout.NORTH);
		this.add(jpParamButtonAndContentList,BorderLayout.CENTER);
		this.add(jpDown,BorderLayout.SOUTH);
		initCompoment();
		//��ʼ������
		initDataList();
	}
	
	
	private static void refresh(){
		
		
		
		
		
		//����jar�е�facade��
		initJarFiles();
		
		//���������ע�ᵽspring������ȥ
		registDubboConsumer();
		
		//ˢ�����
		refreshCompoments();
		
	}



	private static void registDubboConsumer() {
		
		SpringUtil.startSpring();
		
		for(ServiceClass serviceClass:serviceClassList){
			
			SpringUtil.registDubboConsumer(serviceClass.getClassName());
			
		}
	}

	
	private static void refreshCompoments() {
		
		dubboServiceListComboBox.removeAllItems();
		
		dubboMethodsListComboBox.removeAllItems();
		
		dubboServiceListComboBox.addItem("---��ѡ��---");
		
		dubboMethodsListComboBox.addItem("---��ѡ��---");
		
		for(ServiceClass serviceClass:serviceClassList){
			
			dubboServiceListComboBox.addItem(serviceClass.getOwnerName()+"--"+serviceClass.getClassName());
			
		}
		
		refreshZkListComboBox(zkLineSet);
		
		//ˢ��dubbo��������
		refreshDubboList();
	}


	private static void initDataList() {
		
		//ˢ��zk����
		refreshZkList();
		
		//ˢ��dubbo��������
		refreshDubboList();
	}
	
	private static void refreshDubboList(){
		
		dubboServiceTreeMap=ConfigUtil.readDubboServices(PATH_DEFAULT+DUBBO_LIST);
		//dubboServiceTreeMap.put(0, new DubboServiceEntity("test", "*facade*", "����"));
		//dubboServiceTreeMap.put(1, new DubboServiceEntity("service", "*Service*", "����"));
		
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
				logger.error(PATH_DEFAULT+PATH_JAR+"��û��jar�ļ�!");
				return ;
			}
			String[] jarFilePathArr=new String[jarFiles.length];
			for(int i=0;i<jarFiles.length;i++){
				jarFilePathArr[i]=jarFiles[i].getAbsolutePath();
			}
			try {
				//PackageUtil.loadFacadeClassFromJars(serviceClassList, jarFilePathArr);
				serviceClassList.clear();//��շ��񼯺�
				PackageUtil.loadDubboServiceFromJars(serviceClassList, jarFilePathArr, dubboServiceTreeMap);
				logger.info(jarFilePathArr+"���سɹ�!");
			} catch (Exception e) {
				logger.error(jarFilePathArr+"����ʧ��!ʧ��ԭ��:"+e.getMessage());
			}
			
		}
	}
	

    private static void initCompoment() {
    	
    	dubboServiceListComboBox.setBackground(Color.LIGHT_GRAY);
    	dubboServiceListComboBox.setPreferredSize(new Dimension(400, 20));
    	dubboServiceListComboBox.addItem("---��ѡ��---");
    	
    	dubboMethodsListComboBox.setBackground(Color.LIGHT_GRAY);
    	dubboMethodsListComboBox.setPreferredSize(new Dimension(400, 20));
    	dubboMethodsListComboBox.addItem("---��ѡ��---");
    	
    	zkListComboBox.setBackground(Color.ORANGE);
    	zkListComboBox.setPreferredSize(new Dimension(400, 20));
    	zkListComboBox.addItem("---��ѡ��---");
    	
    	
    	
    	
    	Box box0=Box.createHorizontalBox();
    	Box box1=Box.createHorizontalBox();
    	Box box2=Box.createHorizontalBox();
    	Box box3=Box.createHorizontalBox();
    	Box box4=Box.createHorizontalBox();
    	

    	final JButton importJarJButton=new JButton("����jar��");
    	final JButton serviceRuleJButton=new JButton("��������");
    	final JButton refreshJarJButton=new JButton("ˢ�·���");
    	box0.add(importJarJButton);
    	box0.add(Box.createHorizontalStrut(20));
    	box0.add(serviceRuleJButton);
    	box0.add(Box.createHorizontalStrut(20));
    	box0.add(refreshJarJButton);

    	
    	//��ʾdubbo�����б�
    	JLabel label=new JLabel("dubbo�����б�:");  
    	box1.add(label); 
    	box1.add(dubboServiceListComboBox); 
    	
    	//��ʾdubbo�����б�
    	JLabel label2=new JLabel("dubbo�����б�:");  
    	box2.add(label2);  
    	box2.add(dubboMethodsListComboBox);  
    	
    	box3.add(new JLabel("zk��ַ:"));
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
    	resultBox.add(new JLabel("��Ӧ���:"));
    	resultTextArea.setEditable(false);
    	resultBox.add(resultTextArea);
    	jpDown.add(resultBox);
    	
    	
    	
    	
    	importJarJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				openFileDialog(importJarJButton);
				
			}

			private void openFileDialog(final JButton jb1) {
				
				JFileChooser filechooser = new JFileChooser();//�����ļ�ѡ����
		        filechooser.setCurrentDirectory(new File(PATH_DEFAULT+PATH_JAR));//���õ�ǰĿ¼
		        filechooser.setAcceptAllFileFilterUsed(false);
		        //��ʾ�����ļ�
		        filechooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
		            public boolean accept(File f) {
		            	if(f.isDirectory()||f.getName().endsWith("jar")){
		            		return true;
		            	}
		                return false;
		            }

		            public String getDescription() {
		                return "�����ļ�(*.jar)";
		            }

		        });
		        int returnVal = filechooser.showOpenDialog(jb1);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	boolean isImportSuc=true;
		        	String importResult="";
		            try {
		            	//�ƶ�jar��
		            	String srcPath=PATH_DEFAULT+PATH_JAR+filechooser.getSelectedFile().getName();
		            	//String destPath=StringUtil.addFileStamp(srcPath);//���ʱ���
		            	//FileUtil.moveFile(filechooser.getSelectedFile().getAbsolutePath(),destPath );
		            	//PackageUtil.loadFacadeClassFromJar(serviceClassList, destPath);
		            	//unLoad();//ж��
		            	//���������ע�ᵽspring������ȥ
		        		//registDubboConsumer();
		        		//��ʼ���б�����
		        		//initDataList();
		            	isImportSuc=true;;
						logger.info("jar������ɹ�,������Ч������!");
					} catch (Exception e) {
						logger.error(filechooser.getSelectedFile().getAbsolutePath()+"jarʧ��,ʧ��ԭ��:"+e.getMessage());
						e.printStackTrace();
						isImportSuc=false;
					}
		            
		            if(isImportSuc){
		            	int res=JOptionPane.showConfirmDialog(null, "jar������ɹ�,��Ч������", "��ʾ", JOptionPane.YES_NO_OPTION);
		            	if(res==JOptionPane.YES_OPTION){ 
		            		System.out.println("ѡ���Ǻ�ִ�еĴ���");    //������ǡ���ִ����������
		            		restart();
		            	}else{
		            		 System.out.println("ѡ����ִ�еĴ���");    //������񡱺�ִ����������
		            	 
		            	} 
		            }else{
		            	
		            	JOptionPane.showMessageDialog(null, "�쳣", "jar������ʧ��", JOptionPane.ERROR_MESSAGE);
		            }

		        }
			}
		});
    	
    	
    	refreshJarJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				refresh();
			}
		});
    	
    	serviceRuleJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
				final JDialog jDialog=new JDialog();
				jDialog.setTitle("���÷������");
				JPanel  operateJPanel=new JPanel();
				JScrollPane  listJPanel=new JScrollPane();
				final JPanel  ruleContentJPanel=new JPanel();
				jDialog.add(operateJPanel,BorderLayout.NORTH);
				jDialog.add(listJPanel,BorderLayout.CENTER);
				jDialog.add(ruleContentJPanel ,BorderLayout.SOUTH);
				ruleContentJPanel.setVisible(false);
				jDialog.setVisible(true);
				jDialog.setSize(500, 500);
		        jDialog.setLocationRelativeTo(null);//���ô��ھ���
		        
		        
		       // JButton addJButton=new JButton("���");
		        JButton modifyJButton=new JButton("�޸�");
		        JButton saveJButton=new JButton("����");
		        //operateJPanel.add(addJButton);
		        operateJPanel.add(modifyJButton);
		        operateJPanel.add(saveJButton);
		        
		        final JTable jTable = new JTable();
		        DefaultTableCellRenderer   r   =   new   DefaultTableCellRenderer();     
		        r.setHorizontalAlignment(JLabel.CENTER);     
		        jTable.setDefaultRenderer(Object.class, r);
		        jTable.setModel(new TableValues(titles,getColumnlistForDubboList()));
			    listJPanel.setViewportView(jTable);
		        	
		        
		        
		        
		        /*JButton closeJButton=new JButton("�ر�");
		        JButton saveJButton=new JButton("����");
		        closeOrSaveJPanel.add(closeJButton);
		        closeOrSaveJPanel.add(saveJButton);*/
		        
		        final TextArea ruleContentArea=new TextArea();
		    	Box ruleContentBox=Box.createVerticalBox();
		    	ruleContentBox.add(new JLabel("���������ļ�չʾ��"));
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
						dubboMethodsListComboBox.addItem("---��ѡ��---");
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
						TextArea paramTextArea=new TextArea(sp.getParamJsonContent());
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
						
						return invokeService();
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
						logger.error(t.getMessage(),t);
						responseMsg=t.getMessage();
					}else{
						logger.error(exception.getMessage(),e);
						responseMsg=exception.toString()+":"+exception.getMessage();
					}
					
				}finally {
					endTime=System.currentTimeMillis();
					resultTextArea.setText(responseMsg+" ��ʱ��"+(endTime-startTime)+"ms");
				} 
			}

			private String invokeService() throws InterruptedException, ExecutionException, TimeoutException,
					NoSuchMethodException, IllegalAccessException, InvocationTargetException {
				String responseMsg;
				String address=zkListComboBox.getSelectedItem().toString().trim();
				
				FileUtil.saveFile(address, PATH_DEFAULT+ZKCONFIG_LIST);
				zkLineSet.add(address);
				refreshZkListComboBox(zkLineSet);
				
				//SpringUtil.unregistRegistryConfigs();
				SpringUtil.registZK(address, address);
				
				String className=dubboServiceListComboBox.getSelectedItem().toString();
				
				String methodItemStr=dubboMethodsListComboBox.getSelectedItem().toString();
				String methodName=methodItemStr.substring(0, methodItemStr.indexOf("("));
				
				List<ServiceParam> serviceParamList=(List<ServiceParam>)methodParamMap.get(methodItemStr);
				
				
				
				Class[] parameterTypes=new Class[serviceParamList.size()];
				Object[] parameter=new Object[serviceParamList.size()];
				for(int i=0;i<serviceParamList.size();i++){
					try {
						parameterTypes[i]=getRealClass(serviceParamList.get(i).getParamName());
						parameter[i]=JSON.parseObject(textAreaParamList.get(i).getText().trim(), parameterTypes[i]);
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						throw new RuntimeException("�ӿڵ���ʧ�ܣ�ʧ��ԭ��:"+e1.getMessage());
					}
				}
				
				Object instance=SpringUtil.getBean(className.substring(className.lastIndexOf("--")+2,className.length()));
				
					
					Method method=instance.getClass().getMethod(methodName, parameterTypes);
					Object response=method.invoke(instance, parameter);
					/*FutureTask futureTask=new FutureTask(new Callable() {
					
					public Object call() throws Exception {
						
						return method.invoke(instance, parameter);
					}
				});
				
				executors.submit(futureTask);
				
				Object response=futureTask.get();*/
					
					responseMsg=JSON.toJSONString(response);
					logger.info("��Ӧ:"+JSON.toJSONString(response));
					
					return responseMsg;
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
    		return Class.forName(className);
    	}
    	
    }
    
    public static void unLoad(){
    	
    	//ж��spring����
    	SpringUtil.stopSpring();
    	
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
    	            Thread.sleep(10000);//��10�룬��֤����������ɺ��ٹص��Լ�
    	            logger.info("����������ɣ�");
    	        } catch (Exception e) {
    	            logger.error("����ʧ�ܣ�ԭ��", e);
    	        }
    	    }
    	});
    	logger.info("����׼��������");
    	System.exit(0);
    	
    }
    
    private static Object[][] getColumnlistForDubboList(){
    	
    	if(dubboServiceTreeMap.isEmpty()){
    		
    		return new Object[][]{
    				{"0","test","*facade*","����"},
    				{"1","service","*Service*","����"}
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
    
    
    private static  void freshRuleTable(JTable jTable,JPanel  ruleContentJPanel){
    	
    	jTable.setModel(new TableValues(titles,getColumnlistForDubboList()));
    	
    	ruleContentJPanel.validate();
    	
    }

}


/**  
 *     ע�⣺һ��ʹ��AbstractTableModel����TableModel��ʵ�֣�ֻ����������ʱʹ��DefaultTableModel��
 */
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


         /**
          * ��ȡ����
          */
         public String getColumnName(int column){
                   return titles[column];
         }
} 

