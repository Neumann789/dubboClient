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
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格

        // 创建及设置窗口
        JFrame frame = new JFrame("爱问");
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加 "Hello World" 标签
//        JLabel label = new JLabel("Hello World");
//        frame.getContentPane().add(label);
        
        fillContent(frame);
        

        // 显示窗口
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);//设置窗口居中
        frame.setResizable(false);
        
        
        try {
        	Image image=ImageIO.read(MainUI.class.getResource("/images/plog.png"));
			frame.setIconImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //添加窗口关闭事件
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
    	
    	
    	JMenu jmMenu=new JMenu("菜单");
    	JMenu jmView=new JMenu("视图");
    	JMenu jmNavi=new JMenu("导航");
    	JMenu jmSet=new JMenu("设置");
    	JMenu jmHelp=new JMenu("帮助");
    	JMenu jmAbout=new JMenu("关于");
    	
    	jmb.add(jmMenu);
    	jmb.add(jmView);
    	jmb.add(jmNavi);
    	jmb.add(jmSet);
    	jmb.add(jmHelp);
    	jmb.add(jmAbout);
    	
    	JMenuItem jmiMenu1=new JMenuItem("子菜单1");
    	JMenuItem jmiMenu2=new JMenuItem("子菜单2");
    	jmMenu.add(jmiMenu1);
    	jmMenu.addSeparator();
    	jmMenu.add(jmiMenu2);
    	
    	JMenuItem jmiView1=new JMenuItem("子视图1");
    	JMenuItem jmiView2=new JMenuItem("子视图2");
    	jmView.add(jmiView1);
    	jmView.addSeparator();
    	jmView.add(jmiView2);
    	
    	JMenuItem jmiNavi1=new JMenuItem("子导航1");
    	JMenuItem jmiNavi2=new JMenuItem("子导航2");
    	jmNavi.add(jmiNavi1);
    	jmNavi.addSeparator();
    	jmNavi.add(jmiNavi2);
    	
    	JMenuItem jmiSet1=new JMenuItem("子设置1");
    	JMenuItem jmiSet2=new JMenuItem("子设置2");
    	jmSet.add(jmiSet1);
    	jmSet.addSeparator();
    	jmSet.add(jmiSet2);
    	
    	JMenuItem jmiHelp1=new JMenuItem("子帮助1");
    	JMenuItem jmiHelp2=new JMenuItem("子帮助2");
    	jmHelp.add(jmiHelp1);
    	jmHelp.addSeparator();
    	jmHelp.add(jmiHelp2);
    	
    	JMenuItem jmiAbout1=new JMenuItem("子关于1");
    	JMenuItem jmiAbout2=new JMenuItem("子关于2");
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
				LoggerUtil.error("ui配置有问题:"+e.getMessage());
			}
	    	
			
			
			if(i==0){//默认选择第一个按钮
				jb.doClick();
			}
			i++;
		}
		
	}
    
    
    

	public static void main(String[] args) {
		
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

