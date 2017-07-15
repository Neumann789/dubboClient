package com.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileMonitorUI extends JPanel{
	
	private static JLabel jl=new JLabel("this is FileMonitor!");
	public FileMonitorUI() {
		this.add(BorderLayout.NORTH,jl);
	}
}
