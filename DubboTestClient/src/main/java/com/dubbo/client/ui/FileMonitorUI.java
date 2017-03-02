package com.dubbo.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 0 设计监控文件池
 * 1 实时监控文件内容
 * 2 实时监控文件操作
 * @author Administrator
 *
 */
public class FileMonitorUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is FileMonitor!");
	public FileMonitorUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
