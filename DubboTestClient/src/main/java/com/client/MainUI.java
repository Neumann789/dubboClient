package com.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;


import com.dubbo.util.FileUtil;
import com.dubbo.util.LoggerUtil;

public class MainUI {

	public final static String TAGS_CONFIG="/tabs.properties";
	
	private static JPanel jpUp=new JPanel();
	
	private static CardLayout jpMainCardLayout=new CardLayout(5, 5);
	
	private static JPanel jpMain=new JPanel(jpMainCardLayout);
	
	private static JToolBar jtb=new JToolBar();
	
	 /**{
     * ��������ʾGUI�������̰߳�ȫ�Ŀ��ǣ�
     * ����������¼������߳��е��á�
     */
    private static void createAndShowGUI() {
        // ȷ��һ��Ư������۷��

        // ���������ô���
        JFrame frame = new JFrame("����");
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ��� "Hello World" ��ǩ
//        JLabel label = new JLabel("Hello World");
//        frame.getContentPane().add(label);
        
        fillContent(frame);
        

        // ��ʾ����
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);//���ô��ھ���
        frame.setResizable(false);
        
        
        try {
        	Image image=ImageIO.read(MainUI.class.getResource("/images/plog.png"));
			frame.setIconImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //��Ӵ��ڹر��¼�
        frame.addWindowListener(new WindowAdapter() {
        	
        	@Override
        	public void windowClosing(WindowEvent e) {
        		// TODO Auto-generated method stub
        		super.windowClosing(e);
        		
        		killSelf();
        		
        	}
        	
        	
        	public void killSelf(){
        		String name = ManagementFactory.getRuntimeMXBean().getName();
        		String pid = name.split("@")[0];
        		try {
        			Runtime.getRuntime().exec("taskkill /PID "+pid+" /F");
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        	
        	
		});
        
    }

    private static void fillContent(JFrame frame) {
    	//frame.getRootPane().setUI(new PmtImageUI());
    	JMenuBar jmb=new JMenuBar();
//    	frame.setContentPane(jp);
    	frame.add(jpUp, BorderLayout.NORTH);
    	
    	frame.add(jpMain, BorderLayout.CENTER);
    	//jpMain.setBackground(Color.black);
    	//frame.add(jpMid, BorderLayout.CENTER);
    	frame.setJMenuBar(jmb);
    	
    	
    	JMenu jmMenu=new JMenu("�˵�");
    	JMenu jmView=new JMenu("��ͼ");
    	JMenu jmNavi=new JMenu("����");
    	JMenu jmSet=new JMenu("����");
    	JMenu jmHelp=new JMenu("����");
    	JMenu jmAbout=new JMenu("����");
    	
    	jmb.add(jmMenu);
    	jmb.add(jmView);
    	jmb.add(jmNavi);
    	jmb.add(jmSet);
    	jmb.add(jmHelp);
    	jmb.add(jmAbout);
    	
    	JMenuItem jmiMenu1=new JMenuItem("�Ӳ˵�1");
    	JMenuItem jmiMenu2=new JMenuItem("�Ӳ˵�2");
    	jmMenu.add(jmiMenu1);
    	jmMenu.addSeparator();
    	jmMenu.add(jmiMenu2);
    	
    	JMenuItem jmiView1=new JMenuItem("����ͼ1");
    	JMenuItem jmiView2=new JMenuItem("����ͼ2");
    	jmView.add(jmiView1);
    	jmView.addSeparator();
    	jmView.add(jmiView2);
    	
    	JMenuItem jmiNavi1=new JMenuItem("�ӵ���1");
    	JMenuItem jmiNavi2=new JMenuItem("�ӵ���2");
    	jmNavi.add(jmiNavi1);
    	jmNavi.addSeparator();
    	jmNavi.add(jmiNavi2);
    	
    	JMenuItem jmiSet1=new JMenuItem("������1");
    	JMenuItem jmiSet2=new JMenuItem("������2");
    	jmSet.add(jmiSet1);
    	jmSet.addSeparator();
    	jmSet.add(jmiSet2);
    	
    	JMenuItem jmiHelp1=new JMenuItem("�Ӱ���1");
    	JMenuItem jmiHelp2=new JMenuItem("�Ӱ���2");
    	jmHelp.add(jmiHelp1);
    	jmHelp.addSeparator();
    	jmHelp.add(jmiHelp2);
    	
    	JMenuItem jmiAbout1=new JMenuItem("�ӹ���1");
    	JMenuItem jmiAbout2=new JMenuItem("�ӹ���2");
    	jmAbout.add(jmiAbout1);
    	jmAbout.addSeparator();
    	jmAbout.add(jmiAbout2);
    	
    	addUITabList();
    	jpUp.add(jtb);
    	BorderLayout bl=new BorderLayout();
    	jpUp.setLayout(bl);
    	jpUp.add(BorderLayout.NORTH, jtb);
    	
	}

	
	private static void addUITabList(){
		
		List<String> lineList=FileUtil.getLineMapList(TAGS_CONFIG);
		
		int i=0;
		
		for(String line:lineList){
			
			String tagName=line.substring(0, line.indexOf("="));
			String uiClass=line.substring(line.indexOf("=")+1);
			final JButton jb=new JButton(tagName);
			jtb.add(BorderLayout.EAST,jb);
			jtb.addSeparator();
			try {
				
				
				jpMain.add((JPanel)Class.forName(uiClass).newInstance(),jb.getText());
				
				jb.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						jpMainCardLayout.show(jpMain, jb.getText());
						
						jb.setForeground(Color.red);
						for(Component c:jtb.getComponents()){
							
							if(c.getClass()==JButton.class){
								
								if(jb!=c){
									c.setForeground(null);
								}
								
							}
							
						}
						
					}
				});
				
			} catch (Exception e) {
				LoggerUtil.error("ui����������:"+e.getMessage());
			}
	    	
			
			
			if(i==0){//Ĭ��ѡ���һ����ť
				jb.doClick();
			}
			i++;
		}
		
	}
    
    
    

	public static void main(String[] args) {
		
        // ��ʾӦ�� GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

