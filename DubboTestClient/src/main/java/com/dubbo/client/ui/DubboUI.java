package com.dubbo.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dubbo.entity.ServiceClass;
import com.dubbo.entity.ServiceMethod;
import com.dubbo.entity.ServiceParam;
import com.dubbo.util.FileUtil;
import com.dubbo.util.PackageUtil;
import com.dubbo.util.SpringUtil;
import com.dubbo.util.StringUtil;
import com.dubbo.util.ZKConfigUtil;

public class DubboUI extends JPanel{

	private final static Logger logger = LoggerFactory.getLogger(DubboUI.class); 
	
	private  static List<ServiceClass> serviceClassList=new ArrayList<ServiceClass>();
	
	private  static Map methodParamMap=new HashMap();
	
	private static String PATH_DEFAULT="D:/dubbclient/";
	
	private static String PATH_JAR="jars/";
	
	private static String ZKCONFIG="zkconfig.list";
	
	private static JPanel jpMid=new JPanel(new FlowLayout());  //new GridLayout(5,2)
	
	private static CardLayout cardLayout=new CardLayout(5, 5);
	
	private static JPanel jpParamContentList=new JPanel(cardLayout);
	
	private static JPanel jpParamButtonList=new JPanel();
	
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
	
	
	
	
	public DubboUI() {
		this.add(jpMid);
		this.add(jpDown);
		init();
		initCompoment();
	}
	
	
	private static void init(){
		
		//����jar�е�facade��
		initJarFiles();
		
		//���������ע�ᵽspring������ȥ
		registDubboConsumer();
		
		//��ʼ���б�����
		initDataList();
		
		
	}



	private static void registDubboConsumer() {
		
		SpringUtil.startSpring();
		
		for(ServiceClass serviceClass:serviceClassList){
			
			SpringUtil.registDubboConsumer(serviceClass.getClassName());
			
		}
	}



	private static void initDataList() {
		
		dubboServiceListComboBox.removeAllItems();
		
		dubboMethodsListComboBox.removeAllItems();
		
		dubboServiceListComboBox.addItem("---��ѡ��---");
		
		dubboMethodsListComboBox.addItem("---��ѡ��---");
		
		for(ServiceClass serviceClass:serviceClassList){
			
			dubboServiceListComboBox.addItem(serviceClass.getClassName());
			
		}
		
		//��ʼ��zk�б�
		zkListComboBox.removeAllItems();
		List<String> lineList=ZKConfigUtil.readFile(PATH_DEFAULT+ZKCONFIG);
		for(String line:lineList){
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
				PackageUtil.loadFacadeClassFromJars(serviceClassList, jarFilePathArr);
				logger.info(jarFilePathArr+"���سɹ�!");
			} catch (Exception e) {
				logger.error(jarFilePathArr+"����ʧ��!ʧ��ԭ��:"+e.getMessage());
			}
			
		}
	}
	

    private static void initCompoment() {
    	
    	final JButton jb1=new JButton("����jar��");
    	
    	jb1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				openFileDialog(jb1);
				
			}

			private void openFileDialog(final JButton jb1) {
				
				JFileChooser filechooser = new JFileChooser();//�����ļ�ѡ����
		        filechooser.setCurrentDirectory(new File("c:/"));//���õ�ǰĿ¼
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
		            try {
		            	//�ƶ�jar��
		            	String srcPath=PATH_DEFAULT+PATH_JAR+filechooser.getSelectedFile().getName();
		            	String destPath=StringUtil.addFileStamp(srcPath);//���ʱ���
		            	FileUtil.moveFile(filechooser.getSelectedFile().getAbsolutePath(),destPath );
		            	PackageUtil.loadFacadeClassFromJar(serviceClassList, destPath);
		            	//unLoad();//ж��
		            	//���������ע�ᵽspring������ȥ
		        		//registDubboConsumer();
		        		//��ʼ���б�����
		        		//initDataList();
						logger.info("jar������ɹ�,������Ч������!");
					} catch (Exception e) {
						logger.error(filechooser.getSelectedFile().getAbsolutePath()+"jarʧ��,ʧ��ԭ��:"+e.getMessage());
						e.printStackTrace();
					}

		        }
			}
		});
    	
    	Box box1=Box.createHorizontalBox();
    	Box box2=Box.createHorizontalBox();
    	Box box3=Box.createHorizontalBox();
    	
    	//��ʾdubbo�����б�
    	JLabel label=new JLabel("dubbo�����б�:");  
    	box1.add(label); 
    	dubboServiceListComboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
				if(dubboServiceListComboBox.getSelectedItem()==null){
					return ;
				}
				
				methodParamMap.clear();
				
				String selectItem=dubboServiceListComboBox.getSelectedItem().toString();
				for(ServiceClass serviceClass:serviceClassList){
					if(selectItem.equals(serviceClass.getClassName())){
						
						
						
						List<ServiceMethod> serviceMethodList=serviceClass.getServiceMethods();
						
						dubboMethodsListComboBox.removeAllItems();
						dubboMethodsListComboBox.addItem("---��ѡ��---");
						for(ServiceMethod sm:serviceMethodList){
							List<ServiceParam> serviceParamList=sm.getServiceParams();
							String serviceParamListStr="";
							for(ServiceParam serviceParam:serviceParamList){
								serviceParamListStr+=serviceParam.getParamName()+",";
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
    	box1.add(dubboServiceListComboBox); 
    	
    	//��ʾdubbo�����б�
    	JLabel label2=new JLabel("dubbo�����б�:");  
    	box2.add(label2);  
    	
    	box2.add(dubboMethodsListComboBox);  
    	
    	dubboMethodsListComboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				jpParamContentList.removeAll();
				jpParamButtonList.removeAll();
				textAreaParamList.clear();
				
				if(dubboMethodsListComboBox.getSelectedItem()!=null&&methodParamMap.containsKey(dubboMethodsListComboBox.getSelectedItem().toString())){
					List<ServiceParam> serviceParamList=(List<ServiceParam>)methodParamMap.get(dubboMethodsListComboBox.getSelectedItem().toString());
					/*int boxNum=0;
					Box horizontalBox=null;
					for(ServiceParam sp:serviceParamList){
						
						if(boxNum%2==0){
							horizontalBox=Box.createHorizontalBox();
						}
						Box box=Box.createVerticalBox();
						box.add(new JLabel(sp.getParamName()));
						TextArea paramTextArea=new TextArea(sp.getParamJsonContent());
						textAreaParamList.add(paramTextArea);
						box.add(paramTextArea);
						horizontalBox.add(box);
						if(boxNum%2==1){
							serviceParamListBox.add(Box.createVerticalStrut(10));
							serviceParamListBox.add(horizontalBox);
						}
						boxNum++;
					}*/
					
					for(final ServiceParam sp:serviceParamList){
						final JButton jb=new JButton(sp.getParamName());
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
    	
    	box3.add(new JLabel("zk��ַ:"));
    	zkListComboBox.setEditable(true);
    	box3.add(zkListComboBox);
    	
    	box3.add(invokJButton);
    	
    	boxn.add(box1);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box2);
    	boxn.add(Box.createVerticalStrut(20));
    	boxn.add(box3);
    	
    	boxn.add(Box.createVerticalStrut(40));
    	//boxn.add(serviceParamListBox);
    	
    	boxn.add(jpParamButtonList);
    	boxn.add(Box.createVerticalStrut(10));
    	boxn.add(jpParamContentList);
    	
    	jpMid.add(boxn);
    	
    	Box resultBox=Box.createVerticalBox();
    	resultBox.add(new JLabel("��Ӧ���:"));
    	resultTextArea.setEditable(false);
    	resultBox.add(resultTextArea);
    	jpDown.add(resultBox);
    	
    	
    	
    	
    	invokJButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String responseMsg="";
				long startTime=0L;
				long endTime=0L;
				try {
				startTime=System.currentTimeMillis();
				String address=zkListComboBox.getSelectedItem().toString().trim();
				SpringUtil.registZK("zk"+address, address);
				
				String className=dubboServiceListComboBox.getSelectedItem().toString();
				
				String methodItemStr=dubboMethodsListComboBox.getSelectedItem().toString();
				String methodName=methodItemStr.substring(0, methodItemStr.indexOf("("));
				
				List<ServiceParam> serviceParamList=(List<ServiceParam>)methodParamMap.get(methodItemStr);
				
				
				
				Class[] parameterTypes=new Class[serviceParamList.size()];
				Object[] parameter=new Object[serviceParamList.size()];
				for(int i=0;i<serviceParamList.size();i++){
					try {
						parameterTypes[i]=Class.forName(serviceParamList.get(i).getParamName());
						parameter[i]=JSON.parseObject(textAreaParamList.get(i).getText().trim(), parameterTypes[i]);
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						throw new RuntimeException("�ӿڵ���ʧ�ܣ�ʧ��ԭ��:"+e1.getMessage());
					}
				}
				
				Object instance=SpringUtil.getBean(className);
				
					
					Method method=instance.getClass().getMethod(methodName, parameterTypes);
					Object response=method.invoke(instance, parameter);
					
					responseMsg=JSON.toJSONString(response);
					logger.info("��Ӧ:"+JSON.toJSONString(response));
				}catch (InvocationTargetException ite) {
					logger.error(ite.getTargetException().getMessage());
					responseMsg=ite.getTargetException().getMessage();
				} 
				catch (Exception exception) {
					logger.error(exception.getMessage());
					responseMsg=exception.getMessage();
				}finally {
					endTime=System.currentTimeMillis();
					resultTextArea.setText(responseMsg+" ��ʱ��"+(endTime-startTime)+"ms");
				} 
			}
		});
		
	}
    
    public static void unLoad(){
    	
    	//ж��spring����
    	SpringUtil.stopSpring();
    	
    	serviceClassList.clear();
    	
    	dubboServiceListComboBox.removeAll();
    	
    	zkListComboBox.removeAll();
    	
    }

}
